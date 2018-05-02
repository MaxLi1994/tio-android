package com.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication.graphic.ModelLoader;
import com.myapplication.graphic.ModelTuple;
import com.myapplication.vision.CameraController;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private CameraController cameraController;
    private List<ModelTuple> modelTupleList;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraController = new CameraController(this);

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            cameraController.createCameraSource();
        } else {
            cameraController.requestCameraPermission();
        }


        Button button = findViewById(R.id.button);
        button.setOnClickListener((view) -> {
            int index = (int) (Math.random() * modelTupleList.size());
            cameraController.showModel(modelTupleList.get(index).type, modelTupleList.get(index).model);
        });

        int[] glassesID = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        CameraController.GRAPHIC_TYPE[] types = new CameraController.GRAPHIC_TYPE[glassesID.length];
        Arrays.fill(types, CameraController.GRAPHIC_TYPE.GLASSES);
        modelTupleList = ModelLoader.loadModels(this.getApplicationContext(), types, Arrays.stream(glassesID).mapToObj(i -> "https://s3.us-east-2.amazonaws.com/ibed/g/" + i + ".png").toArray(String[]::new));


    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        cameraController.startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        cameraController.pause();
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraController.destroy();
    }
}
