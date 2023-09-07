package com.example.basicchatapp.Activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.basicchatapp.Activities.FriendsAndRequests.FriendsAndRequestsActivity;
import com.example.basicchatapp.Activities.SearchAndAddFriends.SearchActivity;
import com.example.basicchatapp.Activities.SettingsActivity.SettingsActivity;
import com.example.basicchatapp.BroadcastReceiver.BroadcastReceiverNetwork;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.FirebaseMethods;
import com.example.basicchatapp.databinding.ActivityMainBinding;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String NETWORK_STATE_CHANGED = "com.example.basicchatapp.NETWORK_STATE_CHANGED";
    public static final String EXTRA_NETWORK_STATE = "network_state";

    private ActivityMainBinding binding;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver br;

    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        broadcastReceiver = new BroadcastReceiverNetwork();
        firebaseMethods = new FirebaseMethods(MainActivity.this);
        firebaseMethods.getNotificationToken();

        List<String> tabNames = new ArrayList<>();
        tabNames.add("Message");
        tabNames.add("Group");
        tabNames.add("Call");

        String[] titleList = {"All Chat", "All Group", "All Call"};

        setSupportActionBar(binding.toolbarMain);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        binding.viewpagerMain.setAdapter(adapter);

        new TabLayoutMediator(binding.tablayoutMain, binding.viewpagerMain,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabNames.get(position));
                    }
                }).attach();

        binding.tablayoutMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.toolbarMain.setTitle(titleList[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        BadgeDrawable badge = Objects.requireNonNull(
                binding.tablayoutMain.getTabAt(0)).getOrCreateBadge();
        badge.setBadgeGravity(BadgeDrawable.TOP_END);
        badge.setNumber(1);
        badge.setBadgeTextColor(Color.BLACK);
        badge.setBackgroundColor(Color.WHITE);

        receiverBroadcast();

        binding.fabMain.setOnClickListener(view -> getToFriendsAndRequests());

    }

    private void getToFriendsAndRequests() {
        startActivity(new Intent(MainActivity.this, FriendsAndRequestsActivity.class));
    }

    private void receiverBroadcast(){

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, filter);

        br = new BroadcastReceiverNetwork() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(NETWORK_STATE_CHANGED)) {
                    boolean isConnected = intent.getBooleanExtra(EXTRA_NETWORK_STATE
                            , false);
                    showBottomSheet(isConnected);
                }
            }
        };

        IntentFilter filter2 = new IntentFilter(NETWORK_STATE_CHANGED);
        registerReceiver(br, filter2);
    }


    public void showBottomSheet(boolean active){
        ModalBottomSheet modalBottomSheet = new ModalBottomSheet();
        if(!active){
            modalBottomSheet.show(getSupportFragmentManager(), ModalBottomSheet.TAG);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_friends:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
//                helper.showShortToast(getApplicationContext(),"new friends");
                return true;
            case R.id.search:
                return true;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}