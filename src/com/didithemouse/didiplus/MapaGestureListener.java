package com.didithemouse.didiplus;

import com.didithemouse.didiplus.etapas.*;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class MapaGestureListener extends GestureDetector.SimpleOnGestureListener {

	ImageView mapa;
	LinearLayout popup;
	Context context;
	Button button;
	LinearLayout transition;

	public MapaGestureListener(Context _context, ImageView _surfaceView,
			LinearLayout _popup, MyAbsoluteLayout badges, Button _button,
			LinearLayout _transition) {
		context = _context;
		mapa = _surfaceView;
		popup = _popup;
		button = _button;
		transition = _transition;
		
		activityInt = new Intent[] {
				new Intent(context, ChinatownActivity.class),
				new Intent(context, ConeyActivity.class),
				new Intent(context, EmpireStateActivity.class)
		};
		
		resString = new int[] {
				R.string.chinatown,
				R.string.coney,
				R.string.empirestate
		};
		
		resPopup = new int[] {
				R.id.Chinatown_popup,
				R.id.coney_popup,
				R.id.empirestate_popup
		};
		flag = new boolean[] {
				ChinatownActivity.isVisited(),
				ConeyActivity.isVisited(),
				EmpireStateActivity.isVisited()
		};
		
		badge = new View[] {
				badges.getChildAt(0), // Estatua
				badges.getChildAt(1), // Chinatown
				badges.getChildAt(2)
			};
				
	}

	@Override
	public boolean onSingleTapUp(MotionEvent ev) {
		Log.d("onSingleTapUp", ev.toString());

		return true;
	}

	@Override
	public void onShowPress(MotionEvent ev) {
		Log.d("onShowPress", ev.toString());
	}

	@Override
	public void onLongPress(MotionEvent ev) {
		Log.d("onLongPress", ev.toString());
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// Log.d("onScroll",e1.toString());
		return true;
	}
	
	Intent activityInt[];
	int resString[];
	int resPopup[];
	boolean flag[];
	View badge[];

	@Override
	public boolean onDown(MotionEvent ev) {
		Log.d("onDown", ev.toString());

		//EVITAR Multitouch
    	if(ev.getPointerCount() > 1) return true;
		

		flag = new boolean[] {
				ChinatownActivity.isVisited(),
				ConeyActivity.isVisited(),
				EmpireStateActivity.isVisited()
		};
		
		for (int i = 0; i < activityInt.length; i++)
		if (isInside(ev, badge[i].getPaddingLeft(), badge[i].getPaddingTop())
				&& !flag[i]) 
		{
			button.setClickable(true);
			TextView tv = (TextView) popup.getChildAt(0);
			tv.setText(resString[i]);

			bringToFront(resPopup[i]);

			if (popup.getVisibility() != View.VISIBLE) {
				popup.setVisibility(View.VISIBLE);
				Animation slideDown = AnimationUtils.loadAnimation(context,
						R.animator.slide_down);
				popup.startAnimation(slideDown);
			}
			if (MochilaContents.numStages > MochilaContents.getInstance().getVisitedPlaces())
				button.setOnClickListener(new ButtonClickListener(activityInt[i]));
			else
			{
				button.getBackground().setColorFilter(0xC0808080,Mode.MULTIPLY);
				button.setClickable(false);
				button.setText("¡Ya fuiste a "+ MochilaContents.numStages+ "lugares!");
			}
			return true;
		}
		
		if (popup.getVisibility() != View.GONE) {
			button.setClickable(false);
			popup.setVisibility(View.GONE);
			Animation slideUp = AnimationUtils.loadAnimation(context,
					R.animator.slide_up);
			popup.startAnimation(slideUp);
			return true;
		}

		return true;
	}

	class ButtonClickListener implements View.OnClickListener
	{
		Intent i;
		public ButtonClickListener (Intent _i) { i = _i;}
		boolean flag = true;
		public void onClick(View v) {
			if(!flag) return;
			flag = false;
			popup.setVisibility(View.GONE);
			Animation slideUp = AnimationUtils.loadAnimation(context,
					R.animator.slide_up);
			popup.startAnimation(slideUp);
			context.startActivity(i);
		}
	
	}
	
	private boolean isInside(MotionEvent ev, float x, float y) {
		// Tama�o de los botones
		int size = 80;
		if (ev.getX() > x && ev.getX() < x + size && ev.getY() > y
				&& ev.getY() < y + size)
			return true;
		return false;
	}

	private void bringToFront(int image) {
		FrameLayout imagenes = (FrameLayout) popup.getChildAt(1);
		View actual = imagenes.findViewById(image);
		imagenes.removeView(actual);
		imagenes.addView(actual);

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return true;
	}
}
