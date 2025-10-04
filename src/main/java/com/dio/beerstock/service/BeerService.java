package com.dio.beerstock.service;

import com.dio.beerstock.dto.BeerDTO;
import com.dio.beerstock.entity.Beer;
import com.dio.beerstock.exception.BeerAlreadyRegisteredException;
import com.dio.beerstock.exception.BeerNotFoundException;
import com.dio.beerstock.exception.BeerStockExceededException;
import com.dio.beerstock.mapper.BeerMapper;
import com.dio.beerstock.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Autowired
    public BeerService(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    @Transactional
    public BeerDTO createBeer(BeerDTO beerDTO)
            throws BeerAlreadyRegisteredException, BeerStockExceededException {
        verifyIfIsAlreadyRegistered(beerDTO.getName());
        validateStock(beerDTO.getQuantity(), beerDTO.getMax());

        Beer beer = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(savedBeer);
    }

    public BeerDTO findById(Long id) throws BeerNotFoundException {
        Beer beer = beerRepository.findById(id)
                .orElseThrow(() -> new BeerNotFoundException(id));
        return beerMapper.toDTO(beer);
    }

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        Beer beer = beerRepository.findByName(name)
                .orElseThrow(() -> new BeerNotFoundException(name));
        return beerMapper.toDTO(beer);
    }

    public List<BeerDTO> listAll() {
        return beerRepository.findAll().stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BeerDTO updateBeer(Long id, BeerDTO beerDTO)
            throws BeerNotFoundException, BeerStockExceededException {
        verifyIfExists(id);
        validateStock(beerDTO.getQuantity(), beerDTO.getMax());

        Beer beerToUpdate = beerMapper.toModel(beerDTO);
        beerToUpdate.setId(id); // mantém o ID original
        Beer updatedBeer = beerRepository.save(beerToUpdate);
        return beerMapper.toDTO(updatedBeer);
    }

    @Transactional
    public void deleteById(Long id) throws BeerNotFoundException {
        verifyIfExists(id);
        beerRepository.deleteById(id);
    }

    @Transactional
    public BeerDTO increment(Long id, int quantityToIncrement)
            throws BeerNotFoundException, BeerStockExceededException {
        if (quantityToIncrement <= 0) {
            throw new BeerStockExceededException("Increment quantity must be greater than zero.");
        }

        Beer beer = verifyIfExists(id);
        int newQuantity = beer.getQuality() + quantityToIncrement;
        validateStock(newQuantity, beer.getMax());
        beer.setQuality(newQuantity);
        Beer updatedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(updatedBeer);
    }

    @Transactional
    public BeerDTO decrement(Long id, int quantityToDecrement)
            throws BeerNotFoundException, BeerStockExceededException {
        if (quantityToDecrement <= 0) {
            throw new BeerStockExceededException("Decrement quantity must be greater than zero.");
        }

        Beer beer = verifyIfExists(id);
        int newQuantity = beer.getQuality() - quantityToDecrement;
        if (newQuantity < 0) {
            throw new BeerStockExceededException(
                    String.format("Cannot remove %d units. Only %d available.", quantityToDecrement, beer.getQuality())
            );
        }
        beer.setQuality(newQuantity);
        Beer updatedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(updatedBeer);
    }

    // =============== Métodos Privados ===============

    private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        if (beerRepository.findByName(name).isPresent()) {
            throw new BeerAlreadyRegisteredException(name);
        }
    }

    private Beer verifyIfExists(Long id) throws BeerNotFoundException {
        return beerRepository.findById(id)
                .orElseThrow(() -> new BeerNotFoundException(id));
    }

    private void validateStock(int quantity, int max) throws BeerStockExceededException {
        if (quantity < 0) {
            throw new BeerStockExceededException("Stock quantity cannot be negative.");
        }
        if (quantity > max) {
            throw new BeerStockExceededException(quantity, max);
        }
    }
}
