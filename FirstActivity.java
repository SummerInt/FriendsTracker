package com.example.summerintensity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class FirstActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 123 ;
    TextView textView1, textView2;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        textView1=(TextView)findViewById(R.id.textView1);
        textView2=(TextView)findViewById(R.id.textView2);
        imageView=(ImageView)findViewById(R.id.imageView);
        screen();
    }

    private void screen() {
        textView1.setTextColor(Progress.colorT1);
        textView2.setTextColor(Progress.colorT2);
        textView1.setBackgroundColor(Progress.colorF1);
        textView2.setBackgroundColor(Progress.colorF2);
        if(Progress.bitmap!=null)
            imageView.setImageBitmap(Progress.bitmap);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.leftBtn:
            case R.id.rightBtn: {
                Progress.colorF1 = Progress.getRandomColor();
                Progress.colorF2 = Progress.getRandomColor();
                Progress.colorT1 = Progress.getRandomColor();
                Progress.colorT2 = Progress.getRandomColor();
                screen();
                break;
            }

            case R.id.centralBtn: {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.imageView: {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), SELECT_IMAGE);
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
                        imageView.setImageBitmap(Progress.bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData()));

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
