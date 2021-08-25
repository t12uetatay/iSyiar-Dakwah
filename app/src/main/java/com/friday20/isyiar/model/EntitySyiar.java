package com.friday20.isyiar.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity()
public class EntitySyiar implements Serializable {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "idSyiar")
    long idSyiar;

    @ColumnInfo(name = "username")
    String username;

    @ColumnInfo(name = "judul")
    String judul;

    @ColumnInfo(name = "deskripsi")
    String deskripsi;

    @ColumnInfo(name = "fileUrl")
    String fileUrl;

    public EntitySyiar (long idSyiar, String username, String judul, String deskripsi, String fileUrl){
        this.idSyiar=idSyiar;
        this.username=username;
        this.judul=judul;
        this.deskripsi=deskripsi;
        this.fileUrl=fileUrl;
    }

    public EntitySyiar(){}

    public long getIdSyiar() {
        return idSyiar;
    }

    public String getUsername() {
        return username;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setIdSyiar(long idSyiar) {
        this.idSyiar = idSyiar;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
