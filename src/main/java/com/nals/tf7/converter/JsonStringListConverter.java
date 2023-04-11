package com.nals.tf7.converter;

import com.nals.tf7.helpers.JsonHelper;
import com.nals.tf7.helpers.StringHelper;
import org.springframework.util.CollectionUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.util.List;

@Converter
public class JsonStringListConverter
    implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(final List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        return JsonHelper.convertObjectToString(list);
    }

    @Override
    public List<String> convertToEntityAttribute(final String jsonStr) {
        if (StringHelper.isBlank(jsonStr)) {
            return null;
        }

        return JsonHelper.readValue(jsonStr, List.class);
    }
}
