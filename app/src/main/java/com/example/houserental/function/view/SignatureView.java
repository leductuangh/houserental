package com.example.houserental.function.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.houserental.R;

public class SignatureView extends View {

    private static float STROKE_WIDTH = 3f;

    /**
     * Need to track this so the dirty region can accommodate the stroke.
     **/
    private static float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private final RectF dirtyRect = new RectF();
    private Paint paint = new Paint();
    private Path path = new Path();
    private boolean isDrawed = false;
    private boolean drawEnable = true;
    private LockableScrollView.IBlockScrollEvent iBlockScrollEvent;
    private Bitmap mBitmap;
    /**
     * Optimizes painting by invalidating the smallest possible area.
     */
    private float lastTouchX;
    private float lastTouchY;

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);

        STROKE_WIDTH = context.getResources().getDimensionPixelSize(R.dimen.signature_point_min_size);
        HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // init view from bitmap
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, paint);
            mBitmap.recycle();
            mBitmap = null;
        } else {
            canvas.drawPath(path, paint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP)
            performClick();

        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                if (iBlockScrollEvent != null)
                    iBlockScrollEvent.changeBlock(false);
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:

                if (!drawEnable) {
                    return true;
                }

                // Start tracking the dirty region.
                resetDirtyRect(eventX, eventY);

                // When the hardware tracks events faster than they are delivered,
                // the
                // event will contain a history of those skipped points.
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                    path.lineTo(historicalX, historicalY);
                }

                // After replaying history, connect the line to the touch point.
                path.lineTo(eventX, eventY);
                isDrawed = true;
                break;
            default:
                if (iBlockScrollEvent != null)
                    iBlockScrollEvent.changeBlock(true);
                return false;
        }

        // Include half the stroke width to avoid clipping.
        invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH), (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                (int) (dirtyRect.right + HALF_STROKE_WIDTH), (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

        lastTouchX = eventX;
        lastTouchY = eventY;
        if (iBlockScrollEvent != null)
            iBlockScrollEvent.changeBlock(false);
        return true;
    }

    /**
     * Called when replaying history to ensure the dirty region includes all points.
     */
    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    /**
     * Resets the dirty region when the motion event occurs.
     */
    private void resetDirtyRect(float eventX, float eventY) {

        // The lastTouchX and lastTouchY were set when the ACTION_DOWN
        // motion event occurred.
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }

    /**
     * Erases the signature.
     */
    public void clearSignature() {
        path.reset();
        // Repaints the entire view.
        invalidate();
        isDrawed = false;
    }

    /**
     * check is signed or not
     *
     * @return
     */
    public boolean isDrawed() {
        return isDrawed;
    }

    public void setBlockScrollEvent(LockableScrollView.IBlockScrollEvent iBlockScrollEvent) {
        this.iBlockScrollEvent = iBlockScrollEvent;
    }

    public void setDrawEnable(boolean drawEnable) {
        this.drawEnable = drawEnable;
    }

    public void rotateScreen() {
    }

    public void initView(Bitmap bitmap) {
        mBitmap = bitmap;
        clearSignature();
    }

    public void initView(Path path) {
        this.path = path;
        invalidate();
    }

    public Path getPath() {
        return path;
    }

    public void scale(float scaleX, float scaleY) {

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);
//		matrix.postScale(scaleX, scaleY);
        path.transform(matrix);
        postInvalidate();
    }

}
