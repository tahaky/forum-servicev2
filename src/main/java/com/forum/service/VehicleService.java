package com.forum.service;

import com.forum.dto.CreateVehicleRequest;
import com.forum.dto.VehicleResponse;
import com.forum.entity.Vehicle;
import com.forum.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Transactional(readOnly = true)
    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehicleResponse createVehicle(CreateVehicleRequest request) {
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(request.getBrand());
        return toResponse(vehicleRepository.save(vehicle));
    }

    private VehicleResponse toResponse(Vehicle vehicle) {
        return new VehicleResponse(vehicle.getId(), vehicle.getBrand());
    }
}
