package com.epam.esm.feignClient;

import com.google.gson.Gson;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FeignClientDecoderFromJsonToMap implements Decoder {

    private final Gson gson;

    public FeignClientDecoderFromJsonToMap(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.body() == null) {
            return null;
        }

        String json = Util.toString(response.body().asReader());
        Map<String, String> result = new HashMap<>();
        result = gson.fromJson(json, result.getClass());
        return result;
    }
}
