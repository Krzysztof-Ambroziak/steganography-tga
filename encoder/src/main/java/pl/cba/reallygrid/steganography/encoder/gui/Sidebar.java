package pl.cba.reallygrid.steganography.encoder.gui;

import pl.cba.reallygrid.steganography.encoder.service.ActionProvider;
import pl.cba.reallygrid.steganography.util.GBC;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;

import static pl.cba.reallygrid.steganography.encoder.gui.Frame.LAYOUT_PADDING;

class Sidebar extends JPanel {
    Sidebar() {
        super(new GridBagLayout());
    }
    
    void addActions(ActionProvider actionProvider) {
        loadImage.addActionListener(actionProvider.loadImageAction);
        createEncodedImage.addActionListener(actionProvider.saveTextToImage);
        saveImage.addActionListener(actionProvider.saveImageAction);
    }
    
    String getTextFromTextArea() {
        return inputText.getText();
    }
    
    void enabledAfterImageLoading() {
        inputText.setEnabled(true);
        createEncodedImage.setEnabled(true);
    }
    
    void enabledAfterEncoding() {
        saveImage.setEnabled(true);
    }
    
    private static final int COMPONENT_MARGIN = 8;
    
    private JButton loadImage = new JButton("Load image");
    private JTextArea inputText = new JTextArea();
    private JButton createEncodedImage = new JButton("Create encoded image");
    private JButton saveImage = new JButton("Save image");
    
    {
        add(loadImage, new GBC(0, 0)
                .fill(GBC.HORIZONTAL)
                .insets(LAYOUT_PADDING, 0, 0, LAYOUT_PADDING));
        add(new JScrollPane(inputText), new GBC(0, 1)
                .fill(GBC.BOTH)
                .weight(1.0, 1.0)
                .insets(COMPONENT_MARGIN, 0, COMPONENT_MARGIN, LAYOUT_PADDING));
        add(createEncodedImage, new GBC(0, 2)
                .insets(0, 0, COMPONENT_MARGIN, LAYOUT_PADDING));
        add(saveImage, new GBC(0, 3)
                .fill(GBC.HORIZONTAL)
                .insets(0, 0, LAYOUT_PADDING, LAYOUT_PADDING));
    }
}
