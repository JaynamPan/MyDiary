package meow.softer.mydiary.entries.calendar;

import static java.util.Collections.binarySearch;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import meow.softer.mydiary.R;
import meow.softer.mydiary.entries.BaseDiaryFragment;
import meow.softer.mydiary.entries.DiaryActivity;
import meow.softer.mydiary.shared.ThemeManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends BaseDiaryFragment implements View.OnClickListener,
        OnDateSelectedListener, DayViewDecorator {

    /**
     * Calendar
     */
    private Calendar calendar;
    private Date currentDate;

    private ThemeManager themeManager;

    /**
     * UI
     */
    private RelativeLayout RL_calendar_content;
    private RelativeLayout RL_calendar_edit_bar;
    private FloatingActionButton FAB_calendar_change_mode;

    /**
     * calendar Mode
     */
    private PageEffectView pageEffectView;
    private MaterialCalendarView materialCalendarView;

    private int currentMode;

    private static final int MODE_DAY = 1;
    private static final int MODE_MONTH = 2;


    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        currentDate = new Date();
        calendar.setTime(currentDate);
        themeManager = ThemeManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        RL_calendar_edit_bar = rootView.findViewById(R.id.RL_calendar_edit_bar);
        RL_calendar_edit_bar.setBackgroundColor(themeManager.getThemeMainColor(getActivity()));

        RL_calendar_content = rootView.findViewById(R.id.RL_calendar_content);

        FAB_calendar_change_mode = rootView.findViewById(R.id.FAB_calendar_change_mode);
        //Set the color
        FAB_calendar_change_mode.getDrawable()
                .setColorFilter(themeManager.getThemeMainColor(getActivity()), PorterDuff.Mode.SRC_ATOP);
        FAB_calendar_change_mode.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //default mode
        currentMode = MODE_DAY;
        initCalendarMode();
    }
    public void refreshCalendar() {
        switch (currentMode) {
            case MODE_DAY:
                //TODO add decorators
                break;
            case MODE_MONTH:
                materialCalendarView.invalidateDecorators();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FAB_calendar_change_mode:
                //togle the mode
                if (currentMode == MODE_DAY) {
                    currentMode = MODE_MONTH;
                } else {
                    currentMode = MODE_DAY;
                }
                initCalendarMode();
                break;
        }
    }
    private void initCalendarMode() {
        RL_calendar_content.removeAllViews();
        switch (currentMode) {
            case MODE_DAY:
                materialCalendarView = null;
                pageEffectView = new PageEffectView(getActivity(), calendar);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                pageEffectView.setLayoutParams(params);
                RL_calendar_content.addView(pageEffectView);
                break;
            case MODE_MONTH:
                pageEffectView = null;
                materialCalendarView = new MaterialCalendarView(getActivity());
                RelativeLayout.LayoutParams calendarViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                materialCalendarView.setLayoutParams(calendarViewParams);
                materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
                materialCalendarView.setSelectionColor(ThemeManager.getInstance().getThemeMainColor(getActivity()));
                materialCalendarView.state().edit()
                        .setFirstDayOfWeek(Calendar.MONDAY)
                        .setCalendarDisplayMode(CalendarMode.MONTHS)
                        .commit();
                materialCalendarView.setCurrentDate(calendar);
                materialCalendarView.setDateSelected(calendar, true);
                materialCalendarView.setOnDateChangedListener(this);
                RL_calendar_content.addView(materialCalendarView);
                //Add view first , then add Decorator
                materialCalendarView.addDecorator(this);
                break;
        }
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return binarySearch(getEntriesList(), day) >= 0;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, ThemeManager.getInstance().getThemeDarkColor(getActivity())));
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //Make calendar sync the new date
        calendar.setTime(date.getDate());

        //Goto the diary position
        int diaryPosition = binarySearch(getEntriesList(), date);
        if (diaryPosition >= 0) {
            ((DiaryActivity) getActivity()).callEntriesGotoDiaryPosition(diaryPosition);
        }
    }
}
