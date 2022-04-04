package com.galvanize.demo.Users;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class UserController {

    //set up repository
    private final UserRepository repository;
    //constructor for repository:
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    @JsonView(Views.ListView.class)
    public Iterable<Users> getAllUsers() {
            return repository.findAll();

    }

    @PostMapping("/users")
    @JsonView(Views.ListView.class)
    public Users saveUsers(@RequestBody Users newUser) {
        return repository.save(newUser);

    }

    @GetMapping("/users/{id}") //this is the get by ID path variable version
    @JsonView(Views.ListView.class)
    public Users getOneUser(@PathVariable Integer id) {
        return repository.findById(id).get();
    }

    @PatchMapping("/users/{id}")
    @JsonView(Views.ListView.class)
    public Users updateOneUser(@PathVariable int id, @RequestBody Map<String, Object> jsonMap) {
        Users oldUserRecord = this.repository.findById(id).get();

        jsonMap.forEach((key, value) -> {
            if (key.equals("email")) {
                oldUserRecord.setEmail((String) value);
            } else if (key.equals("password")) {
                oldUserRecord.setPassword((String) value);
            }
        });
        return this.repository.save(oldUserRecord);

    }

    @DeleteMapping("/users/{id}")
    public UserCounter deleteOneUserAndReturnTotalUserCount(@PathVariable int id) {
        repository.deleteById(id);
        long count = repository.count();
        UserCounter newCounter = new UserCounter();
        newCounter.setTotalCount(count);

        return newCounter;

    }

    @PostMapping("/users/authenticate")
    public UserAuth returnUserPassMatchResults(@RequestBody Users user) {

        Users oldUserRecord = this.repository.findUsersByEmail(user.getEmail());
        if (oldUserRecord.getPassword().equals(user.getPassword())) {
            return new UserAuth(true, oldUserRecord);
        } return new UserAuth(false);

    }

}
