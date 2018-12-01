package pl.cba.reallygrid.steganography.decoder.model;

public class Model {
    public int getTextLength() {
        return textLength;
    }
    
    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }
    
    public int[] getPixels() {
        return pixels;
    }
    
    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }
    
    private int textLength;
    private int[] pixels;
}
