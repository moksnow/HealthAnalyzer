package com.health.ai.reco.demo.util;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

/**
 * @author M_Khandan
 * Date: 6/1/2025
 * Time: 7:09 PM
 */
public class TextExtractorUtil {

    public static String extractText(File file) throws IOException, TesseractException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".pdf")) {
            return extractTextFromPDF(file);
        } else if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return extractTextFromImage(file);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileName);
        }
    }

    private static String extractTextFromPDF(File file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private static String extractTextFromImage(File file) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("D:\\ocr"); // Set to path of tessdata folder
        tesseract.setLanguage("eng");
        return tesseract.doOCR(file);
    }

}
