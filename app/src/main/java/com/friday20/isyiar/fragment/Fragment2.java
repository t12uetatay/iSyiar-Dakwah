package com.friday20.isyiar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.friday20.isyiar.PlayActivity;
import com.friday20.isyiar.R;
import com.friday20.isyiar.adapter.SyiarAdapter;
import com.friday20.isyiar.helpers.Space;
import com.friday20.isyiar.model.EntityLike;
import com.friday20.isyiar.model.EntitySyiar;
import com.friday20.isyiar.model.Syiar;
import com.friday20.isyiar.model.User;
import com.friday20.isyiar.preference.DataUser;
import com.friday20.isyiar.vm.AppViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Fragment2 extends Fragment implements SyiarAdapter.AdapterListener {
    private static final String TAG = Fragment2.class.getSimpleName();
    private SwipeRefreshLayout swipe;
    private RecyclerView recyclerView;
    private SyiarAdapter adapter;
    private List<Syiar> syiarList =new ArrayList<>();
    private DatabaseReference mDatabaseReference, df;
    private FirebaseDatabase mFirebaseInstance;
    private User user;
    private AppViewModel viewModel;

    public Fragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_2, container, false);
        user= DataUser.getInstance(getActivity()).getUser();
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        syiarList = new ArrayList<>();
        swipe=view.findViewById(R.id.swipe);
        recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new Space(20, 0));
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("entitas_likes");
        adapter = new SyiarAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                        loadData();
                    }
                },3000);
            }
        });

        loadData();

        final LiveData<List<Syiar>> liveData = viewModel.getMyLikes(user.getUsername());
        liveData.observe(this, new Observer<List<Syiar>>() {
            @Override
            public void onChanged(List<Syiar> syiars) {
                adapter.setDataList(syiars);
            }

        });

        return view;
    }


    private void loadData(){
        mDatabaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                syiarList.clear();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()){
                    EntityLike like = mDataSnapshot.getValue(EntityLike.class);
                    EntityLike entity = new EntityLike(
                            like.getIdLike(), like.getIdSyiar(), user.getUsername()
                    );
                    viewModel.insertLike(entity);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
                Toast.makeText(getActivity(), databaseError.getDetails()+" "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }


    @Override
    public void onClick(Syiar syiar, int position, ImageView imageView) {
        Intent i = new Intent(getActivity(), PlayActivity.class);
        i.putExtra("data", syiar);
        startActivity(i);
    }
}
