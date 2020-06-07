package service;

import entity.PlayerBackup;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Slf4j
public class BackupStorage {

    public static void save(PlayerBackup backup) throws SQLException {

        try (final Connection connection = DataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO backup values(?,?,?,?,?,?,?,?)")
        ) {
            preparedStatement.setDate(1, new Date(backup.getDate().toEpochDay()));
            preparedStatement.setString(2, backup.getPlayerId());
            preparedStatement.setString(3, backup.getZooId());
            preparedStatement.setDate(4, new Date(backup.getSavedAt().getTime()));
            preparedStatement.setString(5, backup.getZooName() == null ? "null" : backup.getZooName() );
            preparedStatement.setInt(6, backup.getStateVersion());
            preparedStatement.setString(7, backup.getSaveReason()== null ? "null" : backup.getSaveReason());
            preparedStatement.setString(8, new String(backup.getZoo(), StandardCharsets.UTF_8));
//            preparedStatement.setString(8, Base64.getEncoder().encodeToString(backup.getZoo()));
            preparedStatement.execute();
            log.info("save {}", backup.backupName());
        }
    }

    public static PlayerBackup getRandomBackup() throws  SQLException {
        try (final Connection connection = DataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement("select * from backup where saveAt = '2020-03-11'")){
//                 preparedStatement.setInt(1, new Random().nextInt(1000));
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PlayerBackup backup = new PlayerBackup();
                backup.setZoo(resultSet.getBytes(8));
//                final String bytes= resultSet.getString(8);
//                System.out.println(bytes);
//                byte[] data = bytes.getBytes();
//                backup.setZoo(data);
                return backup;
            }
        }
        return null;
    }

    public void saveBatch(List<PlayerBackup> batch) throws SQLException {
        try (final Connection connection = DataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO backup values(?,?,?,?,?,?,?,?)")
        ) {
            for (PlayerBackup backup : batch) {
                preparedStatement.setDate(1, new Date(backup.getDate().toEpochDay()));
                preparedStatement.setString(2, backup.getPlayerId());
                preparedStatement.setString(3, backup.getZooId());
                preparedStatement.setDate(4, new Date(backup.getSavedAt().getTime()));
                preparedStatement.setString(5, backup.getZooName() == null ? "null" : backup.getZooName());
                preparedStatement.setInt(6, backup.getStateVersion());
                preparedStatement.setString(7, backup.getSaveReason() == null ? "null" : backup.getSaveReason());
                preparedStatement.setString(8, new String(backup.getZoo(), StandardCharsets.UTF_8));
                preparedStatement.addBatch();
            }
            final int[] rows = preparedStatement.executeBatch();
            log.info("<<<<<<<<<<<<<<<--- insert {} rows --->>>>>>>>>>>>>", rows.length);
        }
    }
}

