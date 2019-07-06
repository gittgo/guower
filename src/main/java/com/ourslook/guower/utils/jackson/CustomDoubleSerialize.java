package com.ourslook.guower.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @author dazer
 * jackson 格式化货币
 *
 * @see CustomPhotoImageSerialize
 */
public class CustomDoubleSerialize extends JsonSerializer<Number> {
    /**
     * 1:数字保留4位小数 new DecimalFormat("##.####")
     * 2:金钱数字保留4位小数且以“￥”开头 new DecimalFormat("$##.####")
     * 3:金钱数字保留4位小数且三位三位的隔开 new DecimalFormat("#,###.####")
     * 4：保留两位小数 ##.00，末尾是0也会显示成两位小数，但是首位是0不显示0
     * 5：保留两位小数 0.00，末尾是0也会显示成两位小数，首位是0也会显示0
     */
    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void serialize(Number aDouble, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeString(df.format(aDouble));
    }
}