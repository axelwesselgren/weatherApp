package weatherapp.tools;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import java.awt.Image;

/**
 * {@code FileTools} is a class that holds static methods for reading, retrieving and resizing images and files.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class FileTools {
    /**
     * Reads an image from a file path and returns it as a {@code BufferedImage}.
     * 
     * @param filePath The path to the image file from the resources folder.
     * @return The image as a {@code BufferedImage}.
     * @throws IOException If the file is not found or the image can't be read.
     */
    public static BufferedImage getBufferedImage(String filePath) throws IOException {
        URL inputStream = FileTools.class.getResource(filePath);

        return ImageIO.read(inputStream);
    }

    /**
     * Reads an image from a file path and returns it as an {@code Image}.
     * 
     * @param filePath The path to the image file from the resources folder.
     * @return The image as an {@code Image}.
     * @throws IOException If the file is not found or the image can't be read.
     */
    public static Image getImage(String filePath) throws IOException {
        return ImageIO.read(FileTools.class.getResourceAsStream(filePath));
    }

    /**
     * Retrieves a file from the resources folder and returns it as a {@code URL}.
     * 
     * @param filePath The path to the file from the resources folder.
     * @return The file as a {@code URL}.
     */
    public static URL getURL(String filePath) {
        return FileTools.class.getResource(filePath);
    }

    /**
     * Resizes a {@code BufferedImage} to a specified downscale factor.
     * 
     * @param original The original BufferedImage to resize.
     * @param downScale The factor to downscale the image by.
     * @return The resized BufferedImage.
     */
    public static BufferedImage getResizedImage(BufferedImage original, int downScale) {
        int width = original.getWidth();
        int height = original.getHeight();

        int widthDS = width / downScale;
        int heightDS = height / downScale;
        
        BufferedImage resizedImage = new BufferedImage(widthDS, heightDS, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        AffineTransform at = AffineTransform.getScaleInstance((double) widthDS / width, (double) heightDS / height);
        g2d.drawRenderedImage(original, at);
        g2d.dispose();

        return resizedImage;
    }
}
