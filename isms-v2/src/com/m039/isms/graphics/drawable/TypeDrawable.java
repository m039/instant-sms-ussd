/** TypeDrawable.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

import com.m039.mqst.R;

/**
 *
 *
 * Created: 12/05/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class TypeDrawable extends Drawable {

    Paint mLinePaint;
    Paint mBackgroundPaint;
    Path mPath;

    static class RectDrawable {
        float outerR[] = new float[8];
        ShapeDrawable shape = new ShapeDrawable(new RoundRectShape(outerR, null, null));
    }

    RectDrawable mRectOuter = new RectDrawable();
    RectDrawable mRectInner = new RectDrawable();

    float mConerSize;
    float mLineSize;

    public TypeDrawable() {
    }

    public TypeDrawable(Context ctx) {
        Resources res = ctx.getResources();

        setConerSize(res.getDimensionPixelSize(R.dimen.type_drawable__coner_size));
        setLineSize(res.getDimensionPixelSize(R.dimen.type_drawable__line_size));
        setBackgroundColor(res.getColor(R.color.type_drawable__background));
        setLineColor(res.getColor(R.color.type_drawable__line));
    }

    public void setConerSize(float conerSize) {
        mConerSize = conerSize;

        mRectOuter.outerR[0] = mConerSize;
        mRectOuter.outerR[1] = mConerSize;
        mRectOuter.outerR[2] = mConerSize;
        mRectOuter.outerR[3] = mConerSize;
        mRectOuter.shape.invalidateSelf();

        mRectInner.outerR[0] = mConerSize;
        mRectInner.outerR[1] = mConerSize;
        mRectInner.outerR[2] = mConerSize;
        mRectInner.outerR[3] = mConerSize;
        mRectInner.shape.invalidateSelf();

        invalidateSelf();
    }

    public void setLineSize(float lineSize) {
        mLineSize = lineSize;
        invalidateSelf();
    }

    public void setLineColor(int color) {
        mRectOuter.shape.getPaint().setColor(color);
        mRectOuter.shape.invalidateSelf();

        invalidateSelf();
    }

    public void setBackgroundColor(int color) {
        mRectInner.shape.getPaint().setColor(color);
        mRectInner.shape.invalidateSelf();

        invalidateSelf();
    }

    @Override
    public boolean getPadding (Rect padding) {
        padding.left = (int) mLineSize * 2;
        padding.top = (int) mLineSize;
        padding.right = (int) mLineSize * 2;
        padding.bottom = (int) mLineSize;

        return true;
    }


    Rect rect = new Rect();

    @Override
    public void draw(Canvas canvas) {
        copyBounds(rect);

        mRectOuter.shape.setBounds(rect);

        rect.left += mLineSize;
        rect.top += mLineSize;
        rect.right -=  mLineSize;

        mRectInner.shape.setBounds(rect);

        mRectOuter.shape.invalidateSelf();
        mRectInner.shape.invalidateSelf();

        mRectOuter.shape.draw(canvas);
        mRectInner.shape.draw(canvas);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE ;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

} // TypeDrawable
