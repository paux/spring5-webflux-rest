package cc.paukner.spring5webfluxrest.controllers;

import cc.paukner.spring5webfluxrest.domain.Category;
import cc.paukner.spring5webfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static cc.paukner.spring5webfluxrest.controllers.CategoryController.BASE_URI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void list() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(
                        Category.builder().description("Cat").build(),
                        Category.builder().description("Dog").build()
                ));

        webTestClient.get().uri(BASE_URI)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        final String id = "THISISTHEID";
        given(categoryRepository.findById(id))
                .willReturn(Mono.just(Category.builder().description("Bird").build()));

        webTestClient.get().uri(BASE_URI + "/" + id)
                .exchange()
                .expectBodyList(Category.class);
    }

    @Test
    void create() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just(Category.builder().description("Cat").build());

        webTestClient.post()
                .uri(BASE_URI)
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }
}
