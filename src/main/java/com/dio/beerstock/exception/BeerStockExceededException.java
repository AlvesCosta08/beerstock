package com.dio.beerstock.exception;

public class BeerStockExceededException extends Exception {
    public BeerStockExceededException(int quantity, int max) {
        super(String.format("Quantity %d exceeds max stock of %d.", quantity, max));
    }

    public BeerStockExceededException(String message) {
        super(message);
    }
}
