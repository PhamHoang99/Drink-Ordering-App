package com.android45.orderdrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android45.orderdrinks.R;
import com.android45.orderdrinks.fragment.CartFragment;
import com.android45.orderdrinks.fragment.FavoriteFragment;
import com.android45.orderdrinks.fragment.HomeFragment;
import com.android45.orderdrinks.fragment.UserFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView mBottomNavigationView;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_FAVORITE = 1;
    private static final int FRAGMENT_NOTIFICATION = 2;
    private static final int FRAGMENT_MY_PROFILE = 3;
    private int mCurrentFragment = FRAGMENT_HOME;
    DatabaseReference reference;
    private CircleImageView avt;
    private TextView nameUser,emailUser;
    Button logOut;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        showUserInformation();


        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        mBottomNavigationView.getMenu().findItem(R.id.nav_button_home ).setChecked(true);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_button_home) {
                    openHomeFragment();
                    navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                } else if (id == R.id.nav_button_favourite) {
                    openFavoriteFragment();
                    navigationView.getMenu().findItem(R.id.nav_favourite).setChecked(true);
                } else if (id == R.id.nav_button_cart) {
                    openCart();
                    navigationView.getMenu().findItem(R.id.nav_cart).setChecked(true);
                }
                else if ((id == R.id.nav_button_profile)){
                    openMyProfile();
                    navigationView.getMenu().findItem(R.id.nav_profile);
                }
                return true;
            }
        });


    }

    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        emailUser.setText(email);

    }

    private void Anhxa() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.navigation_view);
        nameUser = navigationView.getHeaderView(0).findViewById(R.id.tvNameTK);
        emailUser =navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        avt = findViewById(R.id.imgAVT);


        mBottomNavigationView = findViewById(R.id.bottom_nav);
        logOut = findViewById(R.id.Log_Out);
        logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(i);
            finish();
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            openHomeFragment();
            mBottomNavigationView.getMenu().findItem(R.id.nav_button_home).setChecked(true);
        } else if (id == R.id.nav_favourite) {
            openFavoriteFragment();
            mBottomNavigationView.getMenu().findItem(R.id.nav_button_favourite).setChecked(true);
        } else if (id == R.id.nav_cart) {
            openCart();
            mBottomNavigationView.getMenu().findItem(R.id.nav_button_cart).setChecked(true);
        }  else if (id == R.id.nav_profile) {
            openMyProfile();
            mBottomNavigationView.getMenu().findItem(R.id.nav_button_profile).setChecked(true);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openHomeFragment() {
        if (mCurrentFragment != FRAGMENT_HOME) {
            replaceFragment(new HomeFragment());
            mCurrentFragment = FRAGMENT_HOME;
        }
    }
    private void openFavoriteFragment() {
        if (mCurrentFragment != FRAGMENT_FAVORITE) {
            replaceFragment(new FavoriteFragment());
            mCurrentFragment = FRAGMENT_FAVORITE;
        }
    }
    private void openCart() {
        if (mCurrentFragment != FRAGMENT_NOTIFICATION) {
            replaceFragment(new CartFragment());
            mCurrentFragment = FRAGMENT_NOTIFICATION;
        }
    }
    private void openMyProfile() {
        if (mCurrentFragment != FRAGMENT_MY_PROFILE) {
            replaceFragment(new UserFragment());
            mCurrentFragment = FRAGMENT_MY_PROFILE;
        }
    }
}