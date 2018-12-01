package pl.cba.reallygrid.steganography.decoder.service;

import pl.cba.reallygrid.steganography.decoder.gui.GuiService;
import pl.cba.reallygrid.steganography.decoder.util.Preferences;
import pl.cba.reallygrid.steganography.decoder.util.PreferencesKey;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowStateListener;
import java.io.File;

public class ActionProvider {
    ActionProvider(GuiService guiService, Controller controller) {
        this.controller = controller;
        this.guiService = guiService;
    }
    
    public ComponentListener frameListener = new FramePositionListener() {
        @Override
        public void componentResized(ComponentEvent e) {
            Component source = e.getComponent();
            saveNewSize(source);
            if(needSaveNewPlace(source)) {
                saveNewPlace(source);
            }
        }
        
        @Override
        public void componentMoved(ComponentEvent e) {
            Component source = e.getComponent();
            saveNewPlace(source);
        }
    };
    
    public WindowStateListener maximizedFrameListener = e -> {
        int state = e.getNewState();
        
        if(state == JFrame.MAXIMIZED_BOTH) {
            Preferences.putBoolean(PreferencesKey.FULL_FRAME, true);
        }
        else {
            Preferences.putBoolean(PreferencesKey.FULL_FRAME, false);
        }
    };
    
    public ActionListener loadImageAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = guiService.getFileChooser();
            
            PreferencesKey lastPath = PreferencesKey.LAST_OPEN_PATH;
            String path = Preferences.getString(lastPath);
            
            fileChooser.setDialogTitle("Open image file");
            fileChooser.setCurrentDirectory(new File(path));
            showOpenDialog(fileChooser);
        }
        
        private void showOpenDialog(JFileChooser fileChooser) {
            JFrame frame = guiService.getFrame();
            int returnValue = fileChooser.showOpenDialog(frame);
            
            if(returnValue == JFileChooser.APPROVE_OPTION) {
                checkSelectedFile(fileChooser.getSelectedFile());
            }
        }
        
        private void checkSelectedFile(File file) {
            Preferences.putString(PreferencesKey.LAST_OPEN_PATH, file.getParent());
            
            if(file.canRead()) {
                controller.loadFile(file);
                controller.decodeFile();
            }
        }
    };
    
    private final GuiService guiService;
    private final Controller controller;
    
    private abstract class FramePositionListener extends ComponentAdapter {
        @Override
        public abstract void componentResized(ComponentEvent e);
        
        @Override
        public abstract void componentMoved(ComponentEvent e);
        
        boolean needSaveNewPlace(Component component) {
            return component.getX() != oldX || component.getY() != oldY;
        }
        
        void saveNewSize(Component component) {
            int width = component.getWidth();
            int height = component.getHeight();
            
            if(width != oldWidth) {
                Preferences.putInteger(PreferencesKey.WIDTH_FRAME, width);
                oldWidth = width;
            }
            if(height != oldHeight) {
                Preferences.putInteger(PreferencesKey.HEIGHT_FRAME, height);
                oldHeight = height;
            }
        }
        
        void saveNewPlace(Component component) {
            int x = component.getX();
            int y = component.getY();
            
            if(x != oldY) {
                Preferences.putInteger(PreferencesKey.POSITION_X, x);
                oldX = x;
            }
            if(y != oldY) {
                Preferences.putInteger(PreferencesKey.POSITION_Y, y);
                oldY = y;
            }
        }
        
        private int oldX = Preferences.getInteger(PreferencesKey.POSITION_X);
        private int oldY = Preferences.getInteger(PreferencesKey.POSITION_Y);
        private int oldWidth = Preferences.getInteger(PreferencesKey.WIDTH_FRAME);
        private int oldHeight = Preferences.getInteger(PreferencesKey.HEIGHT_FRAME);
    }
}
