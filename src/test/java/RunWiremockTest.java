import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
public class RunWiremockTest {

    WebTestClient webTestClient;

    @BeforeEach
    void setUp(WireMockRuntimeInfo wireMockRuntimeInfo) {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl(wireMockRuntimeInfo.getHttpBaseUrl())
            .build();
    }

    @Test
    @DisplayName("Should simply run Wiremock having some stubs from 'test/resources/mappings'")
    void shouldRunWiremock(WireMockRuntimeInfo wireMockRuntimeInfo) {
        WireMock wireMock = wireMockRuntimeInfo.getWireMock();

        assertThat(wireMock.allStubMappings().getMappings()).isNotEmpty();
    }

    @Test
    @DisplayName("Should have loaded movie-infos via mappings")
    void shouldHaveGetMovieInfosEndpointSetUp() {
        var path = "/v1/movie-infos";

        webTestClient.get().uri(path)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(7)
        ;
    }
}
