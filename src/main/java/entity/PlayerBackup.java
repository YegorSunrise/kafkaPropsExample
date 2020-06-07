package entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import service.BackupStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.StringJoiner;

public class PlayerBackup implements Serializable {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDate date;
    private String playerId;
    private String zooId;

    private Date savedAt;
    private String zooName;
    private int stateVersion;
    private String saveReason;
    private byte[] zoo;

    public PlayerBackup() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getZooId() {
        return zooId;
    }

    public void setZooId(String zooId) {
        this.zooId = zooId;
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    public String getZooName() {
        return zooName;
    }

    public void setZooName(String zooName) {
        this.zooName = zooName;
    }

    public int getStateVersion() {
        return stateVersion;
    }

    public void setStateVersion(int stateVersion) {
        this.stateVersion = stateVersion;
    }

    public String getSaveReason() {
        return saveReason;
    }

    public void setSaveReason(String saveReason) {
        this.saveReason = saveReason;
    }

    public byte[] getZoo() {
        return zoo;
    }

    public void setZoo(byte[] zoo) {
        this.zoo = zoo;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PlayerBackup.class.getSimpleName() + "[", "]")
                .add("date=" + date)
                .add("playerId='" + playerId + "'")
                .add("zooId='" + zooId + "'")
                .add("savedAt=" + savedAt)
                .add("zooName='" + zooName + "'")
                .add("zoo Length=" + zoo.length)
                .add("stateVersion=" + stateVersion)
                .add("saveReason='" + saveReason + "'")
                .toString();
    }

    public String backupName(){
        return playerId + "_v" + stateVersion;
    }
}
