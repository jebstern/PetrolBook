package com.jebstern.petrolbook;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jebstern.petrolbook.extras.Utilities;
import com.jebstern.petrolbook.fragments.AllRefuelsFragment;
import com.jebstern.petrolbook.fragments.HelpFragment;
import com.jebstern.petrolbook.fragments.HomeFragment;
import com.jebstern.petrolbook.fragments.NewRefuelFragment;
import com.jebstern.petrolbook.fragments.SettingsFragment;

public class FragmentHolderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /** This enables us to fetch views from the "nav_header_main.xml'. Handy if want to dynamically manipulate text etc. **/
        View header = navigationView.getHeaderView(0);
        TextView tvHeaderUsername = (TextView) header.findViewById(R.id.tv_header_username);
        tvHeaderUsername.setText(Utilities.readUsernameFromPreferences(this));

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
            setTitle("Home");
        } else if (id == R.id.nav_new_refuel) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new NewRefuelFragment()).commit();
            setTitle("New refuel");
        } else if (id == R.id.nav_all_refuels) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AllRefuelsFragment()).commit();
            setTitle("All refuels");
        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
            setTitle("Settings");
        } else if (id == R.id.nav_help) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HelpFragment()).commit();
            setTitle("Help");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
