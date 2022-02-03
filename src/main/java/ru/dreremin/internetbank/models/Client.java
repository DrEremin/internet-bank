package ru.dreremin.internetbank.models;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.persistence.*;

@Slf4j
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    private String patronymic;

    private LocalDate birthday;

    private String address;

    public Client() {}

    public Client(String firstName,
                  String lastName,
                  String patronymic,
                  String birthday,
                  String address) throws DateTimeParseException {

        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.address = address;
        try {
            this.birthday = LocalDate.parse(birthday);
        } catch (DateTimeParseException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }
}
