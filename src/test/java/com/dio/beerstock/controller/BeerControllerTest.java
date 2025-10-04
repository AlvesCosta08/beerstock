package com.dio.beerstock.controller;

import com.dio.beerstock.dto.BeerDTO;
import com.dio.beerstock.enums.BeerType;
import com.dio.beerstock.exception.BeerAlreadyRegisteredException;
import com.dio.beerstock.exception.BeerNotFoundException;
import com.dio.beerstock.exception.BeerStockExceededException;
import com.dio.beerstock.exception.GlobalExceptionHandler;
import com.dio.beerstock.service.BeerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BeerControllerTest {

    private static final String BEER_API_URL_PATH = "/api/v1/beers";
    private static final long VALID_BEER_ID = 1L;
    private static final long INVALID_BEER_ID = 2L;
    private static final String VALID_BEER_NAME = "Heineken";
    private static final String INVALID_BEER_NAME = "Invalid Beer";

    private MockMvc mockMvc;

    @Mock
    private BeerService beerService;

    @InjectMocks
    private BeerController beerController;

    private ObjectMapper objectMapper;
    private BeerDTO validBeerDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(beerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        validBeerDTO = BeerDTO.builder()
                .id(VALID_BEER_ID)
                .name("Heineken")
                .brand("Heineken")
                .max(50)
                .quantity(10)
                .type(BeerType.LAGER)
                .build();
    }

    @Test
    void whenPOSTIsCalledThenABeerIsCreated() throws Exception {
        // Given
        when(beerService.createBeer(validBeerDTO)).thenReturn(validBeerDTO);

        // When & Then
        mockMvc.perform(post(BEER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBeerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(validBeerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(validBeerDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(validBeerDTO.getType().name())));

        verify(beerService, times(1)).createBeer(validBeerDTO);
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenBadRequestIsReturned() throws Exception {
        // Given
        BeerDTO invalidBeerDTO = BeerDTO.builder()
                .brand("Heineken")
                .max(50)
                .quantity(10)
                .type(BeerType.LAGER)
                .build(); // Name is missing

        // When & Then
        mockMvc.perform(post(BEER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBeerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation Error")));

        verify(beerService, never()).createBeer(any());
    }

    @Test
    void whenPOSTIsCalledWithAlreadyRegisteredBeerThenConflictIsReturned() throws Exception {
        // Given
        String beerName = "Heineken";
        when(beerService.createBeer(validBeerDTO))
                .thenThrow(new BeerAlreadyRegisteredException(beerName));

        // When & Then
        mockMvc.perform(post(BEER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBeerDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Conflict")))
                .andExpect(jsonPath("$.message", is("Beer with name '" + beerName + "' is already registered.")));

        verify(beerService, times(1)).createBeer(validBeerDTO);
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // Given
        when(beerService.findById(VALID_BEER_ID)).thenReturn(validBeerDTO);

        // When & Then
        mockMvc.perform(get(BEER_API_URL_PATH + "/" + VALID_BEER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(validBeerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(validBeerDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(validBeerDTO.getType().name())));

        verify(beerService, times(1)).findById(VALID_BEER_ID);
    }

    @Test
    void whenGETIsCalledWithInvalidIdThenNotFoundIsReturned() throws Exception {
        // Given
        when(beerService.findById(INVALID_BEER_ID))
                .thenThrow(new BeerNotFoundException(INVALID_BEER_ID));

        // When & Then
        mockMvc.perform(get(BEER_API_URL_PATH + "/" + INVALID_BEER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Beer with id '" + INVALID_BEER_ID + "' not found.")));

        verify(beerService, times(1)).findById(INVALID_BEER_ID);
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // Given
        when(beerService.findByName(VALID_BEER_NAME)).thenReturn(validBeerDTO);

        // When & Then
        mockMvc.perform(get(BEER_API_URL_PATH + "/name/" + VALID_BEER_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(validBeerDTO.getName())))
                .andExpect(jsonPath("$.type", is(validBeerDTO.getType().name())));

        verify(beerService, times(1)).findByName(VALID_BEER_NAME);
    }

    @Test
    void whenGETIsCalledWithInvalidNameThenNotFoundIsReturned() throws Exception {
        // Given
        when(beerService.findByName(INVALID_BEER_NAME))
                .thenThrow(new BeerNotFoundException(INVALID_BEER_NAME));

        // When & Then
        mockMvc.perform(get(BEER_API_URL_PATH + "/name/" + INVALID_BEER_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Beer with name '" + INVALID_BEER_NAME + "' not found.")));

        verify(beerService, times(1)).findByName(INVALID_BEER_NAME);
    }

    @Test
    void whenGETListIsCalledThenOkStatusIsReturned() throws Exception {
        // Given
        List<BeerDTO> beerList = Collections.singletonList(validBeerDTO);
        when(beerService.listAll()).thenReturn(beerList);

        // When & Then
        mockMvc.perform(get(BEER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(validBeerDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(validBeerDTO.getBrand())))
                .andExpect(jsonPath("$[0].type", is(validBeerDTO.getType().name())));

        verify(beerService, times(1)).listAll();
    }

    @Test
    void whenGETListIsCalledThenEmptyListIsReturned() throws Exception {
        // Given
        when(beerService.listAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get(BEER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(beerService, times(1)).listAll();
    }

    @Test
    void whenPUTIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // Given
        when(beerService.updateBeer(VALID_BEER_ID, validBeerDTO)).thenReturn(validBeerDTO);

        // When & Then
        mockMvc.perform(put(BEER_API_URL_PATH + "/" + VALID_BEER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBeerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(validBeerDTO.getName())))
                .andExpect(jsonPath("$.type", is(validBeerDTO.getType().name())));

        verify(beerService, times(1)).updateBeer(VALID_BEER_ID, validBeerDTO);
    }

    @Test
    void whenPUTIsCalledWithInvalidIdThenNotFoundIsReturned() throws Exception {
        // Given
        when(beerService.updateBeer(INVALID_BEER_ID, validBeerDTO))
                .thenThrow(new BeerNotFoundException(INVALID_BEER_ID));

        // When & Then
        mockMvc.perform(put(BEER_API_URL_PATH + "/" + INVALID_BEER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBeerDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Beer with id '" + INVALID_BEER_ID + "' not found.")));

        verify(beerService, times(1)).updateBeer(INVALID_BEER_ID, validBeerDTO);
    }

    @Test
    void whenPUTIsCalledWithInvalidDTOThenBadRequestIsReturned() throws Exception {
        // Given
        BeerDTO invalidBeerDTO = BeerDTO.builder()
                .brand("Heineken")
                .max(50)
                .quantity(10)
                .type(BeerType.LAGER)
                .build(); // Name is missing

        // When & Then
        mockMvc.perform(put(BEER_API_URL_PATH + "/" + VALID_BEER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBeerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation Error")));

        verify(beerService, never()).updateBeer(anyLong(), any());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentIsReturned() throws Exception {
        // Given
        doNothing().when(beerService).deleteById(VALID_BEER_ID);

        // When & Then
        mockMvc.perform(delete(BEER_API_URL_PATH + "/" + VALID_BEER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService, times(1)).deleteById(VALID_BEER_ID);
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundIsReturned() throws Exception {
        // Given
        doThrow(new BeerNotFoundException(INVALID_BEER_ID))
                .when(beerService).deleteById(INVALID_BEER_ID);

        // When & Then
        mockMvc.perform(delete(BEER_API_URL_PATH + "/" + INVALID_BEER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Beer with id '" + INVALID_BEER_ID + "' not found.")));

        verify(beerService, times(1)).deleteById(INVALID_BEER_ID);
    }

    @Test
    void whenPATCHIncrementIsCalledThenOkStatusIsReturned() throws Exception {
        // Given
        int quantityToIncrement = 10;
        when(beerService.increment(VALID_BEER_ID, quantityToIncrement)).thenReturn(validBeerDTO);

        // When & Then
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + "/increment")
                        .param("quantityToIncrement", String.valueOf(quantityToIncrement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(validBeerDTO.getName())))
                .andExpect(jsonPath("$.type", is(validBeerDTO.getType().name())));

        verify(beerService, times(1)).increment(VALID_BEER_ID, quantityToIncrement);
    }

    @Test
    void whenPATCHIncrementIsCalledWithInvalidIdThenNotFoundIsReturned() throws Exception {
        // Given
        int quantityToIncrement = 10;
        when(beerService.increment(INVALID_BEER_ID, quantityToIncrement))
                .thenThrow(new BeerNotFoundException(INVALID_BEER_ID));

        // When & Then
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + INVALID_BEER_ID + "/increment")
                        .param("quantityToIncrement", String.valueOf(quantityToIncrement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Beer with id '" + INVALID_BEER_ID + "' not found.")));

        verify(beerService, times(1)).increment(INVALID_BEER_ID, quantityToIncrement);
    }

    @Test
    void whenPATCHIncrementIsCalledWithExceededStockThenBadRequestIsReturned() throws Exception {
        // Given
        int quantityToIncrement = 100;
        when(beerService.increment(VALID_BEER_ID, quantityToIncrement))
                .thenThrow(new BeerStockExceededException((int) VALID_BEER_ID, quantityToIncrement));

        // When & Then
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + "/increment")
                        .param("quantityToIncrement", String.valueOf(quantityToIncrement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Quantity " + VALID_BEER_ID + " exceeds max stock of " + quantityToIncrement + ".")));

        verify(beerService, times(1)).increment(VALID_BEER_ID, quantityToIncrement);
    }

    @Test
    void whenPATCHDecrementIsCalledThenOkStatusIsReturned() throws Exception {
        // Given
        int quantityToDecrement = 5;
        when(beerService.decrement(VALID_BEER_ID, quantityToDecrement)).thenReturn(validBeerDTO);

        // When & Then
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + "/decrement")
                        .param("quantityToDecrement", String.valueOf(quantityToDecrement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(validBeerDTO.getName())))
                .andExpect(jsonPath("$.type", is(validBeerDTO.getType().name())));

        verify(beerService, times(1)).decrement(VALID_BEER_ID, quantityToDecrement);
    }

    @Test
    void whenPATCHDecrementIsCalledWithInvalidIdThenNotFoundIsReturned() throws Exception {
        // Given
        int quantityToDecrement = 5;
        when(beerService.decrement(INVALID_BEER_ID, quantityToDecrement))
                .thenThrow(new BeerNotFoundException(INVALID_BEER_ID));

        // When & Then
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + INVALID_BEER_ID + "/decrement")
                        .param("quantityToDecrement", String.valueOf(quantityToDecrement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Beer with id '" + INVALID_BEER_ID + "' not found.")));

        verify(beerService, times(1)).decrement(INVALID_BEER_ID, quantityToDecrement);
    }

    @Test
    void whenPATCHDecrementIsCalledWithInsufficientStockThenBadRequestIsReturned() throws Exception {
        // Given
        int quantityToDecrement = 100;
        when(beerService.decrement(VALID_BEER_ID, quantityToDecrement))
                .thenThrow(new BeerStockExceededException((int) VALID_BEER_ID, quantityToDecrement));

        // When & Then
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + "/decrement")
                        .param("quantityToDecrement", String.valueOf(quantityToDecrement))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Quantity " + VALID_BEER_ID + " exceeds max stock of " + quantityToDecrement + ".")));

        verify(beerService, times(1)).decrement(VALID_BEER_ID, quantityToDecrement);
    }
}