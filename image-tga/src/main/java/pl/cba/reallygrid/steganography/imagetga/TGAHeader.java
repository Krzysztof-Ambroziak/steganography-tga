package pl.cba.reallygrid.steganography.imagetga;

import java.awt.Transparency;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

final class TGAHeader {
    static TGAHeader read(DataInputStream inputStream) throws TGAFormatException, IOException {
//        typedef struct _TgaHeader
//        {
//            BYTE IDLength;        /* 00h  Size of Image ID field */
//            BYTE ColorMapType;    /* 01h  Color map type */
//            BYTE ImageType;       /* 02h  Image type code */
//            WORD CMapStart;       /* 03h  Color map origin */
//            WORD CMapLength;      /* 05h  Color map length */
//            BYTE CMapDepth;       /* 07h  Depth of color map entries */
//            WORD XOffset;         /* 08h  X origin of image */
//            WORD YOffset;         /* 0Ah  Y origin of image */
//            WORD Width;           /* 0Ch  Width of image */
//            WORD Height;          /* 0Eh  Height of image */
//            BYTE PixelDepth;      /* 10h  Image pixel size */
//            BYTE ImageDescriptor; /* 11h  Image descriptor byte */
//        } TGAHEAD;
        TGAHeader header = new TGAHeader();
        
        header.readFirstThreeBytes(inputStream);
        header.readColorMapSpecification(inputStream);
        header.readImageSpecification(inputStream);
        
        if(header.idLength > 0) {
            header.readImageID(inputStream);
        }
        
        return header;
    }
    
    static TGAHeader getTrueColorHeader(String textSize, int width, int height, int transparency) {
        TGAHeader header = new TGAHeader();
        
        header.idLength = textSize.length();
        header.colorMapType = ColorMapType.NO_COLOR_MAP;
        header.imageType = ImageType.UNCOMPRESSED_TRUE_COLOR;
        
        header.colorMapStart = 0;
        header.colorMapLength = 0;
        header.colorMapSize = 0;
        
        header.width = width;
        header.height = height;
        header.pixelDepth = (transparency == Transparency.OPAQUE) ? 24 : 32;
        header.alphaChannelBits = (transparency == Transparency.OPAQUE) ? 0 : 8;
        header.imageOrigin = ImageOrigin.TOP_LEFT;
        
        header.imageId = textSize.getBytes();
        
        return header;
    }
    
    private static int alphaBits(int imageDescriptor) {
        return imageDescriptor & 0x0000000F; // first 4 bits
    }
    
    private static int descriptorEncoder(int alphaChannelBits, ImageOrigin imageOrigin) {
        int value = imageOrigin.originValue();
        value <<= 4;
        value |= alphaChannelBits;
        
        return value;
    }
    
    private TGAHeader() {
    }
    
    private void readFirstThreeBytes(DataInputStream inputStream) throws TGAFormatException, IOException {
        // Preambule: 3 bytes (1 byte + 1 byte + 1 byte)
        idLength = inputStream.readUnsignedByte();
        
        int colorMapTypeValue = inputStream.readUnsignedByte();
        colorMapType = ColorMapType.colorMapTypeFromInt(colorMapTypeValue);
        
        int imageTypeValue = inputStream.readUnsignedByte();
        imageType = ImageType.imageTypeFromInt(imageTypeValue);
    }
    
    private void readColorMapSpecification(DataInputStream inputStream) throws IOException {
        // Color map specification: 5 bytes (2 bytes + 2 bytes + 1 byte)
        colorMapStart = Converter.convertFirstTwoBytes(inputStream.readUnsignedShort());
        colorMapLength = Converter.convertFirstTwoBytes(inputStream.readUnsignedShort());
        colorMapSize = inputStream.readUnsignedByte();
    }
    
