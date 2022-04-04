package com.galvanize.demo.UserTests;


import com.galvanize.demo.Users.UserRepository;
import com.galvanize.demo.Users.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository repository;

    @Test //Endpoint #1 successfully tested
    @Transactional
    @Rollback
    void getUsersOnlyShowsIdAndEmail() throws Exception {
        Users testUser = new Users();
        testUser.setEmail("theDude@dude.com");
        testUser.setPassword("Dudepassword");

        Users testUser2 = new Users();
        testUser2.setEmail("theDudette@dudette.com");
        testUser2.setPassword("Dudettepassword");

        this.repository.save(testUser);
        this.repository.save(testUser2);

        this.mvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].email", is("theDude@dude.com")))
                .andExpect(jsonPath("$[1].email", is("theDudette@dudette.com")));

    }

    @Test //Endpoint #2 POSTs a new user and returns only expected values
    @Transactional
    @Rollback
    void postUserAndReturnOnlyDesiredInfo() throws Exception {

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"john@example.com\", \"password\": \"something-secret\"}"))

                //.andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("john@example.com") ));

    }

    @Test //Endpoint #3 get single ID via path variable
    @Transactional
    @Rollback
    void getSingleUser() throws Exception {
        Users testUser = new Users();
        testUser.setEmail("1theDude@dude.com");
        testUser.setPassword("Dudepassword");
        Users record = this.repository.save(testUser);
        String path = String.format("/users/%d", record.getId());

        this.mvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(record.getId())))
                .andExpect(jsonPath("$.email", is("1theDude@dude.com")));

    }

    @Test // Endpoint #4: PATCH /users/{id}
    @Transactional
    @Rollback
    void updateSingleUserEmail() throws Exception {
        Users testUser = new Users();
        testUser.setEmail("john@example.com");
        testUser.setPassword("Dudepassword");
        Users record = this.repository.save(testUser);
        String path = String.format("/users/%d", record.getId());

        this.mvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"john@gmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(record.getId())))
                .andExpect(jsonPath("$.email", is("john@gmail.com")));

    }

    @Test // Endpoint #4: PATCH /users/{id}
    @Transactional
    @Rollback
    void updateSingleUserPassword() throws Exception {
        Users testUser = new Users();
        testUser.setEmail("john@example.com");
        testUser.setPassword("Dudepassword");
        Users record = this.repository.save(testUser);
        String path = String.format("/users/%d", record.getId());

        this.mvc.perform(patch(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\": \"password1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(record.getId())))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test //Endpoint #5 Delete and return the count with an Object
    @Transactional
    @Rollback
    void deleteSingleUserAndReturnCountRemaining() throws Exception {
        Users testUser = new Users();
        testUser.setEmail("theDude@dude.com");
        testUser.setPassword("Dudepassword");
        Users testUser2 = new Users();
        testUser2.setEmail("theDudette@dudette.com");
        testUser2.setPassword("Dudettepassword");
        Users testUser3 = new Users();
        testUser3.setEmail("theDudette@dudette.com");
        testUser3.setPassword("Dudettepassword");
        Users testUser4 = new Users();
        testUser4.setEmail("theDudette@dudette.com");
        testUser4.setPassword("Dudettepassword");

        this.repository.save(testUser);
        this.repository.save(testUser2);
        this.repository.save(testUser3);
        Users record = this.repository.save(testUser4);

        String path = String.format("/users/%d", record.getId());

        this.mvc.perform(delete(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount", is(3)));

    }

    @Test
    @Transactional
    @Rollback
    void postAndMatchUserPasswordBoolean() throws Exception {
        Users testUser = new Users();
        testUser.setEmail("john7@example.com");
        testUser.setPassword("password123");

        this.repository.save(testUser);



        this.mvc.perform(post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"john7@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated", is(true)))
                .andExpect(jsonPath("$.user.id", is(testUser.getId())))
                .andExpect(jsonPath("$.user.email", is("john7@example.com")));
    }

    @Test
    @Transactional
    @Rollback
    void postAndNotMatchUserPasswordBoolean() throws Exception {
        Users testUser = new Users();
        testUser.setEmail("john7@example.com");
        testUser.setPassword("password123");

        this.repository.save(testUser);

        this.mvc.perform(post("/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"john7@example.com\", \"password\": \"password1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated", is(false)));

    }

}
