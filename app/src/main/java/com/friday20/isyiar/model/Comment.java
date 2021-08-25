package com.friday20.isyiar.model;

public class Comment {
    String idcom;
    String idsyiar;
    String username;
    String msg;
    String tanggal;

    public Comment(String idcom, String idsyiar, String username, String msg){
        this.idcom=idcom;
        this.idsyiar=idsyiar;
        this.username=username;
        this.msg=msg;
    }

    public Comment(){}

    public String getIdcom() {
        return idcom;
    }

    public String getIdsyiar() {
        return idsyiar;
    }

    public String getUsername() {
        return username;
    }

    public String getMsg() {
        return msg;
    }
}
