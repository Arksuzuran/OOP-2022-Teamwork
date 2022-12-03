package com.example.teamproject.layers;

import javafx.scene.canvas.Canvas;

public interface LayerFactory {

    Layer CreateLayer(Canvas canvas);
}
