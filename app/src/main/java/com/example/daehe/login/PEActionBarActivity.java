package com.example.daehe.login;

import android.app.FragmentManager;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import java.util.ArrayList;

public class PEActionBarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;
    private User user;

    public User getUser(){
        return user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
            String name = acct.getDisplayName();
            String email = acct.getEmail();
            Uri photo = acct.getPhotoUrl();
            user = new User(name, email, photo, new ArrayList<Message>(), new ArrayList<Event>());
        }
    }
/*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
>>>>>>> master
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }
*/
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
                getSupportFragmentManager().beginTransaction( )
                        .replace(R.id.contentframe, new MessageFragment(), "Message")
                        .commit();
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
