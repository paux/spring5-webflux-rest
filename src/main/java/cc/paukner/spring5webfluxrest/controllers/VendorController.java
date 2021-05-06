package cc.paukner.spring5webfluxrest.controllers;

import cc.paukner.spring5webfluxrest.domain.Vendor;
import cc.paukner.spring5webfluxrest.repositories.VendorRepository;
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
public class VendorController {

    public static final String BASE_URI = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping(BASE_URI)
    public Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping(BASE_URI + "/{id}")
    public Mono<Vendor> getById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(BASE_URI)
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorSteam) {
        return vendorRepository.saveAll(vendorSteam).then();
    }

    @PutMapping(BASE_URI + "/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id); // Why? Why not just expect the ID already set? - Because path!
        return vendorRepository.save(vendor);
    }

    @PatchMapping(BASE_URI + "/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {
        // Normally, you only let a DTO in, and business logic is in a service layer
        Vendor existingVendor = vendorRepository.findById(id).block();
        boolean vendorPatched = false;
        // go through every attribute and patch the existing object
        if (existingVendor != null && existingVendor.getFirstName() != vendor.getFirstName()) {
            existingVendor.setFirstName(vendor.getFirstName());
            vendorPatched = true;
        }
        if (existingVendor != null && existingVendor.getLastName() != vendor.getLastName()) {
            existingVendor.setLastName(vendor.getLastName());
            vendorPatched = true;
        }
        if (vendorPatched) {
            return vendorRepository.save(existingVendor); // if anything was patched
        }
        return Mono.just(existingVendor); // if unmodified
    }
}
