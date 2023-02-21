package com.microservice.order.service.feignclient;

import com.microservice.order.service.dto.InventoryRequestDto;
import com.microservice.order.service.dto.InventoryResponseDto;
import com.microservice.order.service.dto.ProductResponseDto;
import com.microservice.order.service.dto.ProductSearchDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@FeignClient(name="inventory-services")
public interface InventoryProxy {
    @GetMapping("/inventorys/findBySkuCode/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCode);

    @GetMapping("/inventorys/findById/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto findInventoryById(@PathVariable("id") Long id) ;

    @GetMapping("/inventorys/findAll")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDto> findAllInventorys() ;

    @PostMapping("/inventorys/input")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponseDto inputInventory(@RequestBody InventoryRequestDto inventoryRequestDto);

    @GetMapping("/inventorys/inputByProdIdAndQty/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponseDto inputInventoryByProductId(@PathVariable("productId") String productId,
                                                          @PathParam("quantity") Integer quantity);

    @PostMapping("/inventorys/inputInventoryByProductSearchDto/{quantity}")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponseDto inputInventoryByProductSearchDto(@RequestBody ProductSearchDto productSearchDto,
                                                                 @PathVariable("quantity") Integer quantity
    ) ;

    @GetMapping("/inventorys/findProdFromProductdb/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto findProdFromProductdb(@PathVariable("productId") String productId);
    @GetMapping("/inventorys/findProdFromInventorydbTimeout/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto findProdFromInventorydbTimeout(@PathVariable("productId") String productId);
    @GetMapping("/inventorys/findProdFromInventorydb/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto findProdFromInventorydb(@PathVariable("productId") String productId);
    @PutMapping("/inventorys/updateQuantity/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public InventoryResponseDto updateInventory(@PathVariable("id") Long id,
                                                @PathParam("quantity") Integer quantity);

    /**
     *  avoid input product name in URL
     * @param inventoryRequestDto
     * @return
     */
    @PostMapping("/inventorys/findInventoryByReqDto")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto findInventoryByName(@RequestBody InventoryRequestDto inventoryRequestDto) ;

}
