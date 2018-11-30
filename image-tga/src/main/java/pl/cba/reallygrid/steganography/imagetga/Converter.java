package pl.cba.reallygrid.steganography.imagetga;

final class Converter {
    static int convertFirstTwoBytes(int number) {
        int first = number & 0x000000FF;
        int second = number & 0x0000FF00;
        
        first <<= 8;
        second >>= 8;
        
        return first | second;
    }
    
    private Converter() {
    }
}
