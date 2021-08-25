package com.friday20.isyiar.model;

public class Favorite {
    String idsyiar;
    String username;
    String judul;
    String deskripsi;
    String urlfile;

    public Favorite(String idsyiar, String username, String judul, String deskripsi, String urlfile){
        this.idsyiar=idsyiar;
        this.username=username;
        this.judul=judul;
        this.deskripsi=deskripsi;
        this.urlfile=urlfile;
    }

    public Favorite(){}

    public String getIdsyiar() {
        return idsyiar;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getJudul() {
        return judul;
    }

    public String getUrlfile() {
        return urlfile;
    }

    public String getUsername() {
        return username;
    }

}
