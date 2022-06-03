package com.restauran.delivery;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.restauran.delivery.repositories.CompletedOrdersRepository;
import com.restauran.delivery.repositories.FavouriteProductRepository;
import com.restauran.delivery.repositories.OrderItemsRepository;
import com.restauran.delivery.repositories.OrderRepository;
import com.restauran.delivery.repositories.PersonalDataRepository;
import com.restauran.delivery.repositories.ProductsRepository;
import com.restauran.delivery.repositories.ShoppingCartRepository;
import com.restauran.delivery.repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class DatabasesTests {

	@Autowired
	CompletedOrdersRepository completedOrdersRepository;

	@Autowired
	FavouriteProductRepository favouriteProductRepository;

	@Autowired
	OrderItemsRepository orderItemsRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	PersonalDataRepository personalDataRepository;

	@Autowired
	ProductsRepository productsRepository;

	@Autowired
	ShoppingCartRepository shoppingCartRepository;

	@Autowired
	UserRepository userRepository;

	@Test
	void testDatabases() {
		
		assertTrue(completedOrdersRepository != null);
		assertTrue(favouriteProductRepository != null);
		assertTrue(orderItemsRepository != null);
		assertTrue(orderRepository != null);
		assertTrue(personalDataRepository != null);
		assertTrue(productsRepository != null);
		assertTrue(shoppingCartRepository != null);
		assertTrue(userRepository != null);
	}

}
