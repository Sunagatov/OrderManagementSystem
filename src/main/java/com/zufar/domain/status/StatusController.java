package com.zufar.domain.status;

import com.zufar.dto.StatusDTO;
import com.zufar.service.DaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "statuses", produces = {MediaType.APPLICATION_JSON_VALUE})
public class StatusController {

    private final DaoService<StatusDTO> statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public @ResponseBody
    Collection<StatusDTO> getStatuses() {
        return this.statusService.getAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    StatusDTO getStatus(@PathVariable Long id) {
        return this.statusService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteStatus(@PathVariable Long id) {
        this.statusService.deleteById(id);
    }

    @PostMapping
    public @ResponseBody
    StatusDTO saveStatus(@RequestBody StatusDTO status) {
        return this.statusService.save(status);
    }

    @PutMapping
    public @ResponseBody
    StatusDTO updateStatus(@RequestBody StatusDTO status) {
        return this.statusService.update(status);
    }
}
