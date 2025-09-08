package com.ingressos.api.enums;

public enum UserRole {
    ADMIN("Administrador"),
    ORGANIZER("Organizador"),
    USER("Usuário");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}