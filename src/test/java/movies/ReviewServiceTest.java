package movies;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

@WireMockTest
class ReviewServiceTest {

    ReviewService reviewService;

    @BeforeEach
    void setUp(WireMockRuntimeInfo wireMockRuntimeInfo) {
        WebClient webClient = WebClient.builder()
                .baseUrl(wireMockRuntimeInfo.getHttpBaseUrl())
                .build();

        reviewService = new ReviewService(webClient);
    }

    @Test
    @DisplayName("Should deliver a review for given movie id")
    void shouldDeliverReview() {
        var movieInfoId = 2;
        var review = new Review(2, 2, "MASTERPIECE", 9.0);

        StepVerifier.create(reviewService.review(movieInfoId))
                .expectNext(review)
                .verifyComplete();
    }
}