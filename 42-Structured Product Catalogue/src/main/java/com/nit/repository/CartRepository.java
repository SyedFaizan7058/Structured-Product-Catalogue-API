package com.nit.repository;

import com.nit.entity.Cart;
import com.nit.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    public Cart findByCustomer(Customer customer);
}
