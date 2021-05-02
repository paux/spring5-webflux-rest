package cc.paukner.spring5webfluxrest.bootstrap;

import cc.paukner.spring5webfluxrest.domain.Category;
import cc.paukner.spring5webfluxrest.domain.Vendor;
import cc.paukner.spring5webfluxrest.repositories.CategoryRepository;
import cc.paukner.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws NullPointerException {
        if (categoryRepository.count().block() == 0) {
            generateCategories();
        }
        if (vendorRepository.count().block() == 0) {
            generateVendors();
        }
    }

    private void generateCategories() {
        categoryRepository.saveAll(List.of(
                Category.builder().description("Hugo").build(),
                Category.builder().description("Sepp").build()
        )).blockLast();
        System.out.println("Generated " + categoryRepository.count().block() + " categories");
    }

    private void generateVendors() {
        vendorRepository.saveAll(List.of(
                Vendor.builder().firstName("Hugo").lastName("Portisch").build(),
                Vendor.builder().firstName("Sepp").lastName("Forcher").build()
        )).blockLast();
        System.out.println("Generated " + vendorRepository.count().block() + " vendors");
    }
}
