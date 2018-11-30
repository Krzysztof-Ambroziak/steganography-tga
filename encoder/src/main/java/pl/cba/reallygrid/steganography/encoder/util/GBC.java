package pl.cba.reallygrid.steganography.encoder.util;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBC extends GridBagConstraints {
    public GBC(int gridx, int gridy) {
        this.gridx = gridx;
        this.gridy = gridy;
    }
    
    public GBC weight(double weightx, double weighty) {
        this.weightx = weightx;
        this.weighty = weighty;
        
        return this;
    }
    
    public GBC fill(int fill) {
        this.fill = fill;
        return this;
    }
    
    public GBC insets(int top, int left, int bottom, int right) {
        this.insets = new Insets(top, left, bottom, right);
        
        return this;
    }
}
