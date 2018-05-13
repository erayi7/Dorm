package com.example.eray.dorm;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyPerm extends ArrayAdapter<String> {

    private final ArrayList<String> statu;
    private final ArrayList<String> permNo;
    private final ArrayList<String> releaseDate;
    private final ArrayList<String> returnDate;
    private final ArrayList<String> decider;
    private final ArrayList<String> adress;
    private final Activity context;


    public MyPerm(ArrayList<String> statu, ArrayList<String> permNo, ArrayList<String> releaseDate, ArrayList<String> returnDate, ArrayList<String> decider, ArrayList<String> adress, Activity context) {

        super((Context) context,R.layout.myperm_view,permNo);
        this.statu = statu;
        this.permNo = permNo;
        this.releaseDate = releaseDate;
        this.returnDate = returnDate;
        this.decider = decider;
        this.adress = adress;
        this.context = (Activity) context;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View myPermView = layoutInflater.inflate(R.layout.myperm_view,null,true);

        TextView permNoText=myPermView.findViewById(R.id.permNoTextR);
        final TextView deciderText=myPermView.findViewById(R.id.deciderText);
        final TextView statuText = myPermView.findViewById(R.id.statuText);
        TextView relaeseText = myPermView.findViewById(R.id.releaseText);
        TextView returnText = myPermView.findViewById(R.id.returnText);
        TextView adressText = myPermView.findViewById(R.id.adressTextR);

        permNoText.setText("Permission No: "+permNo.get(position));
        deciderText.setText(decider.get(position));
        statuText.setText(statu.get(position));
        relaeseText.setText(releaseDate.get(position));
        returnText.setText(returnDate.get(position));
        adressText.setText(adress.get(position));

        final Button cancel=myPermView.findViewById(R.id.cancelList);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                myRef.child("Permissions").child(permNo.get(position)).child("Decider").setValue("Self");
                myRef.child("Permissions").child(permNo.get(position)).child("Statu").setValue("Canceled");
                cancel.setVisibility(View.INVISIBLE);
                statuText.setText("Canceled");
                deciderText.setText("Decider: Self");
                deciderText.setVisibility(View.VISIBLE);


            }
        });

        if (statu.get(position).equals("Pending")){
            deciderText.setVisibility(View.INVISIBLE);
        }
        else {
            myPermView.findViewById(R.id.cancelList).setVisibility(View.INVISIBLE);
        }

        return myPermView;


    }

}
