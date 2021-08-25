package com.friday20.isyiar.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.friday20.isyiar.model.EntityLike;
import com.friday20.isyiar.model.EntitySyiar;
import com.friday20.isyiar.model.Params;
import com.friday20.isyiar.model.Syiar;
import com.friday20.isyiar.room.AppDatabase;
import com.friday20.isyiar.room.DBOpertion;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AppRepository {
    private DBOpertion dao;
    private LiveData<List<Syiar>> liveDataSyiar;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        dao = db.dbOpertion();
        liveDataSyiar = dao.getAllSyiar();
    }

    public LiveData<List<Syiar>> getAllSyiar() {
        return liveDataSyiar;
    }

    public LiveData<List<Syiar>> getMySyiars(String id) {
        liveDataSyiar = dao.getMySyiars(id);
        return liveDataSyiar;
    }

    public void insertSyiar(EntitySyiar entity) {
        new insertSyiarAsync(dao).execute(entity);
    }

    private static class insertSyiarAsync extends AsyncTask<EntitySyiar, Void, Long> {

        private DBOpertion mAppDao;

        insertSyiarAsync(DBOpertion appDao) {
            mAppDao = appDao;
        }

        @Override
        protected Long doInBackground(EntitySyiar... entity) {
            long id = mAppDao.insertSyiar(entity[0]);
            return id;
        }
    }

    public void deleteSyiar(EntitySyiar entity) {
        new deleteSyiarAsync(dao).execute(entity);
    }

    private static class deleteSyiarAsync extends AsyncTask<EntitySyiar, Void, Void> {

        private DBOpertion mAppDao;

        deleteSyiarAsync(DBOpertion appDao) {
            mAppDao = appDao;
        }

        @Override
        protected Void doInBackground(EntitySyiar... entity) {
            mAppDao.deleteSyiar(entity[0]);
            return null;
        }
    }

    public void deleteAllSyiar() {
        new deleteAllSyiarAsync(dao).execute();
    }

    private static class deleteAllSyiarAsync extends AsyncTask<EntitySyiar, Void, Void> {

        private DBOpertion mAppDao;

        deleteAllSyiarAsync(DBOpertion appDao) {
            mAppDao = appDao;
        }

        @Override
        protected Void doInBackground(EntitySyiar... entity) {
            mAppDao.deleteAllSyiar();
            return null;
        }
    }

    //like
    public LiveData<List<Syiar>> getMyLikes(String id) {
        liveDataSyiar = dao.getMyLikes(id);
        return liveDataSyiar;
    }

    public EntityLike checkLike(Params params) throws ExecutionException, InterruptedException {
        return new getCheckLikeAsync(dao).execute(params).get();
    }

    private static class getCheckLikeAsync extends AsyncTask<Params, Void, EntityLike> {

        private DBOpertion mAppDao;

        getCheckLikeAsync(DBOpertion appDao) {
            mAppDao = appDao;
        }

        @Override
        protected EntityLike doInBackground(Params... params) {
            return mAppDao.checkFav(params[0].getP1(), params[0].getP2());
        }
    }

    public void insertLike(EntityLike entity) {
        new insertLikeAsync(dao).execute(entity);
    }

    private static class insertLikeAsync extends AsyncTask<EntityLike, Void, Long> {

        private DBOpertion mAppDao;

        insertLikeAsync(DBOpertion appDao) {
            mAppDao = appDao;
        }

        @Override
        protected Long doInBackground(EntityLike... entity) {
            long id = mAppDao.insertLike(entity[0]);
            return id;
        }
    }

    public void deleteLike(EntityLike entity) {
        new deleteLikeAsync(dao).execute(entity);
    }

    private static class deleteLikeAsync extends AsyncTask<EntityLike, Void, Void> {

        private DBOpertion mAppDao;

        deleteLikeAsync(DBOpertion appDao) {
            mAppDao = appDao;
        }

        @Override
        protected Void doInBackground(EntityLike... entity) {
            mAppDao.deleteLike(entity[0]);
            return null;
        }
    }
}
