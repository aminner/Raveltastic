package com.unravel.amanda.unravel.ravelryapi;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.unravel.amanda.unravel.ravelryapi.models.ColorFamily;
import com.unravel.amanda.unravel.ravelryapi.models.Paginator;
import com.unravel.amanda.unravel.ravelryapi.models.Pattern;
import com.unravel.amanda.unravel.ravelryapi.models.PatternFull;
import com.unravel.amanda.unravel.ravelryapi.response.RavelApiResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Amanda on 10/23/2015.
 */
public class RavelryDeserializer implements JsonDeserializer<RavelApiResponse> {
    private static final String TAG = "Deserializer";
    Gson _gson;

    public RavelryDeserializer()
    {
        _gson = new Gson();
    }
    @Override
    public RavelApiResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject obj = json.getAsJsonObject();
            Iterator iterator = obj.entrySet().iterator();
            Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator.next();

            List<Object> responses = new ArrayList<Object>();
            Paginator page = null; //= _gson.fromJson(entry.getValue(), Paginator.class);
            if (entry.getKey().equals("paginator"))
                page = _gson.fromJson(entry.getValue(), Paginator.class);
            else if (entry.getKey().equals("color_families"))
                responses = Arrays.asList((Object[]) _gson.fromJson(entry.getValue(), ColorFamily[].class));
            else if (entry.getKey().equals("pattern"))
                responses.add(_gson.fromJson(entry.getValue(), PatternFull.class));
            if (entry.getKey().equals("paginator")) {
                entry = (Map.Entry<String, JsonElement>) iterator.next();
                if (entry.getKey().equals("patterns"))
                    responses = Arrays.asList((Object[]) _gson.fromJson(entry.getValue(), Pattern[].class));
            }
            return new RavelApiResponse(page, responses);
        } catch(Exception ex){
            Log.d(TAG, Log.getStackTraceString(ex));

        }
        return null;
    }
}
