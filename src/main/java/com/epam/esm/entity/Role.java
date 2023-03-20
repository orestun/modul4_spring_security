package com.epam.esm.entity;

import com.epam.esm.utils.Roles;
import jakarta.persistence.*;
import lombok.Getter;

@Embeddable
@Getter
public class Role{
    private String role;

    public Role(Roles role) {
        this.role = role.name();
    }

    public Role() {

    }
}
