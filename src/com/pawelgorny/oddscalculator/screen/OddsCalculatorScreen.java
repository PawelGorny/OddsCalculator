package com.pawelgorny.oddscalculator.screen;

import jpoker.poker.Card;
import jpoker.poker.Operation;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Touchscreen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.GaugeField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.DialogFieldManager;
import net.rim.device.api.ui.container.FlowFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.util.IntVector;
import net.rimlib.blackberry.api.advertising.app.Banner;

import com.pawelgorny.oddscalculator.Constants;
import com.pawelgorny.oddscalculator.ImageButtonField;
import com.pawelgorny.oddscalculator.exception.HandCardsNotSelectedException;

public class OddsCalculatorScreen extends MainScreen {
	private boolean saving=false;
    private ImageButtonFieldOpener[] cardsHand=new ImageButtonFieldOpener[2];
    private ImageButtonFieldOpener[] cardsTable=new ImageButtonFieldOpener[4];
    private ImageButtonFieldOpener[] cardsVs=new ImageButtonFieldOpener[4];
    private IntVector cardsList;
    private IntVector cardsListPVsP=new IntVector(cardsVs.length);
    private int[] cards;
    private CardSelector cardSelector;
    private boolean initCompleted=false;
    private ResultsScreen resultsScreen;
    private Banner advBanner=new Banner(Constants.ZONE_ID, null);
    //private HandEval eval;
    public static GaugeField resultProgress;

    public OddsCalculatorScreen() {
        super( MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR );
        setTitle( "Hold'em Odds Calculator" );
        cardSelector=new CardSelector();
        initializeCards();
        ButtonField buttonField_1 = new ButtonField( Constants.ButtonCalculate1, ButtonField.CONSUME_CLICK | ButtonField.FIELD_RIGHT );
        add( buttonField_1 );
        buttonField_1.setChangeListener( new FieldChangeListener() {
            public void fieldChanged( Field arg0, int arg1 ) {
                calculate();
            }
        } );
        /*initializeCardsPVsP();
        ButtonField buttonField_2 = new ButtonField( "Calculate All-in result", ButtonField.CONSUME_CLICK | ButtonField.FIELD_RIGHT );
        add( buttonField_2 );
        buttonField_2.setChangeListener( new FieldChangeListener() {
            public void fieldChanged( Field arg0, int arg1 ) {
                calculatePVsP();
            }
        } );*/        
        resultsScreen=new ResultsScreen();
        resultProgress=new GaugeField("", 0, 100, 0, GaugeField.LABEL_AS_PROGRESS);
        add(resultProgress);
        advBanner.setMMASize(Banner.MMA_SIZE_AUTO);
        advBanner.setTestModeFlag(false);
        add(advBanner);
        initCompleted=true;
    }

    public static void setProgressGauge(int percent){
    	if (percent%5!=0)
    		return;
		try{
			synchronized (UiApplication.getEventLock()) {
				OddsCalculatorScreen.resultProgress.setValue(percent);
				UiApplication.getUiApplication().repaint();
			}
			}catch (Exception exc){}
    }
    
