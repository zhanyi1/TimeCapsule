package com.example.timecapsule.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.timecapsule.activity.ChangeActivity;
import com.example.timecapsule.activity.LoginActivity;
import com.example.timecapsule.activity.RegisterActivity;
import com.example.timecapsule.utils.MyViewPager;
import com.example.timecapsule.R;
import com.example.timecapsule.activity.ChangeinforActivity;
import com.example.timecapsule.adapter.MainFragmentPagerAdapter;
import com.example.timecapsule.db.User;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchFragment extends Fragment {


    private NavigationView navView;
    private DrawerLayout mDrawerLayout;
    private TextView textViewUN;
    public static final String ACCOUNT = "NONE";
    private String username = "None";
    private String name = "";
    private String phone = "";
    private String location = "";
    private String mail = "";
    private String des = "";
    public static final String EMAIL = "NONE";
    private TabLayout tabLayout;
    private MyViewPager viewPager;
    private FragmentManager mFragmentManager;
    private MainFragmentPagerAdapter mainFragmentPagerAdapter;
    private Button logout;
    private User currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);
        navView = (NavigationView)  root.findViewById(R.id.nav_view);
        textViewUN = (TextView) navView.getHeaderView(0).findViewById(R.id.username);
        logout = (Button) navView.getHeaderView(0).findViewById(R.id.logout);

        tabLayout = (TabLayout) root.findViewById(R.id.tablayout);
        viewPager = (MyViewPager) root.findViewById(R.id.main_viewpager);

        //Initialize ViewPager
        mFragmentManager = getChildFragmentManager();
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(mFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainFragmentPagerAdapter.addFragment(new SearchClassFragment(), "Search");
        mainFragmentPagerAdapter.addFragment(new YoursFragment(), "Yours");
        viewPager.setAdapter(mainFragmentPagerAdapter);
        //Initialize TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.addTab(tabLayout.newTab().setText("Yours"));
        tabLayout.setupWithViewPager(viewPager);
        ////Initialize Toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mDrawerLayout = root.findViewById(R.id.drawer_layout);
        setHasOptionsMenu(true);
        setMenuVisibility(true);

        refreshUser();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_change:
                        if(currentUser != null){
                            Intent intent = new Intent(getActivity(), ChangeinforActivity.class);
                            startActivity(intent);
                            break;
                        }else{
                            new AlertDialog.Builder(getContext()).setTitle("You should log in firstly")
                                    .setNegativeButton("Yes", null)
                                    .show();
                        }
                }
                return true;
            }
        });

        //Set the login and logout button
        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(logout.getText().equals("logout")){
                    new AlertDialog.Builder(getContext()).setTitle("Are you sure to log out?")
                            .setNegativeButton("cancel", null)
                            .setPositiveButton("ensure", (dialogInterface, i) -> {
                                User.logOut();
                                // refresh data after log out
                                refreshUser();
                            })
                            .show();
                }else{
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }


            }
        });


        return root;
    }


    public void refreshUser() {
        // fetch currentUser from Bmob
        currentUser = BmobUser.getCurrentUser(User.class);
        //Load user information
        if (currentUser != null) {
            username = currentUser.getUsername();
            name = currentUser.getName();
            phone = currentUser.getPhone();
            location = currentUser.getLocation();
            mail = currentUser.getEmail();
            des = currentUser.getDescription();
            textViewUN.setText(username);
            navView.getMenu().findItem(R.id.nav_name).setTitle("Nickname: "+name);
            navView.getMenu().findItem(R.id.nav_phone).setTitle("Phone: "+phone);
            navView.getMenu().findItem(R.id.nav_location).setTitle("Location: "+location);
            navView.getMenu().findItem(R.id.nav_mail).setTitle("Mail: "+mail);
            navView.getMenu().findItem(R.id.nav_des).setTitle("Description: "+des);
        } else {
            navView.getMenu().findItem(R.id.nav_name).setTitle("Nickname: ");
            navView.getMenu().findItem(R.id.nav_phone).setTitle("Phone: ");
            navView.getMenu().findItem(R.id.nav_location).setTitle("Location: ");
            navView.getMenu().findItem(R.id.nav_mail).setTitle("Mail: ");
            navView.getMenu().findItem(R.id.nav_des).setTitle("Description: ");
            textViewUN.setText("Visitor");
            logout.setText("login");
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        refreshUser();
    }

}

