package com.microtech.smartshop.controller;

import com.microtech.smartshop.dto.request.CreateProductRequest;
import com.microtech.smartshop.dto.request.UpdateProductRequest;
import com.microtech.smartshop.dto.response.ProductResponse;
import com.microtech.smartshop.entity.Product;
import com.microtech.smartshop.mapper.ProductMapper;
import com.microtech.smartshop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller pour la gestion des produits
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    /**
     * POST /api/products
     * Crée un nouveau produit
     */
    @PostMapping
    @Operation(summary = "Create product", description = "Create a new product")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        log.info("Creating new product: {}", request.getName());

        Product product = productMapper.toEntity(request);
        Product savedProduct = productService.create(product);
        ProductResponse response = productMapper.toResponse(savedProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/products/{id}
     * Récupère un produit par ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Get product details by ID")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        log.info("Fetching product: {}", id);

        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductResponse response = productMapper.toResponse(product);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/products
     * Récupère tous les produits avec pagination
     */
    @GetMapping
    @Operation(summary = "Get all products", description = "Get all available products with pagination")
    public ResponseEntity<Page<ProductResponse>> getAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("Fetching all products (page: {}, size: {})", pageable.getPageNumber(), pageable.getPageSize());

        Page<Product> products = productService.findAllAvailable(pageable);
        Page<ProductResponse> response = products.map(productMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/products/search
     * Recherche de produits par nom
     */
    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products by name")
    public ResponseEntity<Page<ProductResponse>> search(
            @RequestParam String name,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Searching products with name: {}", name);

        Page<Product> products = productService.searchByName(name, pageable);
        Page<ProductResponse> response = products.map(productMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/products/{id}
     * Met à jour un produit
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Update product information")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        log.info("Updating product: {}", id);

        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productMapper.updateEntity(request, product);
        Product updatedProduct = productService.update(id, product);
        ProductResponse response = productMapper.toResponse(updatedProduct);

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/products/{id}
     * Supprime un produit (soft delete)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Soft delete a product")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting product: {}", id);

        productService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/products/{id}/restore
     * Restaure un produit supprimé
     */
    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore product", description = "Restore a soft deleted product")
    public ResponseEntity<ProductResponse> restore(@PathVariable Long id) {
        log.info("Restoring product: {}", id);

        productService.restore(id);
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductResponse response = productMapper.toResponse(product);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/products/{id}/check-stock
     * Vérifie la disponibilité du stock
     */
    @GetMapping("/{id}/check-stock")
    @Operation(summary = "Check stock", description = "Check if product has sufficient stock")
    public ResponseEntity<Boolean> checkStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        log.info("Checking stock for product {}: {} units", id, quantity);

        boolean available = productService.checkStock(id, quantity);
        return ResponseEntity.ok(available);
    }
}