package com.zufar.domain.status;

import com.zufar.dto.StatusDTO;
import com.zufar.exception.StatusNotFoundException;
import com.zufar.service.DaoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatusService implements DaoService<StatusDTO> {

    private static final Logger LOGGER = LogManager.getLogger(StatusService.class);
    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Collection<StatusDTO> getAll() {
        return ((Collection<Status>) this.statusRepository.findAll())
                .stream()
                .map(StatusService::convertToStatusDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<StatusDTO> getAll(String sortBy) {
        return ((Collection<Status>) this.statusRepository.findAll(Sort.by(sortBy)))
                .stream()
                .map(StatusService::convertToStatusDTO)
                .collect(Collectors.toList());
    }

    public StatusDTO getById(Long id) {
        Status statusEntity = this.statusRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "The status with id = " + id + " not found.";
            StatusNotFoundException statusNotFoundException = new StatusNotFoundException(errorMessage);
            LOGGER.error(errorMessage, statusNotFoundException);
            return statusNotFoundException;
        });
        return StatusService.convertToStatusDTO(statusEntity);
    }

    public StatusDTO save(StatusDTO status) {
        Status statusEntity = StatusService.convertToStatus(status);
        statusEntity = this.statusRepository.save(statusEntity);
        return StatusService.convertToStatusDTO(statusEntity);
    }

    public StatusDTO update(StatusDTO status) {
        this.isExists(status.getId());
        Status statusEntity = StatusService.convertToStatus(status);
        statusEntity = this.statusRepository.save(statusEntity);
        return StatusService.convertToStatusDTO(statusEntity);
    }

    public void deleteById(Long id) {
        this.isExists(id);
        this.statusRepository.deleteById(id);
    }

    public Boolean isExists(Long id) {
        if (!this.statusRepository.existsById(id)) {
            final String errorMessage = "The status with id = " + id + " not found.";
            StatusNotFoundException statusNotFoundException = new StatusNotFoundException(errorMessage);
            LOGGER.error(errorMessage, statusNotFoundException);
            throw statusNotFoundException;
        }
        return true;
    }

    public static StatusDTO convertToStatusDTO(Status status) {
        Objects.requireNonNull(status, "There is no status to convert.");
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setId(status.getId());
        statusDTO.setName(status.getName());
        return statusDTO;
    }

    public static Status convertToStatus(StatusDTO status) {
        Objects.requireNonNull(status, "There is no status to convert.");
        Status statusEntity = new Status();
        statusEntity.setId(status.getId());
        statusEntity.setName(status.getName());
        return statusEntity;
    }
}
