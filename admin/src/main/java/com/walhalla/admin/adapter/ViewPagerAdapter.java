package com.walhalla.admin.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentStateAdapter
        //FragmentStatePagerAdapter
        //RecyclerView.Adapter<ViewPagerAdapter.PagerVH>
{//  FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    //    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        return mFragmentList.get(position);
//    }

    //    @NonNull
//    @Override
//    public PagerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new PagerVH(LinearLayout.inflate(parent.getContext(), R.layout.about, null));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PagerVH holder, int position) {
//
//    }

//    @Override
//    public int getItemCount() {
//        return mFragmentList.size();
//    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }

//    @Override
//    public int getCount() {
//        return 4;
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
    }

//    public void addFragments(List<Fragment> fragments) {
//        mFragmentList.clear();
//        mFragmentList.addAll(fragments);
//    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titles.get(position);
//    }
//    public class PagerVH extends RecyclerView.ViewHolder {
//        public PagerVH(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
}