	private void initializeCards(){
    	add(new LabelField(Constants.Hand));
    	HorizontalFieldManager hfm=new HorizontalFieldManager();
    	int i=0;
    	while (i<cardsHand.length)
    	{
        	cardsHand[i]=new ImageButtonFieldOpener(Constants.CARD_CLEAR, i);
        	hfm.add(cardsHand[i]);
        	i++;
        }
    	add(hfm);
    	add(new LabelField(Constants.Table));
    	hfm=new HorizontalFieldManager();
        for (int j=0; j<cardsTable.length; j++, i++){
        	cardsTable[j]=new ImageButtonFieldOpener(Constants.CARD_CLEAR, i);
        	hfm.add(cardsTable[j]);
        	if (j==2){
        		SpacerField spacerField=new SpacerField(Constants.SPACER_WIDTH,0);
        		hfm.add(spacerField);
        	}
        }
        add(hfm);
    }
/*
    private void initializeCardsPVsP(){
    	int i=cardsHand.length+cardsTable.length;
    	add(new LabelField("All-in hands"));
    	HorizontalFieldManager hfm=new HorizontalFieldManager();
    	hfm.add(new LabelField("1)"));
        for (int j=0; j<cardsVs.length/2; j++, i++){
        	cardsVs[j]=new ImageButtonFieldOpener(Constants.CARD_CLEAR, i);
        	hfm.add(cardsVs[j]);
        }
        hfm.add(new LabelField("2)"));
        for (int j=cardsVs.length/2; j<cardsVs.length; j++, i++){
        	cardsVs[j]=new ImageButtonFieldOpener(Constants.CARD_CLEAR, i);
        	hfm.add(cardsVs[j]);
        }
        add(hfm);
    }
    
    
    private void calculatePVsP() {
    	int cardsArr[];
		try {
			cardsArr = getCardsValuesPVsP();
		} catch (HandCardsNotSelectedException e) {
			Dialog.alert(Constants.HandCardsNotSelected);
			return;
		}
		if (eval==null)
			eval=new HandEval();
		for (int i=0; i<cardsArr.length; i++){
			cardsArr[i]=Math.abs(cardsArr[i]-52);
		}
		int numberOfPlayers=cardsArr.length/2;
		double[] result=eval.computePreFlopEquityForSpecificHoleCards(cardsArr, numberOfPlayers);
		resultsScreen.open(result);
	}
	
     private int[] getCardsValuesPVsP() throws HandCardsNotSelectedException {
		for (int i=0; i<cardsVs.length; i++){
			if (cardsVs[i]!=null && cardsVs[i].getValue()==Constants.CARD_CLEAR){
				throw new HandCardsNotSelectedException();
			}
		}
		setSelectedCardsVectorPVsP();
		return cardsListPVsP.getArray();
	}
 */  
    protected void makeMenu( Menu menu, int instance ) {
    	super.makeMenu(menu, instance);
        //MenuItem mntmSayHello = new NewMenuItem();
        //menu.add( mntmSayHello );
    }
    
    
    private void calculate() {
        int cardsArr[];
		try {
			cardsArr = getCardsValues();
		} catch (HandCardsNotSelectedException e) {
			Dialog.alert(Constants.HandCardsNotSelected);
			return;
		}
		setProgressGauge(0);
    	int counts[] = null;
    	long dead = 0;
    	Card card1=null;
    	Card card2=null;
    	cards=new int[7];
    	for (int i=0; i<cardsArr.length; i++)
    	    {
    		Card card=new Card(cardsArr[i]-1);
    		cards[i] = card.value();
//      		System.out.println (cards[i]);
    		dead |= Operation.one<<cards[i];
    		switch (i){
    		case 0:
    			card1=card;break;
    		case 1:
    			card2=card;break;
    			}
    	    }//for
    	if (cardsArr.length==2 && card1!=null && card2!=null){
    		if (card1.rank().equals(card2.rank()))
    		{//pair
    			counts=jpoker.poker.results.ResultPair.getResults(card1.rank());
    		}else
    			if (card1.suit().equals(card2.suit()))
    			{
    				counts=jpoker.poker.results.ResultSuited.getResults(card1.rank(), card2.rank());
    			}
    			else
    			{
    				counts=jpoker.poker.results.ResultOffsuit.getResults(card1.rank(), card2.rank());
    			}
    	}
    	if (counts==null)
    	{
    		counts=Operation.calculate(cards, dead, cardsArr.length);
    	}
    	setProgressGauge(0);
    	int sum = 0;
    	//String result="";
    	//int length=counts.length;
    	for (int i=0; i<counts.length; i++)
    	{
    		sum += counts[i];
    	}
    	/*for (int i=0; i<length; i++)
    	    {
    		result+= Valuation.StringRanking[i] + ": " + net.rim.device.api.util.MathUtilities.round(100*counts[i]/sum) +"\n";
    		System.out.println (counts[i]);
    		//sum += counts[i];
    	    }*/
    	//System.out.println("total: " + sum);
    	//Dialog.inform( result );
    	resultsScreen.open(counts, sum);
    }
    
