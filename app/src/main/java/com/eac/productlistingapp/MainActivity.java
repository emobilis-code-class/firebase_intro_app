package com.eac.productlistingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eac.productlistingapp.adapter.ProductRecyclerViewAdapter;
import com.eac.productlistingapp.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //firebase database
    FirebaseDatabase database ;
    //firebase database reference
    DatabaseReference myRef;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set up
        //initialize
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("products");

        //write /savve

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start activity
                startActivity(new Intent(MainActivity.this,AddProductActivity.class));
            }
        });

        //
       // myRef.setValue("MyData");
        //myRef.push().setValue("My other data");
        getMyData();
    }

    private void getMyData(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot item:snapshot.getChildren()
                     ) {
                    productList.add(item.getValue(Product.class));
                }

                //adapter
                ProductRecyclerViewAdapter adapter = new ProductRecyclerViewAdapter(MainActivity.this
                        ,productList);
                recyclerView.setAdapter(adapter);
                

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}