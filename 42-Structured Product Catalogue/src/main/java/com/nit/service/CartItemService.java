package com.nit.service;

import com.nit.entity.CartItem;

public interface CartItemService {
    public void saveCartItem(CartItem cartItem);

    void deleteCartItem(CartItem cartItem);
}