    private void setSelectedCardsVector(){
    	int c=2;
    	for (int i=0; i<cardsTable.length; i++){
			if (cardsTable[i]!=null &&cardsTable[i].getValue()!=Constants.CARD_CLEAR){
				c++;
			}
		}
    	cardsList=new IntVector(2+c);
		for (int i=0; i<cardsHand.length; i++){
			if (cardsHand[i]!=null &&cardsHand[i].getValue()!=Constants.CARD_CLEAR)
				cardsList.addElement(cardsHand[i].getValue());
		}
		for (int i=0; i<cardsTable.length; i++){
			if (cardsTable[i]!=null &&cardsTable[i].getValue()!=Constants.CARD_CLEAR){
				cardsList.addElement(cardsTable[i].getValue());
			}
		}
    }

    private void setSelectedCardsVectorPVsP(){
    	cardsListPVsP.removeAllElements();
		for (int i=0; i<cardsVs.length; i++){
			if (cardsVs[i]!=null &&cardsVs[i].getValue()!=Constants.CARD_CLEAR)
				cardsListPVsP.addElement(cardsVs[i].getValue());
			/*else if (cardsVs[i]!=null &&cardsVs[i].getValue()==Constants.CARD_CLEAR)
			{
				throw new HandCardsNotSelectedException();
			}*/
		}//for
		
    }
    
   
    
    private int[] getCardsValues() throws HandCardsNotSelectedException {
		for (int i=0; i<cardsHand.length; i++){
			if (cardsHand[i]!=null && cardsHand[i].getValue()==Constants.CARD_CLEAR){
				throw new HandCardsNotSelectedException();
			}
		}
		setSelectedCardsVector();
		int[]cards=new int[cardsList.size()];
		for (int i=0; i<cardsList.size(); i++)
			cards[i]=cardsList.elementAt(i);
		return cards;
	}



	protected boolean onSavePrompt(){
		if (saving){
			saving=false;
			return onSave();
		}
		return true;
	}
	
	private void changeCard(int cardToChange){
		if (cardToChange<cardsHand.length){
			if (cardsHand[cardToChange]!=null)
				cardsHand[cardToChange].setValue(cardSelector.getSelectedValue());
			//cardsHand[cardToChange].setBitmap(Bitmap.getBitmapResource(Constants.getCardImage(cardSelector.selectedValue)));
		}else if (cardToChange<(cardsTable.length+cardsHand.length))
		{
			cardToChange-=cardsHand.length;
			if (cardsTable[cardToChange]!=null)
				cardsTable[cardToChange].setValue(cardSelector.getSelectedValue());
			//cardsTable[cardToChange].setBitmap(Bitmap.getBitmapResource(Constants.getCardImage(cardSelector.selectedValue)));
		}else
		{
			cardToChange-=(cardsTable.length+cardsHand.length);
			if (cardsVs[cardToChange]!=null)
				cardsVs[cardToChange].setValue(cardSelector.getSelectedValue());
		}
	}
	
	private final class ImageButtonFieldOpener extends ImageButtonField {

		int index;
		
		protected boolean keyChar(char character, int status, int time) {
			if (character==Characters.BACKSPACE)
			{
				initCompleted=false;
				setValue(Constants.CARD_CLEAR);
				initCompleted=true;
			}
			return super.keyChar(character, status, time);
		}
		
	    public ImageButtonFieldOpener(int value, int ix) {    	
	        super(Bitmap.getBitmapResource(Constants.getCardImage(value)));
	        this.index=ix;
	        setValue(value);
	        if (Touchscreen.isSupported()){
	        }
	    }
	    
		public void fieldChangeNotify(int context){
			if (!initCompleted)
				return;
			if (this.index<(cardsHand.length+cardsTable.length))
			{
			setSelectedCardsVector();
			cardSelector.open(this.value, cardsList, this.index);
			}
			else
			{
				setSelectedCardsVectorPVsP();
				cardSelector.open(this.value, cardsListPVsP, this.index);
			}
		}
	}

	class CardSelector {
		private SelectorPopupScreen popup;
		private ImageButtonFieldSelector[] cards=new ImageButtonFieldSelector[52];
		private int selectedValue=Constants.CARD_UNDEFINED;
		private DialogFieldManager dialogFieldManager;
		private FlowFieldManager fieldManager;
		private int openerIx;
		
