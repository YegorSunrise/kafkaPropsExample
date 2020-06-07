package util;

import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Properties;

public class PropertyHolder {
    private static Properties adminProperties = new Properties();

    public static final String PERSON_TOPIC = "fs-person-topic";
    public static final String BACKUP_TOPIC = "backup-topic";
    public final static String BOOTSTRAP_SERVERS = "localhost:9094,localhost:9092,localhost:9093";

    private PropertyHolder() {
    }

    public static Properties getAdminProperties() {
        adminProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        return adminProperties;
    }

}
