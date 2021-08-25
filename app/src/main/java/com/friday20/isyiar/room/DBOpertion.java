package com.friday20.isyiar.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.friday20.isyiar.model.EntityFollow;
import com.friday20.isyiar.model.EntityLike;
import com.friday20.isyiar.model.EntitySyiar;
import com.friday20.isyiar.model.Syiar;

import java.util.List;

@Dao
public interface DBOpertion {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSyiar(EntitySyiar entity);

    @Query("SELECT EntitySyiar.*, ifnull((SELECT COUNT(*) FROM EntityLike WHERE EntityLike.idSyiar=EntitySyiar.idSyiar), 0) as suka FROM EntitySyiar ORDER BY idSyiar, judul ASC")
    LiveData<List<Syiar>> getAllSyiar();

    @Query("SELECT EntitySyiar.*, ifnull((SELECT COUNT(*) FROM EntityLike WHERE EntityLike.idSyiar=EntitySyiar.idSyiar), 0) as suka FROM EntitySyiar WHERE username=:id")
    LiveData<List<Syiar>> getMySyiars(String id);


    @Delete
    void deleteSyiar(EntitySyiar entity);

    @Query("DELETE FROM EntitySyiar")
    void deleteAllSyiar();
    //like
    @Query("SELECT EntitySyiar.*, ifnull((SELECT COUNT(*) FROM EntityLike WHERE EntityLike.idSyiar=EntitySyiar.idSyiar), 0) as suka FROM EntityLike INNER JOIN EntitySyiar ON EntitySyiar.idSyiar=EntityLike.idSyiar WHERE EntityLike.userLike=:id")
    LiveData<List<Syiar>> getMyLikes(String id);

    @Query("SELECT * FROM EntityLike WHERE idSyiar=:p1 AND userLike=:p2")
    EntityLike checkFav(long p1, String p2);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLike(EntityLike entity);

    @Delete
    void deleteLike(EntityLike entity);

    @Query("DELETE FROM EntityLike")
    void deleteAllLike();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFollow(EntityFollow entity);


    @Delete
    void deleteFollow(EntityFollow entity);

    @Query("DELETE FROM EntityFollow")
    void deleteAllFollow();
}
