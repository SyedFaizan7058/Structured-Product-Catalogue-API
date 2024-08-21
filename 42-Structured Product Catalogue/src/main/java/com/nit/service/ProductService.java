package com.nit.service;

import com.nit.dto.Filters;
import com.nit.entity.Customer;
import com.nit.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    public Boolean upsertProduct(List<Product> product);
    public List<Product> getAllProducts();
    public Boolean deleteProduct(Integer id);
    public List<Product> getProductsByFilter(Filters filters);
    public List<Product> getSortedProduct(String order);

    Optional<Product> findById(Integer productId);

    @Transactional
    Boolean purchaseProduct(String productName, Integer quantity, Customer customer) throws Exception;
}
