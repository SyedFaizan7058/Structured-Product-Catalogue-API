package com.nit.service.impl;

import com.nit.dto.Filters;
import com.nit.entity.Order;
import com.nit.entity.*;
import com.nit.repository.*;
import com.nit.service.ProductService;
import com.nit.utils.SendEmailNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SendEmailNotification sendEmailNotification;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

/*
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private RatingRepository ratingRepository;
*/

    //Method to add or update the product
    @Override
    public Boolean upsertProduct(List<Product> product) {

        for (Product p : product) {
            // Set the product reference in attribute
            if (p.getAttributes() != null) {
                for (Attribute attribute : p.getAttributes()) {
                    attribute.setProduct(p);
                }
            }

            // Set the product reference in availability
            if (p.getAvailability() != null) {
                p.getAvailability().setProduct(p);
            }

            // Set the product reference in category
            if (p.getCategories() != null) {
                for (Category category : p.getCategories()) {
                    category.setProduct(p);
                }
            }

            // Set the product reference in ratings
            if (p.getRatings() != null) {
                for (Rating rating : p.getRatings()) {
                    rating.setProduct(p);
                }
            }
        }

        //Adding list of products at a time
        List<Product> products = productRepository.saveAll(product);
        return !products.isEmpty();
    }

    //Method to get all products
    @Override
    public List<Product> getAllProducts() {
        //To get all the products
        return productRepository.findAll();
    }

    //Method to delete product based on given id
    @Override
    public Boolean deleteProduct(Integer id) {

        //Checking product is available or not
        if (productRepository.existsById(id)) {
            //To delete the product based on given id
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

//    Method to filter the data by given fields using query by example
//    @Override
//    public List<Product> getProductsByFilter(Filters filters) {
//
//        //Setting the fields to product class
//        Product product = new Product();
//
//        Category category = new Category();
//        category.setCategoryName(filters.getCategoryName());
//        product.setCategories(List.of(category));
//        System.out.println(category);
//
//        Attribute attributes = filters.getAttributes();
//        product.setAttributes(List.of(attributes));
//        System.out.println(attributes);
//
//        System.out.println(filters.getAttributes());
//        System.out.println(filters.getCategoryName());
//        System.out.println(filters.getName());
//
//        product.setName(filters.getName());
//
//        //Using query by example filtering the data
//        Example<Product> productExample = Example.of(product);
//
//        return productRepository.findAll(productExample);
//    }

    //Method to filter the data based on given value using custom query
    @Override
    public List<Product> getProductsByFilter(Filters filters) {
        String name = filters.getName();
        String categoryName = filters.getCategoryName();
        Attribute attribute = filters.getAttributes();

        String attributeKey = attribute != null ? attribute.getKey() : null;
        String attributeValue = attribute != null ? attribute.getValue() : null;

        return productRepository.findByFilters(name, categoryName, attributeKey, attributeValue);
    }

    //Method to sort the data based on the product price in given order
    @Override
    public List<Product> getSortedProduct(String order) {
        int pageNumber = 2;
        int pageSize = 5;

        PageRequest pageRequest;

        if (order.equalsIgnoreCase("asc")) {
            // Sorting in ascending order
            pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.ASC, "price"));

        } else if (order.equalsIgnoreCase("desc")) {
            // Sorting in descending order
            pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "price"));
        } else {
            // If the order is neither "asc" nor "desc", return an empty list
            return Collections.emptyList();
        }
        // Return the paginated and sorted result
        return productRepository.findAll(pageRequest).getContent();
    }

    @Override
    public Optional<Product> findById(Integer productId) {
        return productRepository.findById(productId);
    }


    @Transactional
    @Override
    public Boolean purchaseProduct(String productName, Integer quantity, Customer customer) throws Exception {

        try {
            Product product = productRepository.findByName(productName);
            if (product == null) {
                throw new RuntimeException("Product not found");
            }
            Availability availability = product.getAvailability();
            Integer availableQty = availability.getQuantity();
            Boolean inStock = availability.getInStock();

            if (!inStock || availableQty < quantity) {
                throw new RuntimeException("Not enough stock available");
            }

            availability.setQuantity(availableQty - quantity);
            if (availability.getQuantity() == 0) {
                availability.setInStock(false);
            }
            //Updating the product
            upsertProduct(List.of(product));

            Order order = new Order();
            order.setProductName(product.getName());
            order.setQuantity(quantity);
            order.setTotalAmount(product.getPrice() * quantity);
            order.setShippingDate(LocalDate.now().plusDays(5));

            //Sending message to customer
            sendEmailNotification.sendOrderConfirmation(customer.getEmail(),
                    product.getName(),
                    quantity,
                    order.getTotalAmount(),
                    LocalDate.now(),
                    LocalDate.now().plusDays(5));

            sendEmailNotification.sendEmailToAdmin(availability.getQuantity(), productName);

            orderRepository.save(order);

            return true;

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

}
