package com.health.ai.reco.demo.util;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TextExtractorUtilTest {

    private File pdfFile;
    private File imageFile;
    private File unsupportedFile;

    @BeforeEach
    void setUp() {
        pdfFile = new File("sample.pdf");
        imageFile = new File("photo.png");
        unsupportedFile = new File("notes.txt");
    }

    @Test
    void extractText_fromPdf_success() throws Exception {
        PDDocument mockDoc = mock(PDDocument.class);

        // Mock static Loader.loadPDF()
        try (MockedStatic<Loader> loaderMock = mockStatic(Loader.class);
             MockedConstruction<PDFTextStripper> mockStripperCons =
                     mockConstruction(PDFTextStripper.class, (stripper, ctx) ->
                             when(stripper.getText(mockDoc)).thenReturn("PDF extracted text"))) {

            loaderMock.when(() -> Loader.loadPDF(pdfFile)).thenReturn(mockDoc);

            String result = TextExtractorUtil.extractText(pdfFile);

            assertEquals("PDF extracted text", result);
            loaderMock.verify(() -> Loader.loadPDF(pdfFile));
        }
    }

    @Test
    void extractText_fromImage_success() throws Exception {
        try (MockedConstruction<Tesseract> mockTessCons =
                     mockConstruction(Tesseract.class, (tess, ctx) -> {
                         when(tess.doOCR(imageFile)).thenReturn("Image extracted text");
                     })) {

            String result = TextExtractorUtil.extractText(imageFile);

            assertEquals("Image extracted text", result);

            Tesseract created = mockTessCons.constructed().get(0);
            verify(created).setDatapath("D:\\ocr");
            verify(created).setLanguage("eng");
            verify(created).doOCR(imageFile);
        }
    }

    @Test
    void extractText_unsupportedFile_throwsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> TextExtractorUtil.extractText(unsupportedFile)
        );
        assertTrue(ex.getMessage().contains("Unsupported file type"));
    }

    @Test
    void extractText_fromPdf_throwsIOException() throws Exception {
        try (MockedStatic<Loader> loaderMock = mockStatic(Loader.class)) {
            loaderMock.when(() -> Loader.loadPDF(pdfFile))
                    .thenThrow(new IOException("PDF load failed"));

            IOException ex = assertThrows(IOException.class,
                    () -> TextExtractorUtil.extractText(pdfFile));

            assertEquals("PDF load failed", ex.getMessage());
        }
    }

    @Test
    void extractText_fromImage_throwsTesseractException() throws Exception {
        try (MockedConstruction<Tesseract> mockTessCons =
                     mockConstruction(Tesseract.class, (tess, ctx) -> {
                         when(tess.doOCR(imageFile)).thenThrow(new TesseractException("OCR failed"));
                     })) {

            TesseractException ex = assertThrows(TesseractException.class,
                    () -> TextExtractorUtil.extractText(imageFile));

            assertEquals("OCR failed", ex.getMessage());
        }
    }
}
