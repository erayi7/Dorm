package com.example.eray.dorm;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
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

public class Activity2 extends AppCompatActivity {

    private EditText searchText;
    private TextView helpText;
    private ConstraintLayout requestLayout;
    private ConstraintLayout resultedLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    ListView listView;
    Requests adapter;
    ArrayList<String> permNoFromFB;
    ArrayList<String> studentNameFromFB;
    ArrayList<String> releaseDateFromFB;
    ArrayList<String> returnDateFromFB;
    ArrayList<String> adressFromFB;

    ListView listView2;
    Resulted adapter2;
    ArrayList<String> permNoFromFB2;
    ArrayList<String> studentNameFromFB2;
    ArrayList<String> releaseDateFromFB2;
    ArrayList<String> returnDateFromFB2;
    ArrayList<String> adressFromFB2;
    ArrayList<String> deciderFromFB2;
    ArrayList<String> statuFromFB2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        helpText=(TextView) findViewById(R.id.helpText);
        searchText= (EditText) findViewById(R.id.searchText);
        requestLayout = (ConstraintLayout) findViewById(R.id.requestLayout);
        resultedLayout = (ConstraintLayout) findViewById(R.id.resultedLayout);


        permNoFromFB=new ArrayList<String>();
        studentNameFromFB=new ArrayList<String>();
        releaseDateFromFB=new ArrayList<String>();
        returnDateFromFB=new ArrayList<String>();
        adressFromFB=new ArrayList<String>();

        permNoFromFB2=new ArrayList<String>();
        studentNameFromFB2=new ArrayList<String>();
        releaseDateFromFB2=new ArrayList<String>();
        returnDateFromFB2=new ArrayList<String>();
        adressFromFB2=new ArrayList<String>();
        deciderFromFB2=new ArrayList<String>();
        statuFromFB2=new ArrayList<String>();


        listView = findViewById(R.id.requestList);
        adapter = new Requests(permNoFromFB, studentNameFromFB, releaseDateFromFB, returnDateFromFB, adressFromFB, this);

        listView2=findViewById(R.id.resultedList);
        adapter2=new Resulted(permNoFromFB2, studentNameFromFB2, releaseDateFromFB2, returnDateFromFB2, adressFromFB2, deciderFromFB2, statuFromFB2, this);




        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userMail = user.getEmail().toString() ;
        myRef.child("Managers").addListenerForSingleValueEvent(new ValueEventListener() {
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

        request();


    }


    public void request(){

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                permNoFromFB.clear();
                studentNameFromFB.clear();
                releaseDateFromFB.clear();
                returnDateFromFB.clear();
                adressFromFB.clear();
                for (DataSnapshot ds:dataSnapshot.child("Permissions").getChildren()){
                    if (ds.child("Statu").getValue().toString().equals("Pending")){
                        permNoFromFB.add(0,ds.child("PermNo").getValue().toString());
                        studentNameFromFB.add(0,dataSnapshot.child("Students").child(ds.child("StudentID").getValue().toString()).child("Name").getValue().toString()+" "+dataSnapshot.child("Students").child(ds.child("StudentID").getValue().toString()).child("Surname").getValue().toString());
                        releaseDateFromFB.add(0,"Release Date: "+ds.child("ReleaseDate").getValue().toString());
                        returnDateFromFB.add(0,"Return Date: "+ds.child("ReturnDate").getValue().toString());
                        adressFromFB.add(0,"Adress: "+dataSnapshot.child("Students").child(ds.child("StudentID").getValue().toString()).child("Adresses").child(ds.child("AdressNo").getValue().toString()).getValue().toString());
                    }

                }

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void resulted(){

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                permNoFromFB2.clear();
                studentNameFromFB2.clear();
                releaseDateFromFB2.clear();
                returnDateFromFB2.clear();
                adressFromFB2.clear();
                statuFromFB2.clear();
                deciderFromFB2.clear();
                for (DataSnapshot ds:dataSnapshot.child("Permissions").getChildren()){
                    if (ds.child("Statu").getValue().toString().equals("Pending")){

                    }
                    else {
                        if (searchText.getText().toString().equals("")){

                            if (ds.child("Statu").getValue().toString().equals("Canceled")){
                                deciderFromFB2.add(0,"Decider: Self");
                            }
                            else {

                                deciderFromFB2.add(0,"Decider: "+dataSnapshot.child("Managers").child(ds.child("Decider").getValue().toString()).child("Name").getValue().toString()+" "+dataSnapshot.child("Managers").child(ds.child("Decider").getValue().toString()).child("Surname").getValue().toString());


                            }
                            permNoFromFB2.add(0,"Permission No: "+ds.child("PermNo").getValue().toString());
                            studentNameFromFB2.add(0,dataSnapshot.child("Students").child(ds.child("StudentID").getValue().toString()).child("Name").getValue().toString()+" "+dataSnapshot.child("Students").child(ds.child("StudentID").getValue().toString()).child("Surname").getValue().toString());
                            releaseDateFromFB2.add(0,"Release Date: "+ds.child("ReleaseDate").getValue().toString());
                            returnDateFromFB2.add(0,"Return Date: "+ds.child("ReturnDate").getValue().toString());
                            adressFromFB2.add(0,"Adress: "+dataSnapshot.child("Students").child(ds.child("StudentID").getValue().toString()).child("Adresses").child(ds.child("AdressNo").getValue().toString()).getValue().toString());
                            statuFromFB2.add(0,ds.child("Statu").getValue().toString());
                        }
                        else {
                            if (dataSnapshot.child("Students").child(ds.child("StudentID").getValue().toString()).child("Name").getValue().toString().equals(searchText.getText().toString())){

                                if (ds.child("Statu").getValue().toString().equals("Canceled")){
                                    deciderFromFB2.add(0,"Decider: Self");
                                }
                                else {

                                    deciderFromFB2.add(0,"Decider: "+dataSnapshot.child("Managers").child(ds.child("Decider").getValue().toString()).child("Name").getValue().toString()+" "+dataSnapshot.child("Managers").child(ds.child("Decider").getValue().toString()).child("Surname").getValue().toString());


                                }
                                permNoFromFB2.add(0,"Permission No: "+ds.child("PermNo").getValue().toString());
                                studentNameFromFB2.add(0,dataSnapshot.child("Students").child(ds.child("StudentID").getValue().toString()).child("Name").getValue().toString()+" "+dataSnapshot.child("Students").child(ds.child("StudentID").getValue().toString()).child("Surname").getValue().toString());
                                releaseDateFromFB2.add(0,"Release Date: "+ds.child("ReleaseDate").getValue().toString());
                                returnDateFromFB2.add(0,"Return Date: "+ds.child("ReturnDate").getValue().toString());
                                adressFromFB2.add(0,"Adress: "+dataSnapshot.child("Students").child(ds.child("StudentID").getValue().toString()).child("Adresses").child(ds.child("AdressNo").getValue().toString()).getValue().toString());
                                statuFromFB2.add(0,ds.child("Statu").getValue().toString());


                            }

                        }


                    }

                }

                listView2.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void resultedButton (View view){

        helpText.setVisibility(View.INVISIBLE);
        resultedLayout.setVisibility(View.VISIBLE);
        requestLayout.setVisibility(View.INVISIBLE);
        resulted();

    }


    public void requestButton (View view){

        helpText.setVisibility(View.INVISIBLE);
        requestLayout.setVisibility(View.VISIBLE);
        resultedLayout.setVisibility(View.INVISIBLE);
        request();


    }
}
