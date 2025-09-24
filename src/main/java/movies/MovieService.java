package movies;

import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

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

    private <T> Function<ClientResponse, Mono<T>> handleMonoResponse(Class<T> clazz) {
        return response -> {
            if (response.statusCode().is2xxSuccessful()) {
                return response.bodyToMono(clazz);
            } else {
                var message = "Oopsie, gotta HTTP %d".formatted(response.statusCode().value());
                return Mono.error(new RuntimeException(message));
            }
        };
    }

    private <T> Function<ClientResponse, Flux<T>> handleFluxResponse(Class<T> clazz) {
        return response -> {
            if (response.statusCode().is2xxSuccessful()) {
                return response.bodyToFlux(clazz);
            } else {
                var message = "Oopsie, gotta HTTP %d".formatted(response.statusCode().value());
                return Flux.error(new RuntimeException(message));
            }
        };
    }
}
