package com.nit.service;

import com.nit.entity.Customer;

public interface CustomerService {
    public Customer registerCustomer(Customer customer);
    public Customer loginCustomer(String email, String password);
}
