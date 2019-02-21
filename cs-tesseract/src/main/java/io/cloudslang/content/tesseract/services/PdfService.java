package io.cloudslang.content.tesseract.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfService {

    public List<File> convertPdfToImage(File file, String destination, Integer dpi) throws Exception {

        File destinationFile = new File(destination);

        if (!destinationFile.exists()) {
            destinationFile.mkdir();
        }
        if (file.exists()) {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            List<File> fileList = new ArrayList<File>();

            String fileName = file.getName().replace(".pdf", "");

            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                // default image files path: original file path
                // if necessary, file.getParent() + "/" => another path
                File fileTemp = new File(destination + fileName + "_" + RandomStringUtils.randomAlphanumeric(15).toUpperCase() + ".png"); // jpg or png
                BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                // 200 is sample dots per inch.
                // if necessary, change 200 into another integer.
                ImageIO.write(image, "PNG", fileTemp); // JPEG or PNG
                fileList.add(fileTemp);
            }
            doc.close();
            return fileList;
        } else {
            System.err.println(file.getName() + " FILE DOES NOT EXIST");
        }
        return null;
    }
}

