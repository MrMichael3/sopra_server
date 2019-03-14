package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.entity.UserLogin;
import ch.uzh.ifi.seal.soprafs19.entity.UserToken;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import jdk.jfr.Registered;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }
    @GetMapping("/users/{userId}")
    User getUser(@PathVariable long userId){
        return this.service.getUser(userId);
    }


    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

    //new put-function, should check username + pw and return true if correct
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/users/validation")
    UserToken verifyUser(@RequestBody UserLogin unverifiedUser){return this.service.verifyUser(unverifiedUser);}

}



