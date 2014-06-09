package com.didithemouse.didiplus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class FingerPaint extends SurfaceView implements SurfaceHolder.Callback {

	final static int canvasHeight = CreateActivity.canvasHeight;
	final static int canvasWidth = CreateActivity.canvasWidth;
	
    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;
    private DragLayer dl;
    
    private Paint       mPaint;
    
    private SurfaceHolder sh;
    
    private boolean canDrag = true;
    private boolean doNothing = false;

    public FingerPaint(Context c, DragLayer _dl) {
        super(c);

        dl = _dl;
        sh = getHolder();
        sh.addCallback(this);
        this.setZOrderOnTop(true);
        //this.setZOrderOnTop(false);
        sh.setFormat(PixelFormat.TRANSPARENT);

        //setCleaner();
       
        //Par�metros de la brocha
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//true);
        mPaint.setDither(true);//true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
                
        isPainting = true;
        
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mCanvas = new Canvas();
    }
    
    public void setBitmap(Bitmap bm)
    {
    	if (bm == null) return;
    	mBitmap=bm;
    	mCanvas.setBitmap(bm);
    	surfaceCreated(sh);
    }
    
    boolean isPainting = true;
    public void setDraw()
    {
        mPaint.setColor(0xFF000000);
    	mPaint.setXfermode(null);
        isPainting = true;
        canDrag=false;
        setSize(8);
    }

    public void setErase()
    {
    	mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
    	mPaint.setColor(0x00FFFFFF);
    	setSize(31);
        isPainting = false;
        canDrag=false;
    }
    
    public void setSize(int size)
    {
        mPaint.setStrokeWidth(size);
        canDrag=false;
    }
    
    public void eraseAll()
    {
    	if (runWhenChange != null) runWhenChange.run();
    	mCanvas.drawColor(0x00FFFFFF, Mode.MULTIPLY);
        mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        Canvas c = sh.lockCanvas();
        if (c != null)
        {
        	c.drawColor(0x00FFFFFF, Mode.MULTIPLY);
            c.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            sh.unlockCanvasAndPost(c);
        }
    }
    
    public void setDrag()
    {
        canDrag=true;
    }
    
    void setDoNothing(boolean b){
    	doNothing=b;
    }
    
    protected void drawx(Canvas canvas){
        if(canvas != null)
        {
    	canvas.drawColor(0x00FFFFFF, Mode.MULTIPLY);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
       // cleanButton(canvas);
        sh.unlockCanvasAndPost(canvas);
        }
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        mPaint.setStyle(Style.FILL);
        mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        mCanvas.drawCircle(x, y, mPaint.getStrokeWidth()/2, mPaint);
        Canvas c = sh.lockCanvas();
        if (c != null)
        {
        	c.drawColor(0x00FFFFFF, Mode.MULTIPLY);
            c.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            c.drawCircle(x, y, mPaint.getStrokeWidth()/2, mPaint);
            //cleanButton(c);
            sh.unlockCanvasAndPost(c);
        }
        mPaint.setStyle(Style.STROKE);
    }
    private void touch_move(float x, float y) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }


    Runnable runWhenChange = null;
    public void setRunWhenChangeRunnable(Runnable _r)
    {
    	runWhenChange = _r;
    }
    
    //Avoid infinite recursion (?)
    MotionEvent me = null;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	if (doNothing) return true;
    	//EVITAR Multitouch
    	if(event.getPointerCount() > 1) return true;
    	

    	
        if (canDrag) 
        {
        	if (me == event) return true;
        	me = event;
        	if (runWhenChange != null && (MotionEvent.ACTION_CANCEL == event.getAction() || MotionEvent.ACTION_UP == event.getAction()))
        		runWhenChange.run();
        	return dl.dispatchTouchEvent(event);
        }
    	
    	float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                drawx(sh.lockCanvas());
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = (x - mX) >= 0? x - mX : mX - x;
                float dy = (y - mY) >= 0? y - mY : mY - y;
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                touch_move(x, y);
                drawx(sh.lockCanvas());
                }
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                drawx(sh.lockCanvas());
            	if (runWhenChange != null) runWhenChange.run();
                break;
        }
        return true;
    }
    

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
    	Canvas c = sh.lockCanvas();
    	if (c != null)
    	{
    		c.drawColor(0x00FFFFFF, Mode.MULTIPLY);
            c.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
    		sh.unlockCanvasAndPost(c);
    	}
	}
	
	/*Bitmap buttonBitmap;
    private void cleanButton(Canvas c)
    {
    	int width = buttonBitmap.getWidth();
    	c.drawBitmap(buttonBitmap, canvasWidth-width , 0 ,null);
    }
    *//*
    private void setCleaner()
    {
    	Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.page_curl_cornerx);
    	int width = bm.getWidth();
    	int height = bm.getHeight();
    	float scaleWidth = 200f / width;
    	float scaleHeight = 100f / height;
    	// create a matrix for the manipulation
    	Matrix matrix = new Matrix();
    	// resize the bit map
    	matrix.postScale(scaleWidth, scaleHeight);
    	// recreate the new Bitmap
    	buttonBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    	bm.recycle();
    }*/
     

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
    
}
