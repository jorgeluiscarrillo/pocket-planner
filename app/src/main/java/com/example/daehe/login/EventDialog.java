package com.example.daehe.login;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Bryan on 2/28/2018.
 */

public class EventDialog extends Dialog {
    public Activity c;
    public Dialog d;
    public Button add;
    EditText date;
    Calendar myCalendar = Calendar.getInstance();

    public EventDialog(Activity a)
    {
        super(a);
        this.c = a;
    }

    public void ShowDialog()
    {
        d = new Dialog(c);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.fragment_create_event);

        final EditText id=(EditText) findViewById(R.id.input_id);
        final EditText name=(EditText) findViewById(R.id.input_name);
        final EditText location=(EditText) d.findViewById(R.id.input_location);
        date =(EditText) d.findViewById(R.id.input_date);

        final TextView check=(TextView) d.findViewById(R.id.event_check);
        final Button confirm=(Button) d.findViewById(R.id.event_button);

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
                new DatePickerDialog(c, datePick, myCalendar
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
        d.show();

        Window window = d.getWindow();
        window.setLayout(1000,1000);
    }

    private void updateLabel(){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(myCalendar.getTime()));
    }

}
