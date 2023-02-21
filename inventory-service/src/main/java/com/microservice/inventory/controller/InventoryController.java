package com.microservice.inventory.controller;

import com.microservice.inventory.dto.InventoryRequestDto;
import com.microservice.inventory.dto.InventoryResponseDto;
import com.microservice.inventory.dto.ProductResponseDto;
import com.microservice.inventory.dto.ProductSearchDto;
import com.microservice.inventory.exception.InventoryException;

import com.microservice.inventory.feignclient.ProductProxy;
import com.microservice.inventory.model.Inventory;
import com.microservice.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    private final ProductProxy productProxy;
    @GetMapping("/findBySkuCode/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @GetMapping("findById/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto findInventoryById(@PathVariable("id") Long id) {
        return inventoryService.findByInventoryById(id);
    }

    @GetMapping("/findAll")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDto> findAllInventorys() {
        return inventoryService.findAll();
    }

    @PostMapping("/input")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponseDto inputInventory(@RequestBody InventoryRequestDto inventoryRequestDto) {
        return inventoryService.inputInventory(inventoryRequestDto);
    }

    @GetMapping("/inputByProdIdAndQty/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponseDto inputInventoryByProductId(@PathVariable("productId") String productId,
                                               @PathParam("quantity") Integer quantity)  {


        return inventoryService.inputInventoryByProductId(productId,quantity);
    }

    @PostMapping("/inputInventoryByProductSearchDto/{quantity}")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponseDto inputInventoryByProductSearchDto(@RequestBody ProductSearchDto productSearchDto,
                                                                 @PathVariable("quantity") Integer quantity
                                                                ) {
        return inventoryService.inputInventoryByProductSearchDto(productSearchDto,quantity);
    }

    @GetMapping("/findProdFromProductdb/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto findProdFromProductdb(@PathVariable("productId") String productId)
    {

        return productProxy.findProductByProductId(productId);
    }

    @GetMapping("/findProdFromInventorydbTimeout/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto findProdFromInventorydbTimeout(@PathVariable("productId") String productId)
    {
        log.info("Wait for start input inventory");
        try {
            Thread.sleep(10000L);
        } catch(InterruptedException e) {}

        log.info("Waiting ended");

        return inventoryService.findInventoryByProductId(productId);
    }
    @GetMapping("/findProdFromInventorydb/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto findProdFromInventorydb(@PathVariable("productId") String productId) throws InterruptedException
    {
       return inventoryService.findInventoryByProductId(productId);
    }
    @PutMapping("/updateQuantity/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public InventoryResponseDto updateInventory(@PathVariable("id") Long id,
                                              @PathParam("quantity") Integer quantity) {
        return inventoryService.updateInventoryByProductId(id,quantity);

    }

    /**
     *  avoid input product name in URL
     * @param inventoryRequestDto
     * @return
     */
    @PostMapping("/findInventoryByReqDto")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto findInventoryByName(@RequestBody InventoryRequestDto inventoryRequestDto) {
        if (inventoryRequestDto.getProductName()==null) {
            throw new InventoryException("Product Name in dto is required!");
        }
        return inventoryService.findInventoryByProductName(inventoryRequestDto.getProductName());
    }


}
