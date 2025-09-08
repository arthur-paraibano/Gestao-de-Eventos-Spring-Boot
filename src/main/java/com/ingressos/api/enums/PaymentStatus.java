package com.ingressos.api.enums;

public enum PaymentStatus {
    PENDING("Pendente"),
    PROCESSING("Processando"),
    APPROVED("Aprovado"),
    REJECTED("Rejeitado"),
    CANCELED("Cancelado"),
    REFUNDED("Reembolsado"),
    EXPIRED("Expirado");

    private final String description;

    PaymentStatus(String description) {
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