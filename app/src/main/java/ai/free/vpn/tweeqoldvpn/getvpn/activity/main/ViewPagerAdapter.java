package ai.free.vpn.tweeqoldvpn.getvpn.activity.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ai.free.vpn.tweeqoldvpn.getvpn.activity.SpeedTestFragment;
import ai.free.vpn.tweeqoldvpn.getvpn.tos.TOSFragment;


public class ViewPagerAdapter extends FragmentStateAdapter
        //FragmentStatePagerAdapter
        //RecyclerView.Adapter<ViewPagerAdapter.PagerVH>
{//  FragmentPagerAdapter {

    //private final List<Fragment> mFragmentList = new ArrayList<>();

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
//
//    @Override
//    public int getItemCount() {
//        return mFragmentList.size();
//    }
    @Override
    public int getItemCount() {
        return 4;
    }

//    @Override
//    public int getCount() {
//        return 4;
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new Home();
        if (position == 1) return new SpeedTestFragment();
        if (position == 2) return new TOSFragment();
//        if (position == 3) return new DashboardFragment();
        return new Fragment();
    }

//    public void addFragment(Fragment fragment, String title) {
//        mFragmentList.add(fragment);
//    }

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