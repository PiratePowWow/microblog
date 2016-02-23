package com.theironyard;

import com.sun.org.apache.xpath.internal.operations.Mod;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static User user;

    public static void main(String[] args) {
        Spark.init();

        Spark.get("/",(((request, response) ->{
            HashMap m = new HashMap();
            if (user==null){
                return new ModelAndView(m, "login.html");
            }else{
                m.put("name", user.name);
                m.put("messages", user.messages);
                return new ModelAndView(m, "messages.html");
            }

        })), new MustacheTemplateEngine());

        Spark.get("/messages", (((request1, response1) -> {
            return new ModelAndView(user, "messages.html");
        }
        )), new MustacheTemplateEngine());

        Spark.get("/new-user", (((request, response) -> {
            return new ModelAndView(null, "newUser.html");
        }
        )), new MustacheTemplateEngine());


        Spark.post("/new-user", ((request1, response1) -> {
            String name = request1.queryParams("name");
            String password = request1.queryParams("password");
            user = new User(name, password);
            response1.redirect("/login");
            return "";
        }));


        Spark.post("/login", ((request, response) -> {
            String name = request.queryParams("loginName");
            user = new User(name);
            response.redirect("/");
            return "";

        }));

        Spark.post("/create-message", ((request, response) -> {
            String message = request.queryParams("message");
            user.messages.add(message);
            response.redirect("/messages");
            return "";
        }));


    }
}
