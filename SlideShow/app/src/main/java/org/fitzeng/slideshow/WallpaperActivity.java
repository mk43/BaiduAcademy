package org.fitzeng.slideshow;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class WallpaperActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Timer timer;
    private LinearLayout dotLayout;
    private static final int DOT_VIEW_CHANGE_SELECTED = -1;

    private static final int[] srcId = {
            R.drawable.pic0,
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic6
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        init();

        listener();
        setTimer();
        setDuration();
    }

    /**
     * Set Duration
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setDuration() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixSpeedScroller scroller = new FixSpeedScroller(this, new AccelerateInterpolator());
            scroller.setDuration(500);
            field.set(viewPager, scroller);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set Listener
      */
    private void listener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageSelected(int position) {
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageScrollStateChanged(int state) {
                // if this page was showed by figure slip, then set timer to continue auto slip for next page
                if (timer == null) {
                    setTimer();
                }
                // Delay to change dotview
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(DOT_VIEW_CHANGE_SELECTED);
                    }
                }, 500);
            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Cancel the timer
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                // when slip out cache view(page0, page6), set an another same page instantly
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(5, false);
                }
                if (viewPager.getCurrentItem() == 6) {
                    viewPager.setCurrentItem(1, false);
                }
                return false;
            }
        });
    }

    /**
     * Set page change timer
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(viewPager.getCurrentItem());
            }
        }, 2000, 2000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5: {
                    viewPager.setCurrentItem(msg.what + 1, true);
                    break;
                }
                // To make start or end slip smoothly
                case 0: {
                    viewPager.setCurrentItem(5, false);
                    viewPager.setCurrentItem(6, true);
                    break;
                }
                case 6: {
                    viewPager.setCurrentItem(1, false);
                    viewPager.setCurrentItem(2, true);
                    break;
                }
                // Refresh the dotView when page was loaded
                case DOT_VIEW_CHANGE_SELECTED:
                    int currentId = viewPager.getCurrentItem();
                    switch (currentId) {
                        case 0: {
                            refreshDotView(5);
                            break;
                        }
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5: {
                            refreshDotView(currentId);
                            break;
                        }
                        case 6: {
                            refreshDotView(1);
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * Init
     */
    private void init() {
        viewPager = (ViewPager) findViewById(R.id.vpWallpaper);
        dotLayout = (LinearLayout) findViewById(R.id.dotLayout);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Add Fragments
        for (int i = 0; i < 7; i++) {
            ViewPagerFragment fragment = new ViewPagerFragment(srcId[i]);
            adapter.addPage(fragment);
        }
        viewPager.setAdapter(adapter);

        // Set Page one to show
        viewPager.setCurrentItem(1, false);

        // Set Page one was selected
        refreshDotView(1);
    }

    /**
     * Create a dot view for an select view
     * @return view
     */
    private View createViewSelected() {
        LinearLayout view = new LinearLayout(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 0, 10, 0);
        view.setLayoutParams(lp);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.dot_selected);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        view.addView(imageView);
        return view;
    }

    /**
     * Create a dot view for an unselected view
     * @return view
     */
    private View createViewUnselected() {
        LinearLayout view = new LinearLayout(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 0, 10, 0);
        view.setLayoutParams(lp);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.dot_unselect);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        view.addView(imageView);
        return view;
    }

    /**
     * Refresh the Dot View when sliped
     * @param selectId  current Id
     */
    private void refreshDotView(int selectId) {
        dotLayout.removeAllViews();
        for (int i = 1; i < 6; i++) {
            if (selectId != i) {
                View viewUnselect = createViewUnselected();
                if (viewUnselect.getParent() != null) {
                    ((ViewGroup) viewUnselect.getParent()).removeView(viewUnselect);
                }
                dotLayout.addView(viewUnselect);
            } else {
                View viewSelected = createViewSelected();
                if (viewSelected.getParent() != null) {
                    ((ViewGroup) viewSelected.getParent()).removeView(viewSelected);
                }
                dotLayout.addView(viewSelected);
            }
        }
    }

    /**
     * Set ViewPager Fragment
     */
    public static class ViewPagerFragment extends Fragment {
        private ImageView image;
        private int id;

        public ViewPagerFragment(int id) {
            this.id = id;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_adapter, container, false);
            image  = (ImageView) rootView.findViewById(R.id.image);
            image.setImageResource(id);
            return rootView;
        }
    }

    /**
     * Set ViewPager Adapter
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();

        // Add pages to ViewPager
        private void addPage(Fragment fragment) {
            fragmentList.add(fragment);
        }

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
