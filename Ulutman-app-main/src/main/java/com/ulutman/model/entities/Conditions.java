package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conditions")
@Getter
@Setter
@NoArgsConstructor
public class Conditions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double pricePerMonth;

    private String utilitiesIncluded;

    private Double deposit;

    private String commission;

    private String prepayment;

    private String leaseTerm;

    private boolean showPhoneNumber;

    private String realtor;

    private Double realtorRating;

    @JsonBackReference
    @OneToOne(mappedBy = "conditions",fetch = FetchType.LAZY)
    private Publish publish;
}
