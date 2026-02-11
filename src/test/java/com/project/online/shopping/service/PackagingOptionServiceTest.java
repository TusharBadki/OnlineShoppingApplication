package com.project.online.shopping.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.online.shopping.dto.PackagingOptionRequestDto;
import com.project.online.shopping.dto.PackagingOptionResponseDto;
import com.project.online.shopping.entity.PackagingOption;
import com.project.online.shopping.mapper.PackagingOptionMapper;
import com.project.online.shopping.repository.PackagingOptionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PackagingOptionServiceTest {

    @Mock
    private PackagingOptionRepository repository;

    @Mock
    private PackagingOptionMapper mapper;

    @InjectMocks
    private PackagingOptionService service;

    //Create Packaging options
    @Test
    void shouldCreatePackagingOption() {
        PackagingOptionRequestDto requestDto = new PackagingOptionRequestDto();
        PackagingOption entity = new PackagingOption();
        PackagingOption savedEntity = new PackagingOption();
        PackagingOptionResponseDto responseDto = new PackagingOptionResponseDto();

        when(mapper.toEntity(requestDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toResponse(savedEntity)).thenReturn(responseDto);

        PackagingOptionResponseDto result = service.create(requestDto);

        assertNotNull(result);
        verify(repository).save(entity);
        verify(mapper).toEntity(requestDto);
        verify(mapper).toResponse(savedEntity);
    }

    //Update Packaging Options
    @Test
    void shouldUpdatePackagingOption() {
        String code = "CE";
        int quantity = 10;

        PackagingOptionRequestDto requestDto = new PackagingOptionRequestDto();
        PackagingOption existingEntity = new PackagingOption();
        PackagingOptionResponseDto responseDto = new PackagingOptionResponseDto();

        when(repository.findByCodeAndQuantity(code, quantity))
            .thenReturn(Optional.of(existingEntity));
        when(repository.save(existingEntity))
            .thenReturn(existingEntity);
        when(mapper.toResponse(existingEntity))
            .thenReturn(responseDto);

        PackagingOptionResponseDto result = service.update(code, quantity, requestDto);

        assertNotNull(result);
        verify(mapper).updateEntityFromDto(requestDto, existingEntity);
        verify(repository).save(existingEntity);
    }

    //Update Api : Packaging option not found
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingPackagingOption() {
        when(repository.findByCodeAndQuantity("CE", 10))
            .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> service.update("CE", 10, new PackagingOptionRequestDto()));

        assertEquals("Product not found", exception.getMessage());
    }

    //Get Packaging Options
    @Test
    void shouldFindPackagingOption() {
        String code = "CE";
        int quantity = 10;

        PackagingOption entity = new PackagingOption();
        PackagingOptionResponseDto responseDto = new PackagingOptionResponseDto();

        when(repository.findByCodeAndQuantity(code, quantity))
            .thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity))
            .thenReturn(responseDto);

        PackagingOptionResponseDto result = service.find(code, quantity);

        assertNotNull(result);
        verify(mapper).toResponse(entity);
    }

    //Get Packaging Options : Packaging Options Not Found
    @Test
    void shouldThrowExceptionWhenPackagingOptionNotFound() {
        when(repository.findByCodeAndQuantity("CE", 10))
            .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
            () -> service.find("CE", 10));
    }

    //Get All Packaging Options
    @Test
    void shouldReturnAllPackagingOptions() {
        PackagingOption entity1 = new PackagingOption();
        PackagingOption entity2 = new PackagingOption();

        PackagingOptionResponseDto dto1 = new PackagingOptionResponseDto();
        PackagingOptionResponseDto dto2 = new PackagingOptionResponseDto();

        when(repository.findAll()).thenReturn(List.of(entity1, entity2));
        when(mapper.toResponse(entity1)).thenReturn(dto1);
        when(mapper.toResponse(entity2)).thenReturn(dto2);

        List<PackagingOptionResponseDto> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    // Delete packaging Options
    @Test
    void shouldDeletePackagingOptionsByCode() {
        String code = "CE";

        PackagingOption entity1 = new PackagingOption();
        PackagingOption entity2 = new PackagingOption();

        when(repository.findByCode(code))
            .thenReturn(List.of(entity1, entity2));

        service.delete(code);

        verify(repository).deleteAll(List.of(entity1, entity2));
    }

    // Delete Packaging Options : Invalid Product Code passed : Exception scenario
    @Test
    void shouldThrowExceptionWhenDeletingNonExistingCode() {
        String code = "LM";

        when(repository.findByCode(code))
            .thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> service.delete(code));

        assertEquals("No packaging options found for product code: LM",
            exception.getMessage());
    }
}
