package pl.cba.reallygrid.steganography.imagetga;

enum ColorMapType {
    NO_COLOR_MAP(0),
    COLOR_MAP(1);
    
    ColorMapType(int mapType) {
        this.mapType = mapType;
    }
    
    int mapTypeValue() {
        return mapType;
    }
    
    static ColorMapType colorMapTypeFromInt(int mapType) {
        switch(mapType & 0x000000FF) {
            case 0:
                return NO_COLOR_MAP;
            case 1:
                return COLOR_MAP;
            default:
                throw new TGAFormatException("Value " + mapType + " is not recognized. Values 0 and 1 are supported only.");
        }
    }
    
    private final int mapType;
}
