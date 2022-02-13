package ru.dreremin.internetbank.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomLocalDateSerializer extends StdSerializer<LocalDate> {

    protected CustomLocalDateSerializer(Class<LocalDate> localDateClass) {
        super(localDateClass);
    }

    protected CustomLocalDateSerializer() {
        this(null);
    }


    @Override
    public void serialize(LocalDate value,
                          JsonGenerator generator,
                          SerializerProvider provider) throws IOException {

        String localDate = value.format(DateTimeFormatter.ISO_LOCAL_DATE);
        generator.writeString(localDate);
    }
}
