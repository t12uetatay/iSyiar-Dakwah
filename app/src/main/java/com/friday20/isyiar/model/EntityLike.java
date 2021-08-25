package com.friday20.isyiar.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class EntityLike {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "idLike")
    long idLike;

    @ColumnInfo(name = "idSyiar")
    long idSyiar;

    @ColumnInfo(name = "userLike")
    String userLike;

    public EntityLike(long idLike, long idSyiar, String userLike){
        this.idLike=idLike;
        this.idSyiar=idSyiar;
        this.userLike=userLike;
    }

    public EntityLike(){}

    public long getIdLike() {
        return idLike;
    }

    public long getIdSyiar() {
        return idSyiar;
    }

    public String getUserLike() {
        return userLike;
    }

    public void setIdLike(long idLike) {
        this.idLike = idLike;
    }

    public void setIdSyiar(long idSyiar) {
        this.idSyiar = idSyiar;
    }

    public void setUserLike(String userLike) {
        this.userLike = userLike;
    }


}
