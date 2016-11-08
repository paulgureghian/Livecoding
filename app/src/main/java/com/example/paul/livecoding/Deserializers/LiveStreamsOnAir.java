package com.example.paul.livecoding.Deserializers;

import com.example.paul.livecoding.POJOs.LiveStreams_OnAir;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

public class LiveStreamsOnAir implements JsonDeserializer<List<LiveStreams_OnAir>> {

    @Override
    public List<LiveStreams_OnAir> deserialize(JsonElement json, Type listType, JsonDeserializationContext context) throws JsonParseException {

        return new Gson().fromJson(

                        json
                        .getAsJsonObject()
                        .get("results").getAsJsonArray(), listType);
    }
}