    private void readImageSpecification(DataInputStream inputStream) throws IOException {
        // Image specification: 10 bytes (2 bytes + 2 bytes + 2 bytes + 2 bytes + 1 byte + 1 byte)
        xOrigin = Converter.convertFirstTwoBytes(inputStream.readUnsignedShort());
        yOrigin = Converter.convertFirstTwoBytes(inputStream.readUnsignedShort());
        
        width = Converter.convertFirstTwoBytes(inputStream.readUnsignedShort());
        height = Converter.convertFirstTwoBytes(inputStream.readUnsignedShort());
        
        pixelDepth = inputStream.readUnsignedByte();
        
        int imageDescriptor = inputStream.readUnsignedByte();
        alphaChannelBits = alphaBits(imageDescriptor);
        imageOrigin = imageOriginBits(imageDescriptor);
    }
    
    private void readImageID(DataInputStream inputStream) throws IOException {
        imageId = new byte[idLength];
        inputStream.readFully(imageId);
    }
    
    private ImageOrigin imageOriginBits(int imageDescriptor) {
        int origin = (imageDescriptor >> 4) & 0x00000003; // 4th and 5th bit
        
        return ImageOrigin.ImageOriginFromInt(origin);
    }
    
    void saveHeader(DataOutputStream dis) throws IOException {
        writeFirstThreeBytes(dis);
        writeColorMapSpecification(dis);
        writeImageSpecification(dis);
        writeImageId(dis);
    }
    
    private void writeFirstThreeBytes(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(idLength);
        outputStream.writeByte(colorMapType.mapTypeValue());
        outputStream.writeByte(imageType.typeValue());
    }
    
    private void writeColorMapSpecification(DataOutputStream outputStream) throws IOException {
        // Color map specification: 5 bytes (2 bytes + 2 bytes + 1 byte)
        outputStream.writeShort(Converter.convertFirstTwoBytes(colorMapStart));
        outputStream.writeShort(Converter.convertFirstTwoBytes(colorMapLength));
        outputStream.writeByte(colorMapSize);
    }
    
    private void writeImageSpecification(DataOutputStream outputStream) throws IOException {
        // Image specification: 10 bytes (2 bytes + 2 bytes + 2 bytes + 2 bytes + 1 byte + 1 byte)
        outputStream.writeShort(Converter.convertFirstTwoBytes(xOrigin));
        outputStream.writeShort(Converter.convertFirstTwoBytes(yOrigin));
        outputStream.writeShort(Converter.convertFirstTwoBytes(width));
        outputStream.writeShort(Converter.convertFirstTwoBytes(height));
        outputStream.writeByte(pixelDepth);
        int descriptor = TGAHeader.descriptorEncoder(alphaChannelBits, imageOrigin);
        outputStream.writeByte(descriptor);
    }
    
    private void writeImageId(DataOutputStream outputStream) throws IOException {
        outputStream.write(imageId);
    }
    
    ColorMapType getColorMapType() {
        return colorMapType;
    }
    
    ImageType getImageType() {
        return imageType;
    }
    
    int getColorMapStart() {
        return colorMapStart;
    }
    
    int getColorMapLength() {
        return colorMapLength;
    }
    
    int getColorMapSize() {
        return colorMapSize;
    }
    
    int getWidth() {
        return width;
    }
    
    int getHeight() {
        return height;
    }
    
    int getPixelDepth() {
        return pixelDepth;
    }
    
    int getAlphaChannelBits() {
        return alphaChannelBits;
    }
    
    ImageOrigin getImageOrigin() {
        return imageOrigin;
    }
    
    String getImageId() {
        if(imageId != null) {
            return new String(imageId);
        }
        return null;
    }
    
    private int idLength;
    private ColorMapType colorMapType;
    private ImageType imageType;
    
    private int colorMapStart;
    private int colorMapLength;
    private int colorMapSize;
    
    private int xOrigin;
    private int yOrigin;
    private int width;
    private int height;
    private int pixelDepth;
    private int alphaChannelBits;
    private ImageOrigin imageOrigin;
    
    private byte[] imageId;
}
