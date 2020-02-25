package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Person;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class PersonDeserializer implements Deserializer<Person> {

    @Override
    public Person deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        Person person;
        try {
            person = objectMapper.readValue(data, Person.class);
            return  person;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
