package com.myapplication.graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import java.util.List;

public class BlusherGraphic extends GraphicOverlay.Graphic {
    private volatile Face face;
    private Model2D model;

    private Paint paint;

    private static final float BLUSHER_SIZE_SCALE = 0.2f;
    private static final float CHEEK_DISTANCE_BASE = 500f;
    private static final float CHEEK_OFFSET_X = -30f;
    private static final float CHEEK_OFFSET_Y = -20f;
    private static final float MINOR_MOVE_THRESHOLD = 50f;

    private PointF prevLeftCheek, prevRightCheek;

    public BlusherGraphic(GraphicOverlay overlay, Model2D model) {
        super(overlay);

        this.model = model;
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    @Override
    public void updateFace(Face face) {
        if (!preventMinorMoves(face)) {
            this.face = face;
            postInvalidate();
        }
    }

    public void updateModel(Model2D model) {
        this.model = model;
    }

    private boolean preventMinorMoves(Face face) {
        if (prevLeftCheek == null || prevRightCheek == null) return false;

        float totalOffset = 0;

        List<Landmark> landmarks = face.getLandmarks();
        for (Landmark l : landmarks) {
            if (l.getType() == Landmark.LEFT_EYE) {
                totalOffset += getDistance(l.getPosition(), prevLeftCheek);
            }
            if (l.getType() == Landmark.RIGHT_EYE) {
                totalOffset += getDistance(l.getPosition(), prevRightCheek);
            }
        }

        return totalOffset < MINOR_MOVE_THRESHOLD;
    }

    @Override
    public void draw(Canvas canvas) {
        if (model != null && model.isReady()) {
            PointF leftCheek, rightCheek;

            List<Landmark> landmarks = face.getLandmarks();
            for (Landmark l : landmarks) {
                if (l.getType() == Landmark.LEFT_CHEEK) {
                    prevLeftCheek = l.getPosition();
                }
                if (l.getType() == Landmark.RIGHT_CHEEK) {
                    prevRightCheek = l.getPosition();
                }
            }

            leftCheek = prevLeftCheek;
            rightCheek = prevRightCheek;

            if (null == leftCheek || null == rightCheek) return;

            Bitmap bitmap = model.getImage();
            Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            float realWidth = scaleX(bitmap.getWidth() * BLUSHER_SIZE_SCALE);
            float realHeight = scaleY(bitmap.getHeight() * BLUSHER_SIZE_SCALE);

            PointF leftCenterPoint = new PointF(translateX(leftCheek.x - CHEEK_OFFSET_X), translateY(leftCheek.y + CHEEK_OFFSET_Y));
            PointF rightCenterPoint = new PointF(translateX(rightCheek.x + CHEEK_OFFSET_X), translateY(rightCheek.y + CHEEK_OFFSET_Y));
            float distance = getDistance(leftCenterPoint, rightCenterPoint);
            realWidth = realWidth * (distance / CHEEK_DISTANCE_BASE);
            realHeight = realHeight * (distance / CHEEK_DISTANCE_BASE);

            RectF leftDestRect = new RectF(leftCenterPoint.x - realWidth / 2, leftCenterPoint.y - realHeight / 2, leftCenterPoint.x + realWidth / 2, leftCenterPoint.y + realHeight / 2);
            RectF rightDestRect = new RectF(rightCenterPoint.x - realWidth / 2, rightCenterPoint.y - realHeight / 2, rightCenterPoint.x + realWidth / 2, rightCenterPoint.y + realHeight / 2);

            canvas.drawBitmap(bitmap, srcRect, leftDestRect, paint);

            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            canvas.drawBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), srcRect, rightDestRect, paint);


//            canvas.drawCircle(leftCenterPoint.x, leftCenterPoint.y, 10f, paint);
//            canvas.drawCircle(rightCenterPoint.x, rightCenterPoint.y, 10f, paint);
        }
    }

    private float getDistance(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow((double) p1.x - (double) p2.x, 2) + Math.pow((double) p1.y - (double) p2.y, 2));
    }

    @Override
    public void destroy() {

    }
}
