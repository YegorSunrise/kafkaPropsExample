package clients.pClients;

public class FirstConsumer {
    public static void main(String[] args) {
        PersonConsumer consumer = new PersonConsumer("first","person_a");
        consumer.runConsumer();
    }
}
