package edu.hitsz.aircraftwar.game.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import edu.hitsz.aircraftwar.R;

public class BackgroundView extends View {
    Drawable mImage;

    public BackgroundView(Context context) {
        super(context);
    }

    public BackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BackgroundView,
                0, 0);
        try {
            mImage = a.getDrawable(R.styleable.BackgroundView_background_image);
            Log.d("BackgroundView", mImage.toString());
        } finally {
            a.recycle();
        }
    }

    public void setImage(Drawable image) {
        this.mImage = image;
        invalidate();
        requestLayout();
    }

    protected void onSizeChanged(){
        upperRect = new Rect(getLeft(), offset-getBottom(), getRight(), offset);
        lowerRect = new Rect(getLeft(), offset, getRight(), offset+getBottom());
    }

    int offset = 0;
    Rect upperRect;
    Rect lowerRect;

    public void tick(){
        offset = (offset+1)%(mImage.getIntrinsicHeight());
        this.invalidate();
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        this.onSizeChanged();
        Log.d("BackgroundView", "onDraw() is called");
        Log.d("BackgroundView", upperRect.toString());
        Log.d("BackgroundView", lowerRect.toString());
        mImage.setBounds(upperRect);
        mImage.draw(canvas);
        mImage.setBounds(lowerRect);
        mImage.draw(canvas);
    }

}
