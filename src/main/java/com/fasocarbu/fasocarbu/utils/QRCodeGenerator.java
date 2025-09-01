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

        // ✅ Calcul du montant (toujours présent)
        BigDecimal montant = ticket.getMontant();
        if (montant == null && ticket.getCarburant() != null && ticket.getQuantite() != null) {
            montant = BigDecimal.valueOf(ticket.getCarburant().getPrix())
                    .multiply(ticket.getQuantite());
        }
        if (montant == null) {
            montant = BigDecimal.ZERO;
        }

        // ✅ Ajout des infos
        qrData.put("id", ticket.getId());
        qrData.put("montant", montant);
        qrData.put("quantite", ticket.getQuantite());
        qrData.put("carburant", ticket.getCarburant() != null ? ticket.getCarburant().getNom() : null);
        qrData.put("vehicule", ticket.getVehicule() != null ? ticket.getVehicule().getImmatriculation() : null);
        qrData.put("station", ticket.getStation() != null ? ticket.getStation().getNom() : null);
        qrData.put("dateEmission", ticket.getDateEmission() != null
                ? ticket.getDateEmission().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : null);

        // ✅ Log debug
        System.out.println("📦 QR Code généré : " + qrData);

        // ✅ Convertir en JSON pour le QR
        return gson.toJson(qrData);
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
