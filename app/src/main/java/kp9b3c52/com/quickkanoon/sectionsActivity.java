package kp9b3c52.com.quickkanoon;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
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
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sectionsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ArrayList<ArrayList<String>> fileNameListList;
    ArrayList<String> headers , filteredHeaders;
    ArrayList<String> fileNames;
    ArrayList<String> fileNameMap  , filteredFileNameMap;
    ArrayAdapter adapter ;
    String headerText ;


    void doSearch(String str) {
        TextView myText = (TextView) findViewById(R.id.sectionResText);
        ListView myList = (ListView) findViewById(R.id.sectionListView);
        if (!str.equals("")) {
            myText.setText(headerText+" , search results for : " + str);
            // ListView myList = (ListView) findViewById(R.id.searchListView);
            filteredHeaders = new ArrayList<>();
            filteredFileNameMap = new ArrayList<>();
            str = str.toLowerCase();
            Pattern p = Pattern.compile("^.*" + str + ".*$");
            for (int i = 0; i < headers.size(); i++) {
                String st = headers.get(i).toLowerCase();
                Matcher m = p.matcher(st);
                if (m.matches()) {
                    filteredFileNameMap.add(fileNameMap.get(i));
                    filteredHeaders.add(headers.get(i));
                    //break;
                }
            }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredHeaders);
        myList.setAdapter(adapter);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(sectionsActivity.this, showLawActivity.class);
                    String message = filteredFileNameMap.get(position);
                    String str[] = message.split(" ");
                    intent.putExtra("fileName", fileNames.get(Integer.parseInt(str[0])));
                    intent.putExtra("lineNo", str[1]);
                    startActivity(intent);
                }
            });
        }
        else
        {
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,headers);
            myList.setAdapter(adapter);
            filteredHeaders = headers ;
            filteredFileNameMap = fileNameMap ;
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(sectionsActivity.this, showLawActivity.class);
                    String message = filteredFileNameMap.get(position);
                    String str[] = message.split(" ");
                    intent.putExtra("fileName", fileNames.get(Integer.parseInt(str[0])));
                    intent.putExtra("lineNo", str[1]);
                    startActivity(intent);
                }
            });

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String message = intent.getStringExtra("tagGroupNo");
        InputStream inputStream = getResources().openRawResource(R.raw.taggroupfilenames);
        CSVFile csvFile = new CSVFile(inputStream);
        fileNameListList = csvFile.read();
        fileNames = fileNameListList.get(Integer.parseInt(message));


        setContentView(R.layout.activity_sections);
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

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Resources res = getResources();
        String tagGroupName[]= res.getStringArray(R.array.tag_group_names);
        TextView myText = (TextView) findViewById(R.id.sectionResText);
        myText.setText(tagGroupName[Integer.parseInt(message)]);
        headerText = tagGroupName[Integer.parseInt(message)] ;
        headers = new ArrayList<>();
        fileNameMap = new ArrayList<>();
        int ctr = 0 ;
        AssetManager am = this.getBaseContext().getAssets();
        for(int i = 0 ; i< fileNames.size();i++){
            String fileName = fileNames.get(i);
            try {
                InputStream is = am.open(fileName+".txt");
                GetHeaders obj = new GetHeaders(is);
                ArrayList<String> temp = obj.read();
                for(int j = 0 ; j < temp.size(); j++)
                {
                    fileNameMap.add(i+" "+j);
                    headers.add(temp.get(j));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        filteredHeaders = headers ;
        filteredFileNameMap = fileNameMap ;
        ListView myList = (ListView) findViewById(R.id.sectionListView);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,headers);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(sectionsActivity.this, showLawActivity.class);
                String message = filteredFileNameMap.get(position);
                String str[] = message.split(" ");
                intent.putExtra("fileName", fileNames.get(Integer.parseInt(str[0])));
                intent.putExtra("lineNo", str[1]);
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
        getMenuInflater().inflate(R.menu.sections, menu);
        MenuItem item = menu.findItem(R.id.action_sec_search);
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
