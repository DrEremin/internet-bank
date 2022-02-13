package ru.dreremin.internetbank.util;

import java.io.IOException;
import java.time.ZoneId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomZoneIdSerializer extends StdSerializer<ZoneId> {

    protected CustomZoneIdSerializer(Class<ZoneId> zoneIdClass) {
        super(zoneIdClass);
    }

    protected CustomZoneIdSerializer() {
        this(null);
    }

    @Override
    public void serialize(ZoneId value,
                          JsonGenerator generator,
                          SerializerProvider provider) throws IOException {
        generator.writeString(value.toString());
    }
}
