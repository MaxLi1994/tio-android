package com.myapplication.graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import java.util.List;

public class GlassesGraphic extends GraphicOverlay.Graphic {
    private volatile Face face;
    private Model2D model;

    private Paint paint;

    private static final float GLASS_SIZE_SCALE = 3f;
    private static final float MINOR_MOVE_THRESHOLD = 30f;

    private PointF prevLeftEye, prevRightEye;

    public GlassesGraphic(GraphicOverlay overlay, Model2D model) {
        super(overlay);

        this.model = model;
        paint = new Paint();
    }

    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    @Override
    public void updateFace(Face face) {
        if(!preventMinorMoves(face)) {
            this.face = face;
            postInvalidate();
        }
    }

    public void updateModel(Model2D model) {
        this.model = model;
    }

    private boolean preventMinorMoves(Face face) {
        if(prevLeftEye == null || prevRightEye == null) return false;

        float totalOffset = 0;

        List<Landmark> landmarks = face.getLandmarks();
        for (Landmark l : landmarks) {
            if (l.getType() == Landmark.LEFT_EYE) {
                totalOffset += getDistance(l.getPosition(), prevLeftEye);
            }
            if (l.getType() == Landmark.RIGHT_EYE) {
                totalOffset += getDistance(l.getPosition(), prevRightEye);
            }
        }

        return totalOffset < MINOR_MOVE_THRESHOLD;
    }
    @Override
    public void draw(Canvas canvas) {
        if (model != null && model.isReady()) {
            PointF leftEye, rightEye;

            List<Landmark> landmarks = face.getLandmarks();
            for (Landmark l : landmarks) {
                if (l.getType() == Landmark.LEFT_EYE) {
                    prevLeftEye = l.getPosition();
                }
                if (l.getType() == Landmark.RIGHT_EYE) {
                    prevRightEye = l.getPosition();
                }
            }

            leftEye = prevLeftEye;
            rightEye = prevRightEye;

            if (null == leftEye || null == rightEye) return;

            Bitmap bitmap = model.getImage();
            Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            PointF centerPoint = new PointF(translateX(leftEye.x + (rightEye.x - leftEye.x) / 2), translateY(leftEye.y + (rightEye.y - leftEye.y) / 2));
            double rotateDegree = -Math.atan2((double) (leftEye.y - rightEye.y), (double)(leftEye.x - rightEye.x)) / Math.PI * 180;
            float realWidth = getDistance(leftEye, rightEye) * GLASS_SIZE_SCALE;
            float realHeight = realWidth / bitmap.getWidth() * bitmap.getHeight();
            realWidth = scaleX(realWidth);
            realHeight = scaleY(realHeight);

            RectF destRect = new RectF(centerPoint.x - realWidth / 2, centerPoint.y - realHeight / 2, centerPoint.x + realWidth / 2, centerPoint.y + realHeight / 2);

            canvas.save();
            canvas.translate(centerPoint.x, centerPoint.y);
            canvas.rotate((float) rotateDegree);
            canvas.translate(-centerPoint.x, -centerPoint.y);
            canvas.drawBitmap(bitmap, srcRect, destRect, paint);
            canvas.restore();
        }
    }

    private float getDistance(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow((double) p1.x - (double) p2.x, 2) + Math.pow((double) p1.y - (double) p2.y, 2));
    }

    @Override
    public void destroy() {
//        model.destroy();
    }
}
