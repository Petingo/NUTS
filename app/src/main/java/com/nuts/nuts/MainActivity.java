package com.nuts.nuts;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private View navHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showAskWeatherDialog();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        initNavHead();

        try {
            Fragment fragment = null;
            fragment = (Fragment) MapFragment.class.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initNavHead(){
        SharedPreferences pref = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        ImageView drawerAvatar = (ImageView) navHeader.findViewById(R.id.drawerAvatar);
        drawerAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        TextView drawerNickName = (TextView) navHeader.findViewById(R.id.drawerNickName);
        String identity = pref.getString("identity","訪客");
        if(identity.equals("guest")){
            identity = "訪客";
        } else {
            identity = "學生";
        }
        drawerNickName.setText(identity);
        TextView drawerEmail = (TextView) navHeader.findViewById(R.id.editMarkerPlaceName);
        drawerEmail.setText(pref.getString("account","guest"));
    }

    private void showAskWeatherDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View view = inflater.inflate(R.layout.ask_weather, null);
        dialogBuilder.setView(view);
        final AlertDialog dialog = dialogBuilder.create();
        ImageView sun = view.findViewById(R.id.sun);
        ImageView rain = view.findViewById(R.id.rain);
        ImageView cloud = view.findViewById(R.id.cloud);
        ImageView doNotKnow = view.findViewById(R.id.dontKnow);
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO send
                dialog.cancel();
            }
        });
        rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO send
                dialog.cancel();
            }
        });
        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO send
                dialog.cancel();
            }
        });
        doNotKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            initNavHead();
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
        // Inflate the menu; this adds items to the action bar i
        // f it is present.
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass = null;

        int id = item.getItemId();
        switch (id){
            case R.id.nav_school:
                fragmentClass = MapFragment.class;
                break;
            case R.id.nav_lottery:
                fragmentClass = LotteryFragment.class;
                break;
            case R.id.nav_ptt_hot:
                fragmentClass = PTTFragment.class;
                break;
            default:
                fragmentClass = MapFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
