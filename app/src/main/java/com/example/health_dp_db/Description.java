package com.example.health_dp_db;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Description extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mDiseaseDesc = mRootReference.child("Disease Description");

    private TextView diseaseHeading, diseaseDesc;

    private String desc, resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);


        resultString = getIntent().getStringExtra("RESULT");

        diseaseHeading = findViewById(R.id.diseaseNameET);

        diseaseDesc = findViewById(R.id.diseaseDescET);

        diseaseHeading.setText("You may have " + resultString);

        mDiseaseDesc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                desc = dataSnapshot.child(resultString).getValue().toString();
                diseaseDesc.setText(desc);
                System.out.println(desc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //diseaseDesc.setText(desc);



    }

    public void NavigateToSuggestion(View view) {

        System.out.println("Clickeddddddddd");

        Intent i = new Intent(Description.this, Suggestion.class);
        i.putExtra("RESULT",resultString);
        startActivity(i);
    }
}
