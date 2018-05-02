package com.myapplication.graphic;

import android.content.Context;

import com.myapplication.vision.CameraController;

import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

    public static List<ModelTuple> loadModels(Context context, CameraController.GRAPHIC_TYPE[] types, String[] uris) {
        if (types.length != uris.length)
            throw new IllegalArgumentException("array of types and uris should have the same length.");

        List<ModelTuple> result = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            Model2D model2D = new Model2D(context, uris[i]);
            model2D.load();

            result.add(new ModelTuple(types[i], model2D));
        }

        return result;
    }

}
