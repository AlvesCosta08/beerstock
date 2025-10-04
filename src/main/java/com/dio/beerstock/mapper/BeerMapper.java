package com.dio.beerstock.mapper;

import com.dio.beerstock.dto.BeerDTO;
import com.dio.beerstock.entity.Beer;
import org.springframework.stereotype.Component;

@Component
public class BeerMapper {

    public Beer toModel(BeerDTO dto) {
        return new Beer(
                dto.getId(),
                dto.getName(),
                dto.getBrand(),
                dto.getMax(),
                dto.getQuantity(), // mapeia quantity → quality
                dto.getType()
        );
    }

    public BeerDTO toDTO(Beer entity) {
        return new BeerDTO(
                entity.getId(),
                entity.getName(),
                entity.getBrand(),
                entity.getMax(),
                entity.getQuality(), // mapeia quality → quantity
                entity.getType()
        );
    }
}