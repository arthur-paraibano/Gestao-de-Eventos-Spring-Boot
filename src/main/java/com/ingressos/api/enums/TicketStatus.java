package com.ingressos.api.enums;

public enum TicketStatus {
    AVAILABLE("Disponível"),
    RESERVED("Reservado"),
    SOLD("Vendido"),
    USED("Utilizado"),
    CANCELED("Cancelado"),
    EXPIRED("Expirado");

    private final String description;

    TicketStatus(String description) {
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