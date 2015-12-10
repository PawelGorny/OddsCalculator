package com.pawelgorny.oddscalculator;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Touchscreen;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;


public class ImageButtonField extends BitmapField
{
	protected int value;
	
	public ImageButtonField() {
		this(0);
    }
	
    public ImageButtonField(Bitmap image) {
        super(image);
        this.setSpace(Constants.NORMAL_PADDING, Constants.NORMAL_PADDING);
        if (Touchscreen.isSupported()){
        	this.setSpace(Constants.TOUCHSCREEN_PADDING, Constants.TOUCHSCREEN_PADDING);
        }
    }

    public ImageButtonField(int value) {    	
        super(Bitmap.getBitmapResource(Constants.getCardImage(value)));
        setValue(value);
    }
        
    public boolean isFocusable() {
        return true;
    }

    protected boolean navigationClick(int status, int time) {
        fieldChangeNotify(0);
        return true;
    }

    protected boolean trackwheelClick(int status, int time) {
        fieldChangeNotify(0);
        return true;
    }

    protected boolean keyChar(char character, int status, int time) {
        if(Characters.ENTER == character || Characters.SPACE == character) {
            fieldChangeNotify(0);
            return true;
        }
        return super.keyChar(character, status, time);
    }
    
    protected void paint(Graphics graphics) {
        super.paint(graphics);
        if (isFocus()) {
            graphics.setGlobalAlpha(128);
            graphics.setColor(0xFF0000);
            graphics.fillRect(0, 0, getWidth(), getHeight());
        }
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		this.setBitmap(Bitmap.getBitmapResource(Constants.getCardImage(this.value)));
	}
	
	public void fieldChangeNotify(int context){
		Dialog.inform( "Selected: "+this.value );
	}
    
    
}