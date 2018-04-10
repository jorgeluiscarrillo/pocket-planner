package com.example.daehe.login;

import android.app.FragmentManager;
import android.net.Uri;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PEActionBarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "PEActionBarActivity";
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;
    private User user;
    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<String> ids = new ArrayList<String>();
    private ArrayList<Event> allEvents = new ArrayList<> ();
    private boolean googleSignIn;
    private boolean facebookSignIn;
    private FirebaseFirestore db;
    public User getUser(){
        return user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
    }

    protected void setMenuBar(int layout){
        setContentView(layout);
        db = FirebaseFirestore.getInstance();

        googleSignIn = false;
        facebookSignIn = false;

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

        if(LoginActivity.mGoogleApiClient != null )
        {
            googleSignIn = true;
            facebookSignIn = false;
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String id = acct.getId();
                String name = acct.getDisplayName();
                String email = acct.getEmail();
                Uri photo = acct.getPhotoUrl();
                if (photo != null) {
                    user = new User(acct.getId(),name, email, photo.toString(), new ArrayList<Message>(), new ArrayList<Event>());
                } else {
                    user = new User(acct.getId(),name, email, null, new ArrayList<Message>(), new ArrayList<Event>());
                }
                View hView =  navigationView.getHeaderView(0);
                if(user.getImage() != null)
                    new DownloadImageTask((ImageView) hView.findViewById(R.id.nav_image_view)).execute(user.getImage().toString());
                TextView navTxt = (TextView) hView.findViewById(R.id.nav_text_view);
                navTxt.setText(user.getName());
            }
        }
        if(LoginActivity.isLoggedInFB())
        {
            googleSignIn = false;
            facebookSignIn = true;
            String name = LoginActivity.GetDisplayName();
            View hView =  navigationView.getHeaderView(0);
            new DownloadImageTask((ImageView) hView.findViewById(R.id.nav_image_view)).execute("https://graph.facebook.com/" + LoginActivity.GetFacebookID() + "/picture?type=large");
            user = new User(LoginActivity.GetFacebookID(),name,LoginActivity.GetFacebookEmail(),"https://graph.facebook.com/" + LoginActivity.GetFacebookID() + "/picture?type=large", new ArrayList<Message>(), new ArrayList<Event>());
            TextView navTxt = (TextView) hView.findViewById(R.id.nav_text_view);
            navTxt.setText(name);
        }

        DocumentReference doc = db.collection("Users").document();


        db.collection("Users")
                .document(user.getID())
                .set(user);

        getEventsFromDB();
        GetAllEventsDB();
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
            db.collection("Users")
                    .document(user.getID())
                    .set(user);
            super.onBackPressed();
        }
    }

    private void getEventsFromDB()
    {
        if(googleSignIn)
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

        if(facebookSignIn)
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

    private void GetAllEventsDB()
    {
        db.collection("All Events")
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
                            //Toast.makeText(getApplicationContext(),"ID: " + ids.get(0), Toast.LENGTH_SHORT).show();
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<Event> types = documentSnapshots.toObjects(Event.class);
                            // Add all to your list
                            allEvents.addAll(types);

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

    public void AddEvents(Event e)
    {
        user.getEvents().add(e);
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

    public ArrayList<Event> GetAllEvents() { return allEvents; }

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

    public boolean GetGoogleSignIn()
    {
        return googleSignIn;
    }

    public boolean GetFacebookSignIn()
    {
        return facebookSignIn;
    }
}
