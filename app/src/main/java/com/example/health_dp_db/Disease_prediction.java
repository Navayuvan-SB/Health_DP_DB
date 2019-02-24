package com.example.health_dp_db;

import android.app.Activity;
import android.content.Context;
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

public class Disease_prediction extends Fragment {

    private AutoCompleteTextView autocompleteText;
    
    private TextView sympLabel;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference mRootReference = firebaseDatabase.getReference();

    private DatabaseReference mSymptomsReference = mRootReference.child("Symptoms");

    private ImageView plusBtn;

    private ListView selectedSympList;

    ArrayAdapter <String> adapterSelectedSymp;

    private Button predictBtn;

    private List<String> selectedSymp;

    private ArrayList<String> contents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_disease_prediction, container, true);

        autocompleteText = view.findViewById(R.id.autocompleteEditText);

        sympLabel = view.findViewById(R.id.SymptomsLabel);

        predictBtn = (Button) view.findViewById(R.id.predictBtn);

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

        plusBtn = (ImageView) view.findViewById(R.id.plusBtnImg);

        selectedSympList = (ListView) view.findViewById(R.id.selectedSymp);

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
