package com.teamm.friendstracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.db.DbManager;
import com.teamm.friendstracker.model.entity.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileEditorActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name, surname;
    private static final int SELECT_IMAGE = 123 ;
    ImageView image;
    public static boolean photoChanged = false;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference mountainsRef = storageRef.child(DbManager.users.getUid()).child("avatar.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);

        name=(EditText)findViewById(R.id.etNewName);
        surname=(EditText)findViewById(R.id.etNewSurname);
        image=(ImageView) findViewById(R.id.imageV);

        name.setText(DbManager.user.getName());
        surname.setText(DbManager.user.getSurname());
        if(DbManager.user.getAvatar())
            avatarDownload();

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    if(isRightName(name.getText().toString()))
                        name.setBackgroundColor(Color.GREEN);
                    else
                        name.setBackgroundColor(Color.RED);
                }
            }
        });

        surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    if(isRightSurname(surname.getText().toString()))
                        surname.setBackgroundColor(Color.GREEN);
                    else
                        surname.setBackgroundColor(Color.RED);
                }
            }
        });
    }

    private boolean isRightName(String name) {
        boolean result = true;
        if (name.length() < 1 || name.length() > 255) {
            Toast.makeText(ProfileEditorActivity.this, "Имя не может быть меньше 1 символа или больше 255", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (containsIllegalsString(name)) {
            Toast.makeText(ProfileEditorActivity.this, "Недопустимые символы в имени", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    private boolean isRightSurname(String surname) {
        boolean result = true;
        if (surname.length() < 1 || surname.length() > 255) {
            Toast.makeText(ProfileEditorActivity.this, "Фамилия не может быть меньше 1 символа или больше 255", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (containsIllegalsString(surname)) {
            Toast.makeText(ProfileEditorActivity.this, "Недопустимые символы в фамилии", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    public boolean containsIllegalsString(String toExamine) {
        Pattern pattern = Pattern.compile("[- @!№;%:?*_+#$^&{}\\[\\]]");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.find();
    }

    public void onClick(View view){
        switch (view.getId()){

            case R.id.bChangePhoto: {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), SELECT_IMAGE);
                break;
            }

            case R.id.bAccept: {
                if(isRightName(name.getText().toString()))
                    if(isRightSurname(surname.getText().toString())) {
                        DbManager.user.setName(name.getText().toString());
                        DbManager.user.setSurname(surname.getText().toString());
                        if (photoChanged) {
                            DbManager.user.setAvatar(true);
                            avatarSave();
                        }
                        DbManager.write();
                        Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                    }
                    else
                        isRightSurname(surname.getText().toString());
                break;
            }

            case R.id.bExit: {
                DbManager.signOut();
                Intent intent = new Intent(ProfileEditorActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap;
                        image.setImageBitmap(bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData()));
                        photoChanged = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Отмена", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void avatarSave(){
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }

    public void avatarDownload(){
        StorageReference storageReference = storageRef.child(DbManager.users.getUid()).child("avatar.jpg");
        Glide.with(ProfileEditorActivity.this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(image);
    }
}
