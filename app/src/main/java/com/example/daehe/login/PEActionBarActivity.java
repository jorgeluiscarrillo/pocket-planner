package com.example.daehe.login;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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

import java.io.InputStream;
import java.util.Map;

public class PEActionBarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;
    private User user;

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
            String id = acct.getId();
            Uri photo = acct.getPhotoUrl();
            user = new User(name, email, id, photo);
        }

        View hView =  navigationView.getHeaderView(0);
        if(user.getImage() != null)
            new DownloadImageTask((ImageView) hView.findViewById(R.id.nav_image_view)).execute(user.getImage().toString());
        TextView navTxt = (TextView)hView.findViewById(R.id.nav_text_view);
        navTxt.setText(user.getName());
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
        switch (id) {
            case R.id.home_button:
                MapFragment mf = (MapFragment) getSupportFragmentManager().findFragmentByTag("MAP");
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
                        .replace(R.id.contentframe, new MessageFragment())
                        .commit();
                break;
            default:
                return false;
        }
        mDrawerLayout.closeDrawers();
        return false;
    }

}
