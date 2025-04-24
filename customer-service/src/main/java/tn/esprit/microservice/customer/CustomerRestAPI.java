package tn.esprit.microservice.customer;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerRestAPI {

    private final CustomerService customerService;

    public CustomerRestAPI(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.addCustomer(customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Customer>> getCustomerById(@PathVariable int id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }


    @GetMapping("/by-email")
    public ResponseEntity<Optional<Customer>> getCustomerByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Customer Microservice!";
    }

// CustomerRestAPI.java

    @GetMapping("/search/nom")
    public ResponseEntity<List<Customer>> searchByNom(@RequestParam String nom) {
        return ResponseEntity.ok(customerService.searchByNom(nom));
    }

    @GetMapping("/search/telephone")
    public ResponseEntity<List<Customer>> searchByTelephone(@RequestParam String tel) {
        return ResponseEntity.ok(customerService.searchByTelephone(tel));
    }

    @GetMapping("/stats")
    public ResponseEntity<String> getStats() {
        long total = customerService.getTotalCustomers();
        return ResponseEntity.ok("Total customers: " + total);
    }


}
