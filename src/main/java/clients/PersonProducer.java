package clients;

import entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import util.PersonSerializer;
import util.PropertyHolder;

import java.util.Properties;

@Slf4j
public class PersonProducer {
    private String topic;
    private Producer<String, Person> producer;
    private int recordCount = 0;

    public PersonProducer(String topic) {
        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, PropertyHolder.BOOTSTRAP_SERVERS);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PersonSerializer.class.getName());
        producerProperties.put(ProducerConfig.ACKS_CONFIG,"all"); //The ”all” acks setting ensures full commit of record to all replicas and is most durable and least fast setting. The Kafka Producer can automatically retry failed requests.
        producerProperties.put(ProducerConfig.RETRIES_CONFIG,Integer.MAX_VALUE);
        producer = new KafkaProducer<>(producerProperties);
        this.topic = topic;
        log.info("load kafka properties");
    }

    public void runProducer(Person person) {
        try {
            final ProducerRecord<String, Person> record = new ProducerRecord<>(topic, person);
            producer.send(record);
            log.info("record: {} send", person);
        } catch (Exception e) {
            producer.flush();
            log.info("flushed {} records", recordCount);
            log.error("error while produced record", e);
        }
        recordCount++;

    }

    public void flushAndCloseProducer() {
        producer.flush();
        log.info("flushed {} records", recordCount);
        producer.close();
        log.info("producer closed");
    }

}

