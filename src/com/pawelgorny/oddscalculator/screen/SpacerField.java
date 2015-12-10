package com.pawelgorny.oddscalculator.screen;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public class SpacerField extends Field {

	private int spacerWidth;
    private int spacerHeight;
    public SpacerField(int width, int height) {
        spacerWidth = width;
        spacerHeight = height;
    }

    protected void layout(int width, int height) {
        setExtent(getPreferredWidth(), getPreferredHeight());
    }

    protected void paint(Graphics g) {
        // nothing to paint; this is a blank field
    }

    public int getPreferredHeight() {
        return spacerHeight;
    }

    public int getPreferredWidth() {
        return spacerWidth;
    }

}
