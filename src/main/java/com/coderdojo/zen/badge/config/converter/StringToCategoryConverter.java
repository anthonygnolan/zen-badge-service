package com.coderdojo.zen.badge.config.converter;

import com.coderdojo.zen.badge.model.Category;
import org.springframework.core.convert.converter.Converter;

public class StringToCategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String source) {
        try {
            return Category.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
