package com.nit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String productName;
    private Integer quantity;
    private Double totalAmount;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDate orderDate;

    private LocalDate shippingDate;
}
