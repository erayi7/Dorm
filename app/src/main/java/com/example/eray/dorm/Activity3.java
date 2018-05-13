package com.example.eray.dorm;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity3 extends AppCompatActivity {


    private TextView helpText;
    private ConstraintLayout layout1;
    private ConstraintLayout layout2;
    private EditText releaseDateText;
    private EditText returnDateText;

    private String adressno="1";
    private String releaseDate;
    private String returnDate;



    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private Spinner spinnerAdress;
    private ArrayAdapter<String> dataAdapterForAdress;
    ArrayList<String> adressList;

    HashMap<String , Object> hashMap;


    ArrayList<String> permNoFromFB;
    ArrayList<String> deciderFromFB;
    ArrayList<String> releaseFromFB;
    ArrayList<String> returnFromFB;
    ArrayList<String> statuFromFB;
    ArrayList<String> adresFromFB;


    ListView listView;
    MyPerm adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);


        permNoFromFB=new ArrayList<String>();
        deciderFromFB=new ArrayList<String>();
        releaseFromFB=new ArrayList<String>();
        returnFromFB=new ArrayList<String>();
        statuFromFB=new ArrayList<String>();
        adresFromFB=new ArrayList<String>();
        listView = findViewById(R.id.myPerm);


        helpText=(TextView) findViewById(R.id.helpText);
        layout1 = (ConstraintLayout) findViewById(R.id.Layout1);
        layout2=(ConstraintLayout) findViewById(R.id.Layout2);
        releaseDateText=(EditText) findViewById(R.id.editText3);
        returnDateText=(EditText) findViewById(R.id.editText4);


        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userMail = user.getEmail().toString() ;
        myRef.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    if (userMail.equals(ds.child("Mail").getValue().toString())){
                        //Log.e("OLDU    ",ds.child("Mail").getValue().toString());
                        Toast.makeText(getApplicationContext(),"Welcome "+ds.child("Name").getValue().toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//HOŞGELDİNİZ DER

        adressList = new ArrayList<>();
        spinnerAdress = (Spinner) findViewById(R.id.spinner);

        final String[] uID = new String[1];
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.child("Students").getChildren()){
                    if (userMail.equals(ds.child("Mail").getValue().toString())){
                        uID[0] =ds.child("ID").getValue().toString();
                        break;
                    }
                }
                adressList.clear();
                for (DataSnapshot deneme:dataSnapshot.child("Students").child(uID[0]).child("Adresses").getChildren()) {
                    adressList.add(deneme.getValue().toString());
                }
                spinnerAdress = (Spinner) findViewById(R.id.spinner);
                dataAdapterForAdress = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, adressList);
                dataAdapterForAdress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAdress.setAdapter(dataAdapterForAdress);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); //Burada adres ekler


        adapter = new MyPerm(statuFromFB,permNoFromFB,releaseFromFB,returnFromFB,deciderFromFB,adresFromFB,this);

    }


    public void createbutton (View view){
        spinnerAdress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adressno=Integer.toString(position+1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.INVISIBLE);
        helpText.setVisibility(View.INVISIBLE);

    }

    public void cancelbutton (View view){
        layout1.setVisibility(View.INVISIBLE);
        helpText.setVisibility(View.VISIBLE);
    }


    public void confirmbutton (View view){
        releaseDate=releaseDateText.getText().toString();
        returnDate=returnDateText.getText().toString();
        helpText.setVisibility(View.VISIBLE);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userMail = user.getEmail().toString() ;
        final String[] ID = new String[1];

        Toast.makeText(getApplicationContext(),"Permission created",Toast.LENGTH_LONG).show();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.child("Students").getChildren()){
                    if (userMail.equals(ds.child("Mail").getValue().toString())){
                        ID[0] =ds.child("ID").getValue().toString();
                        break;
                    }
                }

                String last = Long.toString(dataSnapshot.child("Permissions").getChildrenCount()+1);

                myRef.child("Permissions").child(last).child("AdressNo").setValue(adressno);
                myRef.child("Permissions").child(last).child("ReleaseDate").setValue(releaseDate);
                myRef.child("Permissions").child(last).child("ReturnDate").setValue(returnDate);
                myRef.child("Permissions").child(last).child("Statu").setValue("Pending");
                myRef.child("Permissions").child(last).child("PermNo").setValue(last);
                myRef.child("Permissions").child(last).child("Decider").setValue("null");
                myRef.child("Permissions").child(last).child("StudentID").setValue(ID[0]);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        releaseDateText.setText("");
        returnDateText.setText("");
        layout1.setVisibility(View.INVISIBLE);
    }


    public void mypermbutton(View view){
        layout2.setVisibility(View.VISIBLE);
        layout1.setVisibility(View.INVISIBLE);
        helpText.setVisibility(View.INVISIBLE);

        myperm();


    }

    public void myperm(){
        final String[] uID = new String[1];

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAuth=FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                final String userMail = user.getEmail().toString() ;

                permNoFromFB.clear();
                deciderFromFB.clear();
                releaseFromFB.clear();
                returnFromFB.clear();
                statuFromFB.clear();
                adresFromFB.clear();
                for (DataSnapshot ds: dataSnapshot.child("Students").getChildren()){
                    if (userMail.equals(ds.child("Mail").getValue().toString())){
                        uID[0] =ds.child("ID").getValue().toString();
                    }
                }

                for (DataSnapshot ds:dataSnapshot.child("Permissions").getChildren()){

                    if (ds.child("StudentID").getValue().toString().equals(uID[0])){
                        if (ds.child("Statu").getValue().toString().equals("Pending")){
                            deciderFromFB.add(0,"null");
                        }
                        else {
                            if (ds.child("Statu").getValue().toString().equals("Canceled")){
                                deciderFromFB.add(0,"Decider: Self");
                            }
                            else {
                                deciderFromFB.add(0,"Decider: "+dataSnapshot.child("Managers").child(ds.child("Decider").getValue().toString()).child("Name").getValue().toString()+" "+dataSnapshot.child("Managers").child(ds.child("Decider").getValue().toString()).child("Surname").getValue().toString());

                            }

                        }

                        permNoFromFB.add(0,ds.child("PermNo").getValue().toString());
                        releaseFromFB.add(0,"Release Date: "+ds.child("ReleaseDate").getValue().toString());
                        returnFromFB.add(0,"Return Date: "+ds.child("ReturnDate").getValue().toString());
                        statuFromFB.add(0,ds.child("Statu").getValue().toString());
                        adresFromFB.add(0,"Adress: "+dataSnapshot.child("Students").child(ds.child("StudentID").getValue().toString()).child("Adresses").child(ds.child("AdressNo").getValue().toString()).getValue().toString());
                        adapter.notifyDataSetChanged();

                    }
                }


                listView.setAdapter(adapter);
                //Log.e("perm",permNoFromFB.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
