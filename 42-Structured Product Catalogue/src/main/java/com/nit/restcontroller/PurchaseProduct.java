package com.nit.restcontroller;

import com.nit.entity.Customer;
import com.nit.exception.UnauthorizedException;
import com.nit.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PurchaseProduct {

    private final ProductService productService;
    //Constructor injection
    public PurchaseProduct(ProductService productService) {
        this.productService = productService;
    }

    //Endpoint to purchase the product
    @GetMapping(value = "/purchase")
    public ResponseEntity<String> buyProduct(@RequestParam String productName,
                                             @RequestParam Integer quantity,
                                             HttpSession session) throws Exception {
        //Fetching customer object from session
        Customer customer = (Customer) session.getAttribute("customer");
        //Condition to check customer is available or not
        if (customer == null) {
            throw new UnauthorizedException("Please login first");
        }
        Boolean purchaseProduct = productService.purchaseProduct(productName, quantity, customer);
        if (!purchaseProduct) {
            return new ResponseEntity<>("Product failed to purchase, please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Product "+productName+" successfully purchased", HttpStatus.OK);
    }
}
