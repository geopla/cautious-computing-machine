package movies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ReactiveResponseTest {

    // no mocking of ClientResponse, because of reasons ...

    @Test
    @DisplayName("Should return Mono from response body for HTTP 200")
    void shouldDoSomething() {

        ClientResponse response = ClientResponse
                .create(HttpStatus.OK)
                .body("Hello World")
                .build();

        Mono<String> applied = ReactiveResponse.handleMonoResponse(String.class).apply(response);

        StepVerifier.create(applied)
                .expectNext("Hello World")
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return Mono signalling error for HTTP 500")
    void shouldReturnErrorMono() {
        ClientResponse response = ClientResponse
                .create(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        Mono<String> applied = ReactiveResponse.handleMonoResponse(String.class).apply(response);

        StepVerifier.create(applied)
                .expectErrorMessage("Oopsie, gotta HTTP 500")
                .verify();
    }

    @Test
    @DisplayName("Should return Flux from response body for HTTP 200")
    void shouldReturnFlux() {
        // we are good with using the real thing but limited to a JSON body
        ClientResponse response = ClientResponse
                .create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body("[{ }, { }]")
                .build();

        record Thingy() { }

        Flux<Thingy> applied = ReactiveResponse.handleFluxResponse(Thingy.class).apply(response);

        StepVerifier.create(applied)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return Flux signalling error for HTTP 400")
    void shouldReturnErrorFlux() {
        ClientResponse response = ClientResponse
                .create(HttpStatus.BAD_REQUEST)
                .build();

        record Thingy() { }

        Flux<Thingy> applied = ReactiveResponse.handleFluxResponse(Thingy.class).apply(response);

        StepVerifier.create(applied)
                .expectErrorMessage("Oopsie, gotta HTTP 400")
                .verify();
    }
}