package pl.cba.reallygrid.steganography.imagetga;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

final class TGAPixelData {
    static TGAPixelData read(TGAHeader header, TGAColorMap colorMap, DataInputStream inputStream) throws TGAFormatException, IOException {
        final TGAPixelData pixelData = new TGAPixelData(header);
        ColorMapType colorMapType = header.getColorMapType();
        
        pixelData.readData(colorMapType, colorMap, inputStream);
        pixelData.setCorrectOrder(header.getImageOrigin());
        
        return pixelData;
    }
    
    static void write(BufferedImage image, DataOutputStream outputStream) throws IOException {
        WritableRaster raster = image.getRaster();
        DataBufferInt dataBuffer = (DataBufferInt)raster.getDataBuffer();
        int[] bufferData = dataBuffer.getData();
        
        if(image.getTransparency() == Transparency.OPAQUE) {
            writeOpaqueData(bufferData, outputStream);
        }
        else {
            writeTranslucentData(bufferData, outputStream);
        }
    }
    
    private static void writeOpaqueData(int[] bufferData, DataOutputStream outputStream) throws IOException {
        for(int data : bufferData) {
            int red = data & 0x000000FF;
            int green = (data >> 8) & 0x000000FF;
            int blue = (data >> 16) & 0x000000FF;
            outputStream.writeByte(red);
            outputStream.writeByte(green);
            outputStream.writeByte(blue);
        }
    }
    
    private static void writeTranslucentData(int[] bufferData, DataOutputStream outputStream) throws IOException {
        for(int data : bufferData) {
            int red = data & 0x000000FF;
            int green = (data >> 8) & 0x000000FF;
            int blue = (data >> 16) & 0x000000FF;
            int alpha = (data >> 24) & 0x000000FF;
            outputStream.writeByte(blue);
            outputStream.writeByte(green);
            outputStream.writeByte(red);
            outputStream.writeByte(alpha);
        }
    }
    
    private TGAPixelData(TGAHeader header) {
        this.width = header.getWidth();
        this.height = header.getHeight();
        this.pixelDepth = header.getPixelDepth();
        this.data = new Pixel[this.width * this.height];
    }
    
    private void readData(ColorMapType colorMapType, TGAColorMap colorMap, DataInputStream inputStream) throws TGAFormatException, IOException {
        switch(colorMapType) {
            case NO_COLOR_MAP:
                readTrueColorPixelTable(inputStream);
                break;
            case COLOR_MAP:
                readColorIndexedPixelTable(colorMap, inputStream);
                break;
            default:
                throw new TGAFormatException();
        }
    }
    
    private void readTrueColorPixelTable(DataInputStream inputStream) throws TGAFormatException, IOException {
        switch(pixelDepth) {
            case 24:
                read24BitColorPixelTable(inputStream);
                break;
            case 32:
                read32BitColorPixelTable(inputStream);
                break;
            default:
                throw new TGAFormatException("Pixel depth other then 24bit or 32bit is not supported.");
        }
    }
    
    private void read24BitColorPixelTable(DataInputStream inputStream) throws IOException {
        byte[] pixel = new byte[3]; // BGR
        
        for(int index = 0; index < width * height; index++) {
            inputStream.readFully(pixel);
            data[index] = Pixel.pixelFromBgr(pixel);
        }
    }
    
    private void read32BitColorPixelTable(DataInputStream inputStream) throws IOException {
        byte[] pixel = new byte[4]; // BGRA
        
        for(int index = 0; index < width * height; index++) {
            inputStream.readFully(pixel);
            data[index] = Pixel.pixelFromBgra(pixel);
        }
    }
    
    private void readColorIndexedPixelTable(TGAColorMap colorMap, DataInputStream inputStream) throws IOException {
        for(int index = 0; index < width * height; index++) {
            int pixelIndex = inputStream.readUnsignedByte();
            Pixel pixel = colorMap.getPixel(pixelIndex);
            data[index] = pixel;
        }
    }
    
    private void setCorrectOrder(ImageOrigin imageOrigin) {
        switch(imageOrigin) {
            case TOP_RIGHT:
                changeHorizontalPixelOrder();
                break;
            case BOTTOM_LEFT:
                changeVerticalPixelOrder();
                break;
            case BOTTOM_RIGHT:
                changeVerticalPixelOrder();
                changeHorizontalPixelOrder();
        }
    }
    
    private void changeHorizontalPixelOrder() {
        for(int heightIndex = 0; heightIndex < height; heightIndex++) {
            for(int widthIndex = 0; widthIndex < width / 2; widthIndex++) {
                int firstIndex = heightIndex * width + widthIndex;
                int secondIndex = heightIndex * width + (width - widthIndex - 1);
                swapPixels(firstIndex, secondIndex);
            }
        }
    }
    
    private void changeVerticalPixelOrder() {
        for(int heightIndex = 0; heightIndex < height / 2; heightIndex++) {
            for(int widthIndex = 0; widthIndex < width; widthIndex++) {
                int firstIndex = heightIndex * width + widthIndex;
                int secondIndex = (height - heightIndex - 1) * width + widthIndex;
                swapPixels(firstIndex, secondIndex);
            }
        }
    }
    
    private void swapPixels(int firstIndex, int secondIndex) {
        Pixel firstPixel = data[firstIndex];
        data[firstIndex] = data[secondIndex];
        data[secondIndex] = firstPixel;
    }
    
    Pixel getPixel(int i) {
        return data[i];
    }
    
    private final Pixel[] data;
    private final int width;
    private final int height;
    private final int pixelDepth;
}
