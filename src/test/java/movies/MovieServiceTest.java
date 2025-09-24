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
}