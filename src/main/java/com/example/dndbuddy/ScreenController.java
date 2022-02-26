package com.example.dndbuddy;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class ScreenController {
    public tableCharacter storedData = null;

    public ScreenController() {
        storedData = null;
    }

    public void sendData(tableCharacter data) {
        this.storedData = data;
    }

    public tableCharacter getStoredData() {
        return this.storedData;
    }
}
