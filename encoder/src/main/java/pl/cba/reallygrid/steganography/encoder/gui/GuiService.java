package pl.cba.reallygrid.steganography.encoder.gui;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class GuiService {
    public void createFrame() {
        SwingUtilities.invokeLater(frame::init);
    }
    
    public void showFrame() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
    
    private Frame frame = new Frame();
    private PicturePanel picturePanel = new PicturePanel();
    private Sidebar sidebar = new Sidebar();
    
    {
        
        frame.add(picturePanel, BorderLayout.CENTER);
        frame.add(sidebar, BorderLayout.EAST);
    }
}
