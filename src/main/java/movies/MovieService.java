package movies;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static movies.ReactiveResponse.handleMonoResponse;
import static movies.ReactiveResponse.handleFluxResponse;

public class MovieService {

    private final WebClient webClient;

    public MovieService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<MovieInfo> movieInfo(long movieId) {
        var path = "/v1/movie-infos/%d".formatted(movieId);

        return webClient
                .get()
                .uri(path)
                .exchangeToMono(handleMonoResponse(MovieInfo.class));
    }

    public Flux<MovieInfo> movieInfos() {
        var path = "/v1/movie-infos";

        return webClient
                .get()
                .uri(path)
                .exchangeToFlux(handleFluxResponse(MovieInfo.class));
    }
}
