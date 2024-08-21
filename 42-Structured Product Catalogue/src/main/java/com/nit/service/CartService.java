package com.nit.service;

import com.nit.entity.Cart;
import com.nit.entity.Customer;

public interface CartService {
    public Cart findCustomer(Customer customer);

    public void addToCart(Cart cart);
}
