package com.friday20.isyiar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.friday20.filepicker.view.FilePickerDialog;
import com.friday20.isyiar.model.EntitySyiar;
import com.friday20.isyiar.model.User;
import com.friday20.isyiar.preference.DataUser;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import kr.co.namee.permissiongen.PermissionGen;

public class UploadActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final int REQUEST_CODE_SELECT_AUDIO = 103;
    private static final int REQUEST_CODE_SELECT_VIDEO = 104;
    private static final String FILE_TYPE_AUDIO = "AUD";
    private static final String DIR_AUDIOS = "MyAudios/";
    private MaterialButton btnupload;
    private TextInputEditText editTexTitle;
    private TextInputEditText editTextDescription;
    private TextInputLayout textInputLayoutTitle;
    private TextInputLayout textInputLayoutDescription;
    private FirebaseDatabase database;
    private DatabaseReference df;
    private StorageReference storage;
    private Uri fileuri;
    private User user;
    private FilePickerDialog dialog;
    private AppCompatImageView ivfile;
    private TextView tvfile;
    private File file;
    public static final int FILEPICKER_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        reqPerms();
        btnupload=findViewById(R.id.btnupload);
        editTexTitle= findViewById(R.id.editTextJudul);
        editTextDescription=findViewById(R.id.editTextDesc);
        textInputLayoutTitle=findViewById(R.id.txJudul);
        textInputLayoutDescription=findViewById(R.id.txDesc);
        ivfile=findViewById(R.id.browse);
        tvfile=findViewById(R.id.tvfile);
        storage= FirebaseStorage.getInstance().getReference("files");
        database = FirebaseDatabase.getInstance();
        df = database.getReference("enititas_syiar");
        user= DataUser.getInstance(this).getUser();

        /*final DialogProperties properties=new DialogProperties();
        dialog=new FilePickerDialog(this,properties);
        dialog.setTitle("Slect a File Audio or Video");
        dialog.setPositiveBtnName("Select");
        dialog.setNegativeBtnName("Cancel");
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type= DialogConfigs.FILE_SELECT;
        String fextension = "";
        if(fextension.length()>0) {
            int commas = countCommas(fextension);

            String[] exts = new String[commas + 1];
            StringBuffer buff = new StringBuffer();
            int i = 0;
            for (int j = 0; j < fextension.length(); j++) {
                if (fextension.charAt(j) == ',') {
                    exts[i] = buff.toString();
                    buff = new StringBuffer();
                    i++;
                } else {
                    buff.append(fextension.charAt(j));
                }
            }
            exts[i] = buff.toString();
            properties.extensions=exts;
        }
        String foffset="/mnt/sdcard";
        if(foffset.length()>0||!foffset.equals("")) {
            properties.root=new File(foffset);
        }

        properties.error_dir=new File("/mnt");
        dialog.setProperties(properties);


        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                final String FilePath = files[0];
                file = new File(FilePath);
                fileuri=Uri.fromFile(new File(file.getAbsolutePath()));
                tvfile.setVisibility(View.VISIBLE);
                tvfile.setText(file.getName());
                btnupload.setEnabled(true);
            }
        });*/

        tvfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvfile.setVisibility(View.INVISIBLE);
                fileuri=null;
                btnupload.setEnabled(false);
            }
        });

        ivfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.show();
                //selectFileType();
                /*String[] PERMISSIONS = {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if(hasPermissions(UploadActivity.this, PERMISSIONS)){
                    ShowFilepicker();
                }else{
                    ActivityCompat.requestPermissions(UploadActivity.this, PERMISSIONS, FILEPICKER_PERMISSIONS);
                }*/
                showActionsDialog();
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    upload();
                }
            }
        });
    }
    //

    /*public void ShowFilepicker(){
        // 1. Initialize dialog
        final StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(UploadActivity.this)
                .withFragmentManager(getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.FILE_PICKER)
                .build();

        // 2. Retrieve the selected path by the user and show in a toast !
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                file = new File(path);
                fileuri=Uri.fromFile(new File(file.getAbsolutePath()));
                tvfile.setVisibility(View.VISIBLE);
                tvfile.setText(file.getName());
                btnupload.setEnabled(true);
            }
        });

        chooser.show();
    }

    /**
     * Helper method that verifies whether the permissions of a given array are granted or not.
     *
     * @param context
     * @param permissions
     * @return {Boolean}
     */
    /*public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }*/

    //@Override
    /*public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FILEPICKER_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                            UploadActivity.this,
                            "Permission granted! Please click on pick a file once again.",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            UploadActivity.this,
                            "Permission denied to read your External storage :(",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                return;
            }
        }
    }*/
    //

    public boolean validate() {
        boolean valid = false;
        String judul = editTexTitle.getText().toString();
        String deskripsi = editTextDescription.getText().toString();
        if (judul.isEmpty()) {
            valid = false;
            textInputLayoutTitle.setError("Please enter title!");
        } else {
            valid = true;
            textInputLayoutTitle.setError(null);
        }

        if (deskripsi.isEmpty()) {
            valid = false;
            textInputLayoutDescription.setError("Please enter description!");
        } else {
            valid = true;
            textInputLayoutDescription.setError(null);
        }

        return valid;
    }

    private void reqPerms(){
        PermissionGen.with(UploadActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    private void upload() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait.....");
        progressDialog.show();
        progressDialog.setCancelable(false);

        final long filename= Long.parseLong(String.valueOf(System.currentTimeMillis()));
        final StorageReference storageReference = storage.child(filename+file.getName());
        UploadTask uploadTask = storageReference.putFile(fileuri);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
            }
        });
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull final Task<Uri> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "Upload Succsessful", Toast.LENGTH_SHORT).show();
                    final EntitySyiar syiar = new EntitySyiar(
                            filename,
                            user.getUsername(),
                            editTexTitle.getText().toString(),
                            editTextDescription.getText().toString().trim(),
                            task.getResult().toString()
                    );

                    df.child(String.valueOf(filename)).setValue(syiar);
                    editTexTitle.setText(null);
                    editTextDescription.setText(null);
                    tvfile.setVisibility(View.INVISIBLE);
                    file=null;
                    btnupload.setEnabled(false);
                } else {
                    Toast.makeText(UploadActivity.this, "File upload unsuccessful. Please try again."
                            , Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private int countCommas(String fextension) {
        int count = 0;
        for(char ch:fextension.toCharArray())
        {   if(ch==',') {
            count++;
        }
        }
        return count;
    }

    private Void selectFile(String tipe) {
        Intent intent = new Intent();
        //intent.setType("video/*");
        intent.setType(tipe);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Audio"), REQUEST_CODE_PERMISSIONS);
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // BACK PRESSED
        if (requestCode == RESULT_CANCELED) {
            return;
        }


        // AUDIO
        if (requestCode == REQUEST_CODE_PERMISSIONS && resultCode == RESULT_OK && null != data) {
            if (null != data.getData()) {
                fileuri = data.getData();
                file = new File(fileuri.toString());
                //fileuri=Uri.fromFile(new File(file.getAbsolutePath()));
                tvfile.setVisibility(View.VISIBLE);
                tvfile.setText(file.getName());
                btnupload.setEnabled(true);
            } else {
                Toast.makeText(this, "No audio chosen", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void showActionsDialog() {
        CharSequence[] items = {"Video", "Audio"};
        new MaterialAlertDialogBuilder(this, R.style.AlertRoundShapeTheme)
                .setTitle("Choos File")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            selectFile("video/*");
                        } else {
                            selectFile("audio/*");
                        }
                    }
                }).show();
    }



}