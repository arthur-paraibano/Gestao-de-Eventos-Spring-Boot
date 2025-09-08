package com.ingressos.api.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ingressos.api.model.EventModel;
import com.ingressos.api.model.OrderModel;
import com.ingressos.api.model.PaymentModel;
import com.ingressos.api.model.TicketModel;
import com.ingressos.api.model.UserModel;
import com.ingressos.api.repository.EventRepository;
import com.ingressos.api.repository.OrderRepository;
import com.ingressos.api.repository.PaymentRepository;
import com.ingressos.api.repository.TicketRepository;
import com.ingressos.api.repository.UserRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // Relatório de vendas por evento
    public byte[] generateEventSalesReport(Integer eventId, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        Optional<EventModel> eventOpt = eventRepository.findById(eventId);
        if (!eventOpt.isPresent()) {
            throw new IllegalArgumentException("Evento não encontrado");
        }

        EventModel event = eventOpt.get();
        List<OrderModel> orders = orderRepository.findByEventAndCreatedAtBetween(event, startDate, endDate);

        return generateSalesReportPDF(event, orders, startDate, endDate);
    }

    // Relatório geral de vendas
    public byte[] generateGeneralSalesReport(LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        List<OrderModel> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);
        return generateGeneralSalesReportPDF(orders, startDate, endDate);
    }

    // Relatório de eventos por organizador
    public byte[] generateOrganizerEventsReport(Integer organizerId, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        Optional<UserModel> organizerOpt = userRepository.findById(organizerId);
        if (!organizerOpt.isPresent()) {
            throw new IllegalArgumentException("Organizador não encontrado");
        }

        UserModel organizer = organizerOpt.get();
        List<EventModel> events = eventRepository.findByUserAndCreatedAtBetween(organizer, startDate, endDate);
        return generateOrganizerReportPDF(organizer, events, startDate, endDate);
    }

    // Relatório de usuários cadastrados
    public byte[] generateUsersReport(LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        List<UserModel> users = userRepository.findByCreatedAtBetween(startDate, endDate);
        return generateUsersReportPDF(users, startDate, endDate);
    }

    // Relatório financeiro
    public byte[] generateFinancialReport(LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        List<PaymentModel> payments = paymentRepository.findByCreatedAtBetween(startDate, endDate);
        return generateFinancialReportPDF(payments, startDate, endDate);
    }

    // Relatório de ingressos por evento
    public byte[] generateTicketsReport(Integer eventId) throws IOException {
        Optional<EventModel> eventOpt = eventRepository.findById(eventId);
        if (!eventOpt.isPresent()) {
            throw new IllegalArgumentException("Evento não encontrado");
        }

        EventModel event = eventOpt.get();
        List<TicketModel> tickets = ticketRepository.findByEvent(event);
        return generateTicketsReportPDF(event, tickets);
    }

    // Métodos privados para geração de PDFs
    private byte[] generateSalesReportPDF(EventModel event, List<OrderModel> orders, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Título
            document.add(new Paragraph("Relatório de Vendas - " + event.getName())
                    .setFont(titleFont)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            // Período
            document.add(new Paragraph("Período: " + formatDateTime(startDate) + " a " + formatDateTime(endDate))
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

            // Informações do evento
            document.add(new Paragraph("Evento: " + event.getName())
                    .setFont(normalFont)
                    .setFontSize(12));
            document.add(new Paragraph("Local: " + event.getAddress().getStreet() + ", " + event.getAddress().getCity())
                    .setFont(normalFont)
                    .setFontSize(12));
            document.add(new Paragraph("Data: " + formatDateTime(event.getStartDate()))
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

            // Estatísticas
            BigDecimal totalRevenue = orders.stream()
                    .map(OrderModel::getTotalAmount)
                    .map(amount -> amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            document.add(new Paragraph("Total de Pedidos: " + orders.size())
                    .setFont(normalFont)
                    .setFontSize(12));
            document.add(new Paragraph("Receita Total: R$ " + totalRevenue)
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

            // Tabela de pedidos
            if (!orders.isEmpty()) {
                Table table = new Table(UnitValue.createPercentArray(new float[]{2, 3, 2, 2}));
                table.setWidth(UnitValue.createPercentValue(100));

                // Cabeçalho
                table.addHeaderCell(new Cell().add(new Paragraph("ID").setFont(titleFont)));
                table.addHeaderCell(new Cell().add(new Paragraph("Cliente").setFont(titleFont)));
                table.addHeaderCell(new Cell().add(new Paragraph("Data").setFont(titleFont)));
                table.addHeaderCell(new Cell().add(new Paragraph("Valor").setFont(titleFont)));

                // Dados
                for (OrderModel order : orders) {
                    table.addCell(new Cell().add(new Paragraph(order.getId().toString()).setFont(normalFont)));
                    table.addCell(new Cell().add(new Paragraph(order.getUser().getName()).setFont(normalFont)));
                    table.addCell(new Cell().add(new Paragraph(formatDateTime(order.getCreatedAt())).setFont(normalFont)));
                    table.addCell(new Cell().add(new Paragraph("R$ " + order.getTotalAmount()).setFont(normalFont)));
                }

                document.add(table);
            }

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private byte[] generateGeneralSalesReportPDF(List<OrderModel> orders, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            document.add(new Paragraph("Relatório Geral de Vendas")
                    .setFont(titleFont)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            document.add(new Paragraph("Período: " + formatDateTime(startDate) + " a " + formatDateTime(endDate))
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

            BigDecimal totalRevenue = orders.stream()
                    .map(OrderModel::getTotalAmount)
                    .map(amount -> amount != null ? BigDecimal.valueOf(amount) : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            document.add(new Paragraph("Total de Pedidos: " + orders.size())
                    .setFont(normalFont)
                    .setFontSize(12));
            document.add(new Paragraph("Receita Total: R$ " + totalRevenue)
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private byte[] generateOrganizerReportPDF(UserModel organizer, List<EventModel> events, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            document.add(new Paragraph("Relatório de Eventos - " + organizer.getName())
                    .setFont(titleFont)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            document.add(new Paragraph("Total de Eventos: " + events.size())
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private byte[] generateUsersReportPDF(List<UserModel> users, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            document.add(new Paragraph("Relatório de Usuários")
                    .setFont(titleFont)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            document.add(new Paragraph("Total de Usuários: " + users.size())
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private byte[] generateFinancialReportPDF(List<PaymentModel> payments, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            document.add(new Paragraph("Relatório Financeiro")
                    .setFont(titleFont)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            BigDecimal totalAmount = payments.stream()
                    .map(PaymentModel::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            document.add(new Paragraph("Total de Pagamentos: " + payments.size())
                    .setFont(normalFont)
                    .setFontSize(12));
            document.add(new Paragraph("Valor Total: R$ " + totalAmount)
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private byte[] generateTicketsReportPDF(EventModel event, List<TicketModel> tickets) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            document.add(new Paragraph("Relatório de Ingressos - " + event.getName())
                    .setFont(titleFont)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            document.add(new Paragraph("Total de Ingressos: " + tickets.size())
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
