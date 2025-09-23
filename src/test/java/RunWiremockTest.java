import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.reactive.server.WebTestClient;

@WireMockTest()
public class RunWiremockTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunWiremockTest.class);

    WebTestClient webTestClient;

    @BeforeEach
    void setUp(WireMockRuntimeInfo wireMockRuntimeInfo) {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl(wireMockRuntimeInfo.getHttpBaseUrl())
            .build();
    }

    @Test
    @DisplayName("Should have loaded movie-infos via mappings")
    void shouldHaveGetMovieInfosEndpointSetUp() {
        var path = "/v1/movie-infos";

        webTestClient.get().uri(path)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(7);
    }

    @Test
    @DisplayName("Should map query parameter movie id to response body file")
    void shouldMapQueryParameterToResponseBody() {
        var path = "/v1/reviews?movieInfoId=2";

        webTestClient.get().uri(path)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].comment").isEqualTo("MASTERPIECE");
    }
}
