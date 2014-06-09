package com.didithemouse.didiplus;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import com.mystictreegames.*;


import android.graphics.Color;

import com.didithemouse.didiplus.R;
import com.didithemouse.didiplus.etapas.EtapaActivity.EtapaEnum;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class WriteActivity extends Activity {

	private FrameLayout content = null;
	private DropPanelWrapper panel = null;
	private ImageView drawing = null;
	EditText inputText = null;
	Button terminar;
	Button tabInicio, tabDesarrollo, tabFin;
	int storyIndex=0; //0: inicio, 1:desarrollo, 2:fin
	int substoryIndex=0; //indexa páginas de inicio, desarrollo o fin
	MochilaContents mc = MochilaContents.getInstance();
	
	final static int objectSize = CreateActivity.objectSize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.write);
		
		content = (FrameLayout) findViewById(R.id.dibujoCanvas);
		drawing = (ImageView) findViewById(R.id.bitmapDraw);
		content.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		
		panel = MochilaContents.getInstance().getDropPanel();
		
		inputText = (EditText) findViewById(R.id.inputText);
		
		terminar = (Button) findViewById(R.id.terminar);
		terminar.setOnClickListener(new View.OnClickListener() {
			boolean flag = true;
			public void onClick(View v) {
				if(!flag) return;
				flag = false;
				mc.setText(storyIndex,inputText.getText().toString());
				Intent i = new Intent(v.getContext(), PresentationActivity.class);
				LogX.i("Write","Ha comenzado la presentación.");
				
				if(mc.hasFinished) mc.hasEdited=true;
				if(!mc.hasEdited) mc.cloneTexts();
				mc.hasFinished=true;
				Saver.savePresentation(Saver.ActivityEnum.END);
				mc.hasFinished=false;
				startActivity(i);
				finish();
			}
		});
		terminar.setClickable(true);
		
		
		tabInicio     = (Button) findViewById(R.id.tabinicio);
		tabDesarrollo = (Button) findViewById(R.id.tabdesarrollo);
		tabFin		  = (Button) findViewById(R.id.tabfin);
		
		tabInicio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setStoryIndex(0);
			}
		});
		tabDesarrollo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setStoryIndex(1);
			}
		});
		tabFin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setStoryIndex(2);
			}
		});
		
		
		
		Button buttonback = (Button) findViewById(R.id.buttonback);
		buttonback.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Intent i = new Intent(v.getContext(), CreateActivity.class);
				LogX.i("Write","El instructor ha vuelto atras.");

				mc.setText(storyIndex,inputText.getText().toString());
				
				panel.cleanPanel(true);
				
				startActivity(i);
				finish();
				return true;
			}
		});
		
		
		inputText.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View view, int i, KeyEvent keyevent) {
				terminar.setClickable(true);
				inputText.setOnKeyListener(null);
				return false;
			}
		});
		
		
		
		ShowContent();
		inputText.setText(mc.getText(0));
		setStoryIndex(0);
		
	}
	
	 public void ShowContent() {
	    	content.removeAllViews();
			DropPanelWrapper p1 = panel;
	    	ArrayList<ViewWrapper> wrappers = p1.getWrappers();
			for(ViewWrapper w : wrappers){
				w.destroyView();
				View iv = w.getView(getApplicationContext());
				if(iv==null) continue;
				int left=0, top=0;
				if(iv instanceof ExtendedImageView){
					ExtendedImageView img = (ExtendedImageView)iv;
					left = (int)(w.getX()*CreateActivity.canvasWidth_mid);
					top = (int)(w.getY()*CreateActivity.canvasHeight_mid);
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int)(objectSize*(CreateActivity.canvasWidth_mid/810f)), (int)(objectSize*(CreateActivity.canvasWidth_mid/810f)));
					lp.leftMargin = left;
					lp.topMargin = top;
					//if(img.getEtapa() == EtapaEnum.EMPIRE) img.setBackgroundResource(R.drawable.borderojo);
					//else if(img.getEtapa() == EtapaEnum.INICIO) img.setBackgroundResource(R.drawable.bordeazul);
					content.addView(img, lp);
				}
			}
			drawing.setImageDrawable(p1.getPanelView(this).getMediumBitmap());
			content.addView(drawing);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
	}
	 
	 public void setStoryIndex(int index)
	 {
		 //esconder/mostrar la tab segun corresponda
		 tabInicio.setBackgroundResource((index==0)? R.drawable.tabinicio : R.drawable.tabinicio_hidden);
		 tabDesarrollo.setBackgroundResource((index==1)? R.drawable.tabdesarrollo : R.drawable.tabdesarrollo_hidden);
		 tabFin.setBackgroundResource((index==2)? R.drawable.tabfin : R.drawable.tabfin_hidden);
		 
		 mc.setText(storyIndex,inputText.getText().toString() ); 
		 inputText.setText(mc.getText(index));
		 storyIndex=index;
		 substoryIndex=0;
	 }

		

		
	@Override
	public void onBackPressed() {}

}