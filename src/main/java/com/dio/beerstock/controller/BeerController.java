package com.dio.beerstock.controller;

import com.dio.beerstock.dto.BeerDTO;
import com.dio.beerstock.exception.BeerAlreadyRegisteredException;
import com.dio.beerstock.exception.BeerNotFoundException;
import com.dio.beerstock.exception.BeerStockExceededException;
import com.dio.beerstock.service.BeerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciamento de cervejas.
 * Fornece endpoints para CRUD e operações de estoque (incremento/decremento).
 */
@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {

    private final BeerService beerService;

    @Autowired
    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    /**
     * Cria uma nova cerveja.
     *
     * @param beerDTO Dados da cerveja a ser criada
     * @return ResponseEntity com a cerveja criada e status 201 (CREATED)
     * @throws BeerAlreadyRegisteredException Se o nome já estiver em uso
     * @throws BeerStockExceededException     Se a quantidade exceder o estoque máximo
     */
    @PostMapping
    public ResponseEntity<BeerDTO> createBeer(@RequestBody @Valid BeerDTO beerDTO)
            throws BeerAlreadyRegisteredException, BeerStockExceededException {
        BeerDTO createdBeer = beerService.createBeer(beerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBeer);
    }

    /**
     * Busca uma cerveja pelo ID.
     *
     * @param id ID da cerveja
     * @return ResponseEntity com a cerveja encontrada e status 200 (OK)
     * @throws BeerNotFoundException Se o ID não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<BeerDTO> findById(@PathVariable Long id) throws BeerNotFoundException {
        BeerDTO beerDTO = beerService.findById(id);
        return ResponseEntity.ok(beerDTO);
    }

    /**
     * Busca uma cerveja pelo nome.
     *
     * @param name Nome da cerveja
     * @return ResponseEntity com a cerveja encontrada e status 200 (OK)
     * @throws BeerNotFoundException Se o nome não for encontrado
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<BeerDTO> findByName(@PathVariable String name) throws BeerNotFoundException {
        BeerDTO beerDTO = beerService.findByName(name);
        return ResponseEntity.ok(beerDTO);
    }

    /**
     * Lista todas as cervejas cadastradas.
     *
     * @return Lista de cervejas (pode ser vazia) com status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<BeerDTO>> listAll() {
        List<BeerDTO> beers = beerService.listAll();
        return ResponseEntity.ok(beers);
    }

    /**
     * Atualiza uma cerveja existente.
     *
     * @param id      ID da cerveja a ser atualizada
     * @param beerDTO Novos dados da cerveja
     * @return ResponseEntity com a cerveja atualizada e status 200 (OK)
     * @throws BeerNotFoundException      Se o ID não existir
     * @throws BeerStockExceededException Se a nova quantidade for inválida
     */
    @PutMapping("/{id}")
    public ResponseEntity<BeerDTO> updateBeer(
            @PathVariable Long id,
            @RequestBody @Valid BeerDTO beerDTO)
            throws BeerNotFoundException, BeerStockExceededException {
        BeerDTO updatedBeer = beerService.updateBeer(id, beerDTO);
        return ResponseEntity.ok(updatedBeer);
    }

    /**
     * Deleta uma cerveja pelo ID.
     *
     * @param id ID da cerveja
     * @return ResponseEntity com status 204 (NO_CONTENT) em caso de sucesso
     * @throws BeerNotFoundException Se o ID não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws BeerNotFoundException {
        beerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Incrementa a quantidade em estoque de uma cerveja.
     *
     * @param id                   ID da cerveja
     * @param quantityToIncrement  Quantidade a ser adicionada
     * @return ResponseEntity com a cerveja atualizada e status 200 (OK)
     * @throws BeerNotFoundException      Se a cerveja não existir
     * @throws BeerStockExceededException Se o incremento exceder o estoque máximo
     */
    @PatchMapping("/{id}/increment")
    public ResponseEntity<BeerDTO> increment(
            @PathVariable Long id,
            @RequestParam int quantityToIncrement)
            throws BeerNotFoundException, BeerStockExceededException {
        BeerDTO updatedBeer = beerService.increment(id, quantityToIncrement);
        return ResponseEntity.ok(updatedBeer);
    }

    /**
     * Decrementa a quantidade em estoque de uma cerveja (ex: venda).
     *
     * @param id                   ID da cerveja
     * @param quantityToDecrement  Quantidade a ser removida
     * @return ResponseEntity com a cerveja atualizada e status 200 (OK)
     * @throws BeerNotFoundException      Se a cerveja não existir
     * @throws BeerStockExceededException Se o estoque ficar negativo
     */
    @PatchMapping("/{id}/decrement")
    public ResponseEntity<BeerDTO> decrement(
            @PathVariable Long id,
            @RequestParam int quantityToDecrement)
            throws BeerNotFoundException, BeerStockExceededException {
        BeerDTO updatedBeer = beerService.decrement(id, quantityToDecrement);
        return ResponseEntity.ok(updatedBeer);
    }
}
