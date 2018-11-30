package pl.cba.reallygrid.steganography.imagetga;

public class TGAFormatException extends RuntimeException {
    public TGAFormatException() {
    }
    
    public TGAFormatException(String message) {
        super(message);
    }
    
    public TGAFormatException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TGAFormatException(Throwable cause) {
        super(cause);
    }
    
    public TGAFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
