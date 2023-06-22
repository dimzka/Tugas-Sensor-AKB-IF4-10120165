package com.tugasakb.tempatmakan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

//Muhamad Dimas Azka Syarif Umair
//IF-4
//10120165
public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottomNavigationView);


        //app first run
        getSupportFragmentManager().beginTransaction().replace(R.id.pageContainer, new InfoFragment()).commit();
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.Info:
                        selectedFragment = new InfoFragment();
                        break;
                    case R.id.Maps:
                        selectedFragment = new MapsFragment();
                        break;
                    case R.id.Profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }

                return getFragmentPage(selectedFragment);
            }
        });
    }
    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.pageContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    }

