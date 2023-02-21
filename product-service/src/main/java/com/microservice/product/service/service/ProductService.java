package com.microservice.product.service.service;

import com.microservice.product.service.dto.ProductRequestDto;
import com.microservice.product.service.dto.ProductResponseDto;
import com.microservice.product.service.dto.ProductSearchDto;
import com.microservice.product.service.exception.ProductException;
import com.microservice.product.service.model.Product;
import com.microservice.product.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;
    public void createProduct(ProductRequestDto productRequestDto) {
        Product product = modelMapper.map(productRequestDto, Product.class);
        productRepository.save(product);
    }
    public ProductResponseDto createProductByBuilder(ProductRequestDto productRequestDto) {
        Optional<Product> productOpt = productRepository.findByName(productRequestDto.getName());

        if (productOpt.isPresent()) {
            throw new ProductException("Product " + productRequestDto.getName() + " already exists in Product Database");
        }

        Product product = Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .price(productRequestDto.getPrice())
                .build();

        product = productRepository.save(product);
        ProductResponseDto productResponseDto= modelMapper.map(product,ProductResponseDto.class);
        log.info("Product {} is saved ", product.getId());
        return productResponseDto;
    }

    public ProductResponseDto findProductByProductSearchDto(ProductSearchDto productSearchDto) {
        Product product=new Product();
        if (!productSearchDto.getProductId().isEmpty() && productSearchDto.getProductId()!=null) {
            product = productRepository.findById(productSearchDto.getProductId()).orElseThrow(
                    ()-> new ProductException("Product Id "+productSearchDto.getProductId()+" is wrong")
            );
        }
        if (!productSearchDto.getProductName().isEmpty() || productSearchDto.getProductName()!=null) {
            product = productRepository.findByName(productSearchDto.getProductName()).orElseThrow(
                    ()-> new ProductException("Product Name "+productSearchDto.getProductName()+" is wrong")
            );
        }
       return modelMapper.map(product,ProductResponseDto.class);
    }
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products!=null) {
            return products.stream()
                    .map(product->{
                        ProductResponseDto productResponseDto = modelMapper.map(product,ProductResponseDto.class);
                        return productResponseDto;
                    }).collect(Collectors.toList());
        }
        return null;
    }





    public ProductResponseDto findProductById(String id) {
        Product product =productRepository.findById(id).orElseThrow(()->
                new ProductException("Product Id "+id+" does not exist "));
        return  modelMapper.map(product,ProductResponseDto.class );
    }
}
