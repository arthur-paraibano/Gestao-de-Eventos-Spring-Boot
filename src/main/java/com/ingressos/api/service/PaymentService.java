package com.ingressos.api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingressos.api.dto.GeneralDto;
import com.ingressos.api.enums.PaymentStatus;
import com.ingressos.api.exceptions.IncorrectParameterException;
import com.ingressos.api.exceptions.IsNullException;
import com.ingressos.api.model.OrderModel;
import com.ingressos.api.model.PaymentModel;
import com.ingressos.api.repository.PaymentRepository;
import com.ingressos.api.util.InternationalizationUtil;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repository;
    @Autowired
    private InternationalizationUtil messageIniernat;

    @Value("${mercadopago.access-token}")
    private String mercadoPagoAccessToken;

    @Value("${mercadopago.public-key}")
    private String mercadoPagoPublicKey;

    @Value("${app.base.url:http://localhost:8080}")
    private String baseUrl;

    @Transactional(readOnly = true)
    public List<PaymentModel> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public PaymentModel findById(GeneralDto dto) {
        if (dto == null || dto.id() == null) {
            throw new IsNullException(messageIniernat.getMessage("id.null"));
        }
        return repository.findById(dto.id())
                .orElseThrow(() -> new IncorrectParameterException(
                messageIniernat.getMessage("descriptions.payment.notfound")));
    }

    // Métodos para integração com Mercado Pago
    @Transactional
    public PaymentModel createPayment(OrderModel order, BigDecimal amount, String paymentMethod) {
        PaymentModel payment = new PaymentModel();
        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return repository.save(payment);
    }

    public String createMercadoPagoPreference(OrderModel order, BigDecimal amount, String description) {
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);
            PreferenceClient client = new PreferenceClient();

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(order.getId().toString())
                    .title(description)
                    .description("Compra de ingressos - Pedido #" + order.getId())
                    .quantity(1)
                    .currencyId("BRL")
                    .unitPrice(amount)
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(baseUrl + "/payment/success")
                    .pending(baseUrl + "/payment/pending")
                    .failure(baseUrl + "/payment/failure")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .externalReference(order.getId().toString())
                    .notificationUrl(baseUrl + "/api/payments/webhook/mercadopago")
                    .build();

            Preference preference = client.create(preferenceRequest);
            return preference.getId();

        } catch (MPException | MPApiException e) {
            throw new RuntimeException("Erro ao criar preferência no Mercado Pago: " + e.getMessage(), e);
        }
    }

    @Transactional
    public PaymentModel updatePaymentStatus(String transactionId, PaymentStatus status, String externalId) {
        Optional<PaymentModel> optionalPayment = repository.findByTransactionId(transactionId);

        if (optionalPayment.isEmpty()) {
            throw new RuntimeException("Pagamento não encontrado para transactionId: " + transactionId);
        }

        PaymentModel payment = optionalPayment.get();
        payment.setStatus(status);
        payment.setExternalPaymentId(externalId);
        payment.setUpdatedAt(LocalDateTime.now());

        if (status == PaymentStatus.APPROVED) {
            payment.setPaidAt(LocalDateTime.now());
        }

        return repository.save(payment);
    }

    public PaymentStatus mapMercadoPagoStatus(String mercadoPagoStatus) {
        return switch (mercadoPagoStatus.toLowerCase()) {
            case "approved" ->
                PaymentStatus.APPROVED;
            case "pending" ->
                PaymentStatus.PENDING;
            case "in_process" ->
                PaymentStatus.PROCESSING;
            case "rejected" ->
                PaymentStatus.REJECTED;
            case "cancelled" ->
                PaymentStatus.CANCELED;
            case "refunded" ->
                PaymentStatus.REFUNDED;
            default ->
                PaymentStatus.PENDING;
        };
    }

    public Optional<PaymentModel> findByTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId);
    }

    public Optional<PaymentModel> findByExternalPaymentId(String externalPaymentId) {
        return repository.findByExternalPaymentId(externalPaymentId);
    }

    public List<PaymentModel> findByOrder(OrderModel order) {
        return repository.findByOrder(order);
    }

}
