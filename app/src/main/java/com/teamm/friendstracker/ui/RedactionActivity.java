package com.teamm.friendstracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.db.DbManager;
import com.teamm.friendstracker.model.entity.User;

import java.io.IOException;

public class RedactionActivity extends AppCompatActivity {
    EditText name, surname;
    private User user = DbManager.user;
    private static final int SELECT_IMAGE = 123 ;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redaction);

        name=(EditText)findViewById(R.id.bChangeName);
        surname=(EditText)findViewById(R.id.bChangeSurname);
        image=(ImageView) findViewById(R.id.image);

        name.setText(user.getName());
        surname.setText(user.getSurname());
    }

    public void onClick(View view){
        switch (view.getId()){

            case R.id.bChangeAvatar: {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), SELECT_IMAGE);
                break;
            }

            case R.id.bAccept: {
                user.setName(name.getText().toString());
                user.setSurname(surname.getText().toString());
                DbManager.write();
                break;
            }

            case R.id.bAnnulment: {
                super.onBackPressed();
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

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Отмена", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
