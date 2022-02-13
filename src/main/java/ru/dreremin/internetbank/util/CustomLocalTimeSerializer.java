package ru.dreremin.internetbank.util;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomLocalTimeSerializer extends StdSerializer<LocalTime> {

    protected CustomLocalTimeSerializer(Class<LocalTime> localTimeClass) {
        super(localTimeClass);
    }

    protected CustomLocalTimeSerializer() {
        this(null);
    }

    @Override
    public void serialize(LocalTime value,
                          JsonGenerator generator,
                          SerializerProvider provider) throws IOException {

        String localTime = value.format(DateTimeFormatter.ISO_LOCAL_TIME);
        generator.writeString(localTime);
    }
}
