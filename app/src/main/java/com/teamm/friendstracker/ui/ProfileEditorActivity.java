package com.teamm.friendstracker.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.db.DbManager;
import com.teamm.friendstracker.model.entity.User;

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
                //DbManager.write();
                finish();
                break;
            case R.id.bExit:
                super.onBackPressed();
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
            }
        }
    }
}
