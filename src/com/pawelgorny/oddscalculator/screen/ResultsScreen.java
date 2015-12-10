package com.pawelgorny.oddscalculator.screen;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Touchscreen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.GaugeField;
import net.rim.device.api.ui.container.DialogFieldManager;
import net.rim.device.api.ui.container.PopupScreen;

import com.pawelgorny.oddscalculator.Constants;

public class ResultsScreen {
	private ResultsPopupScreen popup;
	private DialogFieldManager dialogFieldManager;
	private GaugeField[] gaugeResults;
	public ResultsScreen(){
		
	}
	
	public void open(int[] results, int sum){
		if (popup!=null && popup.isDisplayed())
			return;
		popup=getPopupScreen(results, sum);
		UiApplication.getUiApplication().pushScreen(popup);
		popup.doPaint();
	}
	
	/*public void open(double[] results){
		if (popup!=null && popup.isDisplayed())
			return;
		popup=getPopupScreen(results);
		UiApplication.getUiApplication().pushScreen(popup);
		popup.doPaint();
	}*/
	
	public void close(){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				if (popup.isDisplayed()){
					UiApplication.getUiApplication().popScreen(popup);
				}
			}
		});
	}
/*
	private ResultsPopupScreen getPopupScreen(double[] results) {
		dialogFieldManager=new DialogFieldManager(DialogFieldManager.VERTICAL_SCROLL, DialogFieldManager.VERTICAL_SCROLL);
		ResultsPopupScreen popup=new ResultsPopupScreen(dialogFieldManager);
		gaugeResults=new GaugeField[results.length];
		for (int i=0; i<results.length; i++){
			int proc=net.rim.device.api.util.MathUtilities.round((float)results[i]);
			String label=Constants.Player+i+": ";
			if (proc==0)
				label+="<1";
			else label+=proc;
			label+="%";
			gaugeResults[i]=new GaugeField(label,0,100,proc,GaugeField.LABEL_AS_PROGRESS);
			dialogFieldManager.addCustomField(gaugeResults[i]);
			//100*results[i]/sum
		}
		return popup;
	}
	*/
	private ResultsPopupScreen getPopupScreen(int[] results, int sum) {
		dialogFieldManager=new DialogFieldManager(DialogFieldManager.VERTICAL_SCROLL, DialogFieldManager.VERTICAL_SCROLL);
		ResultsPopupScreen popup=new ResultsPopupScreen(dialogFieldManager);
		gaugeResults=new GaugeField[results.length];
		for (int i=0; i<results.length; i++){
			if (results[i]==0)
				continue;
			float procf=((float)100*results[i])/sum;
			int proc=net.rim.device.api.util.MathUtilities.round(procf);
			String label=Constants.RESULT_NAME[i];
			if (proc==0)
				label+="~0";
			else if (procf<1)
				label+="<1";
			else label+=proc;
			label+="%";
			gaugeResults[i]=new GaugeField(label,0,sum,results[i],GaugeField.LABEL_AS_PROGRESS);
			dialogFieldManager.addCustomField(gaugeResults[i]);
			//100*results[i]/sum
		}
		if (Touchscreen.isSupported()){
		ButtonField buttonField_1 = new ButtonField( Constants.ButtonOK, ButtonField.CONSUME_CLICK | ButtonField.FIELD_RIGHT );
		dialogFieldManager.addCustomField(buttonField_1);
		buttonField_1.setChangeListener( new FieldChangeListener() {
            public void fieldChanged( Field arg0, int arg1 ) {
                close();
            }
        } );
		}
		return popup;
	}
	
	private class ResultsPopupScreen extends PopupScreen{

		public ResultsPopupScreen(Manager delegate){
			super(delegate);
		}
		
		protected boolean keyChar(char c, int status, int time){
			if (c==Characters.ESCAPE)
				close();
			if (c==Characters.ENTER)
				close();
			if (c==Characters.BACKSPACE)
				close();
			return super.keyChar(c, status, time);
		}
		
		protected boolean navigationClick(int status, int time){
			close();
			return super.navigationClick(status, time);
		}
	}

}
