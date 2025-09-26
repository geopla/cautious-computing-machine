package movies;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static movies.ReactiveResponse.handleFluxResponse;

public class ReviewService {

    private final WebClient webClient;

    public ReviewService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Review> review(int movieInfoId) {
        String path = "/v1/reviews";

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("movieInfoId", movieInfoId)
                        .build()
                )
                .exchangeToFlux(handleFluxResponse(Review.class));
    }
}
