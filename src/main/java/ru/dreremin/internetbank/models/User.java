package ru.dreremin.internetbank.models;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;


public class User {

    private long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate birthDay;
    private String password;
    private String login;
}
