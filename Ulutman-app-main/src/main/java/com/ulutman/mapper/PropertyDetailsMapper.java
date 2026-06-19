package com.ulutman.mapper;

import com.ulutman.model.dto.PropertyDetailsRequest;
import com.ulutman.model.dto.PropertyDetailsResponse;
import com.ulutman.model.entities.PropertyDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertyDetailsMapper {

    public PropertyDetails mapToEntity(PropertyDetailsRequest request){
        PropertyDetails propertyDetails = new PropertyDetails();
        propertyDetails.setTotalArea(request.getTotalArea());
        propertyDetails.setLivingArea(request.getLivingArea());
        propertyDetails.setCeilingHeight(request.getCeilingHeight());
        propertyDetails.setLayout(request.getLayout());
        propertyDetails.setBathroomType(request.getBathroomType());
        propertyDetails.setHasBalcony(request.getHasBalcony());
        propertyDetails.setViewFromWindow(request.getViewFromWindow());
        propertyDetails.setYearOfConstruction(request.getYearOfConstruction());
        propertyDetails.setHasGarbageChute(request.getHasGarbageChute());
        propertyDetails.setNumberOfElevators(request.getNumberOfElevators());
        propertyDetails.setBuildingType(request.getBuildingType());
        propertyDetails.setOverlappingType(request.getOverlappingType());
        propertyDetails.setHasParking(request.getHasParking());
        propertyDetails.setEntrances(request.getEntrances());
        propertyDetails.setHeatingType(request.getHeatingType());
        propertyDetails.setIsEmergency(request.getIsEmergency());
        propertyDetails.setHasRefrigerator(request.getHasRefrigerator());
        propertyDetails.setHasWashingMachine(request.getHasWashingMachine());
        propertyDetails.setHasTelevision(request.getHasTelevision());
        propertyDetails.setHasShower(request.getHasShower());
        propertyDetails.setHasFurnitureInRooms(request.getHasFurnitureInRooms());
        propertyDetails.setHasDishWasher(request.getHasDishWasher());
        propertyDetails.setHasAirConditioner(request.getHasAirConditioner());
        propertyDetails.setHasInternet(request.getHasInternet());
        propertyDetails.setHasKitchenFurniture(request.getHasKitchenFurniture());
        propertyDetails.setTransportType(request.getTransportType());
        propertyDetails.setKitchenArea(request.getKitchenArea());
        propertyDetails.setWalkingDistance(request.getWalkingDistance());
        propertyDetails.setTransportDistance(request.getTransportDistance());
        propertyDetails.setDistrict(request.getDistrict());
        propertyDetails.setQuantity(request.getQuantity());
        propertyDetails.setFloor(request.getFloor());
        return propertyDetails;
    }

    public PropertyDetailsResponse mapToResponse(PropertyDetails propertyDetails) {
        return PropertyDetailsResponse.builder()
                .id(propertyDetails.getId())
                .totalArea(propertyDetails.getTotalArea())
                .livingArea(propertyDetails.getLivingArea())
                .ceilingHeight(propertyDetails.getCeilingHeight())
                .layout(propertyDetails.getLayout())
                .bathroomType(propertyDetails.getBathroomType())
                .hasBalcony(propertyDetails.getHasBalcony())
                .viewFromWindow(propertyDetails.getViewFromWindow())
                .yearOfConstruction(propertyDetails.getYearOfConstruction())
                .hasGarbageChute(propertyDetails.getHasGarbageChute())
                .numberOfElevators(propertyDetails.getNumberOfElevators())
                .buildingType(propertyDetails.getBuildingType())
                .overlappingType(propertyDetails.getOverlappingType())
                .hasParking(propertyDetails.getHasParking())
                .entrances(propertyDetails.getEntrances())
                .heatingType(propertyDetails.getHeatingType())
                .isEmergency(propertyDetails.getIsEmergency())
                .hasRefrigerator(propertyDetails.getHasRefrigerator())
                .hasWashingMachine(propertyDetails.getHasWashingMachine())
                .hasTelevision(propertyDetails.getHasTelevision())
                .hasShower(propertyDetails.getHasShower())
                .hasFurnitureInRooms(propertyDetails.getHasFurnitureInRooms())
                .hasDishwasher(propertyDetails.getHasDishWasher()) // Обратите внимание на правильное название
                .hasAirConditioner(propertyDetails.getHasAirConditioner())
                .hasInternet(propertyDetails.getHasInternet())
                .hasKitchenFurniture(propertyDetails.getHasKitchenFurniture())
                .transportType(propertyDetails.getTransportType())
                .kitchenArea(propertyDetails.getKitchenArea())
                .walkingDistance(propertyDetails.getWalkingDistance())
                .transportDistance(propertyDetails.getTransportDistance())
                .district(propertyDetails.getDistrict())
                .quantity(propertyDetails.getQuantity())
                .floor(propertyDetails.getFloor())
                .build();
    }
}
