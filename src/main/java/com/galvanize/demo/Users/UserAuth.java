package com.galvanize.demo.Users;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAuth {

    private boolean authenticated;
    private Users user;

    public UserAuth(boolean authenticated, Users user) {
        this.authenticated = authenticated;
        this.user = user;
    }

    public UserAuth(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
