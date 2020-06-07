package clients.pbClients;

import entity.PlayerBackup;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import service.BackupStorage;
import util.BackupDeserializer;
import util.PropertyHolder;

import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BackupsConsumer {
    private Consumer<String, PlayerBackup> consumer;

    public BackupsConsumer(String groupId) {
        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, PropertyHolder.BOOTSTRAP_SERVERS);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BackupDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, -1); //value of -1, which lets the underlying operating system tune the buffer size based on network conditions.
        consumerProperties.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, 1000);
        consumerProperties.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, 50);
        consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(Collections.singleton(PropertyHolder.BACKUP_TOPIC));
    }

    public void runConsumer() {
        ConsumerRecords<String, PlayerBackup> consumerRecords;
        BackupStorage storage = new BackupStorage();
        while (true) {
            consumerRecords = consumer.poll(Duration.ofSeconds(100));
            final Iterator<ConsumerRecord<String, PlayerBackup>> iterator = consumerRecords.iterator();
            List<PlayerBackup> batch = new ArrayList<>();
            while (iterator.hasNext()) {
                final ConsumerRecord<String, PlayerBackup> next = iterator.next();
                PlayerBackup backup = next.value();
                batch.add(backup);
            }
            if (batch.size() > 0) {
                try {
                    storage.saveBatch(batch);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            consumer.commitAsync();
        }
    }

}
