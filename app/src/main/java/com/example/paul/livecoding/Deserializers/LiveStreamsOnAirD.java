package com.example.paul.livecoding.Deserializers;

import com.example.paul.livecoding.POJOs.LiveStreamsOnAirP;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

public class LiveStreamsOnAirD implements JsonDeserializer<List<LiveStreamsOnAirP>> {

    @Override
    public List<LiveStreamsOnAirP> deserialize(JsonElement json, Type listType, JsonDeserializationContext context) throws JsonParseException {

        return new Gson().fromJson(

                        json
                        .getAsJsonObject()
                        .get("results").getAsJsonArray(), listType);
    }
}
