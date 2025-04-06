package ai.free.vpn.tweeqoldvpn.getvpn.activity.step;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import ai.free.vpn.tweeqoldvpn.getvpn.R;

public class MyViewPagerAdapter extends PagerAdapter {
    private final Context context;

    public final int[] layouts = new int[]{
            R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3
    };

    public MyViewPagerAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layouts[position], container, false);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}