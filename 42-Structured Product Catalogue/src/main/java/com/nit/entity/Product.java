package com.nit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String name;

    @Column(length = 1000)
    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Double price;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    @NotEmpty
    @Valid
    private List<Category> categories;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    @NotEmpty
    @Valid
    private List<Attribute> attributes;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "product",fetch = FetchType.EAGER)
    @JsonManagedReference
    @NotNull
    @Valid
    private Availability availability;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    @NotEmpty
    @Valid
    private List<Rating> ratings;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonBackReference
    private List<CartItem> cartItems;
}
