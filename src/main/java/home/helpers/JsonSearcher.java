package home.helpers;

import com.google.gson.*;
import java.util.ArrayList;
import java.util.List;

public class JsonSearcher {



    public List<String> findElementsByName(String text,String name) {
        List<String> req = new ArrayList<>();
        try{
            com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
            JsonElement jsonElement = parser.parse(text);
            if (jsonElement.isJsonObject()) {
                if (jsonElement.getAsJsonObject().get(name) != null) {
                    req.add(jsonElement.getAsJsonObject().get(name).getAsString());
                }
            }
            else if (jsonElement.isJsonArray()) {
                JsonArray array = jsonElement.getAsJsonArray();
                for (JsonElement element: array) {
                    for (String s : findElement(element, name))
                        req.add(s);
                }
            }
        }catch (Exception e){
            throw e;
        }
        return req;
    }

    private List<String> findElement(JsonElement jsonElement,String name) {
        List<String> req = new ArrayList<>();
        try{
            if (jsonElement.isJsonObject()) {
                if (jsonElement.getAsJsonObject().get(name) != null) {
                    req.add(jsonElement.getAsJsonObject().get(name).getAsString());
                }
            }
            else if (jsonElement.isJsonArray()){
                JsonArray array = jsonElement.getAsJsonArray();
                for (JsonElement element: array) {
                    for (String s : findElement(element, name))
                        req.add(s);
                }
            }
        }catch (Exception e){
            throw e;
        }
        return req;
    }

 }
