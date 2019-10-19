package com.zufar.service;


import com.zufar.status.Status;
import com.zufar.status.StatusRepository;
import com.zufar.status.StatusService;
import com.zufar.status.StatusDTO;
import com.zufar.exception.StatusNotFoundException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatusServiceTest {

    @Autowired
    @Qualifier("statusService")
    private DaoService<StatusDTO> statusService;

    @MockBean
    private StatusRepository statusRepository;

    private static final long ACTIVE_STATUS_ID = 1L;
    private static final String ACTIVE_STATUS_NAME = "Active";
    private static final Status ACTIVE_STATUS_ENTITY = new Status(ACTIVE_STATUS_ID, ACTIVE_STATUS_NAME);
    private static final StatusDTO ACTIVE_STATUS = new StatusDTO(ACTIVE_STATUS_ID, ACTIVE_STATUS_NAME);


    private static final long IN_PROCESS_STATUS_ID = 2L;
    private static final String IN_PROCESS_STATUS_NAME = "In-progress";
    private static final Status IN_PROCESS_STATUS_ENTITY = new Status(IN_PROCESS_STATUS_ID, IN_PROCESS_STATUS_NAME);
    private static final StatusDTO IN_PROCESS_STATUS = new StatusDTO(IN_PROCESS_STATUS_ID, IN_PROCESS_STATUS_NAME);

    private static final long COMPLETE_STATUS_ID = 3L;
    private static final String COMPLETE_STATUS_NAME = "Completed";
    private static final Status COMPLETE_STATUS_ENTITY = new Status(COMPLETE_STATUS_ID, COMPLETE_STATUS_NAME);
    private static final StatusDTO COMPLETE_STATUS = new StatusDTO(COMPLETE_STATUS_ID, COMPLETE_STATUS_NAME);

    private static final Collection<Status> ENTITY_STATUSES = new ArrayList<>();

    @BeforeClass
    public static void setUp() {
        ENTITY_STATUSES.add(ACTIVE_STATUS_ENTITY);
        ENTITY_STATUSES.add(COMPLETE_STATUS_ENTITY);
        ENTITY_STATUSES.add(IN_PROCESS_STATUS_ENTITY);
    }

    @Test
    public void whenGetAllCalledThenCollectionShouldBeFound() {
        when(statusRepository.findAll()).thenReturn(ENTITY_STATUSES);
        
        Collection<StatusDTO> expected = this.getExampleStatuses();
        Collection<StatusDTO> actual = this.statusService.getAll();
        
        verify(statusRepository, times(1)).findAll();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void whenGetAllWithSortByCalledThenCollectionShouldBeFound() {
        final String SORT_BY_ATTRIBUTE_ID = "id";
        Collection<Status> statuses = ENTITY_STATUSES
                .stream()
                .sorted(Comparator.comparing(Status::getId))
                .collect(Collectors.toList());
        
        
        when(statusRepository.findAll(Sort.by(SORT_BY_ATTRIBUTE_ID))).thenReturn(statuses);
        
        
        Collection<StatusDTO> expected = this.getExampleStatuses()
                .stream()
                .sorted(Comparator.comparing(StatusDTO::getId))
                .collect(Collectors.toList());
        Collection<StatusDTO> actual = this.statusService.getAll(SORT_BY_ATTRIBUTE_ID);

        
        verify(statusRepository, times(1)).findAll(Sort.by(SORT_BY_ATTRIBUTE_ID));
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void whenValidIdThenStatusShouldBeFound() {
        when(statusRepository.findById(ACTIVE_STATUS_ID)).thenReturn(Optional.of(ACTIVE_STATUS_ENTITY));
        
        StatusDTO actual = statusService.getById(ACTIVE_STATUS_ID);
        StatusDTO expected = getExpectedStatus();

        verify(statusRepository, times(1)).findById(ACTIVE_STATUS_ID);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test(expected = StatusNotFoundException.class)
    public void whenInvalidIdThenStatusNotFoundExceptionShouldThrow() {
        Long INVALID_STATUS_ID = 55555L;
        when(statusRepository.existsById(INVALID_STATUS_ID)).thenReturn(false);
        
        statusService.getById(INVALID_STATUS_ID);
    }

    @Test
    public void whenSaveCalledThenStatusShouldBeReturned() {
        final Status status = new Status(null, ACTIVE_STATUS_NAME);
        
        when(statusRepository.save(status)).thenReturn(ACTIVE_STATUS_ENTITY);
        
        StatusDTO actual = statusService.save(new StatusDTO(null, ACTIVE_STATUS_NAME));
        StatusDTO expected = getExpectedStatus();

        verify(statusRepository, times(1)).save(status);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void whenUpdateCalledThenStatusShouldBeReturned() {
        when(statusRepository.save(ACTIVE_STATUS_ENTITY)).thenReturn(ACTIVE_STATUS_ENTITY);
        when(statusRepository.existsById(ACTIVE_STATUS_ID)).thenReturn(true);
        
        StatusDTO actual = statusService.update(ACTIVE_STATUS);
        StatusDTO expected = getExpectedStatus();

        verify(statusRepository, times(1)).save(ACTIVE_STATUS_ENTITY);
        verify(statusRepository, times(1)).existsById(ACTIVE_STATUS_ID);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void whenConvertStatusEntityCalledThenStatusDTOShouldBeReturned() {
        StatusDTO actual = StatusService.convertToStatusDTO(ACTIVE_STATUS_ENTITY);
        StatusDTO expected = getExpectedStatus();

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void whenConvertStatusDTOCalledThenStatusEntityShouldBeReturned() {
        Status actual = StatusService.convertToStatus(ACTIVE_STATUS);
        Status expected = getExpectedStatusEntity();

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void whenDeleteStatusByIdCalled() {
        when(statusRepository.existsById(ACTIVE_STATUS_ID)).thenReturn(true);
        doNothing().when(statusRepository).deleteById(ACTIVE_STATUS_ID);
        
        statusService.deleteById(ACTIVE_STATUS_ID);

        verify(statusRepository, times(1)).existsById(ACTIVE_STATUS_ID);
        verify(statusRepository, times(1)).deleteById(ACTIVE_STATUS_ID);
    }
    
    private Collection<StatusDTO> getExampleStatuses() {
        Collection<StatusDTO> exampleStatuses = new ArrayList<>();
        exampleStatuses.add(ACTIVE_STATUS);
        exampleStatuses.add(COMPLETE_STATUS);
        exampleStatuses.add(IN_PROCESS_STATUS);
        return exampleStatuses;
    }

    private StatusDTO getExpectedStatus() {
        return ACTIVE_STATUS;
    }

    private Status getExpectedStatusEntity() {
        return ACTIVE_STATUS_ENTITY;
    }
}
