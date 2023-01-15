package com.mygdx.game.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class StandRoot {
    public boolean success;
    @JsonProperty("Stands")
    public ArrayList<Stand> stands;
}
