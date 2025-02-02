package org.example.springbootdeveloper.User.domain;

import lombok.Getter;

@Getter
public enum Role {
    GUEST("ROLE_GUEST"), USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }
}
