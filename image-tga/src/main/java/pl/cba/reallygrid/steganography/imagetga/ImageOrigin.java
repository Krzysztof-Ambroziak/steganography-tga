package pl.cba.reallygrid.steganography.imagetga;

enum ImageOrigin {
    BOTTOM_LEFT(0),
    BOTTOM_RIGHT(1),
    TOP_LEFT(2),
    TOP_RIGHT(3);
    
    ImageOrigin(int value) {
        this.value = value;
    }
    
    public int originValue() {
        return value;
    }
    
    static ImageOrigin ImageOriginFromInt(int value) {
        switch(value & 0x00000003) {
            case 0:
                return BOTTOM_LEFT;
            case 1:
                return BOTTOM_RIGHT;
            case 2:
                return TOP_LEFT;
            case 3:
                return TOP_RIGHT;
            default:
                // default section is not necessary because val can only take values 0, 1, 2, and 3.
                // So null will never be returned. However writing it is good practice.
                return null;
        }
    }
    
    private final int value;
}
