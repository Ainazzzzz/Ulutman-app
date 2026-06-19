package com.ulutman.model.dto;

import com.ulutman.model.enums.TransportType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertyDetailsResponse {

    Long id;

    Double totalArea;

    Double livingArea;

    Double ceilingHeight;

    String layout;

    String bathroomType;

    Boolean hasBalcony;

    String viewFromWindow;

    Integer yearOfConstruction;

    Boolean hasGarbageChute;

    Integer numberOfElevators;

    String buildingType;

    String overlappingType;

    Boolean hasParking;

    Integer entrances;

    String heatingType;

    Boolean isEmergency;

    Boolean hasRefrigerator;

    Boolean hasWashingMachine;

    Boolean hasTelevision;

    Boolean hasShower;

    Boolean hasFurnitureInRooms;

    Boolean hasDishwasher;

    Boolean hasAirConditioner;

    Boolean hasInternet;

    Boolean hasKitchenFurniture;

    @Enumerated(EnumType.STRING)
    TransportType transportType;

    Double kitchenArea;

    Integer walkingDistance;

    Integer transportDistance;

    String district;

    String quantity;

    Integer floor;
}
