package pl.cba.reallygrid.steganography.decoder.gui;

import pl.cba.reallygrid.steganography.decoder.service.ActionProvider;
import pl.cba.reallygrid.steganography.util.GBC;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;

class Sidebar extends JPanel {
    Sidebar() {
        super(new GridBagLayout());
    }
    
    void addActions(ActionProvider actionProvider) {
        loadImage.addActionListener(actionProvider.loadImageAction);
    }
    
    void enabledAfterImageLoading() {
        encodedText.setEnabled(true);
    }
    
    void putTextToTextArea(String text) {
        encodedText.setText(text);
    }
    
    private static final int COMPONENT_MARGIN = 8;
    
    private JButton loadImage = new JButton("Load image");
    private JTextArea encodedText = new JTextArea();
    
    {
        add(loadImage, new GBC(0, 0)
                .ipad(100, 0)
                .insets(Frame.LAYOUT_PADDING, 0, 0, Frame.LAYOUT_PADDING));
        add(new JScrollPane(encodedText), new GBC(0, 1)
                .fill(GBC.BOTH)
                .weight(1.0, 1.0)
                .insets(COMPONENT_MARGIN, 0, COMPONENT_MARGIN, Frame.LAYOUT_PADDING));
        
        encodedText.setEnabled(false);
    }
}
