package com.didithemouse.didiplus;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.didithemouse.didiplus.MyAbsoluteLayout.LayoutParams;
import com.didithemouse.didiplus.etapas.EtapaActivity.EtapaEnum;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class CreateActivity extends Activity implements OnTouchListener{

	private DragController dragController;
	private DragLayer dragLayer;
	FrameLayout fl;
	private FingerPaint fp;
	Button next;
	boolean deleting = false;
	
	
	//Constants
	final static int canvasHeight = 620;
	final static int canvasWidth = 810;
	
	final static int canvasHeight_mid = (int)(canvasHeight/1.619);
	final static int canvasWidth_mid = (int)(canvasWidth/1.619);
	
	final static int canvasHeight_small = (int)(canvasHeight/2.375);
	final static int canvasWidth_small = (int)(canvasWidth/2.375);
	
	final static int topBarHeight = 50+63;
	final static int canvasSelectorWidth = 150;
	final static int noteSpiralWidth = 40;
	
	final static int objectSize = 115;
	
	private RelativeLayout toolbar;
	private ImageButton[] toolbar_button;
		
	private DropPanelWrapper panel = MochilaContents.getInstance().getDropPanel();
	private ArrayList<ViewWrapper> items = MochilaContents.getInstance().getItems();
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dragController = new DragController(this);
		dragController.setCreateMode(true);

		setContentView(R.layout.create);  
		dragLayer = (DragLayer) findViewById(R.id.canvas_big_draglayer);
		dragLayer.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
			
		dragLayer.setDragController(dragController);
				
		
		dragController.setFixedDropTarget(dragLayer);
		dragController.addDropTarget(dragLayer);
		
		updateItemsInDragLayer();
		
		next = (Button) findViewById(R.id.terminar);

		
		next.setOnClickListener(new View.OnClickListener() {
			boolean flag = true;
			public void onClick(View v) {
				if(!flag) return;
				flag = false;
				Intent i = new Intent(v.getContext(), WriteActivity.class);
				LogX.i("Write","Se ha iniciado Write.");


				removePanelItems();
				
				
				Saver.savePresentation(Saver.ActivityEnum.WRITE);
				startActivity(i);
				finish();
			}
		});
		next.setClickable(true);
				
	
		FrameLayout fl = (FrameLayout) findViewById(R.id.canvas_framelayout);
		if(panel!=null)
		{
			fp= new FingerPaint(this.getApplicationContext(), dragLayer);
			fp.setBitmap(panel.getBitmap());
			fl.addView(fp);
			fp.setMinimumWidth(canvasWidth);
			fp.setMinimumHeight(canvasHeight);
		}
		
		
		/*Aqui seteamos fingerpaint */
		toolbar = (RelativeLayout) findViewById(R.id.canvas_toolbarlayout);
		
		toolbar_button = new ImageButton[]{(ImageButton)(toolbar.getChildAt(0)),
				(ImageButton)(toolbar.getChildAt(1)),(ImageButton)(toolbar.getChildAt(2))};
		
		//Seteamos que hara el click de la MANO
		toolbar_button[0].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fp.setDrag(); 
				setDrag(true);
				changeSelectedToolbar(0);
				LogX.i("Create","Se ha utilizado la mano.");
			    }
		});
		
		//LAPIZ
		toolbar_button[1].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fp.setDraw();	
				setDrag(false);
				changeSelectedToolbar(1);
				LogX.i("Create","Se ha seleccionado el lapiz.");
			}
		});
		
		//Seteamos que hara el click de la GOMA
		toolbar_button[2].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fp.setErase();
				setDrag(false);
				changeSelectedToolbar(2);
				LogX.i("Create","Se ha seleccionado la goma.");
			    }
		});
		changeSelectedToolbar(0);
		
		fp.setDrag();
				
		//nueva.bringToFront();

		showPanel();
		
		
	}

	void changeSelectedToolbar(int index)
	{
		for (int i=0; i < toolbar_button.length; i++) 
			toolbar_button[i].setColorFilter(0);
		toolbar_button[index].setColorFilter(0xC00080FF);
	}
	

		
	
	public void showPanel()
	{
		DropPanel dp = panel.getPanelView(this);
		
		dp.setVisibility(View.VISIBLE);
				

		dragLayer.addView(dp,0);
		
		DragLayer.LayoutParams lp = new LayoutParams(canvasWidth, canvasHeight,canvasSelectorWidth+noteSpiralWidth,topBarHeight);
		dragLayer.updateViewLayout(dp, lp);
		dp.setDragController(dragController);
		
		
		dragController.addDropTarget(dp);
		if(fp != null)
		fp.setBitmap(panel.getBitmap());
				
		updateViewsInPanel();
		
		dragLayer.invalidate();
}
	


    @Override
	public boolean onTouch(View v, MotionEvent m) {
    	if(deleting){return true;}
    	//EVITAR Multitouch
    	if(m.getPointerCount() > 1) return true;
		if(!canDrag) {
			Matrix translate = new Matrix();
			translate.setTranslate(v.getLeft()-canvasSelectorWidth-noteSpiralWidth, v.getTop()-topBarHeight);
			m.transform(translate);
			return  fp.dispatchTouchEvent(m);
		}
    	if(m.getAction()==MotionEvent.ACTION_MOVE || m.getAction()==MotionEvent.ACTION_DOWN)
    		return startDrag(v);
    	return false;
	}
    
    boolean canDrag=true;
	void setDrag(boolean value)
	{
		canDrag = value;
	}
    
	public boolean startDrag (View v)
	{
		if (! (v instanceof ExtendedImageView)) return true;

		Object dragInfo = v;
		dragController.startDrag (v, dragLayer, dragInfo, DragController.DRAG_ACTION_MOVE);
		return true;
	}
	
	@Override
	public void onBackPressed() {
	}

	
	public void updateViewsInPanel()
	{
		int verticalOffset=topBarHeight;
		int horizontalOffset = canvasSelectorWidth+noteSpiralWidth;
		for(ViewWrapper w : panel.getWrappers()){
			View iv = w.getView(this);
			if(! (iv instanceof ExtendedImageView)) continue;
			if(!panel.getItems(getApplicationContext()).contains(iv)){
				panel.getItems(getApplicationContext()).add(iv);
			}
			
			int left=0, top=0;
			
			ExtendedImageView img = (ExtendedImageView) iv;
			img.setScaleX(1f);
			img.setScaleY(1f);
			
			//if(img.getEtapa() == EtapaEnum.EMPIRE) img.setBackgroundResource(R.drawable.borderojo);
			//else if(img.getEtapa() == EtapaEnum.INICIO) img.setBackgroundResource(R.drawable.bordeazul);
			//else if(img.getEtapa() == EtapaEnum.CONEY) img.setBackgroundResource(R.drawable.bordeverde);
			
				
			left = (int)(w.getX()*canvasWidth);
			top = (int)(w.getY()*canvasHeight);
			
			iv.setContentDescription("no");
			MyAbsoluteLayout.LayoutParams lp = new MyAbsoluteLayout.LayoutParams(objectSize,objectSize, (int)(horizontalOffset+left) , (int)(verticalOffset+top));
			if(iv.getParent() != null)((ViewGroup)iv.getParent()).removeView(iv);
			iv.setOnTouchListener(this); 
			iv.setVisibility(0);
			dragLayer.addView(iv, lp);
			items.remove(w);

		}
		
	}
	
	void removePanelItems()
	{
		for(ViewWrapper vw: panel.getWrappers())
		{
			View v = vw.getView(this);
			dragLayer.removeView(v);
			items.remove(vw);
		}
	}
		
	
	void updateItemsInDragLayer()
	{
		int left=0, top=0;
		for(ViewWrapper w : items){
			View iv = w.getView(this);
			if(! (iv instanceof ExtendedImageView)) continue;
			
			ExtendedImageView img = (ExtendedImageView) iv;
			img.setScaleX(1f);
			img.setScaleY(1f);
			
			//if(img.getEtapa() == EtapaEnum.EMPIRE) img.setBackgroundResource(R.drawable.borderojo);
			//else if(img.getEtapa() == EtapaEnum.INICIO) img.setBackgroundResource(R.drawable.bordeazul);
			//else if(img.getEtapa() == EtapaEnum.CONEY) img.setBackgroundResource(R.drawable.bordeverde);
			
			iv.setContentDescription("no");
			MyAbsoluteLayout.LayoutParams lp = new MyAbsoluteLayout.LayoutParams(objectSize,objectSize, (int)(canvasSelectorWidth+noteSpiralWidth+canvasWidth+left) , (int)(topBarHeight+top));
			if(iv.getParent() != null)((ViewGroup)iv.getParent()).removeView(iv);
			left = left==150 ? 0:150;
			top = left==150? top:objectSize+top;
			
			iv.setOnTouchListener(this); 
			iv.setVisibility(0);
			dragLayer.addView(iv, lp);

		}
	}
}
