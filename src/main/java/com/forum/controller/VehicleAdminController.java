package com.forum.controller;

import com.forum.dto.CreateVehicleBrandRequest;
import com.forum.dto.VehicleBrandResponse;
import com.forum.service.VehicleAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class VehicleAdminController {

    private final VehicleAdminService vehicleAdminService;

    @PostMapping("/brands")
    public ResponseEntity<VehicleBrandResponse> createBrand(@RequestBody CreateVehicleBrandRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleAdminService.createBrand(request));
    }

    @GetMapping("/brands")
    public ResponseEntity<List<VehicleBrandResponse>> getAllBrands() {
        return ResponseEntity.ok(vehicleAdminService.getAllBrands());
    }
}
