package pl.cba.reallygrid.steganography.encoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.cba.reallygrid.steganography.encoder.gui.GuiService;

public class Controller {
    public void start() {
        guiService.createFrame();
        guiService.showFrame();
        LOGGER.info("Application is ready to use.");
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private GuiService guiService = new GuiService();
}
