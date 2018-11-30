package pl.cba.reallygrid.steganography.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextCipher {
    public static void encode(final String text, final int[] destination) {
        TextCipher encoder = new TextCipher(text.getBytes(), destination);
        encoder.runEncode();
        LOGGER.info("Text has been encoded successfully.");
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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TextCipher.class);
    
    private final byte[] charCodes;
    private final int[] destination;
    
    private enum Channel {
        FIRST(0),
        SECOND(8),
        THIRD(16),
        FOURTH(24); // Fourth channel is not use. Not every tga file has alpha channel.
        
        Channel(int channelPosition) {
            this.channelPosition = channelPosition;
        }
        
        private final int channelPosition;
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
