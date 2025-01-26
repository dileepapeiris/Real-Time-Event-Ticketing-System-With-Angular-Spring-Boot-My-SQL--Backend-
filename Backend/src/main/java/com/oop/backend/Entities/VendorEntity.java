package com.oop.backend.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendorEntity {
    @Id
    @GeneratedValue(generator = "vendor-id-generator")
    @GenericGenerator(name = "vendor-id-generator", strategy = "com.oop.backend.IdGenerators.VendorCustomIdGenerator")
    private String id;

    private String fullName;
    private String address;
    private String telephoneNumber;
    private String gender;

    @Column(length = 100, unique = true, nullable = false)
    private String email;
    private String password;
    private String token;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Handle bidirectional relationship
    private List<Event> events; // List of events created by the vendor
}
