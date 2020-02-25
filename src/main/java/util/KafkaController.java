package util;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Slf4j
public class KafkaController {
    //kafka server need to be work
    private Properties properties;

    public KafkaController(Properties properties) {
        this.properties = properties;
    }

    public void createTopic(String topicName, int partition, short replFactor) {
        try (AdminClient adminClient = AdminClient.create(properties)) {
            if (!getTopicsList().contains(topicName)) {
                final NewTopic newTopic = new NewTopic(topicName, partition, replFactor); //new NewTopic(topicName, numPartitions, replicationFactor)
                adminClient.createTopics(new ArrayList<NewTopic>() {{
                    add(newTopic);
                }});
                System.out.println("create topic " + topicName.toUpperCase());
            } else {
                System.out.println("warn topic already exist");
            }
        }
    }

    public Set<String> getTopicsList() {
        try (AdminClient adminClient = AdminClient.create(properties)) {
            final Set<String> topics = adminClient.listTopics().names().get();

            if (topics.isEmpty()) {
                log.warn("topic list is empty");
                return Collections.emptySet();
            } else {
                return topics;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        log.warn("topic list is empty");
        return Collections.emptySet();
    }

    //Not work better never delete topics!!
//    public void deleteTopic(String topicName) {
//        try (AdminClient adminClient = AdminClient.create(properties)) {
//            adminClient.deleteTopics(new ArrayList<String>() {{
//                add(topicName);
//            }});
//            System.out.println("delete topics " + topicName);
//            log.warn("delete topic {}",topicName);
//        }
//    }
//
//    public void deleteAllTopics(Set<String> topicsName) {
//        try (AdminClient adminClient = AdminClient.create(properties)) {
//            adminClient.deleteTopics(topicsName);
//            log.warn("delete topics {}",topicsName);
//        }
//    }

    public void seeBrokerData() {


    }

    public static void main(String[] args) {
        KafkaController kafkaController = new KafkaController(PropertyHolder.getAdminProperties());
//        kafkaController.createTopic("my_topic1", 1, (short) 1);
//        kafkaController.getTopicsList().forEach(System.out::println);
        System.out.println(kafkaController.getTopicsList());
    }

}
