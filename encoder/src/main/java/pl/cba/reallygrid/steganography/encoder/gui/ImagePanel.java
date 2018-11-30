package pl.cba.reallygrid.steganography.encoder.gui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

class ImagePanel extends JPanel {
    ImagePanel(String title) {
        super(new BorderLayout());
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(
                        EMPTY_BORDER_MARGIN,
                        EMPTY_BORDER_MARGIN,
                        EMPTY_BORDER_MARGIN,
                        EMPTY_BORDER_MARGIN));
        setBorder(border);
    }
    
    void addImage(BufferedImage compatibleImage) {
        image = compatibleImage;
    }
    
    BufferedImage getImage() {
        return image;
    }
    
    private static final int EMPTY_BORDER_MARGIN = 6;
    private BufferedImage image;
    
    private JPanel panel = new JPanel(null) {
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(image, 0, 0, null);
        }
    };
    
    {
        add(new JScrollPane(panel), BorderLayout.CENTER);
    }
}
