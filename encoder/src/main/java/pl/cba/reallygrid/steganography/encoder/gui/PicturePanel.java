package pl.cba.reallygrid.steganography.encoder.gui;

import pl.cba.reallygrid.steganography.util.CompatibleImage;
import pl.cba.reallygrid.steganography.util.GBC;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

class PicturePanel extends JPanel {
    PicturePanel() {
        super(new GridBagLayout());
    }
    
    void addOriginalImage(BufferedImage image) {
        BufferedImage compatibleImage = CompatibleImage.toCompatibleImage(image);
        originalImagePanel.addImage(compatibleImage);
    }
    
    void addEncodedImage(BufferedImage image) {
        encodedImagePanel.addImage(image);
    }
    
    BufferedImage getOriginalImage() {
        return originalImagePanel.getImage();
    }
    
    private ImagePanel originalImagePanel = new ImagePanel("Original image");
    private ImagePanel encodedImagePanel = new ImagePanel("Encoded image");
    
    {
        add(originalImagePanel, new GBC(0, 0)
                .fill(GBC.BOTH)
                .weight(1.0, 1.0)
                .insets(Frame.LAYOUT_PADDING, Frame.LAYOUT_PADDING, Frame.LAYOUT_PADDING, Frame.LAYOUT_PADDING));
        add(encodedImagePanel, new GBC(1, 0)
                .fill(GBC.BOTH)
                .weight(1.0, 1.0)
                .insets(Frame.LAYOUT_PADDING, 0, Frame.LAYOUT_PADDING, Frame.LAYOUT_PADDING));
    }
}
