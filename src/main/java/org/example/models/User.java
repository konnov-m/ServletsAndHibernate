package org.example.models;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "login")
    private String login;

    public User(Long id, String name, String password, String login) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.login = login;
    }

    public User() {

    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

}
