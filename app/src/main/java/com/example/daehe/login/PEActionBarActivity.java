package com.example.daehe.login;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PEActionBarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "PEActionBarActivity";
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;
    private User user;
    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<String> ids = new ArrayList<String>();
    private ArrayList<Event> allEvents = new ArrayList<Event> ();
    private boolean googleSignIn;
    private boolean facebookSignIn;
    ViewEventFragment view;
    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;
    private ListenerRegistration allEventRegistration;
    public static GoogleApiClient mGoogleApiClient;
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
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
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
                    for(int i = 1; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        getSupportFragmentManager().popBackStack();
                    }
                    getSupportFragmentManager().beginTransaction( )
                            .replace(R.id.contentframe, new EventFragment(), "Event")
                            .commit();
                }
                else
                {
                    for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        getSupportFragmentManager().popBackStack();
                    }

                    getSupportFragmentManager().beginTransaction( )
                            .replace(R.id.contentframe, new EventFragment(), "Event")
                            .addToBackStack(null)
                            .commit();
                }

                break;
            case R.id.nav_message:
                Toast.makeText(this, "This is message", Toast.LENGTH_SHORT).show();

                if(!(mf != null && mf.isVisible()))
                {
                    for(int i = 1; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        getSupportFragmentManager().popBackStack();
                    }
                    getSupportFragmentManager().beginTransaction( )
                            .replace(R.id.contentframe, new MessageFragment(), "Message")
                            .commit();
                }
                else
                {
                    for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                        getSupportFragmentManager().popBackStack();
                    }
                    getSupportFragmentManager().beginTransaction( )
                            .replace(R.id.contentframe, new MessageFragment(), "Message")
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case R.id.nav_viewEvent:
                view = new ViewEventFragment();
                Toast.makeText(this,"Viewing events", Toast.LENGTH_SHORT).show();
                if(!(mf != null && mf.isVisible()))
                {
                    for(int i = 1; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        getSupportFragmentManager().popBackStack();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentframe, view, "View Event")
                            .commit();
                }
                else
                {
                    for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                        getSupportFragmentManager().popBackStack();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentframe, view, "View Event")
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case R.id.nav_findEvent:
                if(!(mf != null && mf.isVisible()))
                {
                    for(int i = 1; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        getSupportFragmentManager().popBackStack();
                    }
                    getSupportFragmentManager().beginTransaction( )
                            .replace(R.id.contentframe, new JoinEventFragment(), "Join Event")
                            .commit();
                }
                else
                {
                    for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                        getSupportFragmentManager().popBackStack();
                    }
                    getSupportFragmentManager().beginTransaction( )
                            .replace(R.id.contentframe, new JoinEventFragment(), "Join Event")
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case R.id.nav_signOut:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to sign out?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                signOut();
                                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("login", false);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        //Set your icon here
                        .setTitle("Sign Out")
                        .setIcon(R.drawable.ic_signout);
                AlertDialog alert = builder.create();
                alert.show();//showing the dialog
            default:
                return false;
        }
        mDrawerLayout.closeDrawers();
        return false;
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                        Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                    }
                });
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
            listenerRegistration.remove();
            allEventRegistration.remove();
            super.onBackPressed();
        }
    }

    private void getEventsFromDB()
    {
        if(googleSignIn)
        {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            listenerRegistration = db.collection("Events")
                    .document(acct.getId())
                    .collection("Events")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            events.clear();
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
                    });
        }

        if(facebookSignIn)
        {
            listenerRegistration = db.collection("Events")
                    .document(LoginActivity.GetFacebookID())
                    .collection("Events")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
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
                    });
        }
    }

    private void GetAllEventsDB()
    {
        allEventRegistration = db.collection("All Events")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        allEvents.clear();
                        List<Event> types = documentSnapshots.toObjects(Event.class);
                        // Add all to your list
                        allEvents.addAll(types);

                        Log.d(TAG, "onSuccess: " + events);
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

    public ViewEventFragment getView() { return view; }
}
