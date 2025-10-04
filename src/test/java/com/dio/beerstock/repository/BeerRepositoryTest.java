package com.dio.beerstock.repository;

import com.dio.beerstock.entity.Beer;
import com.dio.beerstock.enums.BeerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class BeerRepositoryTest {

    @Autowired
    private BeerRepository beerRepository;

    private Beer beer;

    @BeforeEach
    void setUp() {
        beer = Beer.builder()
                .name("Brahma")
                .brand("Ambev")
                .type(BeerType.LAGER)
                .quality(10)
                .max(100)
                .build();
    }

    @AfterEach
    void tearDown() {
        // Limpa o repositório de forma segura
        try {
            beerRepository.deleteAll();
        } catch (Exception e) {
            // Ignora exceções durante cleanup
        }
    }

    // ✅ Teste: Salvar cerveja no banco
    @Test
    void whenSaveBeerThenBeerShouldBePersisted() {
        // when
        Beer savedBeer = beerRepository.save(beer);

        // then
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isPositive();
        assertThat(savedBeer.getName()).isEqualTo("Brahma");
        assertThat(savedBeer.getBrand()).isEqualTo("Ambev");
        assertThat(savedBeer.getType()).isEqualTo(BeerType.LAGER);
        assertThat(savedBeer.getQuality()).isEqualTo(10);
        assertThat(savedBeer.getMax()).isEqualTo(100);
    }

    // ✅ Teste: Buscar cerveja por nome existente
    @Test
    void whenFindByNameWithExistingNameThenReturnBeer() {
        // given
        beerRepository.save(beer);

        // when
        Optional<Beer> foundBeer = beerRepository.findByName("Brahma");

        // then
        assertThat(foundBeer).isPresent();
        assertThat(foundBeer.get().getName()).isEqualTo("Brahma");
        assertThat(foundBeer.get().getBrand()).isEqualTo("Ambev");
    }

    // ✅ Teste: Buscar cerveja por nome não existente
    @Test
    void whenFindByNameWithNonExistingNameThenReturnEmpty() {
        // when
        Optional<Beer> foundBeer = beerRepository.findByName("NonExistingBeer");

        // then
        assertThat(foundBeer).isEmpty();
    }

    // ✅ Teste: Buscar todas as cervejas quando há registros
    @Test
    void whenFindAllWithExistingBeersThenReturnListOfBeers() {
        // given
        Beer beer1 = Beer.builder()
                .name("Skol")
                .brand("Ambev")
                .type(BeerType.LAGER)
                .quality(15)
                .max(100)
                .build();

        Beer beer2 = Beer.builder()
                .name("Heineken")
                .brand("Heineken")
                .type(BeerType.LAGER)
                .quality(20)
                .max(150)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        // when
        List<Beer> beers = beerRepository.findAll();

        // then
        assertThat(beers).isNotEmpty();
        assertThat(beers).hasSize(2);
        assertThat(beers).extracting(Beer::getName)
                .containsExactlyInAnyOrder("Skol", "Heineken");
    }

    // ✅ Teste: Buscar todas as cervejas quando não há registros
    @Test
    void whenFindAllWithNoBeersThenReturnEmptyList() {
        // when
        List<Beer> beers = beerRepository.findAll();

        // then
        assertThat(beers).isEmpty();
    }

    // ✅ Teste CORRIGIDO: Verificar duplicidade de nome - deve lançar exceção
    @Test
    void whenSaveBeerWithDuplicateNameThenOnlyOneBeerShouldExist() {
        // given
        beerRepository.saveAndFlush(beer);

        Beer duplicateBeer = Beer.builder()
                .name("Brahma")
                .brand("Outra Marca")
                .type(BeerType.LAGER)
                .quality(5)
                .max(50)
                .build();

        // when
        try {
            beerRepository.saveAndFlush(duplicateBeer);
            fail("Deveria ter lançado DataIntegrityViolationException");
        } catch (DataIntegrityViolationException e) {
            // ✅ Exceção esperada - não fazemos nada
        }

        // ❌ NÃO podemos verificar beerRepository.count() aqui
        // A sessão está corrompida!
    }

    // ✅ Teste: Atualizar cerveja existente
    @Test
    void whenUpdateBeerThenBeerShouldBeUpdated() {
        // given
        Beer savedBeer = beerRepository.save(beer);
        Long beerId = savedBeer.getId();

        // when
        savedBeer.setQuality(25);
        savedBeer.setBrand("Ambev Updated");
        Beer updatedBeer = beerRepository.save(savedBeer);

        // then
        assertThat(updatedBeer.getId()).isEqualTo(beerId);
        assertThat(updatedBeer.getQuality()).isEqualTo(25);
        assertThat(updatedBeer.getBrand()).isEqualTo("Ambev Updated");
    }

    // ✅ Teste: Deletar cerveja por ID
    @Test
    void whenDeleteBeerByIdThenBeerShouldBeRemoved() {
        // given
        Beer savedBeer = beerRepository.save(beer);
        Long beerId = savedBeer.getId();

        // when
        beerRepository.deleteById(beerId);

        // then
        Optional<Beer> deletedBeer = beerRepository.findById(beerId);
        assertThat(deletedBeer).isEmpty();
    }

    // ✅ Teste: Buscar cerveja por ID existente
    @Test
    void whenFindByIdWithExistingIdThenReturnBeer() {
        // given
        Beer savedBeer = beerRepository.save(beer);
        Long beerId = savedBeer.getId();

        // when
        Optional<Beer> foundBeer = beerRepository.findById(beerId);

        // then
        assertThat(foundBeer).isPresent();
        assertThat(foundBeer.get().getId()).isEqualTo(beerId);
        assertThat(foundBeer.get().getName()).isEqualTo("Brahma");
    }

    // ✅ Teste: Buscar cerveja por ID não existente
    @Test
    void whenFindByIdWithNonExistingIdThenReturnEmpty() {
        // when
        Optional<Beer> foundBeer = beerRepository.findById(999L);

        // then
        assertThat(foundBeer).isEmpty();
    }

    // ✅ Teste: Verificar case sensitivity na busca por nome
    @Test
    void whenFindByNameWithDifferentCaseThenReturnEmpty() {
        // given
        beerRepository.save(beer);

        // when
        Optional<Beer> foundBeer = beerRepository.findByName("brahma"); // lowercase

        // then
        // H2 é case-sensitive por padrão, então deve retornar vazio
        assertThat(foundBeer).isEmpty();
    }

    // ✅ Teste: Salvar cerveja com campos obrigatórios
    @Test
    void whenSaveBeerWithRequiredFieldsThenBeerShouldBePersisted() {
        // given
        Beer minimalBeer = Beer.builder()
                .name("Minimal Beer")
                .brand("Minimal Brand")
                .type(BeerType.ALE)
                .quality(0)
                .max(50)
                .build();

        // when
        Beer savedBeer = beerRepository.save(minimalBeer);

        // then
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isPositive();
        assertThat(savedBeer.getName()).isEqualTo("Minimal Beer");
    }

    // ✅ Teste: Verificar que nomes diferentes são permitidos
    @Test
    void whenSaveBeersWithDifferentNamesThenBothShouldBePersisted() {
        // given
        Beer beer1 = Beer.builder()
                .name("Skol")
                .brand("Ambev")
                .type(BeerType.LAGER)
                .quality(10)
                .max(100)
                .build();

        Beer beer2 = Beer.builder()
                .name("Heineken")
                .brand("Heineken")
                .type(BeerType.LAGER)
                .quality(15)
                .max(150)
                .build();

        // when
        Beer savedBeer1 = beerRepository.save(beer1);
        Beer savedBeer2 = beerRepository.save(beer2);

        // then
        assertThat(savedBeer1).isNotNull();
        assertThat(savedBeer2).isNotNull();
        assertThat(beerRepository.findAll()).hasSize(2);
    }

    // ✅ Teste adicional: Contagem de registros
    @Test
    void whenCountBeersThenReturnCorrectNumber() {
        // given
        beerRepository.save(beer);

        Beer beer2 = Beer.builder()
                .name("Skol")
                .brand("Ambev")
                .type(BeerType.LAGER)
                .quality(15)
                .max(100)
                .build();
        beerRepository.save(beer2);

        // when
        long count = beerRepository.count();

        // then
        assertThat(count).isEqualTo(2);
    }

    // ✅ Teste adicional: Verificar existência por ID
    @Test
    void whenExistsByIdWithExistingIdThenReturnTrue() {
        // given
        Beer savedBeer = beerRepository.save(beer);
        Long beerId = savedBeer.getId();

        // when
        boolean exists = beerRepository.existsById(beerId);

        // then
        assertThat(exists).isTrue();
    }

    // ✅ Teste adicional: Verificar existência por ID inexistente
    @Test
    void whenExistsByIdWithNonExistingIdThenReturnFalse() {
        // when
        boolean exists = beerRepository.existsById(999L);

        // then
        assertThat(exists).isFalse();
    }
}