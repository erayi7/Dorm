package com.example.eray.dorm;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Resulted extends ArrayAdapter<String> {

    private final ArrayList<String> permNo;
    private final ArrayList<String> studentName;
    private final ArrayList<String> releaseDate;
    private final ArrayList<String> returnDate;
    private final ArrayList<String> adress;
    private final ArrayList<String> decider;
    private final ArrayList<String> statu;
    private final Activity context;


    public Resulted(ArrayList<String> permNo, ArrayList<String> studentName, ArrayList<String> releaseDate, ArrayList<String> returnDate, ArrayList<String> adress, ArrayList<String> decider, ArrayList<String> statu, Activity context) {
        super(context,R.layout.resulted_view,permNo);
        this.permNo = permNo;
        this.studentName = studentName;
        this.releaseDate = releaseDate;
        this.returnDate = returnDate;
        this.adress = adress;
        this.decider = decider;
        this.statu = statu;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater= context.getLayoutInflater();
        View resultedView = layoutInflater.inflate(R.layout.resulted_view,null,true);

        TextView permNoText = resultedView.findViewById(R.id.permNoTextResult);
        TextView studentNameText=resultedView.findViewById(R.id.studentNameTextResult);
        TextView releaseDateText=resultedView.findViewById(R.id.releaseDateTextResult);
        TextView returnDateText=resultedView.findViewById(R.id.returnDateTextResult);
        TextView adressText=resultedView.findViewById(R.id.adressTextResult);
        TextView deciderText=resultedView.findViewById(R.id.decirderTextResult);
        TextView statuText=resultedView.findViewById(R.id.statuTextResult);

        permNoText.setText(permNo.get(position));
        studentNameText.setText(studentName.get(position));
        releaseDateText.setText(releaseDate.get(position));
        returnDateText.setText(returnDate.get(position));
        adressText.setText(adress.get(position));
        deciderText.setText(decider.get(position));
        statuText.setText(statu.get(position));


        return resultedView;
    }
}
