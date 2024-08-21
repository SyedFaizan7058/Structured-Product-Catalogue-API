package com.nit.service.impl;

import com.nit.entity.CartItem;
import com.nit.repository.CartItemRepository;
import com.nit.service.CartItemService;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void saveCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        cartItemRepository.deleteById(cartItem.getId());
    }
}
