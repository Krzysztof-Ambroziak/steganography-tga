package pl.cba.reallygrid.steganography.imagetga;

final class Pixel {
    static Pixel pixelFromBgr(byte[] bgr) {
        int red = (int)bgr[0] & 0x000000FF;
        int green = (int)bgr[1] & 0x000000FF;
        int blue = (int)bgr[2] & 0x000000FF;
        
        return new Pixel(red, green, blue);
    }
    
    static Pixel pixelFromBgra(byte[] bgra) {
        int red = (int)bgra[0] & 0x000000FF;
        int green = (int)bgra[1] & 0x000000FF;
        int blue = (int)bgra[2] & 0x000000FF;
        int alpha = (int)bgra[3] & 0x000000FF;
        
        return new Pixel(red, green, blue, alpha);
    }
    
    static int encodeColor(Pixel pixel) {
        int red = pixel.red;
        int green = pixel.green << 8;
        int blue = pixel.blue << 16;
        int alpha = pixel.alpha << 24;
        
        return red | green | blue | alpha;
    }
    
    private Pixel(int red, int green, int blue) {
        this(red, green, blue, 0xFF);
    }
    
    private Pixel(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    
    final int red;
    final int green;
    final int blue;
    final int alpha;
}
