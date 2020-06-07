package clients.pbClients;

public class SecondBackupConsumer {
    public static void main(String[] args) {
        BackupsConsumer consumer = new BackupsConsumer("backup-group");
        consumer.runConsumer();
    }
}
