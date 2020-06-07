package clients.pClients;

import entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import service.PersonStorage;
import util.PersonDeserializer;
import util.PropertyHolder;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

@Slf4j
public class PersonConsumer {
    private Consumer<String, Person> consumer;
    private String table;

    public PersonConsumer(String groupId, String table) {
        this.table = table;
        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, PropertyHolder.BOOTSTRAP_SERVERS);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, PersonDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, -1); //value of -1, which lets the underlying operating system tune the buffer size based on network conditions.
        consumerProperties.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, 1000);
        consumerProperties.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, 50);
        consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(Collections.singleton(PropertyHolder.PERSON_TOPIC));
    }

    public void runConsumer() {
        ConsumerRecords<String, Person> consumerRecords;
        while (true) {
            consumerRecords = consumer.poll(Duration.ofSeconds(100));
            final Iterator<ConsumerRecord<String, Person>> iterator = consumerRecords.iterator();
            while (iterator.hasNext()) {
                final ConsumerRecord<String, Person> next = iterator.next();
                Person person = next.value();
                StringBuilder consumerMeta = new StringBuilder();
                consumerMeta
                        .append("topic: ").append(next.topic())
                        .append(" partition: ").append(next.partition());
                if (person != null) {
                    try {
                        PersonStorage.save(person, consumerMeta.toString(), table);
                    } catch (SQLException logged) {
                        log.error("error while save person",logged);
                    }
                    log.info("value {} save in storage", person);
                }
            }
            consumer.commitAsync();
        }
    }

}
