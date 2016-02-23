package com.theironyard;

import com.sun.org.apache.xpath.internal.operations.Mod;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static HashMap<String, User> users = new HashMap<>();


    public static void main(String[] args) {
        Spark.init();

        Spark.get("/",(((request, response) ->{
            User user = getUserFromSession(request.session());
            if (user==null){
                return new ModelAndView(null, "login.html");
            }else{
                return new ModelAndView(user, "messages.html");
            }

        })), new MustacheTemplateEngine());

        Spark.get("/messages", (((request, response) -> {
            User user = getUserFromSession(request.session());
            return new ModelAndView(user, "messages.html");
        }
        )), new MustacheTemplateEngine());

        Spark.get("/new-user", (((request, response) -> {
            return new ModelAndView(null, "newUser.html");
        }
        )), new MustacheTemplateEngine());


        Spark.post("/new-user", ((request1, response1) -> {
            String name = request1.queryParams("loginName");
            String password = request1.queryParams("password");
            User user = new User(name, password);
            users.put(name, user);
            Session session = request1.session(true);
            session.attribute("userName", name);
            response1.redirect("/");
            return "";
        }));


        Spark.post("/login", ((request, response) -> {
            String name = request.queryParams("loginName");
            String password = request.queryParams("password");
            if(users.get(name)==null){
                response.redirect("/");
            }
            else if (users.get(name).password.equals(password)) {
                Session session = request.session(true);
                session.attribute("userName", name);
                response.redirect("/");
            }
            else {
                response.redirect("/login");
            }
            return "";

        }));

        Spark.post(
                "/create-message",
                ((request, response) -> {
                    User user = getUserFromSession(request.session());
                    String message = request.queryParams("message");
                    user.messages.add(message);
                    response.redirect("/messages");
                    return "";
        }));
        Spark.post(
                "/logout",
                ((request, response) -> {
                    request.session().removeAttribute("userName");
                    request.session().invalidate();
                    response.redirect("/");
                    return "";
                })
        );


    }
    static User getUserFromSession(Session session){
        String name = session.attribute("userName");
        return users.get(name);
    }
}
