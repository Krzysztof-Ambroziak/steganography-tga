package pl.cba.reallygrid.steganography.encoder.gui;

import pl.cba.reallygrid.steganography.encoder.service.ActionProvider;
import pl.cba.reallygrid.steganography.util.CompatibleImage;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

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
    
    public void addOriginalImage(BufferedImage image) {
        SwingUtilities.invokeLater(() -> {
            picturePanel.addOriginalImage(image);
            picturePanel.repaint();
        });
    }
    
    public void addEncodedImage(int[] encodedPixels) {
        BufferedImage originalImage = picturePanel.getOriginalImage();
        BufferedImage encodedImage = CompatibleImage.createCompatibleImage(originalImage);
        int[] encodedData = getPixelData(encodedImage);
        System.arraycopy(encodedPixels, 0, encodedData, 0, encodedData.length);
        
        SwingUtilities.invokeLater(() -> {
            picturePanel.addEncodedImage(encodedImage);
            picturePanel.repaint();
        });
    }
    
    private int[] getPixelData(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferInt dataBuffer = (DataBufferInt)raster.getDataBuffer();
        return dataBuffer.getData();
    }
    
    public String getText() {
        return sidebar.getTextFromTextArea();
    }
    
    public void enabledComponentsAfterImageLoading() {
        sidebar.enabledAfterImageLoading();
    }
    
    public void enabledComponentsAfterEncoding() {
        sidebar.enabledAfterEncoding();
    }
    
    public void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(frame, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    public void showFrame() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
    
    private Frame frame = new Frame();
    private PicturePanel picturePanel = new PicturePanel();
    private Sidebar sidebar = new Sidebar();
    private JFileChooser fileChooser = new JFileChooser();
    
    {
        
        frame.add(picturePanel, BorderLayout.CENTER);
        frame.add(sidebar, BorderLayout.EAST);
    }
}
