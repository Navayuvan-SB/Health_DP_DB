package com.example.health_dp_db;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Disease_prediction extends Fragment {

    private AutoCompleteTextView autocompleteText;

    private TextView sympLabel;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference mRootReference = firebaseDatabase.getReference();

    private DatabaseReference mSymptomsReference = mRootReference.child("Symptoms");

    private DatabaseReference mDiseaseReference = mRootReference.child("Disease");

    private DatabaseReference mMatchReference = mRootReference.child("Match");

    private DatabaseReference mResultReference = mRootReference.child("Result");

    private ImageView plusBtn;

    private ListView selectedSympList;

    ArrayAdapter <String> adapterSelectedSymp;

    private Button predictBtn;

    private List<String> selectedSymp;

    private ArrayList<String> contents;

    private ArrayList<Integer> sympIds = new ArrayList<Integer>();

    private static int maxIds = 0;

    public String predDiseaseId ;

    private TextView resultTextView;

    private ArrayList<Integer> resInInt = new ArrayList<Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_disease_prediction, container, true);

        autocompleteText = view.findViewById(R.id.autocompleteEditText);

        sympLabel = view.findViewById(R.id.SymptomsLabel);

        predictBtn = view.findViewById(R.id.predictBtn);


        contents = new ArrayList<String>();

        mSymptomsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){

                    String get = childDataSnapshot.getKey();

                    contents.add(get);
                    System.out.println("Key :" + childDataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,contents);

        autocompleteText.setAdapter(adapter);

        plusBtn = view.findViewById(R.id.plusBtnImg);

        selectedSympList = view.findViewById(R.id.selectedSymp);

        selectedSymp = new ArrayList<String>();

        adapterSelectedSymp = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,selectedSymp);

        selectedSympList.setAdapter(adapterSelectedSymp);

        registerForContextMenu(selectedSympList);

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String get = autocompleteText.getText().toString();

                autocompleteText.setText("");

                int flag = 0;
                for (String str: selectedSymp){
                    if (str.equals(get)){
                        flag = 1;
                    }
                }

                if (flag != 1){
                    if (get.equals("")){

                    }else {

                        selectedSymp.add(get);
                    }
                }

                //Toast.makeText(getActivity().getApplicationContext(), get, Toast.LENGTH_LONG).show();

                adapterSelectedSymp.notifyDataSetChanged();

            }
        });


        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predition();

            }
        });

        return view;
    }


    private void predition() {

            mSymptomsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (String str : selectedSymp){
                        sympIds.add(Integer.parseInt(dataSnapshot.child(str).getValue().toString()));
                        //System.out.println(Integer.parseInt(dataSnapshot.child(str).getValue().toString()));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mMatchReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){

                        int count = 0;
                        String get = childDataSnapshot.getValue().toString();

                        StringTokenizer tokenizer = new StringTokenizer(get, ", ");
                        while (tokenizer.hasMoreTokens()) {

                            int j = Integer.parseInt(tokenizer.nextToken());

                            System.out.println(j);

                            for (int i : sympIds){
                              if (i == j){

                                  count += 1;
                                  System.out.print("True ");
                                  System.out.print(i);
                                  System.out.print(" : ");
                                  System.out.print(j);
                                  System.out.print("\n");
                              }
                            }
                            //System.out.println(tokenizer.nextToken());
                        }

                        if (count > maxIds){

                            predDiseaseId = childDataSnapshot.getKey();

                           // System.out.print("Whhhoooo");
                           // System.out.print(predDiseaseId);

                            maxIds = count;

                            //System.out.println("Changed");
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

        mResultReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String str = dataSnapshot.getValue().toString();

                int res = Integer.parseInt(str);

                resInInt.add(res);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDiseaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = resInInt.get(0);

                for (DataSnapshot child : dataSnapshot.getChildren()){
                    if (Integer.parseInt(child.getValue().toString()) == i){
                        Toast.makeText(getActivity().getApplicationContext(), child.getKey(), Toast.LENGTH_LONG).show();
                        System.out.println("\n\n\n Result \n\n\n");
                        System.out.println(child.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        int IndesSelected = info.position;

        if (item.getTitle()=="Delete"){

            adapterSelectedSymp.remove(adapterSelectedSymp.getItem(info.position));

        }
        return super.onContextItemSelected(item);
    }


}
