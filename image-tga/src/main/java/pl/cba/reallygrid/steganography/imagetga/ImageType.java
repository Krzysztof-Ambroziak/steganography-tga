package pl.cba.reallygrid.steganography.imagetga;

enum ImageType {
    NO_IMAGE_DATA(0),
    UNCOMPRESSED_COLOR_MAPPED(1),
    UNCOMPRESSED_TRUE_COLOR(2),
    UNCOMPRESSED_BLACK_AND_WHITE(3),
    RUN_LENGTH_COLOR_MAPPED(9),
    RUN_LENGTH_TRUE_COLOR(10),
    RUN_LENGTH_BLACK_AND_WHITE(11);
    
    ImageType(int type) {
        this.type = type;
    }
    
    int typeValue() {
        return type;
    }
    
    static ImageType imageTypeFromInt(int type) {
        switch(type & 0x000000FF) {
            case 0:
                return NO_IMAGE_DATA;
            case 1:
                return UNCOMPRESSED_COLOR_MAPPED;
            case 2:
                return UNCOMPRESSED_TRUE_COLOR;
            case 3:
                return UNCOMPRESSED_BLACK_AND_WHITE;
            case 9:
                return RUN_LENGTH_COLOR_MAPPED;
            case 10:
                return RUN_LENGTH_TRUE_COLOR;
            case 11:
                return RUN_LENGTH_BLACK_AND_WHITE;
            default:
                throw new TGAFormatException("Unknown Image Type. Values 0, 1, 2, 3, 9, 10 and 11 are supported only.");
        }
    }
    
    private final int type;
}
