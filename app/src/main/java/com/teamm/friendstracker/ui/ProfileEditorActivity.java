package com.teamm.friendstracker.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileEditorActivity extends AppCompatActivity implements View.OnClickListener {

    Button bChangePhoto;
    Button bAccept;
    Button bExit;
    Uri selectedImage = null;
    EditText name;
    EditText surname;
    private User user = DbManager.user;
    private static final int SELECT_IMAGE = 1 ;
    ImageView image;
    public static boolean photoChanged = false;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference mountainsRef = storageRef.child(DbManager.users.getUid()).child("avatar.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);
        bChangePhoto = (Button) findViewById(R.id.bChangePhoto);
        bAccept = (Button) findViewById(R.id.bAccept);
        bExit = (Button) findViewById(R.id.bExit);
        name=(EditText)findViewById(R.id.etNewName);
        surname=(EditText)findViewById(R.id.etNewSurname);
        image=(ImageView) findViewById(R.id.imageView2);

        bChangePhoto.setOnClickListener(this);
        bAccept.setOnClickListener(this);
        bExit.setOnClickListener(this);

        name.setText(DbManager.user.getName());
        surname.setText(DbManager.user.getSurname());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bChangePhoto:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Выберите изображение"), SELECT_IMAGE);
                break;
            case R.id.bAccept:
                String newName = name.getText().toString();
                String newSurname = surname.getText().toString();
                String newPhoto;
                DbManager.user.setName(newName);
                DbManager.user.setName(newSurname);
                /*if(!newName.isEmpty()){
                    user.setName(newName);
                }
                if(!newSurname.isEmpty()){
                    user.setSurname(newSurname);
                }*/
                if(selectedImage != null){
                    newPhoto = selectedImage.toString();
                }else {
                    newPhoto = "";
                }
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("name", newName);
                intent.putExtra("surname", newSurname);
                intent.putExtra("photo", newPhoto);
                setResult(RESULT_OK, intent);
                if(photoChanged){
                    DbManager.user.setAvatar(true);
                    avatarSave();
                }
                DbManager.write();
                finish();
                break;
            case R.id.bExit:
                DbManager.signOut();
                Intent intentExit = new Intent(ProfileEditorActivity.this, LoginActivity.class);
                startActivity(intentExit);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();
                image.setImageURI(null);
                image.setImageURI(selectedImage);
                photoChanged = true;
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
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(mountainsRef)
                .into(image);
    }
}
