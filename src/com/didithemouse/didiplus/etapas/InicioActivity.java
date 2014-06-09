package com.didithemouse.didiplus.etapas;

import com.didithemouse.didiplus.DragController;
import com.didithemouse.didiplus.DragLayer;
import com.didithemouse.didiplus.DropTarget;
import com.didithemouse.didiplus.ExtendedImageView;
import com.didithemouse.didiplus.LogX;
import com.didithemouse.didiplus.MapaActivity;
import com.didithemouse.didiplus.Mochila;
import com.didithemouse.didiplus.MochilaContents;
import com.didithemouse.didiplus.R;
import com.didithemouse.didiplus.Saver;
import com.didithemouse.didiplus.MyAbsoluteLayout.LayoutParams;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class InicioActivity extends EtapaActivity implements
		View.OnTouchListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		title = getString(R.string.didielraton);
		setContentView(R.layout.inicio);
		mySurfaceView = (InicioSurfaceView) this
				.findViewById(R.id.surface_view);
		mySurfaceView.setVisibility(View.INVISIBLE);
		badgeDrawable = (R.drawable.badge_didi_small);
		etapa = EtapaEnum.INICIO;
		tutorialInicialization();
	}
	
	private void tutorialInicialization()
	{
		ImageView badgeX = (ImageView) findViewById(R.id.badge);
		badgeX.setVisibility(View.GONE);
		mochila = (Mochila) findViewById(R.id.backpack);
		mochilaView = (ImageView) findViewById(R.id.backpack);
		dragController = new DragController(this);
		dragLayer = (DragLayer) findViewById(R.id.drag_layer);
		
		mochila.setDropRunnable(new Runnable(){
			@Override
			public void run() {
				mochila.setDropRunnable(null);
				dragController.removeDropTarget(dragLayer);
				dragController.removeDropTarget((DropTarget)mochila);
				
				LogX.i("Read (" + title + ")","Ha completado el tutorial.");		
								
				PropertyValuesHolder finalA = PropertyValuesHolder.ofFloat("alpha", 0.20f);
				ObjectAnimator disappear_backpack = ObjectAnimator
						.ofPropertyValuesHolder(mochilaView, finalA).setDuration(2000);
				AnimatorSet move = new AnimatorSet();
				move.play(disappear_backpack);
				move.start();

				move.addListener(new AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {}
					@Override
					public void onAnimationRepeat(Animator animation) {	}
					@Override
					public void onAnimationEnd(Animator animation) {
						MochilaContents.getInstance().getItems().clear();
						genericInicialization();}
					@Override
					public void onAnimationCancel(Animator animation) {}
				});

				
				 }});

		//dropPanel = MochilaContents.getInstance().searchForPanel(title);
		//badge.setImageResource(badgeDrawable);
		/*
		if (dropPanel == null) {
			// primera vez que entra aquí
			dropPanel = MochilaContents.getInstance().getNewPanel();
			dropPanel.setTitle(title);
		}
*/
		ImageView backpack = (ImageView) findViewById(R.id.backpack_intro);
		backpack.setVisibility(View.GONE);

		
		ExtendedImageView badge_eim = new ExtendedImageView(getApplicationContext(),badgeDrawable,1, etapa);
		badge_eim.setImageResource(badgeDrawable);
		badge_eim.setContentDescription("no");
		badge_eim.setVisibility(ImageView.VISIBLE);
		dragLayer.addView(badge_eim);
		badge_eim.setOnTouchListener(this);
		
		dragLayer.updateViewLayout(badge_eim, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 390, 310));
		
		dragLayer.setDragController(dragController);
		dragController.addDropTarget(dragLayer);
				
		
		mochilaView.setContentDescription("no");
		dragController.addDropTarget((DropTarget) findViewById(R.id.backpack));

		DragLayer.LayoutParams lp1 = new LayoutParams(179, 205, 0, 540);
		dragLayer.updateViewLayout(mochilaView, lp1);

	}
	
	@Override
	protected void genericInicialization() {

		mySurfaceView.setActivity(this);
		mySurfaceView.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
        mySurfaceView.setVisibility(View.VISIBLE);
        
        LogX.i("Read (" + title + ")","Ha comenzado la etapa.");

		mochila = (Mochila) findViewById(R.id.backpack);
		
		mochila.setDropRunnable(new Runnable(){
			@Override
			public void run() {
				checkObjects(); }});

		inicializarBoton();
				
		/*** SETUP DE LAS VISTAS ***/
		setupViews();
		mochilaView.setAlpha(1f);
		mochilaView.setVisibility(View.VISIBLE);
		
	}
	
	@Override
	void setObjects() {
		//deben ser los tama�os originales / 1.85                                                        x,y
		posiciones[0] = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 688,  76); //Despertador
		posiciones[1] = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 923, -11); //Mapa
		posiciones[2] = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 385, 132); //Polera
	
		//PONER EN EL ORDEN ANTERIOR !!
		drawables = new int[] {R.drawable.inicio_despertador,
				   R.drawable.inicio_mapa,
				   R.drawable.inicio_polera
		};
	}
	
	public static boolean visitedFlag = false;
	@Override
	public void setVisited(){visitedFlag = true;}
	public static boolean isVisited(){return visitedFlag;}
	
	@Override
	protected void inicializarBoton() {
        volver = (ImageButton) findViewById(R.id.volver);
		volver.setOnClickListener(new View.OnClickListener() {
			boolean flag = true;
			public void onClick(View v) {
				if(!flag) return;
				flag = false;
				MochilaContents.getInstance().cleanPanels();
	        	Intent intent = new Intent(v.getContext().getApplicationContext(), MapaActivity.class);
	        	setVisited();
	        	Saver.savePresentation(Saver.ActivityEnum.ETAPA);
	        	startActivity(intent);
	        	finish();
			}
		});
	}
	@Override
	protected void setOverlay() {
		FrameLayout overlay = (FrameLayout) findViewById(R.id.overlay);
		overlay.setForeground(getResources().getDrawable(R.drawable.inicio_overlay));
	}
}
