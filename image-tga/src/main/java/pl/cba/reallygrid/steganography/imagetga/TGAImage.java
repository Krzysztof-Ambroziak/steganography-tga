package pl.cba.reallygrid.steganography.imagetga;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TGAImage {
    public static BufferedImage read(File file, StringBuilder builder) throws TGAFormatException, IOException {
        try(DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            TGAImage image = new TGAImage(dis);
            String imageId = image.header.getImageId();
            builder.append(imageId);
            
            return image.buildImage();
        }
    }
    
    public static void write(String text, BufferedImage image, File file) throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        int transparency = image.getTransparency();
        
        TGAHeader header = TGAHeader.getTrueColorHeader(text, width, height, transparency);
        
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            header.saveHeader(dos);
            TGAPixelData.write(image, dos);
            TGAFooter.write(dos);
        }
    }
    
    private TGAImage(DataInputStream inputStream) throws TGAFormatException, IOException {
        this.inputStream = inputStream;
        
        readHeader();
        assertNoRleCompression();
        readColorMap();
        // footer is omitted
        readPixelData();
    }
    
    private void readHeader() throws TGAFormatException, IOException {
        header = TGAHeader.read(inputStream);
    }
    
    private void assertNoRleCompression() throws TGAFormatException {
        ImageType imageType = header.getImageType();
        
        if(imageType == ImageType.RUN_LENGTH_COLOR_MAPPED
                || imageType == ImageType.RUN_LENGTH_TRUE_COLOR
                || imageType == ImageType.RUN_LENGTH_BLACK_AND_WHITE) {
            throw new TGAFormatException("Run Length compression is not supported.");
        }
    }
    
    private void readColorMap() throws TGAFormatException, IOException {
        colorMap = TGAColorMap.read(header, inputStream);
    }
    
    private void readPixelData() throws TGAFormatException, IOException {
        pixelData = TGAPixelData.read(header, colorMap, inputStream);
    }
    
    private BufferedImage buildImage() {
        int width = header.getWidth();
        int height = header.getHeight();
        int imageType = header.getAlphaChannelBits() == 0 ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        
        BufferedImage image = new BufferedImage(width, height, imageType);
        getRasterFromImage(image);
        
        return image;
    }
    
    private void getRasterFromImage(BufferedImage image) {
        Raster raster = image.getRaster();
        DataBufferInt dataBuffer = (DataBufferInt)raster.getDataBuffer();
        int[] data = dataBuffer.getData();
        for(int i = 0; i < data.length; i++) {
            copyColorIntoImage(data, i);
        }
    }
    
    private void copyColorIntoImage(int[] data, int i) {
        Pixel pixel = pixelData.getPixel(i);
        int color = Pixel.encodeColor(pixel);
        data[i] = color;
    }
    
    private TGAHeader header;
    private TGAColorMap colorMap;
    private TGAPixelData pixelData;
    private DataInputStream inputStream;
}
