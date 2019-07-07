package com.github.franckyi.cmpdl.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.Instant;
import java.util.List;

public interface IBean {

    default IBean fromJson(JSONObject json) throws Exception {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (json.has(field.getName())) {
                field.setAccessible(true);
                Object val = json.get(field.getName());
                if (val == JSONObject.NULL) {
                    if (field.getType().isPrimitive()) {
                        if (field.getType().equals(boolean.class)) {
                            val = false;
                        } else if (field.getType().equals(byte.class)) {
                            val = (byte) 0;
                        } else if (field.getType().equals(short.class)) {
                            val = (short) 0;
                        } else if (field.getType().equals(int.class)) {
                            val = 0;
                        } else if (field.getType().equals(long.class)) {
                            val = 0L;
                        } else if (field.getType().equals(float.class)) {
                            val = 0f;
                        } else if (field.getType().equals(double.class)) {
                            val = 0.0;
                        }
                        field.set(this, val);
                    } else {
                        field.set(this, null);
                    }
                } else if (val instanceof JSONObject && IBean.class.isAssignableFrom(field.getType())) {
                    field.set(this, ((IBean) field.getType().newInstance()).fromJson(((JSONObject) val)));
                } else if (val instanceof JSONArray && List.class.isAssignableFrom(field.getType())) {
                    ParameterizedType type = (ParameterizedType) field.getGenericType();
                    JSONArray array = (JSONArray) val;
                    field.set(this, CMPDLConverterFactory.toList(type, array));
                } else if (val instanceof Integer && Enum.class.isAssignableFrom(field.getType())) {
                    field.set(this, IEnum.fromJson(field.getType(), (int) val));
                } else if (val instanceof String && Instant.class.isAssignableFrom(field.getType())) {
                    field.set(this, Instant.parse((String) val));
                } else {
                    field.set(this, val);
                }
            }
        }
        return this;
    }

    default JSONObject toJson() {
        JSONObject json = new JSONObject();
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                Object val = field.get(this);
                if (val instanceof IBean) {
                    json.put(field.getName(), ((IBean) val).toJson());
                } else if (val instanceof List) {
                    List list = (List) val;
                    json.put(field.getName(), CMPDLConverterFactory.fromList(list));
                } else if (val instanceof IEnum) {
                    json.put(field.getName(), ((IEnum) val).toJson());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json;
    }


}
