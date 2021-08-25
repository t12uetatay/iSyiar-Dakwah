package com.friday20.isyiar.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class EntityFollow {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "idFollow")
    long idFollow;

    @ColumnInfo(name = "username")
    String username;

    @ColumnInfo(name = "userFollow")
    String userFollow;

    public EntityFollow(long idFollow, String username, String userFollow){
        this.idFollow=idFollow;
        this.username=username;
        this.userFollow=userFollow;
    }

    public EntityFollow(){}

    public long getIdFollow() {
        return idFollow;
    }

    public String getUsername() {
        return username;
    }

    public String getUserFollow() {
        return userFollow;
    }

    public void setIdFollow(long idFollow) {
        this.idFollow = idFollow;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserFollow(String userFollow) {
        this.userFollow = userFollow;
    }
}
