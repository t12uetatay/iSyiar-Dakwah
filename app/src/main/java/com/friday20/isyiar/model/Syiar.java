package com.friday20.isyiar.model;

import androidx.room.ColumnInfo;

import java.io.Serializable;

public class Syiar implements Serializable {
    long idSyiar;
    String username;
    String judul;
    String deskripsi;
    String fileUrl;
    int suka=0;

    public Syiar(long idSyiar, String username, String judul, String deskripsi, String fileUrl, int suka){
        this.idSyiar=idSyiar;
        this.username=username;
        this.judul=judul;
        this.deskripsi=deskripsi;
        this.fileUrl=fileUrl;
        this.suka=suka;
    }

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

    public int getSuka() {
        return suka;
    }
}
