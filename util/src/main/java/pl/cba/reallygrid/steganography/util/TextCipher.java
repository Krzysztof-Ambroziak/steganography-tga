package pl.cba.reallygrid.steganography.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextCipher {
    public static void encode(final String text, final int[] destination) {
        TextCipher encoder = new TextCipher(text.getBytes(), destination);
        encoder.runEncode();
        LOGGER.info("Text has been encoded successfully.");
    }
    
    public static String decode(int size, int[] pixels) {
        int steps = (size + 2) / 3; // text will be decoding from 8 pixels and 3 channels
        byte[] bytes = new byte[size];
        
        for(int step = 0; step < steps; ++step) {
            readThreeBytes(step, bytes, pixels);
        }
        
        return new String(bytes);
    }
    
    private TextCipher(byte[] charCodes, int[] destination) {
        this.charCodes = charCodes;
        this.destination = destination;
    }
    
    private void runEncode() {
        int steps = (charCodes.length + 2) / 3; // text will be encoding on three channel: R, G and B.
        
        for(int step = 0; step < steps; ++step) {
            encodeChars(step * 3); // encoding max 3 chars into 8 pixels
        }
    }
    
    private void encodeChars(int charIndex) {
        int pixelIndex = 8 * (charIndex / 3);
        int chr = Byte.toUnsignedInt(charCodes[charIndex]);
        
        saveCharOnEightPixels(chr, Channel.FIRST, pixelIndex); // encoding first char on first channel
        
        if(charIndex + 1 < charCodes.length) {
            chr = charCodes[charIndex + 1];
            saveCharOnEightPixels(chr, Channel.SECOND, pixelIndex); // encoding second char on second channel
        }
        if(charIndex + 2 < charCodes.length) {
            chr = charCodes[charIndex + 2];
            saveCharOnEightPixels(chr, Channel.THIRD, pixelIndex); // encoding third char on third channel
        }
    }
    
    private void saveCharOnEightPixels(int chr, Channel channel, int pixelIndex) {
        for(int i = 0; i < 8; i++) {
            Bit bit = Bit.bitFromInt((chr >> i) & 0x00000001);
            int pixel = destination[pixelIndex + i];
            int newChannelBits = bit.returnPixel(channel, pixel);
            
            destination[pixelIndex + i] = newChannelBits;
        }
    }
    
    private static void readThreeBytes(int step, byte[] bytes, int[] pixels) {
        int size = bytes.length;
        
        readByte(step, Channel.FIRST, pixels, bytes);
        if((step * 3 + Channel.SECOND.channelOrder) < size) {
            readByte(step, Channel.SECOND, pixels, bytes);
        }
        if((step * 3 + Channel.THIRD.channelOrder) < size) {
            readByte(step, Channel.THIRD, pixels, bytes);
        }
    }
    
    private static void readByte(int step, Channel channel, int[] pixels, byte[] bytes) {
        int index = step * 8;
        int charUnit = 0;
        
        for(int i = 0; i < 8; i++) {
            int pixel = (pixels[index + i] >> channel.channelPosition) & 0x00000001;
            charUnit |= (pixel << i);
        }
        bytes[step * 3 + channel.channelOrder] = (byte)charUnit;
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TextCipher.class);
    
    private final byte[] charCodes;
    private final int[] destination;
    
    private enum Channel {
        FIRST(0, 0),
        SECOND(8, 1),
        THIRD(16, 2),
        FOURTH(24, 3); // Fourth channel is not use. Not every tga file has alpha channel.
        
        Channel(int channelPosition, int channelOrder) {
            this.channelPosition = channelPosition;
            this.channelOrder = channelOrder;
        }
        
        private final int channelPosition;
        private final int channelOrder;
    }
    
    private enum Bit {
        ZERO {
            @Override
            public int returnPixel(Channel channel, int pixel) {
                int bit = 0x00000001 << channel.channelPosition;
                bit = ~bit;
                return pixel & bit;
            }
        },
        ONE {
            @Override
            public int returnPixel(Channel channel, int pixel) {
                int bit = 0x00000001 << channel.channelPosition;
                return pixel | bit;
            }
        };
        
        static Bit bitFromInt(int i) {
            return (i == 0) ? Bit.ZERO : Bit.ONE;
        }
        
        abstract int returnPixel(Channel channel, int pixel);
    }
}
