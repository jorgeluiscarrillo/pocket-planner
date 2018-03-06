package com.example.daehe.login;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bryan on 3/6/2018.
 */

public class UpdateEventDialog extends Dialog {
    private String eventName;
    private String eventLoc;
    private String eventDate;
    private Activity c;
    private Dialog d;
    private FirebaseFirestore db;
    private TextView title;
    private EditText date;
    private Calendar myCalendar = Calendar.getInstance();
    private EditText name;
    private EditText location;
    private Button confirm;
    private Event updatedEvent;

    public UpdateEventDialog(Activity a, Event e)
    {
        super(a);
        this.c = a;
        updatedEvent = e;
        db = FirebaseFirestore.getInstance();
    }

    public void ShowDialog()
    {
        d = new Dialog(c);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.fragment_create_event);

        title = (TextView) d.findViewById(R.id.dialog_title);
        name=(EditText) d.findViewById(R.id.input_name);
        location=(EditText) d.findViewById(R.id.input_location);
        date =(EditText) d.findViewById(R.id.input_date);
        confirm=(Button) d.findViewById(R.id.event_button);

        String myFormat = "MM/dd/yy";
        Date eDate = updatedEvent.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        title.setText("Update Event");
        name.setText(updatedEvent.getName());
        location.setText(updatedEvent.getLocation());
        date.setText(sdf.format(eDate));

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

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventName = name.getText().toString();
                eventLoc = location.getText().toString();
                eventDate = date.getText().toString();

                if(eventName.equals("") && eventLoc.equals("") && eventDate.equals(""))
                {
                    Toast.makeText(c, "Name, Location, Date is blank", Toast.LENGTH_SHORT).show();
                }
                else if(eventName.equals("") && eventLoc.equals(""))
                {
                    Toast.makeText(c, "Name and Location is blank", Toast.LENGTH_SHORT).show();
                }
                else if(eventName.equals("") && eventDate.equals(""))
                {
                    Toast.makeText(c, "Name and Date is blank", Toast.LENGTH_SHORT).show();
                }
                else if(eventLoc.equals("") && eventDate.equals(""))
                {
                    Toast.makeText(c, "Location and Date is blank", Toast.LENGTH_SHORT).show();
                }
                else if(eventName.equals(""))
                {
                    Toast.makeText(c, "Name is blank", Toast.LENGTH_SHORT).show();
                }
                else if(eventDate.equals(""))
                {
                    Toast.makeText(c, "Date is blank", Toast.LENGTH_SHORT).show();
                }
                else if(eventLoc.equals(""))
                {
                    Toast.makeText(c, "Location is blank", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String myFormat = "MM/dd/yy";
                    Date inputDate = null;
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    try{
                        inputDate = sdf.parse(eventDate);
                    }
                    catch(ParseException e)
                    {

                    }
                    updatedEvent.setName(eventName);
                    updatedEvent.setLocation(eventLoc);
                    updatedEvent.setDate(inputDate);

                    int pos = MainActivity.events.indexOf(updatedEvent);

                    db.collection("Events")
                            .document("user")
                            .collection("Events")
                            .document(MainActivity.ids.get(pos))
                            .set(updatedEvent);
                    d.dismiss();
                }
            }
        });
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
