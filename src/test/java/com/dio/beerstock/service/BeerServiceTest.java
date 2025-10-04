package com.dio.beerstock.service;

import com.dio.beerstock.dto.BeerDTO;
import com.dio.beerstock.entity.Beer;
import com.dio.beerstock.enums.BeerType;
import com.dio.beerstock.exception.BeerAlreadyRegisteredException;
import com.dio.beerstock.exception.BeerNotFoundException;
import com.dio.beerstock.exception.BeerStockExceededException;
import com.dio.beerstock.mapper.BeerMapper;
import com.dio.beerstock.repository.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    private static final long VALID_BEER_ID = 1L;
    private static final long INVALID_BEER_ID = 2L;
    private static final String BEER_NAME = "Brahma";
    private static final String BEER_BRAND = "Ambev";

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private BeerMapper beerMapper;

    @InjectMocks
    private BeerService beerService;

    // ✅ Teste: Criar cerveja com sucesso
    @Test
    void whenValidBeerInformedThenItShouldBeCreated() throws BeerAlreadyRegisteredException, BeerStockExceededException {
        // given
        BeerDTO beerDTO = createValidBeerDTO();
        Beer expectedSavedBeer = createValidBeer();

        // when
        when(beerRepository.findByName(beerDTO.getName())).thenReturn(Optional.empty());
        when(beerMapper.toModel(beerDTO)).thenReturn(expectedSavedBeer);
        when(beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);
        when(beerMapper.toDTO(expectedSavedBeer)).thenReturn(beerDTO);

        // then
        BeerDTO createdBeerDTO = beerService.createBeer(beerDTO);

        assertThat(createdBeerDTO.getId(), is(equalTo(VALID_BEER_ID)));
        assertThat(createdBeerDTO.getName(), is(equalTo(BEER_NAME)));
        assertThat(createdBeerDTO.getBrand(), is(equalTo(BEER_BRAND)));
    }

    // ❌ Teste: Criar cerveja duplicada
    @Test
    void whenDuplicatedNameIsGivenThenAnExceptionShouldBeThrown() {
        // given
        BeerDTO beerDTO = createValidBeerDTO();

        // when
        when(beerRepository.findByName(beerDTO.getName())).thenReturn(Optional.of(createValidBeer()));

        // then
        assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(beerDTO));
    }

    // ✅ Teste: Buscar cerveja por nome
    @Test
    void whenValidBeerNameIsGivenThenReturnThatBeer() throws BeerNotFoundException {
        // given
        Beer validBeer = createValidBeer();
        BeerDTO expectedBeerDTO = createValidBeerDTO();

        // when
        when(beerRepository.findByName(BEER_NAME)).thenReturn(Optional.of(validBeer));
        when(beerMapper.toDTO(validBeer)).thenReturn(expectedBeerDTO);

        // then
        BeerDTO foundBeerDTO = beerService.findByName(BEER_NAME);

        assertThat(foundBeerDTO, is(equalTo(expectedBeerDTO)));
        assertThat(foundBeerDTO.getName(), is(equalTo(BEER_NAME)));
    }

    // ❌ Teste: Buscar cerveja inexistente por nome
    @Test
    void whenInvalidBeerNameIsGivenThenThrowAnException() {
        // when
        when(beerRepository.findByName(String.valueOf(INVALID_BEER_ID))).thenReturn(Optional.empty());

        // then
        assertThrows(BeerNotFoundException.class, () -> beerService.findByName(String.valueOf(INVALID_BEER_ID)));
    }

    // ✅ Teste CORRIGIDO: Incrementar estoque com sucesso
    @Test
    void whenBeerStockIsIncrementedThenReturnIncrementedBeer() throws BeerNotFoundException, BeerStockExceededException {
        // given
        Beer beer = createValidBeer(); // quantity = 10
        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = beer.getQuality() + quantityToIncrement; // 20

        BeerDTO expectedBeerDTO = createValidBeerDTO();
        expectedBeerDTO.setQuantity(expectedQuantityAfterIncrement); // Atualiza o DTO com a quantidade esperada

        // when
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(Optional.of(beer));
        when(beerRepository.save(beer)).thenReturn(beer);
        when(beerMapper.toDTO(beer)).thenReturn(expectedBeerDTO);

        BeerDTO incrementedBeerDTO = beerService.increment(VALID_BEER_ID, quantityToIncrement);

        // then
        assertThat(incrementedBeerDTO.getQuantity(), equalTo(expectedQuantityAfterIncrement)); // Deve ser 20
        verify(beerRepository).save(beer); // Verifica se o save foi chamado
        assertThat(beer.getQuality(), equalTo(expectedQuantityAfterIncrement)); // Verifica se a entidade foi atualizada
    }

    // ❌ Teste: Incrementar estoque acima do limite
    @Test
    void whenIncrementIsHigherThanMaxThenThrowAnException() {
        // given
        Beer beer = createValidBeer();
        beer.setQuality(90); // quantidade atual
        int quantityToIncrement = 20; // excede o max (100)

        // when
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(Optional.of(beer));

        // then
        assertThrows(BeerStockExceededException.class, () -> beerService.increment(VALID_BEER_ID, quantityToIncrement));
    }

    // ✅ Teste CORRIGIDO: Decrementar estoque com sucesso
    @Test
    void whenBeerStockIsDecrementedThenReturnDecrementedBeer() throws BeerNotFoundException, BeerStockExceededException {
        // given
        Beer beer = createValidBeer(); // quantity = 10
        int quantityToDecrement = 5;
        int expectedQuantityAfterDecrement = beer.getQuality() - quantityToDecrement; // 5

        BeerDTO expectedBeerDTO = createValidBeerDTO();
        expectedBeerDTO.setQuantity(expectedQuantityAfterDecrement); // Atualiza o DTO com a quantidade esperada

        // when
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(Optional.of(beer));
        when(beerRepository.save(beer)).thenReturn(beer);
        when(beerMapper.toDTO(beer)).thenReturn(expectedBeerDTO);

        BeerDTO decrementedBeerDTO = beerService.decrement(VALID_BEER_ID, quantityToDecrement);

        // then
        assertThat(decrementedBeerDTO.getQuantity(), equalTo(expectedQuantityAfterDecrement)); // Deve ser 5
        verify(beerRepository).save(beer); // Verifica se o save foi chamado
        assertThat(beer.getQuality(), equalTo(expectedQuantityAfterDecrement)); // Verifica se a entidade foi atualizada
    }

    // ❌ Teste: Decrementar estoque acima do disponível
    @Test
    void whenDecrementIsHigherThanStockThenThrowAnException() {
        // given
        Beer beer = createValidBeer();
        beer.setQuality(5); // quantidade atual
        int quantityToDecrement = 10; // maior que o estoque

        // when
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(Optional.of(beer));

        // then
        assertThrows(BeerStockExceededException.class, () -> beerService.decrement(VALID_BEER_ID, quantityToDecrement));
    }

    // ✅ Teste: Listar todas as cervejas
    @Test
    void whenListBeerIsCalledThenReturnAListOfBeers() {
        // given
        Beer validBeer = createValidBeer();
        BeerDTO beerDTO = createValidBeerDTO();

        // when
        when(beerRepository.findAll()).thenReturn(Collections.singletonList(validBeer));
        when(beerMapper.toDTO(validBeer)).thenReturn(beerDTO);

        List<BeerDTO> beerDTOList = beerService.listAll();

        // then
        assertThat(beerDTOList, is(not(empty())));
        assertThat(beerDTOList.get(0).getName(), is(equalTo(BEER_NAME)));
    }

    // ✅ Teste: Atualizar cerveja com sucesso
    @Test
    void whenValidBeerIdIsGivenThenReturnUpdatedBeer() throws BeerNotFoundException, BeerStockExceededException {
        // given
        long id = 1L;
        BeerDTO beerDTO = createValidBeerDTO();
        Beer beer = createValidBeer();

        // when
        when(beerRepository.findById(id)).thenReturn(Optional.of(beer));
        when(beerMapper.toModel(beerDTO)).thenReturn(beer);
        when(beerRepository.save(beer)).thenReturn(beer);
        when(beerMapper.toDTO(beer)).thenReturn(beerDTO);

        BeerDTO updatedBeer = beerService.updateBeer(id, beerDTO);

        // then
        assertThat(updatedBeer, is(equalTo(beerDTO)));
    }

    // ✅ Teste: Buscar cerveja por ID
    @Test
    void whenValidBeerIdIsGivenThenReturnBeer() throws BeerNotFoundException {
        // given
        Beer validBeer = createValidBeer();
        BeerDTO expectedBeerDTO = createValidBeerDTO();

        // when
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(Optional.of(validBeer));
        when(beerMapper.toDTO(validBeer)).thenReturn(expectedBeerDTO);

        // then
        BeerDTO foundBeerDTO = beerService.findById(VALID_BEER_ID);

        assertThat(foundBeerDTO, is(equalTo(expectedBeerDTO)));
        assertThat(foundBeerDTO.getId(), is(equalTo(VALID_BEER_ID)));
    }

    // ❌ Teste: Buscar cerveja por ID inexistente
    @Test
    void whenInvalidBeerIdIsGivenThenThrowException() {
        // when
        when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(BeerNotFoundException.class, () -> beerService.findById(INVALID_BEER_ID));
    }

    // ✅ Teste: Deletar cerveja com sucesso
    @Test
    void whenValidBeerIdIsGivenThenDeleteBeer() throws BeerNotFoundException {
        // given
        Beer beer = createValidBeer();

        // when
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(Optional.of(beer));
        doNothing().when(beerRepository).deleteById(VALID_BEER_ID);

        // then
        assertDoesNotThrow(() -> beerService.deleteById(VALID_BEER_ID));
        verify(beerRepository).deleteById(VALID_BEER_ID);
    }

    // === Métodos auxiliares ===
    private BeerDTO createValidBeerDTO() {
        return BeerDTO.builder()
                .id(VALID_BEER_ID)
                .name(BEER_NAME)
                .brand(BEER_BRAND)
                .type(BeerType.LAGER)
                .quantity(10)
                .max(100)
                .build();
    }

    private Beer createValidBeer() {
        return Beer.builder()
                .id(VALID_BEER_ID)
                .name(BEER_NAME)
                .brand(BEER_BRAND)
                .type(BeerType.LAGER)
                .quality(10)
                .max(100)
                .build();
    }
}