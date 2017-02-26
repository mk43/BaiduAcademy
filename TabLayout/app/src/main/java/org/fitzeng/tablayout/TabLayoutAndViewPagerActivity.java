package org.fitzeng.tablayout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutAndViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<TabLayout.Tab> tabListView = new ArrayList<>();    // To save all TabCards

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_and_view_pager);
        initLayout();
        initEvents();
    }

    /**
     * Setting Tab Click Listener
     */
    private void initEvents() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Called when the tab was selected
                tabListView.get(tab.getPosition()).setIcon(R.mipmap.selected_icon);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Called when a tab from selected switch to unselected
                tabListView.get(tab.getPosition()).setIcon(R.mipmap.ic_launcher);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Called when a tab selected more than twice consecutively
            }
        });
    }

    /**
     * Init Layout, Load Fragments and draw Layouts.
     */
    private void initLayout() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Add Fragments
        for (int i = 0; i < 4; i++) {
            adapter.addPage(new ViewPagerFragment("PAGE" + i), "PAGE" + i);
        }
        viewPager.setAdapter(adapter);
        // Connect TabLayout and ViewPage
        tabLayout.setupWithViewPager(viewPager);

        // Draw Tab View
        for (int i = 0; i < 4; i++) {
            tabListView.add(tabLayout.getTabAt(i));
            if (i == 0) {
                tabListView.get(i).setIcon(R.mipmap.selected_icon);
            } else {
                tabListView.get(i).setIcon(R.mipmap.ic_launcher);
            }
            tabListView.get(i).setText("PAGE" + i);
        }
    }
}
