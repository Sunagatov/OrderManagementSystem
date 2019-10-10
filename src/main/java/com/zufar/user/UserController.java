package com.zufar.user;

import com.zufar.service.DaoService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = "users", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {

    private final DaoService<UserDTO> userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public @ResponseBody
    Collection<UserDTO> getUsers() {
        return this.userService.getAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    UserDTO getUser(@PathVariable Long id) {
        return this.userService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        this.userService.deleteById(id);
    }

    @PostMapping
    public @ResponseBody
    UserDTO saveUser(@RequestBody UserDTO user) {
        return this.userService.save(user);
    }

    @PutMapping
    public @ResponseBody
    UserDTO updateUser(@RequestBody UserDTO user) {
        return this.userService.update(user);
    }
}
