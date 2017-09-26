package kp9b3c52.com.quickkanoon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

public class bookmarksActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayAdapter adapter ;
    ArrayList<String> bookmarks , listNames;
    void getBookmarks()
    {
        bookmarks = new ArrayList<>();
        listNames = new ArrayList<>();
        AssetManager am = this.getBaseContext().getAssets();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> temp = sharedPreferences.getStringSet("bookmarks",null);
        for(String str : temp){
            bookmarks.add(str);
            //listNames.add(str);
            String msg[] = str.split(" ");
            InputStream is = null;
            try {
                is = am.open(msg[0]+".txt");
                GetHeaders obj = new GetHeaders(is);
                ArrayList<String> tem = obj.read();
                listNames.add(tem.get(Integer.parseInt(msg[1])));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!bookmarks.isEmpty()) {
            ListView myList = (ListView) findViewById(R.id.bookmarksListView);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listNames);
            myList.setAdapter(adapter);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(bookmarksActivity.this, showLawActivity.class);
                    String message = bookmarks.get(position);
                    String str[] = message.split(" ");
                    intent.putExtra("fileName", str[0]);
                    intent.putExtra("lineNo", str[1]);
                    startActivity(intent);
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
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

        getBookmarks();
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
        getMenuInflater().inflate(R.menu.bookmarks, menu);
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
