package com.grappetite.zoya.restapis.webmaps;


import com.grappetite.zoya.dataclasses.SymptomData;
import com.grappetite.zoya.enums.Gender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetMaps {
    public static Map<String,String> symptoms(String mainBodyPart, String subBodyPart, Gender gender) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("bodypart",subBodyPart);
        map.put("category",mainBodyPart);
        map.put("gender",gender.toString());
        return map;
    }
    public static Map<String,String> condition(long conditionId) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("condition_id",String.valueOf(conditionId));
        return map;
    }
    public static Map<String,String> doctorTypesByCity(String city) {
        HashMap<String,String> map  = new HashMap<>();
        map.put("city",city);
        map.put("order_by","specialization");
        map.put("group_by","specialization");
        map.put("direction","asc");
        return map;
    }
    public static Map<String,String> conditions(ArrayList<SymptomData> symptoms,String mainBodyPart,String subBodyPart, Gender gender) {
        HashMap<String,String> map  = new HashMap<>();
        StringBuilder ids= null;
        for (SymptomData data : symptoms) {
            if (ids==null)
                ids = new StringBuilder(String.valueOf(data.getId()));
            else
                ids.append(",").append(data.getId());
        }
        map.put("symptom_ids",ids!=null?ids.toString():"");
        map.put("bodypart",subBodyPart);
        map.put("category",mainBodyPart);
        map.put("gender",gender.toString());
        return map;
    }
}
