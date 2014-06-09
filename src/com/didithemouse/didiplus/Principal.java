package com.didithemouse.didiplus;

import com.didithemouse.didiplus.Saver.ActivityEnum;
import com.didithemouse.didiplus.etapas.InicioActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Principal extends Activity {
  
	EditText etNumber;
	EditText etName;
	Button cargar;
	Button comenzar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.principal);
		MochilaContents.getInstance().restart();
		LogX.cleanLogger();

		System.gc();			
		
		View vcomenzar = findViewById(R.id.iniciars);
		etNumber = (EditText) findViewById(R.id.numKidText);
		etName = ((EditText) findViewById(R.id.kidNameText));
		etNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_DATETIME_VARIATION_NORMAL);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
		View bg = findViewById(R.id.bg);
		bg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				updateButtons();
			}
		});		
		
		  //fast test
		/*
bg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String kidName = "testeest";
				int kidNumber = 1;
				MochilaContents.getInstance().setKid(kidNumber, kidName);
				MochilaContents.getInstance().addItem(new ViewWrapper(0.2f, 0.3f, 82, 97 ,R.drawable.badge_coney_small,0.3f));
				MochilaContents.getInstance().addItem(new ViewWrapper(0.3f, 0.2f, 97, 82 ,R.drawable.chinatown_arroz,0.3f));
				Intent intent = new Intent(v.getContext().getApplicationContext(), CreateActivity.class);
		        startActivity(intent);
		        finish();
			}
		});	
// *////////////////////////
		
		comenzar = (Button) vcomenzar;
		comenzar.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		comenzar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String kidName = etName.getText().toString();
				String textNumber = etNumber.getText().toString();
				int kidNumber = 0;
				
				try	{  kidNumber = Integer.parseInt(textNumber);} 
				catch (Exception ignoreException) { return;}
				
				if (kidName == null || kidName.equals("")) {return;}
				

				if (MochilaContents.getInstance().kidExists(kidNumber))	{return;}

				MochilaContents.getInstance().setKid(kidNumber, kidName);
				LogX.i("Start","Comienza el juego");
				Intent intent = new Intent(v.getContext().getApplicationContext(), InicioActivity.class);
		        startActivity(intent);
		        finish();
			}
		});
		setComenzarState(false);

		
		cargar = (Button) findViewById(R.id.load);
		cargar.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		cargar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String kidName = etName.getText().toString();
				String textNumber = etNumber.getText().toString();
				int kidNumber = 0;
				try
				{
				   kidNumber = Integer.parseInt(textNumber);
				}
				catch (NumberFormatException ignoreException) { return;}
				
				MochilaContents.getInstance().setKid(kidNumber, kidName);

				ActivityEnum result = Saver.loadPresentation();
				Intent intent; 
				
				Context c = v.getContext().getApplicationContext();
				if(result == ActivityEnum.ERROR) return;
				else if(result == ActivityEnum.ETAPA || result == ActivityEnum.MAPA) 
					intent = new Intent(c, MapaActivity.class);
				else if(result == ActivityEnum.CREATE)
					intent = new Intent(c, CreateActivity.class);
				else if(result == ActivityEnum.WRITE || (result == ActivityEnum.END && !MochilaContents.getInstance().hasEdited))
					intent = new Intent(c,WriteActivity.class);
				else 
					intent = new Intent(c, PresentationActivity.class);
				
				LogX.i("Restart","Se ha vuelto a cargar la aplicación.");
		        startActivity(intent);
		        finish();
			}
		});
		setCargarState(false);
					
		
		etNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				updateButtons();
				return false;
			}
		});
		etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				updateButtons();
				return false;
			}
		});
    }
    
    void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    
    void updateButtons()
    {
    	if( cargar == null ||  comenzar == null) return;
    	String textNumber = etNumber.getText().toString();
    	String kidName = etName.getText().toString();
		int kidNumber = -1;
		try
		{
		   kidNumber = Integer.parseInt(textNumber);
		}
		catch (NumberFormatException ignoreException) {}
		if(MochilaContents.getInstance().kidExists(kidNumber))
		{
			setCargarState(true);
			setComenzarState(false);
			
		}
		else if (kidName == null || kidName.equals(""))
		{
			setCargarState(false);
			setComenzarState(false);
		}
		else
		{
			setCargarState(false);
			setComenzarState(true);
		}
    }
    
    void setCargarState(boolean isActive)
    {
    	cargar.setClickable(isActive);
		cargar.setFocusable(isActive);
		cargar.setTextColor(isActive? Color.WHITE:Color.DKGRAY);
    }
    void setComenzarState(boolean isActive)
    {
    	comenzar.setClickable(isActive);
    	comenzar.setFocusable(isActive);
    	comenzar.setTextColor(isActive? Color.WHITE:Color.DKGRAY);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	}
    
    @Override
    protected void onPause() {
        super.onPause();

    }
    
    @Override
    protected void onResume() {
        super.onResume();

    }
    
    
}