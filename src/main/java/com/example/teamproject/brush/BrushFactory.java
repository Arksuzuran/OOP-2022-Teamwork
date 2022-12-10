package com.example.teamproject.brush;

import com.example.teamproject.layers.Layer;

public interface BrushFactory {
    Brush makeBrush(Layer activeLayer);
}
