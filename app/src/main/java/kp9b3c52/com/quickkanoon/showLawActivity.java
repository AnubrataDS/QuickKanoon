package kp9b3c52.com.quickkanoon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class showLawActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String fileName , lineNo ;
    Boolean isBookMarked = false;
    Menu ActionBar ;
    void doParse(ArrayList<String> str)
    {

        /*
        /n/ = Name of act , along with year
        /s/ = Section
        /o/ = Offence
        /p/ = Maximum punishment
        /c/ = Comments
        */
        TextView myText = (TextView) findViewById(R.id.lawText);
        String txt = "" ;
        for(String st : str)
        {
            //txt+=st;
            st = st.trim();
            if(st.startsWith("/n/"))
            {
                txt+="Act : "+st.substring(3)+"\n\n";
            }
            else if(st.startsWith("/s/"))
            {
                txt+="Section : "+st.substring(3)+"\n\n";
            }
            else if(st.startsWith("/o/"))
            {
                txt+="Offence : "+st.substring(3)+"\n\n";
            }
            else if(st.startsWith("/p/"))
            {
                txt+="Maximum penalty : "+st.substring(3)+"\n\n";
            }
            else if(st.startsWith("/c/"))
            {
                txt+="Comments : "+st.substring(3)+"\n\n";
            }
            else
            {
                txt+=st;
            }
        }
        myText.setText(txt);
        myText.setMovementMethod(new ScrollingMovementMethod());
    }

    void checkBookmark()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> temp = sharedPreferences.getStringSet("bookmarks",null);
        if(temp == null) {
            editor.putStringSet("bookmarks", new HashSet<String>()).apply();
            isBookMarked = false;
        }
        else{
            if(temp.contains(fileName+" "+lineNo))
            {
                isBookMarked = true ;
                ActionBar.findItem(R.id.action_bookmark).setIcon(R.drawable.ic_star_black_24dp);
            }
            else
            {
                isBookMarked = false ;
                ActionBar.findItem(R.id.action_bookmark).setIcon(R.drawable.ic_star_border_black_24dp);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_law);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), helpActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        fileName = intent.getStringExtra("fileName");
        lineNo = intent.getStringExtra("lineNo");
        AssetManager am = this.getBaseContext().getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName+".txt");
            GetHeaders obj = new GetHeaders(is);
            doParse(obj.getText(Integer.parseInt(lineNo)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_law, menu);
        ActionBar = menu ;
        checkBookmark();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, settingsActivity.class);
            startActivity(intent);
            return true;
        }

        if(id== R.id.action_bookmark){
            //checkBookmark();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            Set<String> temp = sharedPreferences.getStringSet("bookmarks",null);
            if(isBookMarked)
            {
                temp.remove(fileName+" "+lineNo);
                isBookMarked = false ;
                sharedPreferences.edit().putStringSet("bookmarks",temp).apply();
                ActionBar.findItem(R.id.action_bookmark).setIcon(R.drawable.ic_star_border_black_24dp);
                Toast.makeText(this, "Bookmark removed",
                        Toast.LENGTH_LONG).show();
            }
            else
            {
                temp.add(fileName+" "+lineNo);
                isBookMarked = true ;
                ActionBar.findItem(R.id.action_bookmark).setIcon(R.drawable.ic_star_black_24dp);
                sharedPreferences.edit().putStringSet("bookmarks",temp).apply();
                Toast.makeText(this, "Bookmark added",
                        Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if(id==R.id.nav_bookmarks){
            Intent intent = new Intent(this, bookmarksActivity.class);
            startActivity(intent);
        }

        if(id==R.id.nav_settings){
            Intent intent = new Intent(this, settingsActivity.class);
            startActivity(intent);
        }

        if(id==R.id.nav_home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if(id==R.id.nav_search){
            Intent intent = new Intent(this, searchActivity.class);
            startActivity(intent);
        }

        if(id==R.id.nav_help){
            Intent intent = new Intent(this, helpActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
