package pl.cba.reallygrid.steganography.imagetga;

import java.io.DataInputStream;
import java.io.IOException;

final class TGAColorMap {
    private TGAColorMap(int mapLength) {
        pixelMap = new Pixel[mapLength];
    }
    
    
    static TGAColorMap read(TGAHeader header, DataInputStream inputStream) throws TGAFormatException, IOException {
        TGAColorMap colorMap = null;
        if(header.getColorMapStart() != 0) {
            throw new TGAFormatException("First Entry Index in Color Map Area should be 0. Other values are not supported");
        }
        
        if(header.getColorMapType() == ColorMapType.COLOR_MAP) {
            colorMap = new TGAColorMap(header.getColorMapLength());
            colorMap.readColorMapArea(header.getColorMapSize(), inputStream);
        }
        
        return colorMap;
    }
    
    private void readColorMapArea(int entrySize, DataInputStream inputStream) throws IOException {
        switch(entrySize) {
            case 24:
                readColor24BitMapArea(inputStream);
                break;
            case 32:
                readColor32BitMapArea(inputStream);
                break;
        }
    }
    
    private void readColor24BitMapArea(DataInputStream inputStream) throws IOException {
        int length = pixelMap.length;
        byte[] pixel = new byte[3]; // BGR
        
        for(int entry = 0; entry < length; entry++) {
            inputStream.readFully(pixel);
            pixelMap[entry] = Pixel.pixelFromBgr(pixel);
        }
    }
    
    private void readColor32BitMapArea(DataInputStream inputStream) throws IOException {
        int length = pixelMap.length;
        byte[] pixel = new byte[4]; // BGRA
        
        for(int entry = 0; entry < length; entry++) {
            inputStream.readFully(pixel);
            pixelMap[entry] = Pixel.pixelFromBgra(pixel);
        }
    }
    
    Pixel getPixel(int index) {
        return pixelMap[index];
    }
    
    private final Pixel[] pixelMap;
}
