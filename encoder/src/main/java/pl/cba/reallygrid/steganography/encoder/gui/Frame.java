package pl.cba.reallygrid.steganography.encoder.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.cba.reallygrid.steganography.encoder.service.ActionProvider;
import pl.cba.reallygrid.steganography.encoder.util.Preferences;
import pl.cba.reallygrid.steganography.encoder.util.PreferencesKeys;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.HeadlessException;

public class Frame extends JFrame {
    Frame() throws HeadlessException {
        super(TITLE);
    }
    
    void init() {
        setDefaultState();
        setCustomPreferences();
    }
    
    private void setDefaultState() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
    }
    
    private void setCustomPreferences() {
        PreferencesKeys fullFrameKey = PreferencesKeys.FULL_FRAME;
        boolean fullFrame = Preferences.getBoolean(fullFrameKey);
        
        if(fullFrame) {
            setExtendedState(MAXIMIZED_BOTH);
            LOGGER.info("Frame state has been set to maximized.");
        }
        else {
            setCustomSize();
            LOGGER.info("Frame preferences have been set.");
        }
    }
    
    private void setCustomSize() {
        PreferencesKeys keyX = PreferencesKeys.POSITION_X;
        PreferencesKeys keyY = PreferencesKeys.POSITION_Y;
        PreferencesKeys keyWidth = PreferencesKeys.WIDTH_FRAME;
        PreferencesKeys keyHeight = PreferencesKeys.HEIGHT_FRAME;
        LOGGER.info("Frame preferences has been loaded.");
        
        setBounds(Preferences.getInteger(keyX),
                  Preferences.getInteger(keyY),
                  Preferences.getInteger(keyWidth),
                  Preferences.getInteger(keyHeight));
    }
    
    void addActions(ActionProvider actionProvider) {
        addComponentListener(actionProvider.frameListener);
        addWindowStateListener(actionProvider.maximizedFrameListener);
    }
    
    public static final int MINIMUM_WIDTH;
    public static final int MINIMUM_HEIGHT;
    static final int LAYOUT_PADDING = 8;
    
    private static final String TITLE = "Steganography encoder";
    private static final Logger LOGGER = LoggerFactory.getLogger(Frame.class);
    
    static {
        MINIMUM_HEIGHT = 400;
        MINIMUM_WIDTH = MINIMUM_HEIGHT * 16 / 9;
    }
}
