package com.example.viveksharma.upload_admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button uploadCat,uploadImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadCat=findViewById(R.id.uploadCategory);
        uploadImg=findViewById(R.id.uploadImage);



    }
        public void onClick(View view){
        Intent intent = new Intent(MainActivity.this,CategoryUpload.class);
        startActivity(intent);

        }


        public void onImageClick(View view){
        Intent i = new Intent(MainActivity.this,UploadImage.class);
        startActivity(i);

        }

}
