package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.DTO.ProductMinDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existsId;
    private Long nonExistsId;
    private Long countTotalProduct;

    @BeforeEach
    void setUp() throws Exception{
        existsId = 1L;
        nonExistsId = 1000L;
        countTotalProduct = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists(){
        service.delete(existsId);
        Assertions.assertEquals(countTotalProduct - 1, repository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           service.delete(nonExistsId);
        });
    }

    @Test
    public void findAllShouldReturnPageWhenPage0Size10(){
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductMinDTO> result = service.findAll("", pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProduct, result.getTotalElements());
    }

    @Test
    public void findAllShouldReturnEmptyPageWhenPageDoesNotExist(){
        PageRequest pageRequest = PageRequest.of(50, 10);
        Page<ProductMinDTO> result = service.findAll("", pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductMinDTO> result = service.findAll("", pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

}
