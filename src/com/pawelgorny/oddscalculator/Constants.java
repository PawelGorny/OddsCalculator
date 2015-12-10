package com.pawelgorny.oddscalculator;

import net.rim.device.api.i18n.ResourceBundle;

import com.pawelgorny.oddscalculator.i18n.LangResource;

public class Constants {

	private static ResourceBundle _resources=ResourceBundle.getBundle(com.pawelgorny.oddscalculator.i18n.LangResource.BUNDLE_ID,
			com.pawelgorny.oddscalculator.i18n.LangResource.BUNDLE_NAME);
	
	private static final String CARDS_PATH="cards/";
	public static final int CARD_UNDEFINED=-1,
			CARD_CLEAR=0,
			NUMBER_OF_RANKS=13,
			NUMBER_OF_SUITS=4,
			ZONE_ID=125012,
			NORMAL_PADDING=4,
			TOUCHSCREEN_PADDING=9,
			SPACER_WIDTH=15;
	public static final String Space=" ", 
			HandCardsNotSelected = _resources.getString(LangResource.INFO_CARDS_NOT_SELECTED),
			//SelectCard = _resources.getString(LangResource.INFO_SELECT_CARD),
			Player=_resources.getString(LangResource.PLAYER)+Constants.Space,
			ButtonCalculate1=_resources.getString(LangResource.BUTTON_CALCULATE1),
			ButtonOK="OK",
			Table=_resources.getString(LangResource.TABLE),
			Hand=_resources.getString(LangResource.HAND),
			ButtonCancel=_resources.getString(LangResource.BUTTON_CANCEL),
			ButtonClear=_resources.getString(LangResource.BUTTON_CLEAR)
			;
	
	public static final String[] RESULT_NAME={
		_resources.getString(LangResource.RESULT_NOPAIR)+Constants.Space,
		_resources.getString(LangResource.RESULT_PAIR)+Constants.Space,
		_resources.getString(LangResource.RESULT_2PAIRS)+Constants.Space,
		_resources.getString(LangResource.RESULT_THREE)+Constants.Space,
		_resources.getString(LangResource.RESULT_STRAIGHT)+Constants.Space,
		_resources.getString(LangResource.RESULT_FLUSH)+Constants.Space,
		_resources.getString(LangResource.RESULT_FULL)+Constants.Space,
		_resources.getString(LangResource.RESULT_FOUR)+Constants.Space,
		_resources.getString(LangResource.RESULT_STRAIGHTFLUSH)+Constants.Space
	  };
	
	public static String getCardImage(int number){
		/*if (1==1)
			return CARDS_PATH+"c"+number+".png";*/
		String path=CARDS_PATH+(net.rim.device.api.system.Display.getWidth()<=320?"30/30_":"40/40_");
		if (number==0){
			return path+"0.png";
		}
		switch ((number-1)%13){
		case 12:
			path+="as_";
			break;
		case 11:
			path+="krol_";
			break;
		case 10:
			path+="dama_";
			break;
		case 9:
			path+="walet_";
			break;
		default:
			path+=(((number-1)%13)+2)+"_";
		}
		switch((number-1)/13){
		case 0:
			path+="karo";
			break;
		case 1:
			path+="pik";
			break;
		case 2:
			path+="kier";
			break;
		case 3:
			path+="trefl";
			break;
		}
		return path+".png";
	}
}
