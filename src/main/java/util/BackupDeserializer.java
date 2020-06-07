package util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entity.Person;
import entity.PlayerBackup;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class BackupDeserializer implements Deserializer<PlayerBackup> {

    @Override
    public PlayerBackup deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat( new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        PlayerBackup backup;
        try {
            backup = objectMapper.readValue(data, PlayerBackup.class);
            backup.setDate(LocalDate.now());
            return  backup;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
