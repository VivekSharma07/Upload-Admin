package com.example.viveksharma.upload_admin;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CategoryUpload extends AppCompatActivity {

    private StorageReference mStorageRef;
    private DatabaseReference mDataRef;
    private ImageView catView;
    private EditText catTitle;
    private EditText catDesc;
    private Button catBrowse;
    private Button catUpload;
    private Uri imgUri;
    public static final String FB_STORAGE_PATH="category/";
    public static final String FB_DATABASE_PATH="category";
    public static final int REQUEST_CODE=1234;
    public String dwnloadURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_upload);
    mStorageRef= FirebaseStorage.getInstance().getReference();
    mDataRef= FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

    //Initialize the UI Elements

        catView=findViewById(R.id.categoryView);
        catTitle=findViewById(R.id.categoryTitle);
        catDesc=findViewById(R.id.catDesc);
        catBrowse=findViewById(R.id.browseforCategory);
        catUpload=findViewById(R.id.catUpload);

    }
    public void browseClick(View view){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Image"),REQUEST_CODE);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imgUri=data.getData();

            try
            {
                Bitmap bmp= MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                catView.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void UploadCategory(View view){
        if(imgUri!=null){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading Image");
            dialog.show();

            StorageReference mRef=mStorageRef.child(FB_STORAGE_PATH+System.currentTimeMillis()+"."+getImageExt(imgUri));

            //Add File to reference
            mRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    //dismiss the dialog
                    dialog.dismiss();

                    //display success toast message

                    Toast.makeText(CategoryUpload.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();

                    //Save Image Info to FireBase Database
//an error may be generated in next line
                    ImageUploadModel imageUploadModel= new ImageUploadModel(catTitle.getText().toString(),catDesc.getText().toString(),taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    //redundant code snippet-
                    String uploadID=mDataRef.push().getKey();
    /*for naming node of recently uploaded in FB database*/
    mDataRef.child(catTitle.getText().toString()).setValue(imageUploadModel);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CategoryUpload.this, "Upload Failed Please Check Network Connection", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    dialog.setMessage((int)progress+" % Uploaded");
                }
            });
        }
        else{
            Toast.makeText(this, "Please Select An Image", Toast.LENGTH_SHORT).show();
        }

    }
}
