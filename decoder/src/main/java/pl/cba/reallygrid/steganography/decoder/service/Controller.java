package pl.cba.reallygrid.steganography.decoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.cba.reallygrid.steganography.decoder.gui.GuiService;
import pl.cba.reallygrid.steganography.decoder.model.Model;
import pl.cba.reallygrid.steganography.imagetga.TGAFormatException;
import pl.cba.reallygrid.steganography.imagetga.TGAImage;
import pl.cba.reallygrid.steganography.util.TextCipher;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Controller {
    public void start() {
        guiService.createFrame();
        guiService.addActions(actionProvider);
        guiService.showFrame();
    }
    
    void loadFile(File file) {
        singleThread.execute(() -> loadImage(file));
    }
    
    private void loadImage(File file) {
        try {
            StringBuilder builder = new StringBuilder();
            BufferedImage image = TGAImage.read(file, builder);
            setTextLength(builder.toString());
            addPixelsToModel(image);
            guiService.addImage(image);
            guiService.enabledComponentsAfterImageLoading();
            LOGGER.info(file.getName() + " has been successfully loaded.");
        }
        catch(TGAFormatException | IOException e) {
            guiService.showWarningDialog(e.getLocalizedMessage());
        }
    }
    
    private void setTextLength(String textLength) {
        int length = Integer.parseInt(textLength);
        model.setTextLength(length);
    }
    
    private void addPixelsToModel(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferInt dataBuffer = (DataBufferInt)raster.getDataBuffer();
        int[] data = dataBuffer.getData();
        
        model.setPixels(data);
    }
    
    void decodeFile() {
        singleThread.execute(this::decodeImage);
    }
    
    private void decodeImage() {
        int textLength = model.getTextLength();
        int[] pixels = model.getPixels();
        String decodedText = TextCipher.decode(textLength, pixels);
        
        guiService.setText(decodedText);
        LOGGER.info("Text has been successfully decoded.");
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private GuiService guiService;
    private Model model;
    private ActionProvider actionProvider;
    private Executor singleThread = Executors.newSingleThreadExecutor();
    
    {
        guiService = new GuiService();
        model = new Model();
        actionProvider = new ActionProvider(guiService, this);
    }
}
