package com.eac.productlistingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.eac.productlistingapp.model.Product;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class AddProductActivity extends AppCompatActivity {

    private EditText edtName,edtPrice,edtDesc;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        this.setTitle("Add products Activity");
        //add back button
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtDesc = findViewById(R.id.edtDesc);
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        imageView = findViewById(R.id.imageView);

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save
                //

                storeImage(edtName.getText().toString(),edtPrice.getText().toString(),
                        edtDesc.getText().toString());
                Toast.makeText(AddProductActivity.this,"Saved successfully",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get Image
                //libray - https://github.com/Dhaval2404/ImagePicker

                ImagePicker.Companion.with(AddProductActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


    }

    private void saveDataToFirebase(Product product){
        FirebaseDatabase database = FirebaseDatabase.getInstance() ;
        //firebase database reference
        DatabaseReference myRef = database.getReference("products");
        myRef.push().setValue(product);

    }

    private void storeImage(final String name, final String price, final String desc){
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference().child("product_images").child("image_"+new Date().getTime());
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("File ", "onFailure: "+exception.getMessage() );
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.e("File", "onSuccess: " );

            }
        });
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    //link
                    Log.d("File Upload", "onComplete: "+downloadUri);
                    Product product = new Product(name,price,desc,downloadUri.toString());
                    saveDataToFirebase(product);
                } else {
                    // Handle failures
                    // ...
                    Log.d("File Upload", "error: ");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            imageView.setImageURI(data.getData());

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}