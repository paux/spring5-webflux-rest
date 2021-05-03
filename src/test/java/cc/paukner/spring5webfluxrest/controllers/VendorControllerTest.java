package cc.paukner.spring5webfluxrest.controllers;

import cc.paukner.spring5webfluxrest.domain.Vendor;
import cc.paukner.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static cc.paukner.spring5webfluxrest.controllers.VendorController.BASE_URI;
import static org.mockito.BDDMockito.given;

class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(
                        Vendor.builder().firstName("Sepp").lastName("Forcher").build(),
                        Vendor.builder().firstName("Hugo").lastName("Hoffmannsthal, von").build()
                ));

        webTestClient.get().uri(BASE_URI)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        final String id = "THISISTHEID";
        given(vendorRepository.findById(id))
                .willReturn(Mono.just(Vendor.builder().firstName("Michael").lastName("Knight").build()));

        webTestClient.get().uri(BASE_URI + "/" + id)
                .exchange()
                .expectBodyList(Vendor.class);
    }
}
