package com.bpzzr.commonlibrary.net.factory;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


public class FastJsonConverterFactory extends Converter.Factory {
    private ParserConfig mParserConfig = ParserConfig.getGlobalInstance();
    private int featureValues = JSON.DEFAULT_PARSER_FEATURE;
    private Feature[] features;

    private SerializeConfig serializeConfig;
    private SerializerFeature[] serializerFeatures;

    private FastJsonConverterFactory() {
    }

    /**
     * Create an default instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     *
     * @return The instance of FastJsonConverterFactory
     */
    public static FastJsonConverterFactory create() {
        return new FastJsonConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            @NonNull Type type, @NonNull Annotation[] annotations,
            @NonNull Retrofit retrofit) {
        return new FastJsonResponseBodyConverter<>(type,
                mParserConfig, featureValues, features);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            @NonNull Type type, @NonNull Annotation[] parameterAnnotations,
            @NonNull Annotation[] methodAnnotations, @NonNull Retrofit retrofit) {
        return new FastJsonRequestBodyConverter<>(serializeConfig, serializerFeatures);
    }

    public ParserConfig getParserConfig() {
        return mParserConfig;
    }

    public FastJsonConverterFactory setParserConfig(ParserConfig config) {
        this.mParserConfig = config;
        return this;
    }

    public int getParserFeatureValues() {
        return featureValues;
    }

    public FastJsonConverterFactory setParserFeatureValues(int featureValues) {
        this.featureValues = featureValues;
        return this;
    }

    public Feature[] getParserFeatures() {
        return features;
    }

    public FastJsonConverterFactory setParserFeatures(Feature[] features) {
        this.features = features;
        return this;
    }

    public SerializeConfig getSerializeConfig() {
        return serializeConfig;
    }

    public FastJsonConverterFactory setSerializeConfig(SerializeConfig serializeConfig) {
        this.serializeConfig = serializeConfig;
        return this;
    }

    public SerializerFeature[] getSerializerFeatures() {
        return serializerFeatures;
    }

    public FastJsonConverterFactory setSerializerFeatures(SerializerFeature[] features) {
        this.serializerFeatures = features;
        return this;
    }
}
