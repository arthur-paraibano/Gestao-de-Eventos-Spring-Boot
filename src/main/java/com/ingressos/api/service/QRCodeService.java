package com.ingressos.api.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
public class QRCodeService {

    private static final int QR_CODE_WIDTH = 300;
    private static final int QR_CODE_HEIGHT = 300;

    public String generateQRCodeData(Integer ticketId, Integer eventId, String userEmail) {
        // Gera um código único para o ingresso
        String uniqueCode = UUID.randomUUID().toString();
        
        // Cria os dados do QR Code com informações do ingresso
        return String.format(
            "TICKET_ID:%d|EVENT_ID:%d|USER:%s|CODE:%s|TIMESTAMP:%d",
            ticketId,
            eventId,
            userEmail,
            uniqueCode,
            System.currentTimeMillis()
        );
    }

    public String generateQRCodeImage(String qrCodeData) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        byte[] qrCodeBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(qrCodeBytes);
    }

    public String generateTicketQRCode(Integer ticketId, Integer eventId, String userEmail) {
        try {
            String qrCodeData = generateQRCodeData(ticketId, eventId, userEmail);
            return generateQRCodeImage(qrCodeData);
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Erro ao gerar QR Code: " + e.getMessage(), e);
        }
    }

    public boolean validateQRCode(String qrCodeData) {
        try {
            // Valida o formato dos dados do QR Code
            String[] parts = qrCodeData.split("\\|");
            if (parts.length != 5) {
                return false;
            }

            // Verifica se contém todas as partes necessárias
            boolean hasTicketId = parts[0].startsWith("TICKET_ID:");
            boolean hasEventId = parts[1].startsWith("EVENT_ID:");
            boolean hasUser = parts[2].startsWith("USER:");
            boolean hasCode = parts[3].startsWith("CODE:");
            boolean hasTimestamp = parts[4].startsWith("TIMESTAMP:");

            return hasTicketId && hasEventId && hasUser && hasCode && hasTimestamp;
        } catch (Exception e) {
            return false;
        }
    }

    public Integer extractTicketId(String qrCodeData) {
        try {
            String[] parts = qrCodeData.split("\\|");
            String ticketIdPart = parts[0].replace("TICKET_ID:", "");
            return Integer.parseInt(ticketIdPart);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair ID do ingresso do QR Code", e);
        }
    }

    public Integer extractEventId(String qrCodeData) {
        try {
            String[] parts = qrCodeData.split("\\|");
            String eventIdPart = parts[1].replace("EVENT_ID:", "");
            return Integer.parseInt(eventIdPart);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair ID do evento do QR Code", e);
        }
    }
}