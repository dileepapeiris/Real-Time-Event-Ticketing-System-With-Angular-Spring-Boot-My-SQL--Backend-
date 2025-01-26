package com.oop.backend.Service;

import com.oop.backend.DTO.UserLogin;
import com.oop.backend.DTO.CustomerRegistration;
import com.oop.backend.DTO.VendorRegistration;
import com.oop.backend.Entities.CustomerEntity;
import com.oop.backend.Entities.VendorEntity;
import com.oop.backend.Repository.CustomerRepository;
import com.oop.backend.Repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AuthService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VendorRepository vendorRepository;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public CustomerEntity registerCustomer(CustomerRegistration customerRegistration) {
        if (customerRepository.existsByEmail(customerRegistration.getEmail())) {
            return null;
        }

        CustomerEntity customerEntity = mapCustomerDTOToEntity(customerRegistration);
        customerEntity.setToken(generateToken());
        return customerRepository.save(customerEntity);
    }

    public VendorEntity registerVendor(VendorRegistration vendorRegistration) {
        if (vendorRepository.existsByEmail(vendorRegistration.getEmail())) {
            return null;
        }

        VendorEntity vendorEntity = mapVendorDTOToEntity(vendorRegistration);
        vendorEntity.setToken(generateToken());
        return vendorRepository.save(vendorEntity);
    }

    public Object login(UserLogin userLogin) {
        if ("customer".equalsIgnoreCase(userLogin.getRole())) {
            CustomerEntity existingCustomerEntity = customerRepository.findByEmail(userLogin.getEmail());
            if (existingCustomerEntity != null &&
                    existingCustomerEntity.getPassword().equals(userLogin.getPassword())) {
                existingCustomerEntity.setPassword(""); // Hide password in response
                return existingCustomerEntity;
            }
        } else if ("vendor".equalsIgnoreCase(userLogin.getRole())) {
            VendorEntity existingVendorEntity = vendorRepository.findByEmail(userLogin.getEmail());
            if (existingVendorEntity != null &&
                    existingVendorEntity.getPassword().equals(userLogin.getPassword())) {
                existingVendorEntity.setPassword(""); // Hide password in response
                return existingVendorEntity;
            }
        }
        return null;
    }

    private String generateToken() {
        byte[] token = new byte[24];
        secureRandom.nextBytes(token);
        return base64Encoder.encodeToString(token);
    }

    private CustomerEntity mapCustomerDTOToEntity(CustomerRegistration dto) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFullName(dto.getFullName());
        customerEntity.setAddress(dto.getAddress());
        customerEntity.setTelephoneNumber(dto.getTelephoneNumber());
        customerEntity.setGender(dto.getGender());
        customerEntity.setEmail(dto.getEmail());
        customerEntity.setPassword(dto.getPassword());
        return customerEntity;
    }

    private VendorEntity mapVendorDTOToEntity(VendorRegistration dto) {
        VendorEntity vendorEntity = new VendorEntity();
        vendorEntity.setFullName(dto.getFullName());
        vendorEntity.setAddress(dto.getAddress());
        vendorEntity.setTelephoneNumber(dto.getTelephoneNumber());
        vendorEntity.setGender(dto.getGender());
        vendorEntity.setEmail(dto.getEmail());
        vendorEntity.setPassword(dto.getPassword());
        return vendorEntity;
    }

    public VendorEntity validateVendorToken(String token) {
    System.out.println("Validating token: " + token);  // Log the token value for debugging
    return vendorRepository.findByToken(token); // Assuming `findByToken` exists
}

    public CustomerEntity validateCustomerToken(String token) {
    System.out.println("Validating token: " + token);  // Log the token value for debugging
    return customerRepository.findByToken(token); // Assuming `findByToken` exists
    }

}


