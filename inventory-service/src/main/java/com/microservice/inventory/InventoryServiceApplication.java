package com.microservice.inventory;

import com.microservice.inventory.model.Inventory;
import com.microservice.inventory.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class InventoryServiceApplication {

	/**
	 * http://localhost:8082/inventorys/findById/1
	 * POST
	 * http://localhost:8082/inventorys/input
	 *
	 *  {
	 *
	 *                 "productId": "63e2b10db5176c6409fcb611",
	 *                 "productName": "Tesla Model S 2020",
	 *                 "quantity": 100,
	 *                 "skuCode" : "Tesla Model S 2020"
	 *
	 * }
	 *
	 * GET
	 * http://localhost:8082/inventorys/findProdFromProductdb/63e2b10db5176c6409fcb611
	 *
	 * GET
	 * http://localhost:8082/inventorys/inputByProdIdAndQty/63e2afdbb5176c6409fcb610?quantity=1250
	 *
	 * http://localhost:8000/inventorys/inputInventoryByProductSearchDto/2455
	 *   {
	 *          "productId": "63e5449e45982a1edee6a4c6",
	 *         "productName": ""
	 *     }
	 *
	 *
	 * POST
	 * http://localhost:8082/inventorys//inputInventoryByProductSearchDto/quantity=310
	 * {
	 *     "
	 * }
	 *
	 * {
	 *
	 *                 "productId": "63e2afdbb5176c6409fcb610",
	 *                 "productName": "HP 2000",
	 *                 "quantity": 200,
	 *                 "skuCode" : "HP 2000"
	 *
	 * }
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
/*	@Bean
	public CommandLineRunner loadData(InventoryRepository repository) {
		return args -> {
			Inventory inventory = Inventory.builder()
					.skuCode("Iphone-14")
					.quantity(200).build();
			Inventory inventory1 = Inventory.builder()
					.skuCode("iWatch")
					.quantity(0).build();
			repository.save(inventory);
			repository.save(inventory1);

		};

	}*/
}
