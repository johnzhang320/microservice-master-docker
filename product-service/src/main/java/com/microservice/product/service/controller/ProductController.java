package com.microservice.product.service.controller;

import com.microservice.product.service.dto.ProductRequestDto;
import com.microservice.product.service.dto.ProductResponseDto;
import com.microservice.product.service.dto.ProductSearchDto;
import com.microservice.product.service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto ProductRequestDto) {
        return productService.createProductByBuilder(ProductRequestDto);
    }

    @GetMapping("/findAll")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/findProductById/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto findProductByProductId(@PathVariable("id") String productId) {
        return productService.findProductById(productId);
    }

    /**
     * Avoid input %20% we use json format to find
     * @param
     * @return
     */
    @PostMapping("/findByProductSearchDto")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto findProductByProductSearchDto(@RequestBody ProductSearchDto productSearchDto) {
        return productService.findProductByProductSearchDto(productSearchDto);
    }
}
