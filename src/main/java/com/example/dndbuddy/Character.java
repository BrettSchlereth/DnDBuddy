package com.example.dndbuddy;

import java.lang.reflect.Array;
import java.util.Hashtable;

public class Character {
    String name = null;
    int level = 1;
    String alignment = null;
    String race = null;
    String Class = null;
    String background = null;
    Hashtable abilityScores = new Hashtable();

    public Character() {
        int [] as = {10, 10, 10, 10,10, 10};
        name = null;
        race = null;
        Class = null;
        background = null;
        abilityScores = addAbilityScores(as);
    }

    public Character(String n, int l, String a, String r, String c, String b, int [] as) {
        name = null;
        level = l;
        alignment = a;
        race = r;
        Class = c;
        background = b;
        abilityScores = addAbilityScores(as);
    }

    public Hashtable addAbilityScores(int [] as) {
        String [] abilities = {"Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma"};
        Hashtable asHash = new Hashtable();
        for (int i = 0; i < as.length; i++) {asHash.put(abilities[i], as[i]);}
        return asHash;
    }

    public void setItem(String category, String item) {
        switch (category) {
            case "Race":
                race = item;
                break;
            case "Class":
                Class = item;
                break;
            case "Background":
                background = item;
                break;
        }
    }

}
