package pl.cba.reallygrid.steganography.encoder.util;

import pl.cba.reallygrid.steganography.encoder.EncoderApplication;
import pl.cba.reallygrid.steganography.encoder.gui.Frame;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Preferences {
    public static boolean getBoolean(PreferencesKeys key) {
        return PREFERENCES.getBoolean(key.name(), (boolean)key.getDefaultValue());
    }
    
    public static void putBoolean(PreferencesKeys key, boolean value) {
        PREFERENCES.putBoolean(key.name(), value);
    }
    
    public static int getInteger(PreferencesKeys key) {
        return PREFERENCES.getInt(key.name(), (int)key.getDefaultValue());
    }
    
    public static void putInteger(PreferencesKeys key, int value) {
        PREFERENCES.putInt(key.name(), value);
    }
    
    public static String getString(PreferencesKeys key) {
        return PREFERENCES.get(key.name(), (String)key.getDefaultValue());
    }
    
    public static void putString(PreferencesKeys key, String value) {
        PREFERENCES.put(key.name(), value);
    }
    
    private Preferences() {
    }
    
    private static final java.util.prefs.Preferences PREFERENCES;
    static final int POSITION_X_CENTERED_ON_SCREEN;
    static final int POSITION_Y_CENTERED_ON_SCREEN;
    static final int WIDTH_FRAME;
    static final int HEIGHT_FRAME;
    static final String DEFAULT_PATH;
    
    static {
        PREFERENCES = java.util.prefs.Preferences.userNodeForPackage(EncoderApplication.class);
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        
        WIDTH_FRAME = Frame.MINIMUM_WIDTH;
        HEIGHT_FRAME = Frame.MINIMUM_HEIGHT;
        POSITION_X_CENTERED_ON_SCREEN = (screenSize.width - WIDTH_FRAME) / 2;
        POSITION_Y_CENTERED_ON_SCREEN = (screenSize.height - HEIGHT_FRAME) / 2;
        DEFAULT_PATH = "";
    }
}
