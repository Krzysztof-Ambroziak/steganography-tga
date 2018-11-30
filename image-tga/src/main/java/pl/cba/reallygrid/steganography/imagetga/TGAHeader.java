package pl.cba.reallygrid.steganography.imagetga;

import java.io.DataInputStream;
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
    
    private static int alphaBits(int imageDescriptor) {
        return imageDescriptor & 0x0000000F; // first 4 bits
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
