package cc.paukner.spring5webfluxrest.controllers;

import cc.paukner.spring5webfluxrest.domain.Category;
import cc.paukner.spring5webfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CategoryController {

    public static final String BASE_URI = "/api/v1/categories";

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping(BASE_URI)
    public Flux<Category> list() {
        return categoryRepository.findAll();
    }

    @GetMapping(BASE_URI + "/{id}")
    public Mono<Category> getById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(BASE_URI)
    public Mono<Void> create(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepository.saveAll(categoryStream).then();
    }

    @PutMapping(BASE_URI + "/{id}")
    public Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id); // Why? Why not just expect the ID already set? - Because path!
        return categoryRepository.save(category);
    }

    @PatchMapping(BASE_URI + "/{id}")
    public Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {
        // Normally, you only let a DTO in, and business logic is in a service layer
        Category existingCategory = categoryRepository.findById(id).block();
        // go through every attribute and patch the existing object
        if (existingCategory != null && existingCategory.getDescription() != category.getDescription()) {
            existingCategory.setDescription(category.getDescription());
            return categoryRepository.save(existingCategory); // if anything was patched
        }
        return Mono.just(existingCategory); // if unmodified
    }
}
