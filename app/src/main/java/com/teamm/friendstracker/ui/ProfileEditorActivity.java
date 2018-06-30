package com.teamm.friendstracker.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.teamm.friendstracker.R;

public class ProfileEditorActivity extends AppCompatActivity implements View.OnClickListener {

    Button bChangePhoto;
    Button bAccept;
    Button bExit;
    static int GALLERY_REQUEST = 1;
    Uri selectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);
        bChangePhoto = (Button) findViewById(R.id.bChangePhoto);
        bAccept = (Button) findViewById(R.id.bAccept);
        bExit = (Button) findViewById(R.id.bExit);
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
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                break;
            case R.id.bAccept:
                EditText etNewName = (EditText) findViewById(R.id.etNewName);
                EditText etNewSurname = (EditText) findViewById(R.id.etNewSurname);
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("name", etNewName.getText().toString());
                intent.putExtra("surname", etNewSurname.getText().toString());
                intent.putExtra("photo", selectedImage.toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.bExit:
                finish();
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();
                //iv.setImageURI(null);
                //iv.setImageURI(selectedImage);
            }
        }
    }
}
