package com.nit.restcontroller;

import com.nit.dto.LoginRequest;
import com.nit.entity.Customer;
import com.nit.exception.UnauthorizedException;
import com.nit.service.CartItemService;
import com.nit.service.CartService;
import com.nit.service.CustomerService;
import com.nit.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/customer")
public class CustomerRestController {

    private final CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //Endpoint to login
    @PostMapping(value = "/login", produces = "text/plain", consumes = "application/json")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest, HttpSession session) {
        //To check customer is available or not
        Customer customer = customerService.loginCustomer(loginRequest.getEmail(), loginRequest.getPassword());

        if (customer != null) {
            //Adding customer reference into session
            session.setAttribute("customer", customer);
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("Incorrect email or password", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/",produces = "application/json")
    public ResponseEntity<Customer> getCustomer(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            throw new UnauthorizedException("User is not authenticated");
        }
        return ResponseEntity.ok(customer);
    }

    //Endpoint to register customer
    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> saveCustomer(@RequestBody @Valid Customer customer) {
        //Calling service class method to save customer
        Customer savedCustomer = customerService.registerCustomer(customer);
        //Condition to check customer is saved or not
        if (savedCustomer == null) {
            return ResponseEntity.internalServerError().body("Customer failed to register");
        }
        return new ResponseEntity<>("Customer registered successfully", HttpStatus.CREATED);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("Logout successful");
    }
}
