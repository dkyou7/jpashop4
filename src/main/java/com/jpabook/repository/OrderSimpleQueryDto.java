package com.jpabook.repository;

import com.jpabook.domain.Address;
import com.jpabook.domain.Order;
import com.jpabook.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;    // 배송지 정보

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address){
        this.orderId = orderId;
        this.name = name; // Lazy 초기
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
