package com.myapplication.vision;

import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.myapplication.graphic.GraphicOverlay;

public class GraphicFaceTracker extends Tracker<Face> {
    private GraphicOverlay mOverlay;
    private GraphicOverlay.Graphic graphic;

    public GraphicFaceTracker(GraphicOverlay overlay, GraphicOverlay.Graphic graphic) {
        mOverlay = overlay;
        this.graphic = graphic;
    }

    /**
     * Update the position/characteristics of the face within the overlay.
     */
    @Override
    public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
        mOverlay.add(graphic);
        graphic.updateFace(face);
    }

    /**
     * Hide the graphic when the corresponding face was not detected.  This can happen for
     * intermediate frames temporarily (e.g., if the face was momentarily blocked from
     * view).
     */
    @Override
    public void onMissing(FaceDetector.Detections<Face> detectionResults) {
        mOverlay.remove(graphic);
    }

    /**
     * Called when the face is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    @Override
    public void onDone() {
        mOverlay.remove(graphic);
    }

    public void updateGraphic(GraphicOverlay.Graphic g) {
        mOverlay.remove(graphic);
        this.graphic = g;
        mOverlay.add(this.graphic);
    }
}
