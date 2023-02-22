package com.microservice.inventory.service;

import com.microservice.inventory.dto.InventoryRequestDto;
import com.microservice.inventory.dto.InventoryResponseDto;
import com.microservice.inventory.dto.ProductResponseDto;
import com.microservice.inventory.dto.ProductSearchDto;
import com.microservice.inventory.exception.InventoryException;
import com.microservice.inventory.feignclient.ProductProxy;
import com.microservice.inventory.model.Inventory;
import com.microservice.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ProductProxy productProxy;

    private final ModelMapper modelMapper;
    @Transactional
    public boolean isInStock(String skuCode) {
        Optional<Inventory> optionalInventory = inventoryRepository.findBySkuCode(skuCode);
        return optionalInventory.isPresent();
    }

    public InventoryResponseDto findByInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(()->
            new InventoryException("Inventory Id "+id+" does not exist")
        );
        return toResponseDto(inventory,Boolean.FALSE);
    }

    public List<InventoryResponseDto> findAll() {
        List<Inventory> inventorys = inventoryRepository.findAll();
        return inventorys.stream().map(inventory -> {
            return toResponseDto(inventory,Boolean.FALSE);
        }).collect(Collectors.toList());
    }
    @Transactional
    public InventoryResponseDto inputInventory(InventoryRequestDto inventoryRequestDto) {
        Inventory inventory = Inventory.builder()
                .skuCode(inventoryRequestDto.getProductName())
                .quantity(inventoryRequestDto.getQuantity())
                .productId(inventoryRequestDto.getProductId())
                .description(inventoryRequestDto.getDescription())
                .productName(inventoryRequestDto.getProductName())
                .build();
        inventory = inventoryRepository.save(inventory);
        return toResponseDto(inventory,Boolean.FALSE);

    }

    @Transactional
    public InventoryResponseDto inputInventoryByProductId(String productId,Integer quantity) {
        checkIfProductIdInInventory(productId);
        ProductResponseDto productResponseDto =productProxy.findProductByProductId(productId);
        log.info(productResponseDto.toString());
        Inventory inventory = Inventory.builder()
                .productId(productResponseDto.getId())
                .productName(productResponseDto.getName())
                .description(productResponseDto.getDescription())
                .skuCode(productResponseDto.getName())
                .quantity(quantity).build();
        inventory = inventoryRepository.save(inventory);
        return toResponseDto(inventory,Boolean.FALSE);
    }

    @Transactional
    public InventoryResponseDto inputInventoryByProductSearchDto(ProductSearchDto productSearchDto, Integer quantity) {
        checkIfProductIdInInventory(productSearchDto.getProductId());
        checkIfProductNameInInventory(productSearchDto.getProductName());
        ProductResponseDto productResponseDto =productProxy.findProductByProductSearchDto(productSearchDto);

        log.info(productResponseDto.toString());
        Inventory inventory = Inventory.builder()
                .productId(productResponseDto.getId())
                .productName(productResponseDto.getName())
                .description(productResponseDto.getDescription())
                .skuCode(productResponseDto.getName())
                .quantity(quantity).build();
        inventory = inventoryRepository.save(inventory);
        return toResponseDto(inventory,Boolean.FALSE);
    }

    @Transactional
    public InventoryResponseDto updateInventoryByProductId(Long id,Integer quantity) {
        Inventory  inventory  = inventoryRepository.findById(id).orElseThrow(()->
                new InventoryException("Inventory Id "+id+" does not exist")
        );
        inventory.setQuantity(quantity);
        inventory = inventoryRepository.save(inventory);
        return toResponseDto(inventory,Boolean.FALSE);

    }

    public InventoryResponseDto findInventoryByProductName(String prodName) {
        Inventory inventory = inventoryRepository.findInventoryByProductName(prodName)
                .orElseThrow(()->
                    new InventoryException("Product Name "+prodName+" not found!" )
                );

       return toResponseDto(inventory,Boolean.FALSE);
    }

    public InventoryResponseDto findInventoryByProductId(String productId) {
        Inventory inventory = inventoryRepository.findInventoryByProductId(productId)
                .orElseThrow(()->
                        new InventoryException("Product Name "+productId+" not found!" )
                );

        return toResponseDto(inventory,Boolean.FALSE);
    }

    private InventoryResponseDto toResponseDto(Inventory inventory,boolean deliver) {
        InventoryResponseDto inventoryResponseDto = modelMapper.map(inventory, InventoryResponseDto.class);
        // missed quantity by modelMapper
        inventoryResponseDto.setQuantity(inventory.getQuantity());
     if (deliver) {
            inventoryResponseDto.setDeliverDateTime(LocalDateTime.now());
        } else {
            inventoryResponseDto.setInputDateTime(LocalDateTime.now());
        }
        return inventoryResponseDto;
    }

    private void checkIfProductIdInInventory(String productId) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findInventoryByProductId(productId);
        if (inventoryOpt.isPresent()) {
            throw new InventoryException("Product Id "+productId+" exists in Inventory database");
        }
    }

    private void checkIfProductNameInInventory(String productName) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findInventoryByProductName(productName);
        if (inventoryOpt.isPresent()) {
            throw new InventoryException("Product Name "+productName+" exists in Inventory database");
        }
    }
}
