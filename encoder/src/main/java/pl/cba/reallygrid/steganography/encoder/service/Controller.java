package pl.cba.reallygrid.steganography.encoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.cba.reallygrid.steganography.encoder.gui.GuiService;
import pl.cba.reallygrid.steganography.encoder.model.Model;
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
        LOGGER.info("Application is ready to use.");
    }
    
    void loadFile(File file) {
        singleThread.execute(() -> loadImage(file));
    }
    
    private void loadImage(File file) {
        try {
            StringBuilder builder = new StringBuilder();
            BufferedImage image = TGAImage.read(file, builder);
            addPixelsToModel(image);
            guiService.addOriginalImage(image);
            guiService.enabledComponentsAfterImageLoading();
            LOGGER.info(file.getName() + " has been successfully loaded.");
        }
        catch(TGAFormatException | IOException e) {
            guiService.showWarningDialog(e.getLocalizedMessage());
        }
    }
    
    private void addPixelsToModel(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferInt dataBuffer = (DataBufferInt)raster.getDataBuffer();
        int[] data = dataBuffer.getData();
        
        model.setOriginalPixels(data);
    }
    
    void saveTextToImage() {
        singleThread.execute(() -> {
            model.copyDataPixel();
            TextCipher.encode(model.getText(), model.getEncodedPixels());
            LOGGER.info("Text was successfully encoded.");
            guiService.addEncodedImage(model.getEncodedPixels());
            guiService.enabledComponentsAfterEncoding();
        });
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private GuiService guiService = new GuiService();
    private Model model = new Model();
    private ActionProvider actionProvider = new ActionProvider(model, guiService, this);
    private Executor singleThread = Executors.newSingleThreadExecutor();
}
