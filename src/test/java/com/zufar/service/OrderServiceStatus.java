package com.zufar.service;

import com.zufar.domain.customer.Customer;
import com.zufar.domain.item.Item;
import com.zufar.domain.order.Order;
import com.zufar.domain.order.OrderRepository;
import com.zufar.domain.order.OrderService;
import com.zufar.domain.product.Product;
import com.zufar.domain.status.Status;
import com.zufar.dto.OrderDTO;
import com.zufar.dto.StatusDTO;
import com.zufar.dto.ItemDTO;
import com.zufar.dto.ProductDTO;
import com.zufar.dto.CustomerDTO;
import com.zufar.exception.OrderNotFoundException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;


@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceStatus {

    @Autowired
    @Qualifier("orderService")
    private DaoService<OrderDTO> orderService;

    @MockBean
    private OrderRepository orderRepository;

    private static final long ORDER_ID = 1L;
    private static final String ORDER_TITLE = "My first order";
    private static final LocalDateTime ORDER_CREATION_DATE_TIME = LocalDateTime
            .of(2019, 9, 1, 5, 2, 4);
    private static final LocalDateTime ORDER_FINISHED_DATE_TIME = LocalDateTime
            .of(2020, 10, 11, 4, 6, 3);
    private static Collection<Order> ENTITY_ORDERS = new ArrayList<>();
    private static Collection<OrderDTO> ORDERS = new ArrayList<>();
    private static OrderDTO ORDER;
    private static Order ORDER_ENTITY;

    @BeforeClass
    public static void setUp() {
        ORDER_ENTITY = getOrderEntity();
        ENTITY_ORDERS.add(ORDER_ENTITY);
        ORDER = getOrder();
        ORDERS.add(ORDER);
    }

    @Test
    public void whenGetAllCalledOrdersShouldReturned() {
        when(orderRepository.findAll()).thenReturn(ENTITY_ORDERS);
        Collection<OrderDTO> actual = orderService.getAll();

        verify(orderRepository, times(1)).findAll();
        assertNotNull(actual);
        assertEquals(ORDERS, actual);
    }

    @Test
    public void whenGetAllWithSortByCalledOrdersShouldReturned() {
        List<Order> orders = ENTITY_ORDERS
                .stream()
                .sorted(Comparator.comparing(Order::getId))
                .collect(Collectors.toList());
        when(orderRepository.findAll(Sort.by("id"))).thenReturn(orders);
        Collection<OrderDTO> actual = orderService.getAll("id");

        verify(orderRepository,  times(1)).findAll(Sort.by("id"));
        assertNotNull(actual);
        assertEquals(ORDERS, actual);


        orders = ENTITY_ORDERS
                .stream()
                .sorted(Comparator.comparing(Order::getCreationDate))
                .collect(Collectors.toList());
        when(orderRepository.findAll(Sort.by("creationDate"))).thenReturn(orders);
        actual = orderService.getAll("creationDate");

        verify(orderRepository, times(1)).findAll(Sort.by("creationDate"));
        assertNotNull(actual);
        assertEquals(ORDERS, actual);
    }

    @Test
    public void whenGetByIdCalledOrderShouldReturned() {
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(ORDER_ENTITY));
        OrderDTO actual = orderService.getById(ORDER_ID);

        verify(orderRepository, times(1)).findById(ORDER_ID);
        assertNotNull(actual);
        assertEquals(ORDER, actual);
    }

    @Test(expected = OrderNotFoundException.class)
    public void whenGetByInvalidIdCalledThrowOrderNotFoundException() {
        long INVALID_STATUS_ID = 55555L;
        when(orderRepository.existsById(INVALID_STATUS_ID)).thenReturn(false);
        orderService.getById(INVALID_STATUS_ID);
    }

    @Test
    public void whenSaveCalledThenOrderShouldBeReturned() {
        Order orderEntity = ORDER_ENTITY;
        orderEntity.setId(null);
        when(orderRepository.save(orderEntity)).thenReturn(ORDER_ENTITY);
        OrderDTO order = ORDER;
        order.setId(null);
        OrderDTO actual = orderService.save(order);

        verify(orderRepository, times(1)).save(orderEntity);
        assertNotNull(actual);
        assertEquals(ORDER, actual);
    }

    @Test
    public void whenUpdateCalledThenOrderShouldBeReturned() {
        when(orderRepository.save(ORDER_ENTITY)).thenReturn(ORDER_ENTITY);
        when(orderRepository.existsById(ORDER_ID)).thenReturn(true);
        OrderDTO actual = orderService.update(ORDER);

        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verify(orderRepository, times(1)).save(ORDER_ENTITY);
        assertNotNull(actual);
        assertEquals(ORDER, actual);
    }

    @Test
    public void whenConvertOrderEntityCalledThenOrderDTOShouldBeReturned() {
        OrderDTO actual = OrderService.convertToOrderDTO(ORDER_ENTITY);

        assertNotNull(actual);
        assertEquals(ORDER, actual);
    }

    @Test
    public void whenConvertOrderDTOCalledThenOrderEntityShouldBeReturned() {
        Order actual = OrderService.convertToOrder(ORDER);

        assertNotNull(actual);
        assertEquals(ORDER_ENTITY, actual);
    }

    @Test
    public void whenDeleteOrderByIdCalled() {
        when(orderRepository.existsById(ORDER_ID)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(ORDER_ID);
        orderService.deleteById(ORDER_ID);

        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verify(orderRepository, times(1)).deleteById(ORDER_ID);
    }

    private static Order getOrderEntity() { 
        Status STATUS_ENTITY = new Status(1L, "Active");
        Customer CUSTOMER_ENTITY = new Customer(1L, "Zufar Sunagatov", "zuf999@mail.ru", "Alice", 
                "YOU_KNOW");
        Product PIZZA_PRODUCT_ENTITY = new Product(1L, "Pizza");
        Product JUICE_PRODUCT_ENTITY = new Product(1L, "JUICE");
        Set<Item> ENTITY_ITEMS = new HashSet<>();
        Item PIZZA_ITEM_ENTITY = new Item(1L, PIZZA_PRODUCT_ENTITY, 3L);
        Item JUICE_ITEM_ENTITY = new Item(2L, JUICE_PRODUCT_ENTITY, 5L);
        ENTITY_ITEMS.add(PIZZA_ITEM_ENTITY);
        ENTITY_ITEMS.add(JUICE_ITEM_ENTITY);
        return new Order(ORDER_ID, ORDER_TITLE, ORDER_CREATION_DATE_TIME, ORDER_FINISHED_DATE_TIME, STATUS_ENTITY,
                CUSTOMER_ENTITY, ENTITY_ITEMS);
    }

    private static OrderDTO getOrder() {
        StatusDTO STATUS = new StatusDTO(1L, "Active");
        CustomerDTO CUSTOMER = new CustomerDTO(1L, "Zufar Sunagatov", "zuf999@mail.ru", "Alice",
                "YOU_KNOW");
        ProductDTO PIZZA_PRODUCT = new ProductDTO(1L, "Pizza");
        ProductDTO JUICE_PRODUCT = new ProductDTO(1L, "JUICE");
        Set<ItemDTO> ITEMS = new HashSet<>();
        ItemDTO PIZZA_ITEM = new ItemDTO(1L, PIZZA_PRODUCT, 3L);
        ItemDTO JUICE_ITEM = new ItemDTO(2L, JUICE_PRODUCT, 5L);
        ITEMS.add(PIZZA_ITEM);
        ITEMS.add(JUICE_ITEM);
        return new OrderDTO(ORDER_ID, ORDER_TITLE, ORDER_CREATION_DATE_TIME, ORDER_FINISHED_DATE_TIME, STATUS, CUSTOMER,
                ITEMS);
    }
}
