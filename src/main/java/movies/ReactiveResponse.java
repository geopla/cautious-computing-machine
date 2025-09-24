package movies;

import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public final class ReactiveResponse {

    public static <T> Function<ClientResponse, Mono<T>> handleMonoResponse(Class<T> clazz) {
        return response -> {
            if (response.statusCode().is2xxSuccessful()) {
                return response.bodyToMono(clazz);
            } else {
                var message = "Oopsie, gotta HTTP %d".formatted(response.statusCode().value());
                return Mono.error(new RuntimeException(message));
            }
        };
    }

    public static <T> Function<ClientResponse, Flux<T>> handleFluxResponse(Class<T> clazz) {
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
