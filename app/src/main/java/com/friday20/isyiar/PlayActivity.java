package com.friday20.isyiar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.friday20.isyiar.adapter.CommentAdapter;
import com.friday20.isyiar.helpers.Space;
import com.friday20.isyiar.model.Comment;
import com.friday20.isyiar.model.EntityLike;
import com.friday20.isyiar.model.EntitySyiar;
import com.friday20.isyiar.model.Favorite;
import com.friday20.isyiar.model.Params;
import com.friday20.isyiar.model.Syiar;
import com.friday20.isyiar.model.User;
import com.friday20.isyiar.preference.DataUser;
import com.friday20.isyiar.room.AppDatabase;
import com.friday20.isyiar.vm.AppViewModel;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class PlayActivity extends AppCompatActivity {
    private Syiar syiar=null;
    private AppCompatTextView judul, deskripsi, pemilik;
    private ProgressBar mProgressBar;
    private AppCompatImageView ivlike;
    private User user;
    private FirebaseDatabase database;
    private DatabaseReference df;
    int fav;
    private AppViewModel viewModel;
    Favorite fv;
    EditText edittext_chatbox;
    List<Comment> list;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    String idf;
    private EntityLike mylike=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        user = DataUser.getInstance(this).getUser();
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        pemilik=findViewById(R.id.textViewNama);
        judul=findViewById(R.id.tvjudul);
        deskripsi=findViewById(R.id.tvdes);
        ivlike=findViewById(R.id.ivlike);
        mProgressBar = findViewById(R.id.progressBar);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        recyclerView = findViewById(R.id.recycleview);
        list = new ArrayList<>();
        adapter = new CommentAdapter(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new Space(5, 0));
        recyclerView.setAdapter(adapter);
        user= DataUser.getInstance(this).getUser();
        database = FirebaseDatabase.getInstance();
        final Intent intent = getIntent();
        syiar = (Syiar) intent.getSerializableExtra("data");
        judul.setText(syiar.getJudul());
        deskripsi.setText(syiar.getDeskripsi());
        pemilik.setText(syiar.getUsername());
        initializePlayer(syiar.getFileUrl());
        ivlike.setTag(R.drawable.ic_favorite_white_24dp);
        Params params = new Params(syiar.getIdSyiar(), user.getUsername());

        checkFav(params);
        ivlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tg= String.valueOf(ivlike.getTag());
                if (tg.equals(String.valueOf(R.drawable.ic_favorite_white_24dp))){
                    addFav(syiar);
                } else {
                    removeFav(mylike);
                }
            }
        });

       findViewById(R.id.button_chatbox_send).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sendComment();
           }
       });


        loadData();

    }

    private void loadData(){
        df = database.getReference("comment").child(String.valueOf(syiar.getIdSyiar()));
        df.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                list.clear();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()){
                    Comment com = mDataSnapshot.getValue(Comment.class);
                    list.add(com);
                }
                adapter.notifyDataSetChanged();
                adapter.setDataList(list);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
                Toast.makeText(PlayActivity.this, databaseError.getDetails()+" "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void loadDataLike(){
        df = database.getReference("entitas_likes");
        df.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
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
                Toast.makeText(PlayActivity.this, databaseError.getDetails()+" "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void checkFav(Params params){
        loadDataLike();
        try {
            mylike = viewModel.checkLike(params);
            if (mylike!=null){
                ivlike.setImageResource(R.drawable.ic_favorite_red_24dp);
                ivlike.setTag(R.drawable.ic_favorite_red_24dp);
            } else {
                ivlike.setImageResource(R.drawable.ic_favorite_white_24dp);
                ivlike.setTag(R.drawable.ic_favorite_white_24dp);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*df = database.getReference("entitas_likes");
        df.child(String.valueOf(syiar.getIdSyiar())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user.getUsername()).exists()) {
                    ivlike.setImageResource(R.drawable.ic_favorite_red_24dp);
                    ivlike.setTag(R.drawable.ic_favorite_red_24dp);

                }else{
                    ivlike.setImageResource(R.drawable.ic_favorite_white_24dp);
                    ivlike.setTag(R.drawable.ic_favorite_white_24dp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void addFav(final Syiar syiar){
        final long idlike = Long.parseLong(String.valueOf(System.currentTimeMillis()));
        final EntityLike entitas = new EntityLike(idlike, syiar.getIdSyiar(), user.getUsername());
        final ProgressDialog mDialog = new ProgressDialog(PlayActivity.this);
        mDialog.setMessage("Please Waiting...");
        mDialog.show();
        mDialog.setCancelable(false);
        df = database.getReference("entitas_likes");
        df.child(String.valueOf(entitas.getIdLike())).setValue(entitas).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDialog.dismiss();
                viewModel.insertLike(entitas);
                Toast.makeText(PlayActivity.this, "Ditambahkan ke favorite!", Toast.LENGTH_LONG).show();
                ivlike.setImageResource(R.drawable.ic_favorite_red_24dp);
                ivlike.setTag(R.drawable.ic_favorite_red_24dp);
            }
        });
    }

    private void removeFav(final EntityLike like){
        final ProgressDialog mDialog = new ProgressDialog(PlayActivity.this);
        mDialog.setMessage("Please Waiting...");
        mDialog.show();
        mDialog.setCancelable(false);
        df = database.getReference("entitas_likes");
        if(df != null){
            df.child(String.valueOf(like.getIdLike())).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDialog.dismiss();
                    viewModel.deleteLike(like);
                    Toast.makeText(PlayActivity.this,"Favorite berhasil di hapus !", Toast.LENGTH_LONG).show();
                    ivlike.setImageResource(R.drawable.ic_favorite_white_24dp);
                    ivlike.setTag(R.drawable.ic_favorite_white_24dp);
                }
            });
        }
    }

    private void sendComment(){
        final ProgressDialog mDialog = new ProgressDialog(PlayActivity.this);
        mDialog.setMessage("Please Waiting...");
        mDialog.show();
        mDialog.setCancelable(false);
        df = database.getReference("comment");
        String id=  new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis());
        Comment comment = new Comment(
                id, String.valueOf(syiar.getIdSyiar()), user.getUsername(), edittext_chatbox.getText().toString()
        );
        df.child(String.valueOf(syiar.getIdSyiar())).child(id).setValue(comment).addOnSuccessListener(this, new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void mVoid){
                edittext_chatbox.setText(null);
                mDialog.dismiss();
            }
        });
    }

    private MediaSource createMediaSource(String videoUrl) {
        String UserAgent = Util.getUserAgent(this, getString(R.string.app_name));
        MediaSource contentMediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                new DefaultHttpDataSourceFactory(UserAgent),
                new DefaultExtractorsFactory(),
                null, null);
        return contentMediaSource;
    }

    SimpleExoPlayer player;

    private void initializePlayer(String url) {
        SimpleExoPlayerView exoPlayerView = findViewById(R.id.exoPlayerView);
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector());
        exoPlayerView.setPlayer(player);
        player.prepare(createMediaSource(url));
        player.addListener(new Player.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object o, int i) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        showProgress();
                        break;
                    case Player.STATE_READY:
                        hideProgress();
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean b) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }

            @Override
            public void onPositionDiscontinuity(int i) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onSeekProcessed() {

            }
        });

    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        player.setPlayWhenReady(true);
        super.onResume();
    }

    @Override
    protected void onPause() {
        player.setPlayWhenReady(false);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }



}