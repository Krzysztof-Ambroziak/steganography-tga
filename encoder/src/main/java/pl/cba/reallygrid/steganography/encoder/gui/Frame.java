package pl.cba.reallygrid.steganography.encoder.gui;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.HeadlessException;

class Frame extends JFrame {
    Frame() throws HeadlessException {
        super(TITLE);
    }
    
    void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
    }
    
    static final int LAYOUT_PADDING = 8;
    private static final int MINIMUM_WIDTH;
    private static final int MINIMUM_HEIGHT;
    
    private static final String TITLE = "Steganography encoder";
    
    static {
        MINIMUM_HEIGHT = 400;
        MINIMUM_WIDTH = MINIMUM_HEIGHT * 16 / 9;
    }
}
