package com.microservice.inventory.feignclient;

import com.microservice.inventory.dto.ProductRequestDto;
import com.microservice.inventory.dto.ProductResponseDto;
import com.microservice.inventory.dto.ProductSearchDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@FeignClient(name="product-services",configuration = ProductConfig.class)

//@FeignClient(value="feignClientDemo", url="http://localhost:8091/products")

/**
 *  FeignClient point to product-services which is registered in Eureka server
 */
@FeignClient(name="product-services")
public interface ProductProxy {
    // adding product server.servlet.context-path before all post or get mapping
    @PostMapping("/products/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequestDto ProductRequestDto);

    @GetMapping("/products/findAll")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getAllProducts();
    @GetMapping("/products/findProductById/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto findProductByProductId(@PathVariable("id") String productId) ;

    @PostMapping("/products/findByProductSearchDto")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto findProductByProductSearchDto(@RequestBody ProductSearchDto productSearchDto);
}
