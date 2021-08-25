package com.friday20.isyiar.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.friday20.isyiar.model.EntityLike;
import com.friday20.isyiar.model.EntitySyiar;
import com.friday20.isyiar.model.Params;
import com.friday20.isyiar.model.Syiar;
import com.friday20.isyiar.repo.AppRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AppViewModel extends AndroidViewModel {
    private AppRepository mRepository;
    private LiveData<List<Syiar>> syisrs;

    public AppViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppRepository(application);
    }

    public LiveData<List<Syiar>> getAllSyiars() {
        if (syisrs == null) {
            syisrs = mRepository.getAllSyiar();
        }
        return syisrs;
    }

    public LiveData<List<Syiar>> getMySyiars(String id) {
        LiveData<List<Syiar>> liveData = mRepository.getMySyiars(id);
        return liveData;
    }

    public void insertSyiar(EntitySyiar entity) {
        mRepository.insertSyiar(entity);
    }

    public void deleteSyiar(EntitySyiar entity) {
        mRepository.deleteSyiar(entity);
    }

    public void deleteAllSyiar() {
        mRepository.deleteAllSyiar();
    }

    //like
    public LiveData<List<Syiar>> getMyLikes(String id) {
        LiveData<List<Syiar>> liveData = mRepository.getMyLikes(id);
        return liveData;
    }

    public EntityLike checkLike(Params params) throws ExecutionException, InterruptedException {
        return mRepository.checkLike(params);
    }

    public void insertLike(EntityLike entity) {
        mRepository.insertLike(entity);
    }

    public void deleteLike(EntityLike entity) {
        mRepository.deleteLike(entity);
    }

}
