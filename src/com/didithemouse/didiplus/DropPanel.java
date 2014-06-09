package com.didithemouse.didiplus;

import java.util.ArrayList;



import com.didithemouse.didiplus.CreateActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class DropPanel extends DragLayer {
	
	private ArrayList<View> items = new ArrayList<View>();
	private ArrayList<ViewWrapper> wrappers = new ArrayList<ViewWrapper>();
	private Bitmap drawLayer;
	
	public DropPanel(Context context,
			ArrayList<ViewWrapper> _wrappers, ArrayList<View> _items,
			Bitmap _drawLayer) {
        super(context, null);
        wrappers = _wrappers;
        items = _items;
        drawLayer = _drawLayer;
    } 

	
	@Override
	public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
		if (!(dragInfo instanceof ExtendedImageView)) return;
		
		ExtendedImageView v = (ExtendedImageView) dragInfo;
		boolean wasDisplayed = true;
		if(items.contains(v)){
			items.remove(v);
			ViewWrapper w = findWrapperByView(v);
			wasDisplayed = w.wasDisplayed();
			wrappers.remove(w);			
		}
		
		int width, height;
		MyAbsoluteLayout.LayoutParams lp = (MyAbsoluteLayout.LayoutParams)this.getLayoutParams();
		width = lp.width;
		height = lp.height;
		double porcentaje_left = (x-xOffset-lp.x)*1f/width;
		double porcentaje_top = (y-yOffset-lp.y)*1f/height;
		items.add(v);		
		ViewWrapper vw = new ViewWrapper(porcentaje_left, porcentaje_top, xOffset, yOffset,v,v.getEtapa());
		vw.setDisplayed(wasDisplayed);
		wrappers.add(vw);
	}
	
	public void updateObject(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
		if (!(dragInfo instanceof ExtendedImageView)) return;
		ExtendedImageView v = (ExtendedImageView) dragInfo;
		int width, height;
		width = ((DragLayer)source).getMeasuredWidth();
		height = ((DragLayer)source).getMeasuredHeight();
		double porcentaje_left = (x-xOffset)*1f/width;
		double porcentaje_top = (y-yOffset)*1f/height;
		items.add(v);		
		ViewWrapper vw = new ViewWrapper(porcentaje_left, porcentaje_top, xOffset, yOffset,v,v.getEtapa());
		wrappers.add(vw);
	}
	
	public static void waiting (int n){
        long t0, t1;
        t0 =  System.currentTimeMillis();
        do{
            t1 = System.currentTimeMillis();
        }
        while ((t1 - t0) < (n * 1000));
    }
	
	@Override
	public void onDragEnter(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
		// TODO Auto-generated method stub
		
		this.setBackgroundColor(0xC00080ff);
		
	}

	@Override
	public void onDragOver(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDragExit(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
		this.setBackgroundColor(Color.WHITE);
	
		View v = (View) dragInfo;		
		if(items.contains(v)){
			items.remove(v);
			ViewWrapper w = findWrapperByView(v);
			wrappers.remove(w);			
		}
	
		
	}
	private ViewWrapper findWrapperByView(View v) {
		
		for(ViewWrapper vw : wrappers){
			if(vw.getView(this.getContext()) == v){
				return vw;
			}
		}
		return null;
	}

	@Override
	public boolean acceptDrop(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
		return true;
	}
	@Override
	public Rect estimateDropLocation(DragSource source, int x, int y,
			int xOffset, int yOffset, DragView dragView, Object dragInfo,
			Rect recycle) {
		// TODO Auto-generated method stub
		return null;
	}

    private BitmapDrawable drawLayerDrawable = null;
    /** @return Bitmap muy chico para usarse en PowerPoint Activity */
    public Drawable getMiniThumbnail()
    {
        if (drawLayerDrawable == null)
                drawLayerDrawable = new BitmapDrawable(drawLayer);
        drawLayerDrawable.setBounds(0, 0, drawLayer.getHeight(), drawLayer.getWidth());
        return drawLayerDrawable;
    }
    
    /** @return Bitmap chico para usarse en CreateActivity (creo que el tama�o est� bien) */
    public Drawable getThumbnail()
    {
        if (drawLayerDrawable == null)
                drawLayerDrawable = new BitmapDrawable(drawLayer);
        drawLayerDrawable.setBounds(0, 0,CreateActivity.canvasWidth_small, CreateActivity.canvasHeight_small);
        return drawLayerDrawable;
    }
    
    public Drawable getMediumBitmap()
    {
        if (drawLayerDrawable == null)
                drawLayerDrawable = new BitmapDrawable(drawLayer);
        drawLayerDrawable.setBounds(0, 0, CreateActivity.canvasWidth_mid, CreateActivity.canvasHeight_mid);
        return drawLayerDrawable;
    }
    
    /** @return Bitmap grande para usarse en Presentation */
    public Drawable getBigBitmap()
    {
        if (drawLayerDrawable == null)
                drawLayerDrawable = new BitmapDrawable(drawLayer);
        drawLayerDrawable.setBounds(0, 0, drawLayer.getHeight(), drawLayer.getWidth());
        return drawLayerDrawable;
    }
    
}
