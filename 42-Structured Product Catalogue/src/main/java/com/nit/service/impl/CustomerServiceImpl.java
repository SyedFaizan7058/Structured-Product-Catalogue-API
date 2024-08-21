package com.nit.service.impl;

import com.nit.entity.Customer;
import com.nit.repository.CustomerRepository;
import com.nit.repository.ProductRepository;
import com.nit.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer registerCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer loginCustomer(String email, String password) {
        Customer byEmail = customerRepository.findByEmail(email);
        if(byEmail.getPassword().equals(password)){
            return byEmail;
        }
        return null;
    }

}
