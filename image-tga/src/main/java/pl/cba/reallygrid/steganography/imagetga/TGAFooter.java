package pl.cba.reallygrid.steganography.imagetga;

import java.io.DataOutputStream;
import java.io.IOException;

class TGAFooter {
    static void write(DataOutputStream outputStream) throws IOException {
        outputStream.write(DEFAULT_FOOTER);
    }
    
    private static final byte[] DEFAULT_FOOTER;
    
    static {
        DEFAULT_FOOTER = new byte[] {
                '\0', '\0', '\0', '\0', // Extension Area Offset
                '\0', '\0', '\0', '\0', // Developer Directory Offset
                'T', 'R', 'U', 'E', 'V', 'I', 'S', 'I', 'O', 'N', '-', 'X', 'F', 'I', 'L', 'E', '\0' // Signature
        };
    }
}
