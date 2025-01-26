package com.oop.backend.Controller;

import com.oop.backend.DTO.UserLogin;
import com.oop.backend.DTO.CustomerRegistration;
import com.oop.backend.DTO.VendorRegistration;
import com.oop.backend.Entities.CustomerEntity;
import com.oop.backend.Entities.VendorEntity;
import com.oop.backend.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/customerregister")
    public synchronized CustomerEntity registerCustomer(@RequestBody CustomerRegistration customerRegistration) {
        CustomerEntity registeredCustomerEntity = authService.registerCustomer(customerRegistration);
        if (registeredCustomerEntity == null) {
            throw new RuntimeException("Customer already exists");
        }
        return registeredCustomerEntity;
    }

    @PostMapping("/vendorregister")
    public synchronized VendorEntity registerVendor(@RequestBody VendorRegistration vendorRegistration) {
        VendorEntity registeredVendorEntity = authService.registerVendor(vendorRegistration);
        if (registeredVendorEntity == null) {
            throw new RuntimeException("Vendor already exists");
        }
        return registeredVendorEntity;
    }

    @PostMapping("/login")
    public synchronized Object login(@RequestBody UserLogin userLogin) {
        Object user = authService.login(userLogin);
        if (user == null) {
            throw new RuntimeException("Invalid username, password, or role");
        }
        System.out.println("login user: " + user);
        return user;
    }
}
