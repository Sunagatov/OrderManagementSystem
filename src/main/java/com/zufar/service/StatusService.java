package com.zufar.service;

import com.zufar.dto.StatusDTO;
import com.zufar.exception.StatusNotFoundException;
import com.zufar.model.Status;
import com.zufar.repository.StatusRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatusService {

    private static final Logger LOGGER = LogManager.getLogger(StatusService.class);
    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Collection<StatusDTO> getAllStatuses() {
        return  ((Collection<Status>) this.statusRepository.findAll())
                .stream()
                .map(StatusService::convertToStatusDTO)
                .collect(Collectors.toList());
    }

    public StatusDTO getStatus(Long id) {
        Status statusEntity = this.statusRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "The status with id = " + id + " not found.";
            StatusNotFoundException statusNotFoundException = new StatusNotFoundException(errorMessage);
            LOGGER.error(errorMessage, statusNotFoundException);
            return statusNotFoundException;
        });
        return StatusService.convertToStatusDTO(statusEntity);
    }

    public StatusDTO saveStatus(StatusDTO status) {
        Status statusEntity = StatusService.convertToStatus(status);
        statusEntity = this.statusRepository.save(statusEntity);
        return StatusService.convertToStatusDTO(statusEntity);
    }

    public StatusDTO updateStatus(StatusDTO status) {
        this.isStatusExists(status.getId());
        Status statusEntity = StatusService.convertToStatus(status);
        statusEntity = this.statusRepository.save(statusEntity);
        return StatusService.convertToStatusDTO(statusEntity);
    }

    public void deleteStatus(Long id) {
        this.isStatusExists(id);
        this.statusRepository.deleteById(id);
    }

    private void isStatusExists(Long id) {
        if (!this.statusRepository.existsById(id)) {
            final String errorMessage = "The status with id = " + id + " not found.";
            StatusNotFoundException statusNotFoundException = new StatusNotFoundException(errorMessage);
            LOGGER.error(errorMessage, statusNotFoundException);
            throw statusNotFoundException;
        }
    }

    public static StatusDTO convertToStatusDTO(Status status) {
        UtilService.isObjectNull(status, LOGGER, "There is no status to convert.");
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setId(status.getId());
        statusDTO.setName(status.getName());
        return statusDTO;
    }

    public static Status convertToStatus(StatusDTO status) {
        UtilService.isObjectNull(status, LOGGER, "There is no status to convert.");
        Status statusEntity = new Status();
        statusEntity.setId(status.getId());
        statusEntity.setName(status.getName());
        return statusEntity;
    }
}
