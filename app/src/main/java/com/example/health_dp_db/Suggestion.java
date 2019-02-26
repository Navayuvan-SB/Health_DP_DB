package com.example.health_dp_db;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Suggestion extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mDiseaseSpec = mRootReference.child("specialist");
    private DatabaseReference mDiseaseDetails = mRootReference.child("Doctor Details");

    String specialist, resultString;

    ArrayList<String> entry;

    private ListView detailListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        resultString = getIntent().getStringExtra("RESULT");

        entry = new ArrayList<String>();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Suggestion.this, android.R.layout.simple_list_item_1, entry);

        detailListView = findViewById(R.id.DetailsLV);

        detailListView.setAdapter(adapter);
        System.out.println("pass" + resultString);

        mDiseaseSpec.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                specialist = dataSnapshot.child(resultString).getValue().toString();
                System.out.println("Specialist" + specialist);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDiseaseDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot str: dataSnapshot.getChildren()){

                    System.out.println("pasassssedDDD");

                    String str1 = str.child("Specialist").getValue().toString().toLowerCase().trim();

                    String str2 = specialist.toLowerCase().trim();

                    System.out.println(" Str 1 : "+ str1);
                    System.out.println(" Str 2 : "+ str2);
                    System.out.print("str1.equals(str2) ");
                    System.out.print(str1.equals(str2));

                    if (str1.equals(str2)){

                        System.out.println("pasassssedDDD");

                        entry.add("Doctor name :" +"\t\t" + str.child("Doctor name").getValue().toString() + "\n" +
                                     "Specialist :" + "\t\t" +str.child("Specialist").getValue().toString() + "\n" +
                                     "Contact :" +"\t\t" + str.child("Contact").getValue().toString() + "\n" +
                                     "Hospital : " +"\t\t" + str.child("Hospital").getValue().toString() + "\n" +
                                     "Address :" +"\t\t" + str.child("Address").getValue().toString() + "\n" +
                                     "Consultation fee: " +"\t\t" + str.child("Consultation fee:").getValue().toString() + "\n");
                        System.out.print("Doctor name :" + str.child("Doctor name").getValue().toString() + "\n" +
                                "Specialist :" + str.child("Specialist").getValue().toString() + "\n" +
                                "Contact :" + str.child("Contact").getValue().toString() + "\n" +
                                "Hospital : " + str.child("Hospital").getValue().toString() + "\n" +
                                "Address :" + str.child("Address").getValue().toString() + "\n" +
                                "Consultation fee: " + str.child("Consultation fee:").getValue().toString());
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
