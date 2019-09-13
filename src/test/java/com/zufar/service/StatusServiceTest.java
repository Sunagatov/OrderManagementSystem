package com.zufar.service;

import com.zufar.domain.status.Status;
import com.zufar.domain.status.StatusRepository;
import com.zufar.domain.status.StatusService;
import com.zufar.dto.StatusDTO;
import com.zufar.exception.StatusNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatusServiceTest {

    @Autowired
    @Qualifier("statusService")
    private DaoService<StatusDTO> statusService;

    @MockBean
    private StatusRepository statusRepository;

    private final long ACTIVE_STATUS_ID = 1L;
    private final String ACTIVE_STATUS_NAME = "Active";
    private final Status ACTIVE_STATUS_ENTITY = new Status(ACTIVE_STATUS_ID, ACTIVE_STATUS_NAME);
    private final StatusDTO ACTIVE_STATUS = new StatusDTO(ACTIVE_STATUS_ID, ACTIVE_STATUS_NAME);


    private final long IN_PROCESS_STATUS_ID = 2L;
    private final String IN_PROCESS_STATUS_NAME = "in-progress";
    private final Status IN_PROCESS_STATUS_ENTITY = new Status(IN_PROCESS_STATUS_ID, IN_PROCESS_STATUS_NAME);
    private final StatusDTO IN_PROCESS_STATUS = new StatusDTO(IN_PROCESS_STATUS_ID, IN_PROCESS_STATUS_NAME);

    private final long COMPLETE_STATUS_ID = 3L;
    private final String COMPLETE_STATUS_NAME = "Completed";
    private final Status COMPLETE_STATUS_ENTITY = new Status(COMPLETE_STATUS_ID, COMPLETE_STATUS_NAME);
    private final StatusDTO COMPLETE_STATUS = new StatusDTO(COMPLETE_STATUS_ID, COMPLETE_STATUS_NAME);

    private final Collection<Status> statuses = new ArrayList<>();


    @Before
    public void setUp() {
        statuses.add(ACTIVE_STATUS_ENTITY);
        statuses.add(COMPLETE_STATUS_ENTITY);
        statuses.add(IN_PROCESS_STATUS_ENTITY);

        Mockito.when(statusRepository.findById(COMPLETE_STATUS_ID))
                .thenReturn(Optional.of(COMPLETE_STATUS_ENTITY));
        Mockito.when(statusRepository.existsById(ACTIVE_STATUS_ID))
                .thenReturn(true);
        Mockito.when(statusRepository.existsById(COMPLETE_STATUS_ID))
                .thenReturn(true);
        Mockito.when(statusRepository.findAll())
                .thenReturn(statuses);
    }

    @Test
    public void whenGetAllCalledThenCollectionShouldBeFound() {
        Mockito.when(statusRepository.findAll()).thenReturn(statuses);
        Collection<StatusDTO> expected = this.getExampleStatuses();
        final Collection<StatusDTO> found = this.statusService.getAll();
        Assert.assertNotNull(found);
        Assert.assertEquals(expected, found);
    }

    @Test
    public void whenGetAllWithSortByCalledThenCollectionShouldBeFound() {
        String SORT_BY_ATTRIBUTE = "name";
        Mockito.when(statusRepository.findAll(Sort.by(SORT_BY_ATTRIBUTE))).thenReturn(statuses);
        Collection<StatusDTO> expected = this.getExampleStatuses();
        final Collection<StatusDTO> found = this.statusService.getAll(SORT_BY_ATTRIBUTE);
        Assert.assertNotNull(found);
        Assert.assertEquals(expected, found);
    }

    @Test
    public void whenValidIdThenStatusShouldBeFound() {
        Mockito.when(statusRepository.findById(ACTIVE_STATUS_ID)).thenReturn(Optional.of(ACTIVE_STATUS_ENTITY));
        StatusDTO found = statusService.getById(ACTIVE_STATUS_ID);
        Assert.assertNotNull(found);
        Assert.assertEquals(ACTIVE_STATUS, found);
    }

    @Test(expected = StatusNotFoundException.class)
    public void whenInvalidIdThenStatusNotFoundExceptionShouldThrow() {
        Long INVALID_STATUS_ID = 55555L;
        statusService.getById(INVALID_STATUS_ID);
    }

    @Test
    public void whenSaveCalledThenStatusShouldBeReturned() {
        Mockito.when(statusRepository.save(new Status(null, ACTIVE_STATUS_NAME))).thenReturn(ACTIVE_STATUS_ENTITY);
        StatusDTO found = statusService.save(new StatusDTO(null, ACTIVE_STATUS_NAME));
        Assert.assertNotNull(found);
        Assert.assertEquals(ACTIVE_STATUS, found);
    }

    @Test
    public void whenUpdateCalledThenStatusShouldBeReturned() {
        Mockito.when(statusRepository.save(ACTIVE_STATUS_ENTITY)).thenReturn(ACTIVE_STATUS_ENTITY);
        StatusDTO found = statusService.update(ACTIVE_STATUS);
        Assert.assertNotNull(found);
        Assert.assertEquals(ACTIVE_STATUS, found);
    }

    @Test
    public void whenConvertStatusEntityCalledThenStatusDTOShouldBeReturned() {
        final StatusDTO found = StatusService.convertToStatusDTO(ACTIVE_STATUS_ENTITY);
        Assert.assertEquals(ACTIVE_STATUS, found);
    }

    @Test
    public void whenConvertStatusDTOCalledThenStatusEntityShouldBeReturned() {
        final Status found = StatusService.convertToStatus(ACTIVE_STATUS);
        Assert.assertEquals(ACTIVE_STATUS_ENTITY, found);
    }

    private Collection<StatusDTO> getExampleStatuses() {
        Collection<StatusDTO> exampleStatuses = new ArrayList<>();
        exampleStatuses.add(ACTIVE_STATUS);
        exampleStatuses.add(COMPLETE_STATUS);
        exampleStatuses.add(IN_PROCESS_STATUS);
        return exampleStatuses;
    }
}
