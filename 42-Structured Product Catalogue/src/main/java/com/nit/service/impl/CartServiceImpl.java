package com.nit.service.impl;

import com.nit.entity.Cart;
import com.nit.entity.Customer;
import com.nit.repository.CartRepository;
import com.nit.service.CartService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart findCustomer(Customer customer) {
        return cartRepository.findByCustomer(customer);
    }

    @Override
    public void addToCart(Cart cart) {
        cartRepository.save(cart);
    }
}
