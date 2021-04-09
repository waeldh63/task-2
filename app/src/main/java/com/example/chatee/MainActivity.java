package com.example.chatee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;


    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
      mToolbar = findViewById(R.id.main_page);
        setSupportActionBar(mToolbar);
      getSupportActionBar().setTitle("chatee");



       viewPager = findViewById(R.id.tab_pager);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
         viewPager.setAdapter(sectionsPagerAdapter);




         tabLayout = findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);







    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
         sendTostart();


        }

}

    private void sendTostart() {
        Intent intent= new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
       getMenuInflater().inflate(R.menu.main_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
if(item.getItemId() == R.id.main_logout){


    FirebaseAuth.getInstance().signOut();
    sendTostart();
}
        if(item.getItemId() == R.id.setting_btn){


            Intent intent = new Intent(MainActivity.this , settingsActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.users_btn){


            Intent intent = new Intent(MainActivity.this , users.class);
            startActivity(intent);
        }

        return true ;
    }
}