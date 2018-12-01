package pl.cba.reallygrid.steganography.encoder.service;

import pl.cba.reallygrid.steganography.encoder.gui.GuiService;
import pl.cba.reallygrid.steganography.encoder.model.Model;
import pl.cba.reallygrid.steganography.encoder.util.Preferences;
import pl.cba.reallygrid.steganography.encoder.util.PreferencesKeys;

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
    ActionProvider(Model model, GuiService guiService, Controller controller) {
        this.controller = controller;
        this.guiService = guiService;
        this.model = model;
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
            Preferences.putBoolean(PreferencesKeys.FULL_FRAME, true);
        }
        else {
            Preferences.putBoolean(PreferencesKeys.FULL_FRAME, false);
        }
    };
    
    public ActionListener loadImageAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = guiService.getFileChooser();
            
            PreferencesKeys lastPath = PreferencesKeys.LAST_OPEN_PATH;
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
            Preferences.putString(PreferencesKeys.LAST_OPEN_PATH, file.getParent());
            
            if(file.canRead()) {
                controller.loadFile(file);
            }
        }
    };
    
    public ActionListener saveTextToImage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = guiService.getText();
            model.setText(text);
            
            if(isEncodePossible()) {
                controller.saveTextToImage();
            }
            else {
                guiService.showWarningDialog("Text is too long to encoder.");
            }
        }
        
        private boolean isEncodePossible() {
            int pixels = model.getOriginalPixels().length;
            int maxChars = (pixels / 8) * 3;
            int textSize = model.getText().getBytes().length;
            
            return pixels >= 8 && textSize <= maxChars;
        }
    };
    
    public ActionListener saveImageAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = guiService.getFileChooser();
    
            PreferencesKeys lastPath = PreferencesKeys.LAST_SAVE_PATH;
            String path = Preferences.getString(lastPath);
            
            fileChooser.setDialogTitle("Save image file");
            fileChooser.setCurrentDirectory(new File(path));
            showSaveDialog(fileChooser);
        }
        
        private void showSaveDialog(JFileChooser fileChooser) {
            JFrame frame = guiService.getFrame();
            int returnValue = fileChooser.showSaveDialog(frame);
            
            if(returnValue == JFileChooser.APPROVE_OPTION) {
                checkSelectedFile(fileChooser.getSelectedFile());
            }
        }
        
        private void checkSelectedFile(File file) {
            Preferences.putString(PreferencesKeys.LAST_SAVE_PATH, file.getParent());
            
            if(file.exists() && file.isFile() && file.canWrite() || !file.exists()) {
                controller.saveFile(file);
            }
        }
    };
    
    private final Model model;
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
                Preferences.putInteger(PreferencesKeys.WIDTH_FRAME, width);
                oldWidth = width;
            }
            if(height != oldHeight) {
                Preferences.putInteger(PreferencesKeys.HEIGHT_FRAME, height);
                oldHeight = height;
            }
        }
        
        void saveNewPlace(Component component) {
            int x = component.getX();
            int y = component.getY();
            
            if(x != oldY) {
                Preferences.putInteger(PreferencesKeys.POSITION_X, x);
                oldX = x;
            }
            if(y != oldY) {
                Preferences.putInteger(PreferencesKeys.POSITION_Y, y);
                oldY = y;
            }
        }
        
        private int oldX = Preferences.getInteger(PreferencesKeys.POSITION_X);
        private int oldY = Preferences.getInteger(PreferencesKeys.POSITION_Y);
        private int oldWidth = Preferences.getInteger(PreferencesKeys.WIDTH_FRAME);
        private int oldHeight = Preferences.getInteger(PreferencesKeys.HEIGHT_FRAME);
    }
}
