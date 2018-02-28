package com.example.daehe.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.widget.DatePicker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.DatePickerDialog;

public class EventFragment extends Fragment {
    View myView;
    boolean checkdate=false;
    String dateis="";
    EditText date;
    final Calendar myCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.fragment_create_event,container,false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText id=(EditText) getView().findViewById(R.id.input_id);
        final EditText name=(EditText) getView().findViewById(R.id.input_name);
        final EditText location=(EditText) getView().findViewById(R.id.input_location);
        date =(EditText) getView().findViewById(R.id.input_date);

        final TextView check=(TextView) getView().findViewById(R.id.event_check);
        final Button confirm=(Button) getView().findViewById(R.id.event_button);

        final DatePickerDialog.OnDateSetListener datePick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), datePick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        /*
        if(checkdate==true) {
            date.setText(dateis);
            checkdate=false;
        }*/
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int num1=Integer.parseInt(id.getText().toString().trim());
                    int num2=Integer.parseInt((name.getText().toString().trim()));
                    int sum=0;
                    sum=num1+num2;

                    check.setText(String.valueOf(sum));
                }
            }
        );

    }

    private void updateLabel(){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(myCalendar.getTime()));
    }
}
