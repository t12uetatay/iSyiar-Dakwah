package com.friday20.isyiar.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
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
import com.friday20.isyiar.UploadActivity;
import com.friday20.isyiar.adapter.Syiar2Adapter;
import com.friday20.isyiar.helpers.Space;
import com.friday20.isyiar.model.EntitySyiar;
import com.friday20.isyiar.model.Syiar;
import com.friday20.isyiar.model.User;
import com.friday20.isyiar.preference.DataUser;
import com.friday20.isyiar.room.AppDatabase;
import com.friday20.isyiar.vm.AppViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fragment3 extends Fragment implements Syiar2Adapter.AdapterListener {
    private static final String TAG = Fragment3.class.getSimpleName();
    private SwipeRefreshLayout swipe;
    private RecyclerView recyclerView;
    private Syiar2Adapter adapter;
    private List<EntitySyiar> syiarList;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;
    private AppViewModel viewModel;
    private User  user;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";

    public Fragment3() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_3, container, false);
        user = DataUser.getInstance(getActivity()).getUser();
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        syiarList = new ArrayList<>();
        swipe=view.findViewById(R.id.swipe);
        recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new Space(20, 0));
        FirebaseApp.initializeApp(getActivity());
        mFirebaseInstance = FirebaseDatabase.getInstance();
        adapter = new Syiar2Adapter(getActivity(), this);
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

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UploadActivity.class));
            }
        });

        final LiveData<List<Syiar>> liveData = viewModel.getMySyiars(user.getUsername());
        liveData.observe(this, new Observer<List<Syiar>>() {
            @Override
            public void onChanged(List<Syiar> syiars) {
                adapter.setDataList(syiars);
            }

        });

        return view;
    }

    private void loadData(){
        mDatabaseReference = mFirebaseInstance.getReference("enititas_syiar");
        mDatabaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                syiarList.clear();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()){
                    EntitySyiar syiar = mDataSnapshot.getValue(EntitySyiar.class);
                    EntitySyiar entity = new EntitySyiar(
                            syiar.getIdSyiar(), syiar.getUsername(),syiar.getJudul(), syiar.getDeskripsi(), syiar.getFileUrl()
                    );
                    viewModel.insertSyiar(entity);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
                Toast.makeText(getActivity(), databaseError.getDetails()+" "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }


    public void popupShowMenu(final Syiar syiar, final int position, ImageView imageButton){
        PopupMenu popup = new PopupMenu(getActivity(), imageButton);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(POPUP_CONSTANT)) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(POPUP_FORCE_SHOW_ICON, boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.edit) {
                    openDialog(syiar);
                    return true;
                } else if (id == R.id.delete) {
                    new MaterialAlertDialogBuilder(getActivity(), R.style.AlertRoundShapeTheme)
                            .setTitle("Konfirmasi!")
                            .setMessage("Apakah anda yakin ingin menghapus syiar ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    hapusData(syiar);
                                }
                            })
                            .setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                    return true;
                } else {
                    return onMenuItemClick(item);
                }

            }
        });
        popup.show();

    }

    @Override
    public void onClick(Syiar syiar, int position, ImageView imageView) {
        popupShowMenu(syiar, position, imageView);
    }

    @Override
    public void onKlick(Syiar syiar, int position) {
        Intent i = new Intent(getActivity(), PlayActivity.class);
        i.putExtra("data", syiarList.get(position));
        startActivity(i);
    }

    private void hapusData(final Syiar syiar){
        final EntitySyiar entity = new EntitySyiar(
                syiar.getIdSyiar(), syiar.getUsername(), syiar.getJudul(), syiar.getDeskripsi(), syiar.getFileUrl()
        );
        final ProgressDialog mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Please Waiting...");
        mDialog.show();
        mDialog.setCancelable(false);
        mDatabaseReference = mFirebaseInstance.getReference("enititas_syiar");
        if(mDatabaseReference != null){
            mDatabaseReference.child(String.valueOf(entity.getIdSyiar())).removeValue().addOnSuccessListener(new OnSuccessListener<Void>(){
                @Override
                public void onSuccess(Void mVoid){
                    mDialog.dismiss();
                    deleteFile(syiar.getFileUrl());
                    viewModel.deleteSyiar(entity);
                }
            });
        }
    }

    private void removeFav(final String idsyiar){
        mDatabaseReference = mFirebaseInstance.getReference("entitas_likes");
        if(mDatabaseReference != null){
            mDatabaseReference.child(idsyiar).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //db.deleteFav();
                }
            });
        }
    }

    private void removeCom(final String idsyiar){
        mDatabaseReference = mFirebaseInstance.getReference("comment");
        if(mDatabaseReference != null){
            mDatabaseReference.child(idsyiar).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }
    }

    public boolean deleteFile(String mImageUrl){ /* Delete file from firebase storage */
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(mImageUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("delete","true");
            }
        });
        return false;
    }

    private void updateData(EntitySyiar syiar){
        final ProgressDialog mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Please Waiting...");
        mDialog.show();
        mDialog.setCancelable(false);
        mDatabaseReference = mFirebaseInstance.getReference("enititas_syiar");
        mDatabaseReference.child(String.valueOf(syiar.getIdSyiar())).setValue(syiar).addOnSuccessListener(new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void mVoid){
                mDialog.dismiss();
                Toast.makeText(getActivity(), "Data berhasil di update !", Toast.LENGTH_LONG).show();
            }
        });
    }

    Dialog slideDialog;
    private void openDialog(final Syiar syiar) {

        slideDialog = new Dialog(getActivity(), R.style.CustomDialogAnimation);
        slideDialog.setContentView(R.layout.form_edit_syiar);

        slideDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = slideDialog.getWindow();
        window.setGravity(Gravity.CENTER);

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        slideDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams.copyFrom(slideDialog.getWindow().getAttributes());

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.45);

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;

        final TextInputEditText newtitle = slideDialog.findViewById(R.id.editTextJudul);
        final TextInputEditText newdesc = slideDialog.findViewById(R.id.editTextDesc);
        newtitle.setText(syiar.getJudul());
        newdesc.setText(syiar.getDeskripsi());
        slideDialog.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntitySyiar syiar1 = new EntitySyiar(
                        syiar.getIdSyiar(), syiar.getUsername(), newtitle.getText().toString(), newdesc.getText().toString(), syiar.getFileUrl()
                );
                updateData(
                        syiar1
                );
                slideDialog.dismiss();
            }
        });

        slideDialog.findViewById(R.id.ivclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideDialog.dismiss();
            }
        });


        slideDialog.getWindow().setAttributes(layoutParams);
        slideDialog.setCancelable(true);
        slideDialog.setCanceledOnTouchOutside(true);
        slideDialog.show();
    }
}
