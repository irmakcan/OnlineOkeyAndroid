package com.irmakcan.android.okey.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.model.Tile;

public class ModelDeserializer {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public static class PositionDeserializer implements JsonDeserializer<Position>{
		@Override
		public Position deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			return Position.fromString(json.getAsString());
		}
	}
	public static class TileDeserializer implements JsonDeserializer<Tile>{
		@Override
		public Tile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			return Tile.fromString(json.getAsString());
		}
	}
}
