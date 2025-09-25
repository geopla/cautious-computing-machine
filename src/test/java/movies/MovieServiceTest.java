package movies;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;

@WireMockTest()
class MovieServiceTest {

    MovieService movieService;

    @BeforeEach
    void setUp(WireMockRuntimeInfo wireMockRuntimeInfo) {
        WebClient webClient = WebClient.builder()
                .baseUrl(wireMockRuntimeInfo.getHttpBaseUrl())
                .build();

        movieService = new MovieService(webClient);
    }

    @Test
    @DisplayName("Should deliver movie info for given id")
    void shouldDeliverMovieInfo() {
        var movieInfoId = 2;
        var movieInfo = new MovieInfo(
                2,
                "The Dark Knight",
                2008,
                List.of("Christian Bale", "Heath Ledger"),
                LocalDate.parse("2008-07-18")
        );

        StepVerifier.create(movieService.movieInfo(movieInfoId))
                .expectNext(movieInfo)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should deliver all seven movies available")
    void shouldDeliverAllMovies() {
        var numberOfMovies = 7;

        StepVerifier.create(movieService.movieInfos())
                .expectNextCount(numberOfMovies)
                .verifyComplete();
    }

    // having fun with Wiremock stubs - but should be tested in ReactiveResponse otherwise
    // TODO use problem details (RFC 9457) instead of plain text

    @Test
    @DisplayName("Should handle Flux response HTTP error")
    void shouldHandleFluxResponseHttpError() {
        var movieInfosPath = "/v1/movie-infos";

        stubFor(get(movieInfosPath)
                .willReturn(aResponse()
                        .withStatus(500)
                        .withStatusMessage("Oh nooo!")
                        .withHeader("Content-Type", "text/plain"))
        );

        StepVerifier.create(movieService.movieInfos())
                .expectErrorMessage("Oopsie, gotta HTTP 500")
                .verify();
    }

    @Test
    @DisplayName("Should ")
    void shouldHandleMonoResponseHttpError() {
        var movieInfoByIdPath = "/v1/movie-infos/{id}";

        stubFor(get(urlPathTemplate(movieInfoByIdPath))
                .withPathParam("id", matching("^[0-9]+$"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withStatusMessage("What the f*** you are looking for?")
                        .withHeader("Content-Type", "text/plain"))
        );

        StepVerifier.create(movieService.movieInfo(42))
                .expectErrorMessage("Oopsie, gotta HTTP 404")
                .verify();
    }
}