package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Person;
import org.apache.kafka.common.serialization.Serializer;

public class PersonSerializer implements Serializer<Person> {

    @Override
    public byte[] serialize(String topic, Person data) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
}
