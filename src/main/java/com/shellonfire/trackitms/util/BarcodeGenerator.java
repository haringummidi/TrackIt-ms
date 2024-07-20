package com.shellonfire.trackitms.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;

public class BarcodeGenerator {

    public static void main(String[] args) {
        String barcodeText = "123456789"; // Your barcode data
        String filePath = "barcode.png"; // Output file path

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeText, BarcodeFormat.CODE_128, 200, 50);
            File file = new File(filePath);
            writeToFile(bitMatrix, "png", file);
            System.out.println("Barcode image created successfully at path: " + filePath);
        } catch (Exception e) {
            System.err.println("Error creating barcode: " + e.getMessage());
        }
    }

    private static void writeToFile(BitMatrix bitMatrix, String format, File file) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(image, format, outputStream);
        } catch (IOException e) {
            System.err.println("Error writing barcode image: " + e.getMessage());
        }
    }
}

