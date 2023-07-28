package com.chotix.mydiary1.entries;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chotix.mydiary1.R;
import com.chotix.mydiary1.db.DBManager;
import com.chotix.mydiary1.entries.calendar.CalendarFragment;
import com.chotix.mydiary1.entries.diary.DiaryFragment;
import com.chotix.mydiary1.entries.entry.EntriesEntity;
import com.chotix.mydiary1.entries.entry.EntriesFragment;
import com.chotix.mydiary1.shared.MyDiaryApplication;
import com.chotix.mydiary1.shared.ThemeManager;
import com.chotix.mydiary1.shared.statusbar.ChinaPhoneHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import info.hoang8f.android.segmented.SegmentedGroup;

import com.chotix.mydiary1.entries.diary.item.IDiaryRow;

public class DiaryActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {
    /**
     * Public data
     */
    private long topicId;
    private boolean hasEntries;

    /**
     * UI
     */
    private LinearLayout LL_diary_topbar_content;
    private ViewPager ViewPager_diary_content;
    private TextView TV_diary_topbar_title;
    private SegmentedGroup SG_diary_topbar;
    private RadioButton But_diary_topbar_entries, But_diary_topbar_calendar, But_diary_topbar_diary;

    /**
     * View pager
     */
    private ScreenSlidePagerAdapter mPagerAdapter;

    /**
     * Google API
     */
    //private GoogleApiClient mGoogleApiClient;

    /**
     * The diary list for every fragment
     */
    private List<EntriesEntity> entriesList = new ArrayList<>();
    private final static int MAX_TEXT_LENGTH = 18;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        //For set status bar
        ChinaPhoneHelper.setStatusBar(this, true);
        setStatusBarBgColor();

        topicId = getIntent().getLongExtra("topicId", -1);
        hasEntries = getIntent().getBooleanExtra("has_entries", true);
        if (topicId == -1) {
            finish();
        }
        /**
         * init UI
         */
        LL_diary_topbar_content = findViewById(R.id.LL_diary_topbar_content);
        SG_diary_topbar = findViewById(R.id.SG_diary_topbar);
        SG_diary_topbar.setOnCheckedChangeListener(this);
        SG_diary_topbar.setTintColor(ThemeManager.getInstance().getThemeDarkColor(this));
        But_diary_topbar_entries = findViewById(R.id.But_diary_topbar_entries);
        But_diary_topbar_calendar = findViewById(R.id.But_diary_topbar_calendar);
        But_diary_topbar_diary = findViewById(R.id.But_diary_topbar_diary);
        TV_diary_topbar_title = findViewById(R.id.TV_diary_topbar_title);
        TV_diary_topbar_title.setTextColor(ThemeManager.getInstance().getThemeDarkColor(this));

        String diaryTitle = getIntent().getStringExtra("diaryTitle");
        if (diaryTitle == null) {
            diaryTitle = "Diary";
        }
        TV_diary_topbar_title.setText(diaryTitle);
        //After SegmentedGroup was created
        initViewPager();
        //initGoogleAPi();

