package rs.ac.singidunum.rank_watch.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import rs.ac.singidunum.rank_watch.fragments.FlexFragment;
import rs.ac.singidunum.rank_watch.fragments.SoloDuoFragment;
import rs.ac.singidunum.rank_watch.models.LolPlayer;

public class LolPlayerPagerAdapter extends FragmentPagerAdapter {
    private LolPlayer lolPlayer;

    public LolPlayerPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                int profileIconId = 0;
                String region = "Eune";
                return SoloDuoFragment.newInstance(lolPlayer,profileIconId,region);
            case 1:
                profileIconId = 0;
                region = "Eune";
                return FlexFragment.newInstance(lolPlayer,profileIconId, region);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2; // Number of tabs/fragments
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Solo/Duo";
            case 1:
                return "Flex 5v5";
            default:
                return null;
        }
    }
}