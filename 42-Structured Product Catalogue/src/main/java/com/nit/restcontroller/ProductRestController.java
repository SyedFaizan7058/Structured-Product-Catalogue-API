package com.nit.restcontroller;

import com.nit.dto.Filters;
import com.nit.entity.Product;
import com.nit.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {

    private final ProductService productService;

    //Constructor injection
    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    //Endpoint to add product
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addProduct(@RequestBody @Valid List<Product> products) {
        //Sending list of products to service class to store the products in database
        Boolean b = productService.upsertProduct(products);
        //Condition to check products is saved or not
        if (b) {
            return new ResponseEntity<>("Products saved Successfully", HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Failed to store the products", HttpStatus.BAD_REQUEST);
    }

    //Endpoint to get all the products
    @GetMapping(value = "/products", produces = "application/json")
    public ResponseEntity<List<Product>> getAllProducts() {
        //Taking all products from database and sending the list of products as response with status code 200
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    //Endpoint to delete product based on given id
    @DeleteMapping(value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        //Sending product id to delete product
        Boolean deletedProduct = productService.deleteProduct(id);
        //Condition to check product is deleted or not
        if (deletedProduct)
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("Product with given id " + id + " is not found", HttpStatus.NOT_FOUND);
    }

    //Endpoint to get products by given filters
    @PostMapping(value = "/search", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> getProductsByFilter(@RequestBody Filters filters) {

        /*Filters is a user-defined class*/
        //Taking the data from database
        List<Product> productsByFilter = productService.getProductsByFilter(filters);
        if (productsByFilter.isEmpty()) {
            return new ResponseEntity<>("Product not available with given filters", HttpStatus.NOT_FOUND);
        }
        //If productsByFilter is not empty then sending list as a response with status code 200
        return new ResponseEntity<>(productsByFilter, HttpStatus.OK);
    }

    //Endpoint to get sorted products based on price in given order i.e(Ascending or Descending)
    @GetMapping(value = "/sort/{order}", produces = "application/json")
    public ResponseEntity<List<Product>> sortProduct(@PathVariable("order") String order) {
        //Taking the data from database
        List<Product> sortedProduct = productService.getSortedProduct(order);
        if (sortedProduct.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //If sortedProduct is not empty then sending list as a response with status code 200
        return ResponseEntity.ok().body(sortedProduct);
    }
}
