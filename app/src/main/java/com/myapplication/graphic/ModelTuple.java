package com.myapplication.graphic;

import com.myapplication.vision.CameraController;

public class ModelTuple {
    public CameraController.GRAPHIC_TYPE type;
    public Model2D model;

    public ModelTuple(CameraController.GRAPHIC_TYPE type, Model2D model) {
        this.type = type;
        this.model = model;
    }
}
