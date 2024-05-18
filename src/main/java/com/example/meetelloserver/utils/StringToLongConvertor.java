package com.example.meetelloserver.utils;

import org.modelmapper.AbstractConverter;

public class StringToLongConvertor extends AbstractConverter<String , Long> {
    @Override
    protected Long convert(String source){
        return source != null ? Long.valueOf(source) : null;
    }
}
