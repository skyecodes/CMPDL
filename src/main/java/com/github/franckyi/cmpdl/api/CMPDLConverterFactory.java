package com.github.franckyi.cmpdl.api;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CMPDLConverterFactory extends Converter.Factory {

    private CMPDLConverterFactory() {
    }

    public static CMPDLConverterFactory create() {
        return new CMPDLConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return value -> {
            try {
                if (type instanceof Class) {
                    Class<?> clazz = (Class<?>) type;
                    if (clazz == Instant.class) {
                        return Instant.parse(value.string());
                    } else if (IBean.class.isAssignableFrom(clazz)) {
                        return ((IBean) clazz.newInstance()).fromJson(new JSONObject(value.string()));
                    }
                } else if (type instanceof ParameterizedType) {
                    ParameterizedType type0 = (ParameterizedType) type;
                    if (List.class.isAssignableFrom((Class<?>) type0.getRawType())) {
                        return toList(type0, new JSONArray(value.string()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return value -> {
            try {
                if (value instanceof List) {
                    return new JSONBody(fromList(((ParameterizedType) type), (List) value));
                } else if (value instanceof IBean) {
                    return new JSONBody(((IBean) value).toJson());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public static List toList(ParameterizedType type, JSONArray array) throws Exception {
        Class<?> type0 = (Class<?>) getParameterUpperBound(0, type);
        List<Object> list = new ArrayList<>();
        if (IBean.class.isAssignableFrom(type0)) {
            for (int i = 0; i < array.length(); i++) {
                list.add(((IBean) type0.newInstance()).fromJson(array.getJSONObject(i)));
            }
        } else {
            list.addAll(array.toList());
        }
        return list;
    }

    public static JSONArray fromList(List<?> list) throws Exception {
        Type[] types = CMPDLConverterFactory.class.getDeclaredMethod("fromList", List.class).getGenericParameterTypes();
        ParameterizedType type = (ParameterizedType) types[0];
        return fromList(type, list);
    }

    public static JSONArray fromList(ParameterizedType type, List<?> list) {
        Class<?> clazz = (Class<?>) type.getActualTypeArguments()[0];
        return fromList(clazz, list);
    }

    public static JSONArray fromList(Class<?> clazz, List<?> list) {
        JSONArray array = new JSONArray();
        if (IBean.class.isAssignableFrom(clazz)) {
            list.stream()
                .map(IBean.class::cast)
                .map(IBean::toJson)
                .forEach(array::put);
        } else {
            array.put(list);
        }
        return array;
    }

    private class JSONBody extends RequestBody {
        private final Object json;

        public JSONBody(JSONArray json) {
            this.json = json;
        }

        public JSONBody(JSONObject json) {
            this.json = json;
        }

        @Override
        public MediaType contentType() {
            return MediaType.get("application/json");
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            sink.writeUtf8(json.toString());
        }
    }
}
