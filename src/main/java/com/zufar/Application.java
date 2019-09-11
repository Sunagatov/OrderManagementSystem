package com.zufar;

import com.zufar.domain.customer.CustomerService;
import com.zufar.dto.*;
import com.zufar.domain.item.ItemService;
import com.zufar.domain.order.OrderService;
import com.zufar.domain.product.ProductService;
import com.zufar.domain.status.StatusService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        final ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        final StatusService statusService = applicationContext.getBean(StatusService.class);
        StatusDTO status = new StatusDTO();
        status.setName("Ахуенно!");
        status = statusService.save(status);
        System.out.println(status);

        ProductDTO product = new ProductDTO();
        product.setName("Продукт!");
        final ProductService productService = applicationContext.getBean(ProductService.class);
        product = productService.save(product);
        System.out.println(product);

        CustomerDTO customer = new CustomerDTO();
        customer.setName("Клиент!");
        customer.setLogin("user_login");
        customer.setEmail("user@mail.com");
        customer.setPassword("qwerty");
        final CustomerService customerService = applicationContext.getBean(CustomerService.class);
        customer = customerService.save(customer);
        System.out.println(customer);

        OrderItemDTO orderItem = new OrderItemDTO();
        orderItem.setQuantity(4L);
        orderItem.setProduct(product);
        final ItemService itemService = applicationContext.getBean(ItemService.class);
        orderItem = itemService.save(orderItem);
        System.out.println(orderItem);

        OrderDTO order = new OrderDTO();
        order.setTitle("Мой первый заказ.");
        order.setCreationDate(LocalDateTime.now());
        order.setLastModifiedDate(LocalDateTime.now());
        order.setCustomer(customer);
        order.setStatus(status);
        Set<OrderItemDTO> orderItems = new HashSet<>();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);
        final OrderService orderService = applicationContext.getBean(OrderService.class);
        order = orderService.save(order);
        System.out.println(order);
    }
}
