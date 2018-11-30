package pl.cba.reallygrid.steganography.encoder.gui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import java.awt.BorderLayout;

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
    
    private static final int EMPTY_BORDER_MARGIN = 6;
    
    private JPanel panel = new JPanel(null);
    
    {
        add(new JScrollPane(panel), BorderLayout.CENTER);
    }
}
