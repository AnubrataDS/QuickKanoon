package kp9b3c52.com.quickkanoon;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class searchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public final static String EXTRA_MESSAGE = "tagGroupNo";
    ArrayAdapter adapter ;
    ArrayList<ArrayList<String>> tagListList;
    ArrayList<Integer> tagNo;
    ArrayList<String> displayList;
    String tagGroupName[] ;
    void doSearch(String str){
        TextView myText = (TextView) findViewById(R.id.searchResText);
        myText.setText("Search results for : "+str);
        ListView myList = (ListView) findViewById(R.id.searchListView);
        displayList = new ArrayList<>();
        tagNo = new ArrayList<>();
        if(!str.equals("")) {
            str = str.toLowerCase();
            //tagNo = new ArrayList<>();
            Pattern p = Pattern.compile("^.*" + str + ".*$");
            for (int i = 0; i < tagListList.size(); i++) {
                ArrayList<String> temp = tagListList.get(i);
                for (String st : temp) {
                    Matcher m = p.matcher(st);
                    if (m.matches()) {
                        tagNo.add(i);
                        displayList.add(tagGroupName[i]);
                        break;
                    }
                }
            }
        }
        else
        {

            int i = 0 ;
            for(String st : tagGroupName){
                displayList.add(st);
                tagNo.add(i++);
            }
        }
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,displayList);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(searchActivity.this, sectionsActivity.class);
                String message = tagNo.get(position)+"";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        InputStream inputStream = getResources().openRawResource(R.raw.taggroups);
        CSVFile csvFile = new CSVFile(inputStream);
        tagListList = csvFile.read();
        Resources res = getResources();
        tagGroupName= res.getStringArray(R.array.tag_group_names);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView myList = (ListView) findViewById(R.id.searchListView);
        displayList = new ArrayList<>();
        tagNo = new ArrayList<>();
        int i = 0 ;
        for(String st : tagGroupName){
            displayList.add(st);
            tagNo.add(i++);
        }
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,displayList);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(searchActivity.this, sectionsActivity.class);
                String message = tagNo.get(position)+"";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });
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
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        doSearch(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        //if(!newText.equals(""))
                        doSearch(newText);
                        return false;
                    }
                }
        );
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
