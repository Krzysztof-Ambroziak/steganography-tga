package pl.cba.reallygrid.steganography.encoder.model;

public class Model {
    public int[] getOriginalPixels() {
        return originalPixels;
    }
    
    public void setOriginalPixels(int[] originalPixels) {
        this.originalPixels = originalPixels;
    }
    
    public int[] getEncodedPixels() {
        return encodedPixels;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public void copyDataPixel() {
        encodedPixels = originalPixels.clone();
    }
    
    private int[] originalPixels;
    private int[] encodedPixels;
    private String text;
}
