package clients.pbClients;

import entity.PlayerBackup;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import util.BackupSerializer;
import util.PropertyHolder;

import java.util.Properties;

@Slf4j
public class BackupsProducer {
    private String topic;
    private Producer<String, PlayerBackup> producer;
    private int recordCount = 0;

    public BackupsProducer(String topic) {
        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, PropertyHolder.BOOTSTRAP_SERVERS);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BackupSerializer.class.getName());
        producerProperties.put(ProducerConfig.ACKS_CONFIG, "all"); //The ”all” acks setting ensures full commit of record to all replicas and is most durable and least fast setting. The Kafka Producer can automatically retry failed requests.
        producerProperties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        producer = new KafkaProducer<>(producerProperties);
        this.topic = topic;
        log.info("load kafka properties");
    }

    public void runProducer(PlayerBackup backup) {
        if (backup == null) {
            log.warn("backup is null");
            return;
        }
        try {
            final ProducerRecord<String, PlayerBackup> record = new ProducerRecord<>(topic, backup);
            producer.send(record);
            log.info("record: {} send", backup.backupName());
        } catch (Exception logged) {
            producer.flush();
            log.info("flushed {} records", recordCount);
            log.error("error while produced record", logged);
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

