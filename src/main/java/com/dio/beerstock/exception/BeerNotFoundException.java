package com.dio.beerstock.exception;

public class BeerNotFoundException extends Exception {
    public BeerNotFoundException(String name) {
        super(String.format("Beer with name '%s' not found.", name));
    }

    public BeerNotFoundException(Long id) {
        super(String.format("Beer with id '%d' not found.", id));
    }
}
