package com.example.daehe.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends PEActionBarActivity {

    public static ArrayList<Event> events = new ArrayList<Event>();
    public static ArrayList<String> ids = new ArrayList<String>();
    private static boolean retrieved = false;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setMenuBar(R.layout.activity_content);
        db = FirebaseFirestore.getInstance();
        if(!retrieved)
        {
            getEventsFromDB();
            retrieved = true;
        }

        getSupportFragmentManager().beginTransaction( )
                .replace(R.id.contentframe, new MapFragment(), "MAP")
                .commit();
    }

    private void getEventsFromDB()
    {
        db.collection("Events")
                .document("user")
                .collection("Events")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if(documentSnapshots.isEmpty())
                        {
                            Log.d(TAG,"onSuccess: LIST EMPTY");
                        }
                        else
                        {
                            for(DocumentSnapshot document : documentSnapshots.getDocuments())
                            {
                                ids.add(document.getId());
                            }
                            //Toast.makeText(getApplicationContext(),"ID: " + ids.get(0), Toast.LENGTH_SHORT).show();
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<Event> types = documentSnapshots.toObjects(Event.class);
                            // Add all to your list
                            events.addAll(types);

                            Log.d(TAG, "onSuccess: " + events);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}