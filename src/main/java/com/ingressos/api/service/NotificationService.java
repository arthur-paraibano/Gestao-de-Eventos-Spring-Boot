package com.ingressos.api.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ingressos.api.model.EventModel;
import com.ingressos.api.model.OrderModel;
import com.ingressos.api.model.TicketModel;
import com.ingressos.api.model.UserModel;

import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@ingressos.com}")
    private String fromEmail;

    @Value("${app.name:Sistema de Ingressos}")
    private String appName;

    @Value("${app.url:http://localhost:8080}")
    private String appUrl;

    // Notificação de confirmação de compra
    public CompletableFuture<Boolean> sendPurchaseConfirmation(OrderModel order, List<TicketModel> tickets) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(order.getUser().getEmail());
                helper.setSubject("Confirmação de Compra - " + order.getEvent().getName());

                String htmlContent = buildPurchaseConfirmationEmail(order, tickets);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                logger.info("Email de confirmação de compra enviado para: {}", order.getUser().getEmail());
                return true;
            } catch (Exception e) {
                logger.error("Erro ao enviar email de confirmação de compra: ", e);
                return false;
            }
        });
    }

    // Notificação de pagamento aprovado
    public CompletableFuture<Boolean> sendPaymentApproved(OrderModel order) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(order.getUser().getEmail());
                helper.setSubject("Pagamento Aprovado - " + order.getEvent().getName());

                String htmlContent = buildPaymentApprovedEmail(order);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                logger.info("Email de pagamento aprovado enviado para: {}", order.getUser().getEmail());
                return true;
            } catch (Exception e) {
                logger.error("Erro ao enviar email de pagamento aprovado: ", e);
                return false;
            }
        });
    }

    // Notificação de pagamento rejeitado
    public CompletableFuture<Boolean> sendPaymentRejected(OrderModel order, String reason) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(order.getUser().getEmail());
                helper.setSubject("Pagamento Rejeitado - " + order.getEvent().getName());

                String htmlContent = buildPaymentRejectedEmail(order, reason);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                logger.info("Email de pagamento rejeitado enviado para: {}", order.getUser().getEmail());
                return true;
            } catch (Exception e) {
                logger.error("Erro ao enviar email de pagamento rejeitado: ", e);
                return false;
            }
        });
    }

    // Notificação de lembrete de evento
    public CompletableFuture<Boolean> sendEventReminder(UserModel user, EventModel event, TicketModel ticket) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(user.getEmail());
                helper.setSubject("Lembrete: " + event.getName() + " acontece em breve!");

                String htmlContent = buildEventReminderEmail(user, event, ticket);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                logger.info("Email de lembrete de evento enviado para: {}", user.getEmail());
                return true;
            } catch (Exception e) {
                logger.error("Erro ao enviar email de lembrete de evento: ", e);
                return false;
            }
        });
    }

    // Notificação de cancelamento de evento
    public CompletableFuture<Boolean> sendEventCancellation(UserModel user, EventModel event, OrderModel order) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(user.getEmail());
                helper.setSubject("Evento Cancelado - " + event.getName());

                String htmlContent = buildEventCancellationEmail(user, event, order);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                logger.info("Email de cancelamento de evento enviado para: {}", user.getEmail());
                return true;
            } catch (Exception e) {
                logger.error("Erro ao enviar email de cancelamento de evento: ", e);
                return false;
            }
        });
    }

    // Notificação de boas-vindas
    public CompletableFuture<Boolean> sendWelcomeEmail(UserModel user) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(user.getEmail());
                helper.setSubject("Bem-vindo ao " + appName + "!");

                String htmlContent = buildWelcomeEmail(user);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                logger.info("Email de boas-vindas enviado para: {}", user.getEmail());
                return true;
            } catch (Exception e) {
                logger.error("Erro ao enviar email de boas-vindas: ", e);
                return false;
            }
        });
    }

    // Notificação de recuperação de senha
    public CompletableFuture<Boolean> sendPasswordReset(UserModel user, String resetToken) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(user.getEmail());
                helper.setSubject("Recuperação de Senha - " + appName);

                String htmlContent = buildPasswordResetEmail(user, resetToken);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                logger.info("Email de recuperação de senha enviado para: {}", user.getEmail());
                return true;
            } catch (Exception e) {
                logger.error("Erro ao enviar email de recuperação de senha: ", e);
                return false;
            }
        });
    }

    // Construção dos templates de email
    private String buildPurchaseConfirmationEmail(OrderModel order, List<TicketModel> tickets) {
        StringBuilder html = new StringBuilder();
        html.append(getEmailHeader("Confirmação de Compra"));

        html.append("<div class='content'>");
        html.append("<h2>Olá, ").append(order.getUser().getName()).append("!</h2>");
        html.append("<p>Sua compra foi realizada com sucesso! Aqui estão os detalhes:</p>");

        html.append("<div class='event-info'>");
        html.append("<h3>").append(order.getEvent().getName()).append("</h3>");
        html.append("<p><strong>Data:</strong> ").append(order.getEvent().getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("</p>");
        html.append("<p><strong>Local:</strong> ").append(order.getEvent().getAddress()).append("</p>");
        html.append("</div>");

        html.append("<div class='order-info'>");
        html.append("<h3>Detalhes do Pedido</h3>");
        html.append("<p><strong>Número do Pedido:</strong> ").append(order.getId()).append("</p>");
        html.append("<p><strong>Quantidade de Ingressos:</strong> ").append(tickets.size()).append("</p>");
        html.append("<p><strong>Valor Total:</strong> R$ ").append(String.format("%.2f", order.getTotalAmount())).append("</p>");
        html.append("</div>");

        html.append("<p>Seus ingressos estão anexados a este email e também podem ser acessados em sua conta.</p>");
        html.append("<div class='cta'>");
        html.append("<a href='").append(appUrl).append("/orders/").append(order.getId()).append("' class='button'>Ver Pedido</a>");
        html.append("</div>");
        html.append("</div>");

        html.append(getEmailFooter());
        return html.toString();
    }

    private String buildPaymentApprovedEmail(OrderModel order) {
        StringBuilder html = new StringBuilder();
        html.append(getEmailHeader("Pagamento Aprovado"));

        html.append("<div class='content'>");
        html.append("<h2>Ótimas notícias, ").append(order.getUser().getName()).append("!</h2>");
        html.append("<p>Seu pagamento foi aprovado e seus ingressos estão confirmados!</p>");

        html.append("<div class='event-info'>");
        html.append("<h3>").append(order.getEvent().getName()).append("</h3>");
        html.append("<p><strong>Data:</strong> ").append(order.getEvent().getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("</p>");
        html.append("</div>");

        html.append("<div class='cta'>");
        html.append("<a href='").append(appUrl).append("/tickets/").append(order.getId()).append("' class='button'>Baixar Ingressos</a>");
        html.append("</div>");
        html.append("</div>");

        html.append(getEmailFooter());
        return html.toString();
    }

    private String buildPaymentRejectedEmail(OrderModel order, String reason) {
        StringBuilder html = new StringBuilder();
        html.append(getEmailHeader("Pagamento Rejeitado"));

        html.append("<div class='content'>");
        html.append("<h2>Olá, ").append(order.getUser().getName()).append("</h2>");
        html.append("<p>Infelizmente, seu pagamento foi rejeitado.</p>");

        if (reason != null && !reason.isEmpty()) {
            html.append("<p><strong>Motivo:</strong> ").append(reason).append("</p>");
        }

        html.append("<p>Você pode tentar realizar o pagamento novamente ou entrar em contato conosco para mais informações.</p>");

        html.append("<div class='cta'>");
        html.append("<a href='").append(appUrl).append("/orders/").append(order.getId()).append("/retry-payment' class='button'>Tentar Novamente</a>");
        html.append("</div>");
        html.append("</div>");

        html.append(getEmailFooter());
        return html.toString();
    }

    private String buildEventReminderEmail(UserModel user, EventModel event, TicketModel ticket) {
        StringBuilder html = new StringBuilder();
        html.append(getEmailHeader("Lembrete de Evento"));

        html.append("<div class='content'>");
        html.append("<h2>Olá, ").append(user.getName()).append("!</h2>");
        html.append("<p>Seu evento está chegando! Não se esqueça:</p>");

        html.append("<div class='event-info'>");
        html.append("<h3>").append(event.getName()).append("</h3>");
        html.append("<p><strong>Data:</strong> ").append(event.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("</p>");
        html.append("<p><strong>Local:</strong> ").append(event.getAddress()).append("</p>");
        html.append("</div>");

        html.append("<p>Tenha seu ingresso em mãos (digital ou impresso) para apresentar na entrada.</p>");

        html.append("<div class='cta'>");
        html.append("<a href='").append(appUrl).append("/tickets/").append(ticket.getId()).append("' class='button'>Ver Ingresso</a>");
        html.append("</div>");
        html.append("</div>");

        html.append(getEmailFooter());
        return html.toString();
    }

    private String buildEventCancellationEmail(UserModel user, EventModel event, OrderModel order) {
        StringBuilder html = new StringBuilder();
        html.append(getEmailHeader("Evento Cancelado"));

        html.append("<div class='content'>");
        html.append("<h2>Olá, ").append(user.getName()).append("</h2>");
        html.append("<p>Lamentamos informar que o evento <strong>").append(event.getName()).append("</strong> foi cancelado.</p>");

        html.append("<p>O reembolso será processado automaticamente e você receberá o valor pago em até 5 dias úteis.</p>");

        html.append("<div class='order-info'>");
        html.append("<p><strong>Número do Pedido:</strong> ").append(order.getId()).append("</p>");
        html.append("<p><strong>Valor a ser reembolsado:</strong> R$ ").append(String.format("%.2f", order.getTotalAmount())).append("</p>");
        html.append("</div>");

        html.append("<p>Se tiver alguma dúvida, entre em contato conosco.</p>");
        html.append("</div>");

        html.append(getEmailFooter());
        return html.toString();
    }

    private String buildWelcomeEmail(UserModel user) {
        StringBuilder html = new StringBuilder();
        html.append(getEmailHeader("Bem-vindo"));

        html.append("<div class='content'>");
        html.append("<h2>Bem-vindo, ").append(user.getName()).append("!</h2>");
        html.append("<p>Obrigado por se cadastrar no ").append(appName).append(". Agora você pode:</p>");

        html.append("<ul>");
        html.append("<li>Descobrir eventos incríveis</li>");
        html.append("<li>Comprar ingressos com segurança</li>");
        html.append("<li>Gerenciar seus ingressos</li>");
        html.append("<li>Receber notificações sobre eventos</li>");
        html.append("</ul>");

        html.append("<div class='cta'>");
        html.append("<a href='").append(appUrl).append("/events' class='button'>Explorar Eventos</a>");
        html.append("</div>");
        html.append("</div>");

        html.append(getEmailFooter());
        return html.toString();
    }

    private String buildPasswordResetEmail(UserModel user, String resetToken) {
        StringBuilder html = new StringBuilder();
        html.append(getEmailHeader("Recuperação de Senha"));

        html.append("<div class='content'>");
        html.append("<h2>Olá, ").append(user.getName()).append("</h2>");
        html.append("<p>Você solicitou a recuperação de sua senha. Clique no botão abaixo para criar uma nova senha:</p>");

        html.append("<div class='cta'>");
        html.append("<a href='").append(appUrl).append("/reset-password?token=").append(resetToken).append("' class='button'>Redefinir Senha</a>");
        html.append("</div>");

        html.append("<p><small>Este link é válido por 24 horas. Se você não solicitou esta recuperação, ignore este email.</small></p>");
        html.append("</div>");

        html.append(getEmailFooter());
        return html.toString();
    }

    private String getEmailHeader(String title) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<title>" + title + "</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f4f4f4; }"
                + ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; }"
                + ".header { background-color: #007bff; color: white; padding: 20px; text-align: center; }"
                + ".content { padding: 30px; }"
                + ".event-info, .order-info { background-color: #f8f9fa; padding: 15px; margin: 15px 0; border-radius: 5px; }"
                + ".button { display: inline-block; background-color: #007bff; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; margin: 15px 0; }"
                + ".cta { text-align: center; margin: 20px 0; }"
                + ".footer { background-color: #6c757d; color: white; padding: 20px; text-align: center; font-size: 12px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<h1>" + appName + "</h1>"
                + "</div>";
    }

    private String getEmailFooter() {
        return "<div class='footer'>"
                + "<p>© 2024 " + appName + ". Todos os direitos reservados.</p>"
                + "<p>Este é um email automático, não responda.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    // Método para envio de notificação push (placeholder para implementação futura)
    public CompletableFuture<Boolean> sendPushNotification(String userId, String title, String message, String data) {
        return CompletableFuture.supplyAsync(() -> {
            // TODO: Implementar integração com Firebase Cloud Messaging ou similar
            logger.info("Push notification enviada para usuário {}: {} - {}", userId, title, message);
            return true;
        });
    }

    // Método para envio de SMS (placeholder para implementação futura)
    public CompletableFuture<Boolean> sendSMS(String phoneNumber, String message) {
        return CompletableFuture.supplyAsync(() -> {
            // TODO: Implementar integração com Twilio ou similar
            logger.info("SMS enviado para {}: {}", phoneNumber, message);
            return true;
        });
    }
}
