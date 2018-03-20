package com.example.daehe.login;

import android.app.FragmentManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class PEActionBarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "PEActionBarActivity";
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;
    private User user;
    private List<User> u;
    private FirebaseFirestore db;

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

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String id = acct.getId();
            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String photo = acct.getPhotoUrl().toString();
            user = new User(id, name, email, photo, new ArrayList<Message>(), new ArrayList<Event>());
            /*if (true) {
                DocumentReference doc = db.collection("Users").document();

                db.collection("Users")
                        .document(user.getID())
                        .set(user);

            }*/

            db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    for(DocumentSnapshot ds : documentSnapshots){
                        Log.d(TAG, "On event: " + ds.getString("name"));
                    }
                }
            });
        }
        View hView =  navigationView.getHeaderView(0);
        if(user.getImage() != null)
            new DownloadImageTask((ImageView) hView.findViewById(R.id.nav_image_view)).execute(user.getImage().toString());
        TextView navTxt = (TextView)hView.findViewById(R.id.nav_text_view);
        navTxt.setText(user.getName());
        Toast.makeText(this, u.get(0).getID(), Toast.LENGTH_SHORT).show();
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
                    getSupportFragmentManager().beginTransaction( )
                            .replace(R.id.contentframe, new MapFragment(), "MAP")
                            .commit();
                }
                else
                {
                    Toast.makeText(this, "Already in home", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.nav_createEvent:
                EventFragment ef = (EventFragment) getSupportFragmentManager().findFragmentByTag("CreateEvent");
                if(!(ef !=null && ef.isVisible()))
                {
                    Toast.makeText(this, "Creating an event", Toast.LENGTH_SHORT).show();
                    EventDialog ed = new EventDialog(this);
                    ed.ShowDialog();
                }
                else
                {
                    Toast.makeText(this, "Already creating an event", Toast.LENGTH_SHORT).show();
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


}
