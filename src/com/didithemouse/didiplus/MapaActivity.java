package com.didithemouse.didiplus;

import com.didithemouse.didiplus.etapas.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapaActivity extends Activity {

	private GestureDetector gd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.mapa);

		ImageView mapa = (ImageView) this.findViewById(R.id.NYC_map);
		mapa.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		LinearLayout popup = (LinearLayout) this
				.findViewById(R.id.locationPopUp);
		LinearLayout transition = (LinearLayout) this
				.findViewById(R.id.transition);
		Button goToPlace = (Button) findViewById(R.id.goToButton);

		setEndButton();

		gd = new GestureDetector(this.getApplicationContext(), new MapaGestureListener(this, mapa,
				popup,  (MyAbsoluteLayout) this
				.findViewById(R.id.badgeButtons),goToPlace, transition));
		
		
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setEndButton();
		setFiltro();
	}
	Button next;
	void setEndButton()
	{
		next = (Button) findViewById(R.id.volver);
		if( MochilaContents.getInstance().getVisitedPlaces() == MochilaContents.numStages
			  || MochilaContents.SKIP_MAP)
		{
			next.setVisibility(View.VISIBLE);
			next.setOnClickListener(new View.OnClickListener() {
				boolean flag = true;
				public void onClick(View v) {
					if(!flag) return;
					flag = false;
					Intent i = new Intent(v.getContext(), CreateActivity.class);
					LogX.i("Create","Ha comenzado Create.");
					Saver.savePresentation(Saver.ActivityEnum.MAPA);
					startActivity(i);
					finish();
				}
			});
		}
		else
		{
			next.setVisibility(View.GONE);
		}
	}
	
	void setFiltro()
	{
		int filtro = 0xC0808080;
		MyAbsoluteLayout badges = (MyAbsoluteLayout) this.findViewById(R.id.badgeButtons);
		TextView counter = (TextView) this.findViewById(R.id.timeLeft);

		if (ChinatownActivity.isVisited())
			((ImageView) badges.getChildAt(0)).setColorFilter(filtro);
		if (ConeyActivity.isVisited())
			((ImageView) badges.getChildAt(1)).setColorFilter(filtro);
		if (EmpireStateActivity.isVisited())
			((ImageView) badges.getChildAt(2)).setColorFilter(filtro);

		counter.setText(MochilaContents.getInstance().getVisitedPlaces() +" "+ getString(R.string.outof) +  " " + MochilaContents.numStages);

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		//EVITAR Multitouch
    	if(e.getPointerCount() > 1) return true;
		if (gd.onTouchEvent(e))
			return true;
		else
			return false;
	}
	
	@Override
	public void onBackPressed() {
	}

}
