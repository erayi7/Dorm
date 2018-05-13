package com.example.eray.dorm;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Requests extends ArrayAdapter<String>{

    private final ArrayList<String> permNo;
    private final ArrayList<String> studentName;
    private final ArrayList<String> releaseDate;
    private final ArrayList<String> returnDate;
    private final ArrayList<String> adress;
    private final Activity context;


    public Requests(ArrayList<String> permNo, ArrayList<String> studentName, ArrayList<String> releaseDate, ArrayList<String> returnDate, ArrayList<String> adress, Activity context) {
        super(context,R.layout.request_view,permNo);
        this.permNo = permNo;
        this.studentName = studentName;
        this.releaseDate = releaseDate;
        this.returnDate = returnDate;
        this.adress = adress;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater= context.getLayoutInflater();
        View requestView = layoutInflater.inflate(R.layout.request_view,null,true);

        TextView permNoText = requestView.findViewById(R.id.permNoTextR);
        TextView studentNameText = requestView.findViewById(R.id.studentNameTextR);
        TextView releaseText=requestView.findViewById(R.id.releaseDateTextR);
        TextView returnText = requestView.findViewById(R.id.returnDateTextR);
        TextView adressText = requestView.findViewById(R.id.adressTextR);


        permNoText.setText("Permission No: "+permNo.get(position));
        studentNameText.setText(studentName.get(position));
        releaseText.setText(releaseDate.get(position));
        returnText.setText(returnDate.get(position));
        adressText.setText(adress.get(position));

        final Button confirm = requestView.findViewById(R.id.confirmR);
        final Button reject=requestView.findViewById(R.id.rejectR);

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference();
                FirebaseAuth mAuth;

                mAuth= FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                final String userMail = user.getEmail().toString() ;
                final String[] uID = new String[1];
                myRef.child("Managers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            if (userMail.equals(ds.child("Mail").getValue().toString())){
                                uID[0] =ds.child("ID").getValue().toString();
                                myRef.child("Permissions").child(permNo.get(position)).child("Decider").setValue(uID[0]);
                                break;
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                reject.setVisibility(View.INVISIBLE);
                confirm.setVisibility(View.INVISIBLE);
                myRef.child("Permissions").child(permNo.get(position)).child("Statu").setValue("Rejected");
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference();
                FirebaseAuth mAuth;

                mAuth= FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                final String userMail = user.getEmail().toString() ;
                final String[] uID = new String[1];
                myRef.child("Managers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            if (userMail.equals(ds.child("Mail").getValue().toString())){
                                uID[0] =ds.child("ID").getValue().toString();
                                myRef.child("Permissions").child(permNo.get(position)).child("Decider").setValue(uID[0]);
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                reject.setVisibility(View.INVISIBLE);
                confirm.setVisibility(View.INVISIBLE);
                myRef.child("Permissions").child(permNo.get(position)).child("Statu").setValue("Confirmed");

            }

        });

        return requestView;
    }
}
