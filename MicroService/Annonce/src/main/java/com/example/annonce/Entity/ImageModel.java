package com.example.annonce.Entity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String filePath;
    @Lob // Annotation utilisée pour indiquer que la colonne stocke de grandes valeurs binaires
    @Column(length = 999999999)
    private byte[] bytes;

    public ImageModel() {
    }

    public ImageModel(long id, String filePath, byte[] bytes) {
        this.id = id;
        this.filePath = filePath;
        this.bytes = bytes;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public ImageModel(byte[] bytes, String name, String Type) {

        this.bytes = bytes;

    }
}
