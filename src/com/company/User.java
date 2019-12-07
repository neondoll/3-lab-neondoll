package com.company;

class User {
    String name;
    private String login;
    private String password;

    User() {
        this.name = "None";
    }
    User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }
    boolean enter(String login, String password) { return this.login.equals(login) && this.password.equals(password); }
    String getName() { return name; }
    String getLogin() { return login; }
    String getPassword() { return password; }
}
