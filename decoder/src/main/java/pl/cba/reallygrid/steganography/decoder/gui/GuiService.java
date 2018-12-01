package pl.cba.reallygrid.steganography.decoder.gui;

import pl.cba.reallygrid.steganography.decoder.service.ActionProvider;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

public class GuiService {
    public void createFrame() {
        SwingUtilities.invokeLater(frame::init);
    }
    
    public void addActions(ActionProvider actionProvider) {
        frame.addActions(actionProvider);
        sidebar.addActions(actionProvider);
    }
    
    public JFileChooser getFileChooser() {
        return fileChooser;
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    public void showFrame() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
    
    public void addImage(BufferedImage image) {
        SwingUtilities.invokeLater(() -> {
            picturePanel.addImage(image);
            picturePanel.repaint();
        });
    }
    
    public void enabledComponentsAfterImageLoading() {
        sidebar.enabledAfterImageLoading();
    }
    
    public void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(frame, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    public void setText(String decodedText) {
        sidebar.putTextToTextArea(decodedText);
    }
    
    private Frame frame;
    private PicturePanel picturePanel;
    private Sidebar sidebar;
    private JFileChooser fileChooser;
    
    {
        frame = new Frame();
        picturePanel = new PicturePanel();
        sidebar = new Sidebar();
        fileChooser = new JFileChooser();
        
        frame.add(picturePanel, BorderLayout.CENTER);
        frame.add(sidebar, BorderLayout.EAST);
    }
}
