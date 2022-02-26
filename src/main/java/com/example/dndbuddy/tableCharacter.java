package com.example.dndbuddy;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class tableCharacter {
    private final String name;
    private final String background;
    private final int level;
    private final String alignment;
    private final String race;
    private final String klass;
    //private final Array skills;
    private final String something;

    public tableCharacter(String [] cData) {
        this.name = cData[0];
        this.background = cData[1];
        this.level = parseInt(cData[2]);
        this.alignment = cData[3];
        this.race = cData[4];
        this.klass = cData[5];
        this.something = cData[6];
        //this.skills = cData[6];
    }

    public String getName() {
        return name;
    }

    public String getBackground() {
        return background;
    }

    public int getLevel() {
        return level;
    }

    public String getAlignment() {
        return alignment;
    }

    public String getRace() {
        return race;
    }

    public String getKlass() {
        return klass;
    }

    public String getSomething() {
        return something;
    }

//    public Array getSkills() {
//        return skills;
//    }



}
