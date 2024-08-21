package com.nit.restcontroller;

import com.nit.entity.Cart;
import com.nit.entity.CartItem;
import com.nit.entity.Customer;
import com.nit.entity.Product;
import com.nit.exception.UnauthorizedException;
import com.nit.service.CartItemService;
import com.nit.service.CartService;
import com.nit.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AddToCartRestController {

    private final CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    public AddToCartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    //Endpoint to add product to customer cart
    @GetMapping(value = "/{productId}/addToCart")
    public ResponseEntity<String> addToCart(@PathVariable Integer productId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            throw new UnauthorizedException("Please login to customer account for next process");
        }

        Optional<Product> productOpt = productService.findById(productId);

        if (productOpt.isEmpty()) {
            return new ResponseEntity<>("Product not found.", HttpStatus.NOT_FOUND);
        }

        Product product = productOpt.get();
        System.out.println("Product Name: " + product.getName());

        Cart cart = cartService.findCustomer(customer);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
            cart.setItems(new HashSet<>());
        }

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElse(new CartItem(product, 0));

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItem.setCart(cart);
        cart.getItems().add(cartItem);

        cartService.addToCart(cart);
        cartItemService.saveCartItem(cartItem);

        return new ResponseEntity<>("Product added to cart successfully.", HttpStatus.OK);
    }

    // Endpoint to remove product from customer cart
    @GetMapping(value = "/{productId}/removeFromCart")
    public ResponseEntity<String> removeFromCart(@PathVariable Integer productId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            throw new UnauthorizedException("Please login to customer account for next process");
        }

        Optional<Product> productOpt = productService.findById(productId);

        if (productOpt.isEmpty()) {
            return new ResponseEntity<>("Product not found.", HttpStatus.NOT_FOUND);
        }

        Product product = productOpt.get();
        System.out.println("Product Name: " + product.getName());

        Cart cart = cartService.findCustomer(customer);
        if (cart == null) {
            return new ResponseEntity<>("Cart not found.", HttpStatus.NOT_FOUND);
        }

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            return new ResponseEntity<>("Product not found in cart.", HttpStatus.NOT_FOUND);
        }

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            // Update cart with the modified cartItem
            cartService.addToCart(cart);
            // Update the cartItem in the database
            cartItemService.saveCartItem(cartItem);
            return new ResponseEntity<>("Product quantity decreased by 1.", HttpStatus.OK);
        } else {
            cart.getItems().remove(cartItem);
            // Update cart after removal
            cartService.addToCart(cart);
            // Remove the cartItem from the database
            cartItemService.deleteCartItem(cartItem);
            return new ResponseEntity<>("Product removed from cart successfully.", HttpStatus.OK);
        }
    }


}
