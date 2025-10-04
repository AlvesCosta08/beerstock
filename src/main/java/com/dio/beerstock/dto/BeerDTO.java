package com.dio.beerstock.dto;

import com.dio.beerstock.enums.BeerType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeerDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotNull
    @Min(1)
    private int max;

    @NotNull
    @Min(0)
    private int quantity; // ⚠️ Aqui usamos "quantity" semanticamente

    @NotNull
    private BeerType type;
}