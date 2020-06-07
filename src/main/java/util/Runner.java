package util;

import entity.PlayerBackup;
import service.BackupStorage;

import java.sql.SQLException;
import java.util.Arrays;

public class Runner {
    public static void main(String[] args) throws SQLException {
        final PlayerBackup randomBackup = BackupStorage.getRandomBackup();
        System.out.println(Arrays.toString(randomBackup.getZoo()));
    }
}
