package pl.cba.reallygrid.steganography.decoder.gui;

import pl.cba.reallygrid.steganography.util.CompatibleImage;
import pl.cba.reallygrid.steganography.util.GBC;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import static pl.cba.reallygrid.steganography.decoder.gui.Frame.LAYOUT_PADDING;

class PicturePanel extends JPanel {
    PicturePanel() {
        super(new GridBagLayout());
    }
    
    void addImage(BufferedImage image) {
        this.image = CompatibleImage.toCompatibleImage(image);
    }
    
    private BufferedImage image;
    
    {
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(LAYOUT_PADDING, LAYOUT_PADDING, LAYOUT_PADDING, LAYOUT_PADDING),
                BorderFactory.createTitledBorder("Image")
        );
        setBorder(border);
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        
        add(new JScrollPane(panel), new GBC(0, 0)
                .fill(GBC.BOTH)
                .weight(1.0, 1.0)
                .insets(LAYOUT_PADDING, LAYOUT_PADDING, LAYOUT_PADDING, LAYOUT_PADDING));
    }
}
