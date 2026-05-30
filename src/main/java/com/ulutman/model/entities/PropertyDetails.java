package com.ulutman.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ulutman.model.enums.TransportType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "propertyDetails")
@Getter
@Setter
@NoArgsConstructor
public class PropertyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalArea;

    private Double livingArea;

    private Double ceilingHeight;

    private String layout;

    private String bathroomType;

    private Boolean hasBalcony;

    private String viewFromWindow;

    private Integer yearOfConstruction;

    private Boolean hasGarbageChute;

    private Integer numberOfElevators;

    private String buildingType;

    private String overlappingType;

    private Boolean hasParking;

    private Integer entrances;

    private String heatingType;

    private Boolean isEmergency;

    private Boolean hasRefrigerator;

    private Boolean hasWashingMachine;

    private Boolean hasTelevision;

    private Boolean hasShower;

    private Boolean hasFurnitureInRooms;

    private Boolean hasDishWasher;

    private Boolean hasAirConditioner;

    private Boolean hasInternet;

    private Boolean hasKitchenFurniture;

    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    private Double kitchenArea;

    private Integer walkingDistance;

    private Integer transportDistance;

    private String district;

    private String quantity;

    private Integer floor;

    @JsonBackReference
    @OneToOne(mappedBy = "propertyDetails", fetch = FetchType.LAZY)
    private Publish publish;
}
