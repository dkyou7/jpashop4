package com.jpabook.api;

import com.jpabook.domain.*;
import com.jpabook.repository.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/v1/orders")
    public List<Order> ordersV1(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        for (Order order : orders){
            order.getMember().getName();
            order.getDelivery().getAddress();
            order.getOrderItems().stream().forEach(o->o.getItem().getName());
        }
        return orders;
    }

    @GetMapping("/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> collect = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset",defaultValue = "0") int offset,
            @RequestParam(value = "limit",defaultValue = "100") int limit){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,limit);

        List<OrderDto> collect = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    @Getter
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;


        public OrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName(); // Lazy 초기
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            order.getOrderItems().forEach(o -> o.getItem().getName());
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new).collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto{

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}

