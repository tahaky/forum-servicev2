package com.forum.service;

import com.forum.dto.CreateVehicleBrandRequest;
import com.forum.dto.VehicleBrandResponse;
import com.forum.entity.VehicleBrand;
import com.forum.repository.VehicleBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleAdminService {

    private final VehicleBrandRepository brandRepository;

    @Transactional
    public VehicleBrandResponse createBrand(CreateVehicleBrandRequest request) {
        VehicleBrand brand = new VehicleBrand();
        brand.setName(request.getName());
        brand.setLogoSvg(request.getLogoSvg());
        brand.setCreatedAt(LocalDateTime.now());
        try {
            brand = brandRepository.save(brand);
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException("Brand already exists: " + request.getName(), ex);
        }
        return toResponse(brand);
    }

    @Transactional(readOnly = true)
    public List<VehicleBrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private VehicleBrandResponse toResponse(VehicleBrand brand) {
        return new VehicleBrandResponse(brand.getId(), brand.getName(), brand.getLogoSvg(), brand.getCreatedAt());
    }
}
