package com.example.android.multitouch;

import java.util.HashMap;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;
import android.view.MotionEvent;

public class MultitouchView extends View
{
    private HashMap<String,PointF> points = new HashMap<String,PointF>();
    private int[] colorArray = {
    		Color.YELLOW,
    		Color.RED,
    		Color.GREEN,
    		Color.BLUE,
    		Color.CYAN,
    		Color.MAGENTA,
    		Color.argb(255, 255, 64, 0),	// Orange
    		Color.argb(255, 128, 0, 128),	// Purple
    		Color.argb(255, 165, 42, 42),	// Brown
    		Color.WHITE,
    		Color.GRAY
    };

	public MultitouchView(Context context)
	{
		super(context);
		setBackgroundColor( Color.BLACK );
		setFocusable( true );
	}

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        int count = event.getPointerCount();
        //int index=(action&MotionEvent.ACTION_POINTER_ID_MASK)>>
        //    MotionEvent.ACTION_POINTER_ID_SHIFT;//API Level 5-7
        int index = event.getActionIndex();//API Level 8+

        switch ( action & MotionEvent.ACTION_MASK )
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                points.put( "" + event.getPointerId(index),
                			new PointF( event.getX(), event.getY() ));
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                points.remove( "" + event.getPointerId(index) );
                break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < count; i++)
                {
                     PointF pos = points.get( "" + event.getPointerId(i) );
                     pos.x = event.getX(i);
                     pos.y = event.getY(i);
                }
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth( 3.0f );

        Object[] keys = points.keySet().toArray();
        for (int i = 0; i < keys.length; i++ )
        {
            int colorIndex = i % 11;

            PointF pos = (PointF)points.get( keys[i] );

            paint.setColor( colorArray[colorIndex] );
            paint.setStyle( Paint.Style.STROKE );
            canvas.drawLine( pos.x, 0, pos.x, canvas.getHeight(), paint );
            canvas.drawLine( 0, pos.y, canvas.getWidth(), pos.y, paint );

            paint.setColor( colorArray[colorIndex] );
            paint.setStyle( Paint.Style.FILL );
            canvas.drawCircle( pos.x, pos.y, 15, paint );
        }

    }
}
