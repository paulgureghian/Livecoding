package com.example.paul.livecoding.pojo_deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

public class LiveStreamsOnAirDeserializer implements JsonDeserializer<List<LiveStreamsOnAir>> {

    @Override
    public List<LiveStreamsOnAir> deserialize(JsonElement json, Type listType, JsonDeserializationContext context) throws JsonParseException {

        return new Gson().fromJson(

                        json
                        .getAsJsonObject()
                        .get("results").getAsJsonArray(), listType);
    }
}
