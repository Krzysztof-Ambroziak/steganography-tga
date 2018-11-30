package pl.cba.reallygrid.steganography.util;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

public class CompatibleImage {
    public static BufferedImage toCompatibleImage(BufferedImage image) {
        GraphicsConfiguration configuration = getConfiguration();
        if(image.getColorModel() == configuration.getColorModel()) {
            return image;
        }
        
        BufferedImage compatibleImage = configuration.createCompatibleImage(image.getWidth(),
                                                                            image.getHeight(),
                                                                            image.getTransparency());
        Graphics graphics = compatibleImage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return compatibleImage;
    }
    
    public static BufferedImage createCompatibleImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int transparency = image.getTransparency();
        
        return getConfiguration().createCompatibleImage(width, height, transparency);
    }
    
    private static GraphicsConfiguration getConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }
    
    private CompatibleImage() {
    }
}
