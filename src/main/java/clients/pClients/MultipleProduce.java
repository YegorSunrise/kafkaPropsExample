package clients.pClients;

import entity.Person;
import util.PropertyHolder;

public class MultipleProduce {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                PersonProducer personProducer = new PersonProducer(PropertyHolder.PERSON_TOPIC);
                for (int j = 0; j < 100; j++) {
                    personProducer.runProducer(new Person());
                }
                personProducer.flushAndCloseProducer();
            }).start();
        }
    }
}