        //Load the all entries from db.
        loadEntries();
    }

    public void loadEntries() {
        entriesList.clear();
        DBManager dbManager = new DBManager(this);
        dbManager.openDB();
        //Select diary info
        Cursor diaryCursor = dbManager.selectDiaryList(getTopicId());
        for (int i = 0; i < diaryCursor.getCount(); i++) {
            //get diary info
            String title = diaryCursor.getString(2);
            if ("".equals(title)) {
                title = getString(R.string.diary_no_title);
            }
            EntriesEntity entity = new EntriesEntity(diaryCursor.getLong(0),
                    new Date(diaryCursor.getLong(1)),
                    title.substring(0, Math.min(MAX_TEXT_LENGTH, title.length())),
                    diaryCursor.getInt(4), diaryCursor.getInt(3),
                    diaryCursor.getInt(5) > 0);

            //select first diary content
            Cursor diaryContentCursor = dbManager.selectDiaryContentByDiaryId(entity.getId());
            if (diaryContentCursor != null && diaryContentCursor.getCount() > 0) {
                String summary = "";
                //Check content Type
                if (diaryContentCursor.getInt(1) == IDiaryRow.TYPE_PHOTO) {
                    summary = getString(R.string.entries_summary_photo);
                } else if (diaryContentCursor.getInt(1) == IDiaryRow.TYPE_TEXT) {
                    summary = diaryContentCursor.getString(3)
                            .substring(0, Math.min(MAX_TEXT_LENGTH, diaryContentCursor.getString(3).length()));
                }
                entity.setSummary(summary);
                diaryContentCursor.close();
            }
            //Add entity
            entriesList.add(entity);
            diaryCursor.moveToNext();
        }
        diaryCursor.close();
        dbManager.closeDB();
    }

    public List<EntriesEntity> getEntriesList() {
        return entriesList;
    }

    /**
     * Init Viewpager
     */
    private void initViewPager() {
        ViewPager_diary_content = findViewById(R.id.ViewPager_diary_content);
        //Make viewpager load one fragment every time.
        ViewPager_diary_content.setOffscreenPageLimit(2);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        ViewPager_diary_content.setAdapter(mPagerAdapter);
        ViewPager_diary_content.addOnPageChangeListener(onPageChangeListener);
        ViewPager_diary_content.setBackground(
                ThemeManager.getInstance().getEntriesBgDrawable(this, getTopicId()));
        if (!hasEntries) {
            ViewPager_diary_content.setCurrentItem(2);
            //Set Default Checked Item
            But_diary_topbar_diary.setChecked(true);
        } else {
            //Set Default Checked Item
            But_diary_topbar_entries.setChecked(true);
        }
    }

    public long getTopicId() {
        return topicId;
    }

    public void gotoPage(int position) {
        ViewPager_diary_content.setCurrentItem(position);
    }

    public void callEntriesGotoDiaryPosition(int position) {

        EntriesFragment entriesFragment = ((EntriesFragment) mPagerAdapter.getRegisteredFragment(0));
        if (entriesFragment != null) {
            gotoPage(0);
            entriesFragment.gotoDiaryPosition(position);
        }
    }

    public void callEntriesListRefresh() {
        EntriesFragment entriesFragment = ((EntriesFragment) mPagerAdapter.getRegisteredFragment(0));
        if (entriesFragment != null) {
            entriesFragment.updateEntriesData();
        }
    }

    public void callCalendarRefresh() {

        CalendarFragment calendarFragment = ((CalendarFragment) mPagerAdapter.getRegisteredFragment(1));
        if (calendarFragment != null) {
            calendarFragment.refreshCalendar();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.But_diary_topbar_entries:
                ViewPager_diary_content.setCurrentItem(0);
                break;
            case R.id.But_diary_topbar_calendar:
                ViewPager_diary_content.setCurrentItem(1);
                break;
            case R.id.But_diary_topbar_diary:
                ViewPager_diary_content.setCurrentItem(2);
                break;
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                default:
                    But_diary_topbar_entries.setChecked(true);
                    break;
                case 1:
                    But_diary_topbar_calendar.setChecked(true);
                    break;
                case 2:
                    But_diary_topbar_diary.setChecked(true);
                    break;
            }
        }
    };

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        private SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseDiaryFragment getItem(int position) {
            BaseDiaryFragment fragment;
            switch (position) {
                default:
                    fragment = new EntriesFragment();
                    break;
                case 1:
                    fragment = new CalendarFragment();
                    break;
                case 2:
                    fragment = new DiaryFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(updateBaseContextLocale(base));

    }

    private Context updateBaseContextLocale(Context context) {
        Locale locale = MyDiaryApplication.mLocale;
        Log.e("Mytest", "init mLocale:" + locale);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }
    private void setStatusBarBgColor(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
    }
}
