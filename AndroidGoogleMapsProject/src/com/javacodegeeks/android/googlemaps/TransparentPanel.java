package com.javacodegeeks.android.googlemaps;

//http://blog.pocketjourney.com/2008/03/15/tutorial-1-transparent-panel-linear-layout-on-mapview-google-map/

// Checkable menu items ?
// Dynamically adding menu intents ?

//TO DO:
//button-size  & style is not perfect, how can we solve it?
//button-focus after push




import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TransparentPanel extends LinearLayout 
{ 
	private Paint	innerPaint, borderPaint ;
    
	public TransparentPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TransparentPanel(Context context) {
		super(context);
		init();
	}

	private void init() {
		innerPaint = new Paint();
		innerPaint.setARGB(225, 75, 75, 75); //gray
		innerPaint.setAntiAlias(true);

		borderPaint = new Paint();
		borderPaint.setARGB(255, 255, 255, 255);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);
	}
	
	public void setInnerPaint(Paint innerPaint) {
		this.innerPaint = innerPaint;
	}

	public void setBorderPaint(Paint borderPaint) {
		this.borderPaint = borderPaint;
	}


    @Override
    protected void dispatchDraw(Canvas canvas) {
    	
    	RectF drawRect = new RectF();
    	drawRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight()); //width and height of the retangle

    	canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
		canvas.drawRoundRect(drawRect, 5, 5, borderPaint);
	
		super.dispatchDraw(canvas);
    }
    
    
}