		public CardSelector(){	
			popup=getPopupScreen();
			for (int c=0; c<Constants.NUMBER_OF_SUITS; c++)
			{
				for (int r=1; r<=Constants.NUMBER_OF_RANKS; r++)
				{
					int nr=c*Constants.NUMBER_OF_RANKS+r;
					cards[nr-1]=new ImageButtonFieldSelector(nr);
				}
			}
		}
		
		private SelectorPopupScreen getPopupScreen(boolean clearButton){
			if (dialogFieldManager!=null){
				if (fieldManager!=null){
					fieldManager.deleteAll();
					try{
					dialogFieldManager.delete(fieldManager);
					}catch(Exception e){}
					fieldManager=null;
				}
			}
			if (popup!=null){
				try{
					popup.deleteAll();
					popup=null;
				}catch(Exception e){}
			}
			dialogFieldManager=new DialogFieldManager(DialogFieldManager.VERTICAL_SCROLL, DialogFieldManager.VERTICAL_SCROLL);
			SelectorPopupScreen popup=new SelectorPopupScreen(dialogFieldManager);
			ButtonField buttonField_1 = new ButtonField( Constants.ButtonCancel, ButtonField.CONSUME_CLICK | ButtonField.FIELD_RIGHT );
			//dialogFieldManager.addCustomField(buttonField_1);
			buttonField_1.setChangeListener( new FieldChangeListener() {
	            public void fieldChanged( Field arg0, int arg1 ) {
	                close();
	            }
	        } );
			if (clearButton){
				ButtonField buttonField_2 = new ButtonField( Constants.ButtonClear, ButtonField.CONSUME_CLICK | ButtonField.FIELD_RIGHT );
				dialogFieldManager.addCustomField(buttonField_2);
				buttonField_2.setChangeListener( new FieldChangeListener() {
		            public void fieldChanged( Field arg0, int arg1 ) {
		                selectedValue=Constants.CARD_CLEAR;
		                changeCard(openerIx);
		                close();
		            }
		        } );
			}
			return popup;

		}
		
		private SelectorPopupScreen getPopupScreen(){
			return getPopupScreen(false);
				}
		
		private void showCards(IntVector cardsList){

			if (fieldManager!=null)
				fieldManager.deleteAll();
			else
				fieldManager=new FlowFieldManager();
			for (int c=0; c<Constants.NUMBER_OF_SUITS; c++)
			{
				for (int r=1; r<=Constants.NUMBER_OF_RANKS; r++)
				{
					int nr=c*Constants.NUMBER_OF_RANKS+r;
					if (cardsList.contains(nr))
						continue;
					//cards[nr-1]=new ImageButtonFieldSelector(nr);
					fieldManager.add(cards[nr-1]);
				}
			}
			dialogFieldManager.addCustomField(fieldManager);
		}
		
		public void open(int selectedCard, IntVector cardsList, int opener){
			if (popup.isDisplayed())
				return;
			initCompleted=false;
			openerIx=opener;
			selectedValue=Constants.CARD_UNDEFINED;
			popup=getPopupScreen(selectedCard!=Constants.CARD_CLEAR);
			showCards(cardsList);
			UiApplication.getUiApplication().pushScreen(popup);
			initCompleted=true;
			popup.doPaint();
		}
		
		public void close(){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					if (popup.isDisplayed()){
						UiApplication.getUiApplication().popScreen(popup);
					}
				}
			});
		}
		
		public int getSelectedValue() {
			int temp=selectedValue;
			selectedValue=Constants.CARD_UNDEFINED;
			return temp;
		}

		private final class ImageButtonFieldSelector extends ImageButtonField {
			public ImageButtonFieldSelector(int value) {    	
		        super(Bitmap.getBitmapResource(Constants.getCardImage(value)));
		        setValue(value);
		    }
			public void fieldChangeNotify(int context){
				if (!initCompleted)
					return;
				selectedValue=this.value;
				changeCard(openerIx);
				close();
			}		
		}
		
		private class SelectorPopupScreen extends PopupScreen{

			public SelectorPopupScreen(Manager delegate){
				super(delegate);
			}
			
			protected boolean keyChar(char c, int status, int time){
				if (c==Characters.ESCAPE)
					close();
				if (c==Characters.BACKSPACE)
					close();
				return super.keyChar(c, status, time);
			}
		}
		
	}

	
}
