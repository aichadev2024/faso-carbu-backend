package com.fasocarbu.fasocarbu.modules.ticket.web;

import com.fasocarbu.fasocarbu.core.util.QRCodeGenerator;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "QR Code", description = "Génération d'images QR Code")
@RestController
@RequestMapping("/api/qr")
public class QRCodeController {

    @Operation(summary = "Générer une image PNG de QR Code")
    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String text) {
        try {
            byte[] qrImage = QRCodeGenerator.generateQRCodeImage(text, 300, 300);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
        } catch (WriterException | IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
