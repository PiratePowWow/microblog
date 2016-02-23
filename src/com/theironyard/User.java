package com.theironyard;

import java.util.ArrayList;

/**
 * Created by PiratePowWow on 2/22/16.
 */
public class User {
    String name;
    String password;
    ArrayList<String> messages = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
