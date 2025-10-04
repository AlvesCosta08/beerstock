package com.dio.beerstock.exception;

public class BeerAlreadyRegisteredException extends Exception {
    public BeerAlreadyRegisteredException(String name) {
        super(String.format("Beer with name '%s' is already registered.", name));
    }
}