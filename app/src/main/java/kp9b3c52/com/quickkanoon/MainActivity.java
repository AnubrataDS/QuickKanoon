package kp9b3c52.com.quickkanoon;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int currentTab = 0;
    TabHost host ;
    View oldView ;

    private Animation inFromLeftAnimation()
    {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(240);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private Animation outToLeftAnimation()
    {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(240);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    public Animation inFromRightAnimation()
    {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(240);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    public Animation outToRightAnimation()
    {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(240);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Home");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Most Popular");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("About");
        host.addTab(spec);
        oldView = host.getCurrentView();
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId)
            {
                View currentView = host.getCurrentView();
                if (host.getCurrentTab() > currentTab)
                {
                    oldView.setAnimation(outToLeftAnimation());
                    currentView.setAnimation( inFromRightAnimation() );
                }
                else
                {
                    oldView.setAnimation( outToRightAnimation() );
                    currentView.setAnimation(inFromLeftAnimation());
                }
                oldView = currentView ;
                currentTab = host.getCurrentTab();
            }
        });
        ListView fpl = (ListView) findViewById(R.id.frontPageList);

        ArrayList<String> fplArr = new ArrayList<>();
        Resources res = getResources();
        String str[] = res.getStringArray(R.array.front_page_list_1);
        for(String st : str){
            fplArr.add(st);
        }
        ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,fplArr);
        fpl.setAdapter(adapter);
        fpl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Intent intent = new Intent(getBaseContext(), sectionsActivity.class);
                //startActivity(intent);
                Intent intent ;
                switch(position){
                    case 0 :
                        intent = new Intent(getBaseContext(), searchActivity.class);
                        startActivity(intent);
                        break ;
                    case 1 :
                        host.setCurrentTab(1);
                        break;
                    case 2 :
                        intent = new Intent(getBaseContext(), bookmarksActivity.class);
                        startActivity(intent);
                        break ;
                    case 3 :
                        intent = new Intent(getBaseContext(), settingsActivity.class);
                        startActivity(intent);
                        break ;
                    case 4 :
                        intent = new Intent(getBaseContext(), helpActivity.class);
                        startActivity(intent);
                        break ;
                    case 5 :
                        host.setCurrentTab(2);
                        break ;
                }
            }
        });

        ListView fpl2 = (ListView) findViewById(R.id.mostPopularList);
        str = res.getStringArray(R.array.tag_group_names);
        ArrayList<String> fplArr2 = new ArrayList<>();
        for(String st : str){
            fplArr2.add(st);
        }
        ArrayAdapter adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,fplArr2);
        fpl2.setAdapter(adapter2);
        fpl2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Intent intent = new Intent(getBaseContext(), sectionsActivity.class);
                //startActivity(intent);
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
        getMenuInflater().inflate(R.menu.main, menu);
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
        if (id == R.id.action_search_frontpage) {
            Intent intent = new Intent(this, searchActivity.class);
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
