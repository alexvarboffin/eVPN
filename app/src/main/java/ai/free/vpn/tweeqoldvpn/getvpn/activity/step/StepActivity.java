package ai.free.vpn.tweeqoldvpn.getvpn.activity.step;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;

import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ai.free.vpn.tweeqoldvpn.getvpn.PrefConstant;

import ai.free.vpn.tweeqoldvpn.getvpn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.walhalla.ui.DLog;

import ai.free.vpn.tweeqoldvpn.getvpn.activity.auth.ChooseLoginRegistrationActivity;
import ai.free.vpn.tweeqoldvpn.getvpn.activity.main.MainActivity;

public class StepActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter adapter;
    private LinearLayout dotsLayout;


    private Button btnSkip, btnNext;
    private PrefConstant session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new PrefConstant(this);
        if (!session.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_step);
        btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);
        btnSkip.setVisibility(View.GONE);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);


        adapter = new MyViewPagerAdapter(this);
        addBottomDots(0);
        changeStatusBarColor();

        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.setAdapter(adapter);


        btnSkip.setOnClickListener(v -> launchHomeScreen());

        btnNext.setOnClickListener(v -> {

            int current = getItem(+1);
            if (current < adapter.getCount()) {

                viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        });
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[adapter.getCount()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    //splash

    private void launchHomeScreen() {
        session.setFirstTimeLaunch(false);
        if (isSessionValid()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, ChooseLoginRegistrationActivity.class));
            finish();
        }
    }

    public boolean isSessionValid() {
        try {
            return FirebaseAuth.getInstance().getCurrentUser() != null;
        } catch (Exception e) {
            DLog.handleException(e);
            return false;
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            DLog.d("@@" + position);

            if (position == adapter.getCount() - 1) {
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                btnNext.setText(getString(R.string.next));
                //btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}