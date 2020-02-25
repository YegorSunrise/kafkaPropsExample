package service;

import entity.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class PersonStorage {
    private static Set<Long> ids = new ConcurrentSkipListSet<>();

    public static void save(Person person, String meta, String table) throws SQLException {

        try (final Connection connection = DataSource.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + table + " values(?,?,?,?)")
        ) {
            final long id = idChecker(ids, person.hashCode() * 31);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, person.getName());
            preparedStatement.setInt(3, person.getAge());
            preparedStatement.setString(4, meta);
            preparedStatement.execute();
        }
    }

    private static long idChecker(Set<Long> ids, long id) {
        if (id < 0) {
            id = id * -1;
        }
        if (ids.contains(id)) {
            long l = new Random().nextLong();
            if (l < 0) {
                l = l * -1;
            }
            id = id + l >> 32;
            idChecker(ids, id);
        } else {
            ids.add(id);
        }
        return id;

    }
}
