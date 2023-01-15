package com.mygdx.game.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class BikeshedRoot {
    public boolean success;
    @JsonProperty("BikeSheds")
    public ArrayList<Bikeshed> bikeSheds;
}
