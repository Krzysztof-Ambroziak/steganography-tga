package pl.cba.reallygrid.steganography.encoder.util;

public enum PreferencesKeys {
    POSITION_X(Preferences.POSITION_X_CENTERED_ON_SCREEN),
    POSITION_Y(Preferences.POSITION_Y_CENTERED_ON_SCREEN),
    WIDTH_FRAME(Preferences.WIDTH_FRAME),
    HEIGHT_FRAME(Preferences.HEIGHT_FRAME),
    FULL_FRAME(false),
    LAST_OPEN_PATH(Preferences.DEFAULT_PATH),
    LAST_SAVE_PATH(Preferences.DEFAULT_PATH);
    
    PreferencesKeys(Object DEFAULT_VALUE) {
        this.DEFAULT_VALUE = DEFAULT_VALUE;
    }
    
    public Object getDefaultValue() {
        return DEFAULT_VALUE;
    }
    
    private final Object DEFAULT_VALUE;
}
