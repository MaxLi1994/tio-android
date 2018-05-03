package com.myapplication.vision;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;
import com.myapplication.R;
import com.myapplication.graphic.BlusherGraphic;
import com.myapplication.graphic.CameraSourcePreview;
import com.myapplication.graphic.GlassesGraphic;
import com.myapplication.graphic.GraphicOverlay;
import com.myapplication.graphic.Model2D;

import java.io.IOException;

public class CameraController {

    private static final String TAG = "FaceTracker";

    private CameraSource mCameraSource = null;

    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;

    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private Activity activity;

    private GraphicOverlay.Graphic currentGraphic, glassesGraphic, blusherGraphic;
    private GraphicFaceTracker faceTracker;

    public enum GRAPHIC_TYPE {
        GLASSES,
        BLUSHER
    }

    public CameraController(Activity activity) {
        this.activity = activity;

        mPreview = activity.findViewById(R.id.preview);
        mGraphicOverlay = activity.findViewById(R.id.faceOverlay);

        glassesGraphic = new GlassesGraphic(mGraphicOverlay, null);
        blusherGraphic = new BlusherGraphic(mGraphicOverlay, null);
        currentGraphic = glassesGraphic;
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    public void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = activity;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     */
    public void createCameraSource() {

        Context context = activity.getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setProminentFaceOnly(true)
                .setClassificationType(FaceDetector.NO_CLASSIFICATIONS)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        faceTracker = new GraphicFaceTracker(mGraphicOverlay, currentGraphic);

        detector.setProcessor(
                new LargestFaceFocusingProcessor.Builder(detector, faceTracker).build());

        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
//                .setRequestedPreviewSize(320, 240)
                .setRequestedPreviewSize(1080, 1776)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
    }


    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    public void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                activity.getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(activity, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }


    public void pause() {
        mPreview.stop();
    }

    public void destroy() {
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    public void showModel(GRAPHIC_TYPE type, Model2D model) {
        GraphicOverlay.Graphic newGraphic = null;
        switch (type) {
            case BLUSHER:newGraphic = blusherGraphic;break;
            case GLASSES:newGraphic = glassesGraphic;break;
        }

        if(!newGraphic.equals(currentGraphic)) {
            faceTracker.updateGraphic(newGraphic);
            currentGraphic = newGraphic;
        }

        currentGraphic.updateModel(model);
    }

}
