package com.oop.backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue(generator = "customer-id-generator")
    @GenericGenerator(name = "customer-id-generator", strategy = "com.oop.backend.IdGenerators.CustomerCustomIdGenerator")
    private String customerId;


    private String fullName;
    private String address;
    private String telephoneNumber;
    private String Gender ;
    @Column(length = 100, unique = true, nullable = false)
    private String email;
    private String password;
    private String token;
}
