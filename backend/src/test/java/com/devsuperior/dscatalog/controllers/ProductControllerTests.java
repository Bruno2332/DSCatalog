package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.DTO.ProductDTO;
import com.devsuperior.dscatalog.DTO.ProductMinDTO;

import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.PageImpl;


import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProductController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private long existsId;
    private long nonExistsId;
    private long dependentId;
    private ProductMinDTO minDto;
    private PageImpl<ProductMinDTO> page;
    private ProductDTO productDTO;
    private String jsonBody;

    @BeforeEach
    void setUp() throws Exception{

        existsId = 1L;
        nonExistsId = 2L;
        dependentId = 3L;
        minDto = Factory.createProductMinDTO();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(minDto));
        jsonBody = objectMapper.writeValueAsString(productDTO);

        when(service.findAll(anyString(), any())).thenReturn(page);
        when(service.findById(existsId)).thenReturn(productDTO);
        when(service.findById(nonExistsId)).thenThrow(ResourceNotFoundException.class);
        when(service.update(eq(existsId), any())).thenReturn(productDTO);
        when(service.update(eq(nonExistsId), any())).thenThrow(ResourceNotFoundException.class);
        when(service.insert(any())).thenReturn(productDTO);

        doNothing().when(service).delete(existsId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistsId);
        doThrow(DatabaseException.class).when(service).delete(dependentId);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception{
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdReturnProductDTOWhenIdExists() throws Exception{
        mockMvc.perform(get("/products/{id}", existsId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void findByIdReturnNotFoundWhenIdDoesNotExists() throws Exception{
        mockMvc.perform(get("/products/{id}", nonExistsId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProductDTO() throws Exception{
        mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExistis() throws Exception{
        mockMvc.perform(put("/products/{id}", existsId)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
        mockMvc.perform(put("/products/{id}", nonExistsId)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception{
        mockMvc.perform(delete("/products/{id}", existsId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
        mockMvc.perform(delete("/products/{id}", nonExistsId))
                .andExpect(status().isNotFound());
    }
}
