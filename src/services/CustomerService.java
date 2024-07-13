package services;

import models.customers.Customer;

import java.util.ArrayList;
import java.util.Collection;

public class CustomerService {
  private static CustomerService INSTANCE;
  private final Collection<Customer> customerData = new ArrayList<>();

  private CustomerService() {
  }

  public static CustomerService getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CustomerService();
    }
    return INSTANCE;
  }

  public void addCustomer(String email, String firstName, String lastName) {
    customerData.add(new Customer(firstName, lastName, email));
  }

  public Customer getCustomer(String email) {
    for (Customer customer : customerData) {
      if (customer.getEmail().equals(email)) {
        return customer;
      }
    }
    return null;
  }

  public Collection<Customer> getAllCustomers() {
    return customerData;
  }
}
