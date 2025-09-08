package com.ingressos.api.enums;

public enum EventStatus {
    DRAFT("Rascunho"),
    PUBLISHED("Publicado"),
    CANCELED("Cancelado"),
    FINISHED("Finalizado"),
    SUSPENDED("Suspenso");

    private final String description;

    EventStatus(String description) {
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