package com.fasocarbu.fasocarbu.utils;

import com.fasocarbu.fasocarbu.models.Ticket;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class QRCodeGenerator {

    private final Gson gson = new Gson();

    public String generateQRCodeForTicket(Ticket ticket) {
        Map<String, Object> qrData = new HashMap<>();

        BigDecimal montant = ticket.getMontant();
        if (montant == null && ticket.getCarburant() != null && ticket.getQuantite() != null) {
            montant = BigDecimal.valueOf(ticket.getCarburant().getPrix())
                    .multiply(ticket.getQuantite());
        }

        // 🟢 DEBUG : afficher toutes les valeurs
        System.out.println("=== Génération QRCode ===");
        System.out.println("ID Ticket       : " + ticket.getId());
        System.out.println(
                "Carburant       : " + (ticket.getCarburant() != null ? ticket.getCarburant().getNom() : "null"));
        System.out.println(
                "Prix Carburant  : " + (ticket.getCarburant() != null ? ticket.getCarburant().getPrix() : "null"));
        System.out.println("Quantité        : " + ticket.getQuantite());
        System.out.println("Montant calculé : " + montant);
        System.out.println("Véhicule        : "
                + (ticket.getVehicule() != null ? ticket.getVehicule().getImmatriculation() : "null"));
        System.out
                .println("Station         : " + (ticket.getStation() != null ? ticket.getStation().getNom() : "null"));
        System.out.println("DateEmission    : " + (ticket.getDateEmission() != null
                ? ticket.getDateEmission().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : "null"));
        System.out.println("=========================");

        qrData.put("id", ticket.getId());
        qrData.put("montant", montant);
        qrData.put("quantite", ticket.getQuantite());
        qrData.put("carburant", ticket.getCarburant() != null ? ticket.getCarburant().getNom() : null);
        qrData.put("vehicule", ticket.getVehicule() != null ? ticket.getVehicule().getImmatriculation() : null);
        qrData.put("station", ticket.getStation() != null ? ticket.getStation().getNom() : null);
        qrData.put("dateEmission", ticket.getDateEmission() != null
                ? ticket.getDateEmission().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : null);

        // ✅ Convertir en JSON et l’utiliser comme contenu du QR
        String jsonQr = gson.toJson(qrData);
        System.out.println("JSON QRCode     : " + jsonQr); // 🟢 DEBUG final
        return jsonQr;
    }

    public static byte[] generateQRCodeImage(String text, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
