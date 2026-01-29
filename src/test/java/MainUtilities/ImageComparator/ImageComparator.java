package MainUtilities.ImageComparator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageComparator {

    public static boolean compareImages(File fileA, File fileB, double threshold) throws IOException {
        BufferedImage imgA = ImageIO.read(fileA);
        BufferedImage imgB = ImageIO.read(fileB);

        if (imgA == null || imgB == null) {
            throw new IllegalArgumentException("One of the images is null or cannot be read.");
        }

        int widthA = imgA.getWidth();
        int heightA = imgA.getHeight();
        int widthB = imgB.getWidth();
        int heightB = imgB.getHeight();

        if (widthA != widthB || heightA != heightB) {
            System.out.println("Image dimensions are different.");
            return false;
        }

        long diffPixels = 0;
        long totalPixels = (long) widthA * heightA;

        for (int y = 0; y < heightA; y++) {
            for (int x = 0; x < widthA; x++) {
                int rgbA = imgA.getRGB(x, y);
                int rgbB = imgB.getRGB(x, y);

                if (rgbA != rgbB) {
                    diffPixels++;
                }
            }
        }

        double diffPercentage = (diffPixels * 100.0) / totalPixels;
        System.out.println("Diff pixels: " + diffPixels + " (" + diffPercentage + "%)");

        return diffPercentage <= threshold;
    }
}
