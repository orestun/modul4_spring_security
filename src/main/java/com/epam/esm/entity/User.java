package com.epam.esm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@Table
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "Name field for user can`t be null")
    @NotBlank(message = "Name field for user should be input using letters A-Z")
    @Size(max = 30, message = "Name field length can`t be more than 30 chars")
    @Column(length = 30)
    private String name;

    @NotNull(message = "Surname field for user can`t be null")
    @NotBlank(message = "Surname field for user should be input using letters A-Z")
    @Size(max = 30, message = "Surname field length can`t be more than 30 chars")
    @Column(length = 30)
    private String surname;

    @NotNull(message = "Email field for user can`t be null")
    @Email(message = "Incorrect email input")
    private String email;

    @NotNull(message = "Password can`t be null")
    @Size(min = 8,message = "Password length of chars should be more than 8")
    private String password;

    public User(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getName(), user.getName()) && Objects.equals(getSurname(), user.getSurname()) && Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSurname(), getEmail());
    }
}
