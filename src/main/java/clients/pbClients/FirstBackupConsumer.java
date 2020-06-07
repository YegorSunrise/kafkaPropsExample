package clients.pbClients;

public class FirstBackupConsumer {
    public static void main(String[] args) {
        BackupsConsumer consumer = new BackupsConsumer("backup-group");
        consumer.runConsumer();
    }
}
