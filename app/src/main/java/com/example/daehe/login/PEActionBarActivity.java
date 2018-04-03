package com.example.daehe.login;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
<<<<<<< HEAD
=======
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
>>>>>>> 853684c718907ed2f2f76cc18183016dbe920a5d
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
=======
import java.util.Locale;

import static android.content.ContentValues.TAG;
>>>>>>> 853684c718907ed2f2f76cc18183016dbe920a5d

public class PEActionBarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "PEActionBarActivity";
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;
    private User user;
    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<String> ids = new ArrayList<String>();
<<<<<<< HEAD
    private FirebaseFirestore db;
=======
    FirebaseFirestore db;
>>>>>>> 853684c718907ed2f2f76cc18183016dbe920a5d

    public User getUser(){
        return user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    protected void setMenuBar(int layout){
        setContentView(layout);
        db = FirebaseFirestore.getInstance();
        getEventsFromDB();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mToggle.syncState();

        if(LoginActivity.mGoogleApiClient != null && LoginActivity.mGoogleApiClient.isConnected())
        {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String id = acct.getId();
                String name = acct.getDisplayName();
                String email = acct.getEmail();
<<<<<<< HEAD
                String photo = acct.getPhotoUrl().toString();
                user = new User(id, name, email, photo, new ArrayList<Message>(), new ArrayList<Event>());
=======
                Uri photo = acct.getPhotoUrl();
                if (photo != null) {
                    user = new User(acct.getId(),name, email, photo.toString(), new ArrayList<Message>(), new ArrayList<Event>());
                } else {
                    user = new User(acct.getId(),name, email, null, new ArrayList<Message>(), new ArrayList<Event>());
                }
>>>>>>> 853684c718907ed2f2f76cc18183016dbe920a5d
                View hView =  navigationView.getHeaderView(0);
                if(user.getImage() != null)
                    new DownloadImageTask((ImageView) hView.findViewById(R.id.nav_image_view)).execute(user.getImage().toString());
                TextView navTxt = (TextView) hView.findViewById(R.id.nav_text_view);
                navTxt.setText(user.getName());
            }
        }
        if(LoginActivity.isLoggedInFB())
        {
            String name = LoginActivity.GetDisplayName();
            View hView =  navigationView.getHeaderView(0);
            new DownloadImageTask((ImageView) hView.findViewById(R.id.nav_image_view)).execute("https://graph.facebook.com/" + LoginActivity.GetFacebookID() + "/picture?type=large");
            TextView navTxt = (TextView) hView.findViewById(R.id.nav_text_view);
            navTxt.setText(name);
        }
<<<<<<< HEAD

        DocumentReference doc = db.collection("Users").document();

        db.collection("Users")
                .document(user.getID())
                .set(user);
=======
>>>>>>> 853684c718907ed2f2f76cc18183016dbe920a5d
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        MapFragment mf = (MapFragment) getSupportFragmentManager().findFragmentByTag("MAP");
        switch (id) {
            case R.id.home_button:
                if(!(mf != null && mf.isVisible()))
                {
                    Toast.makeText(this, "Returning to home", Toast.LENGTH_SHORT).show();
                    for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        getSupportFragmentManager().popBackStack();
                    }
                }
                else
                {
                    Toast.makeText(this, "Already in home", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.nav_createEvent:
                EventFragment ef = (EventFragment) getSupportFragmentManager().findFragmentByTag("CreateEvent");
                if(!(mf != null && mf.isVisible()))
                {
                    getSupportFragmentManager().beginTransaction( )
                            .replace(R.id.contentframe, new EventFragment(), "Event")
                            .commit();
                }
                else
                {
                    getSupportFragmentManager().beginTransaction( )
                            .replace(R.id.contentframe, new EventFragment(), "Event")
                            .addToBackStack(null)
                            .commit();
                }

                break;
            case R.id.nav_other:
                Toast.makeText(this, "This is other", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_message:
                Toast.makeText(this, "This is message", Toast.LENGTH_SHORT).show();

                if(!(mf != null && mf.isVisible()))
                {
                    getSupportFragmentManager().beginTransaction( )
                            .replace(R.id.contentframe, new MessageFragment(), "Message")
                            .commit();
                }
                else
                {
                    getSupportFragmentManager().beginTransaction( )
                            .replace(R.id.contentframe, new MessageFragment(), "Message")
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case R.id.nav_viewEvent:
                Toast.makeText(this,"Viewing events", Toast.LENGTH_SHORT).show();
                if(!(mf != null && mf.isVisible()))
                {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentframe, new ViewEventFragment(), "View Event")
                            .commit();
                }
                else
                {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentframe, new ViewEventFragment(), "View Event")
                            .addToBackStack(null)
                            .commit();
                }
                break;
            default:
                return false;
        }
        mDrawerLayout.closeDrawers();
        return false;
    }

    public void onBackPressed()
    {
        FragmentManager fm = getFragmentManager();
        if(fm.getBackStackEntryCount()>0 )
        {
            fm.popBackStack();
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void getEventsFromDB()
    {
        if(LoginActivity.mGoogleApiClient != null)
        {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            db.collection("Events")
                    .document(acct.getId())
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

        if(LoginActivity.isLoggedInFB())
        {
            db.collection("Events")
                    .document(LoginActivity.GetFacebookID())
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

    public void AddEvents(Event e)
    {
        events.add(e);
    }

    public void RemoveEvents(Event e)
    {
        events.remove(e);
    }

    public ArrayList<Event> GetEvents()
    {
        return events;
    }

    public Event getEventFromList(int i)
    {
        return events.get(i);
    }

    public ArrayList<String> GetEventIds()
    {
        return ids;
    }

    public void AddId(String s)
    {
        ids.add(s);
    }
}
