package clients.pbClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entity.PlayerBackup;
import util.PropertyHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MultipleBackupProducer {
    private static final String PATH = "C:\\cygwin64\\home\\user\\work\\cm\\backups\\";

    private static final List<Path> pathList = new ArrayList<>();

    static {
        try {
            pathList.addAll(Collections.synchronizedList(Files.list(Paths.get(PATH)).collect(Collectors.toList())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
//        for (int i = 0; i < 100; i++) {
//            new Thread(() -> {
//                BackupsProducer backupsProducer = new BackupsProducer(PropertyHolder.BACKUP_TOPIC);
//                for (int j = 0; j < 100; j++) {
//                    backupsProducer.runProducer(getRandomBackup());
//                    System.out.println("----------------------------left "+pathList.size()+" files ------------------ ");
//                }
//                backupsProducer.flushAndCloseProducer();
//            }).start();
//        }
        BackupsProducer backupsProducer = new BackupsProducer(PropertyHolder.BACKUP_TOPIC);
        for(Path file : pathList){
            try{
                final PlayerBackup playerBackup = getPlayerBackup(file);
                backupsProducer.runProducer(playerBackup);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        backupsProducer.flushAndCloseProducer();
    }

    private static PlayerBackup getPlayerBackup(Path file) throws IOException {
        final BufferedReader bufferedReader = Files.newBufferedReader(file);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        final PlayerBackup playerBackup = objectMapper.readValue(bufferedReader, PlayerBackup.class);
        bufferedReader.close();
        return playerBackup;
    }

    private static PlayerBackup getRandomBackup() {
        try {
            synchronized (pathList) {
                final Path path = pathList.get(new Random().nextInt(pathList.size()));
                pathList.remove(path);
                return getPlayerBackup(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
