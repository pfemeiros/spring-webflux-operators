package br.com.pfemeiros.springwebfluxoperators;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpringWebfluxOperatorsApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testMap() {
        Flux<String> menu = Flux.just("Pagar boleto", "Ver fatura", "Tranferir")
                .map(name -> name.toUpperCase());

        StepVerifier.create(menu)
                .expectNextMatches(it -> it.equals("PAGAR BOLETO"))
                .expectNextMatches(it -> it.equals("VER FATURA"))
                .expectNextMatches(it -> it.equals("TRANFERIR"))
                .verifyComplete();
    }

    @Test
    void testFlatMap() {
        Flux<String> menu = Flux.just("Pagar boleto", "Ver fatura", "Tranferir")
                .flatMap(name -> Mono.just(name.toUpperCase()));

        StepVerifier.create(menu)
                .expectNextMatches(it -> it.equals("PAGAR BOLETO"))
                .expectNextMatches(it -> it.equals("VER FATURA"))
                .expectNextMatches(it -> it.equals("TRANFERIR"))
                .verifyComplete();
    }

    @Test
    void testFlatMapSequential() {
        Flux<String> menu = Flux.just("Pagar boleto", "Ver fatura", "Tranferir")
                .flatMapSequential(name -> Mono.just(name.toUpperCase()));

        StepVerifier.create(menu)
                .expectNextMatches(it -> it.equals("PAGAR BOLETO"))
                .expectNextMatches(it -> it.equals("VER FATURA"))
                .expectNextMatches(it -> it.equals("TRANFERIR"))
                .verifyComplete();
    }

    @Test
    void testFilterWhen() {
        Flux<String> menu = Flux.just("Pagar boleto", "Ver fatura", "Tranferir")
                .filterWhen(it -> isButtonAvailable());

        StepVerifier.create(menu)
                .verifyComplete();
    }

    @Test
    void testBlock() {
        String button = Mono.just("Pagar boleto")
                .share()
                .block();

        assertEquals("Pagar boleto", button);
    }

    @Test
    void testFluxFromIterable() {
        Flux<String> stringFlux = Flux.fromIterable(List.of("Pagar boleto", "Ver fatura"));

        StepVerifier.create(stringFlux)
                .expectNextMatches(it -> it.equals("Pagar boleto"))
                .expectNextMatches(it -> it.equals("Ver fatura"))
                .verifyComplete();
    }

    @Test
    void testSwitchIfEmpty() {
        Mono<Object> menu = Mono.empty()
                .switchIfEmpty(Mono.just("Pagar boleto"));

        StepVerifier.create(menu)
                .expectNextMatches(it -> it.equals("Pagar boleto"))
                .verifyComplete();
    }

    @Test
    void testZipWhen() {
        Mono<String> menu = Mono.just("Nome")
                .zipWhen(it -> Mono.just("Sobrenome"))
                .map(tuple -> tuple.getT1().concat(" ").concat(tuple.getT2()));

        StepVerifier.create(menu)
                .expectNextMatches(it -> it.equals("Nome Sobrenome"))
                .verifyComplete();
    }

    @Test
    void then() {
        Mono<Void> mono = Mono.just("Nome")
                .doOnSuccess(it -> saveName())
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    private void saveName() {

    }

    private Mono<Boolean> isButtonAvailable() {
        return Mono.just(false);
    }

}
