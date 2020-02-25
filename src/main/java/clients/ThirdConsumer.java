package clients;

public class ThirdConsumer {
    public static void main(String[] args) {
        PersonConsumer consumer = new PersonConsumer("second","person_b");
        consumer.runConsumer();
    }
}
