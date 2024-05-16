package com.auth.domain.enums;

public enum RoleName {

    ADMIN("admin"),
    USER("user"),
    LEADER("leader");

    private final String name;

    RoleName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
