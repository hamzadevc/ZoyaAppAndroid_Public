package com.grappetite.zoya.adapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.fragments.TutorialFragment;


public class TutorialPagerAdapter extends FragmentStatePagerAdapter {
    private int[] tutorial = new int[16];
    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
        tutorial[0] = R.drawable.tutorial_1;
        tutorial[1] = R.drawable.tutorial_2;
        tutorial[2] = R.drawable.tutorial_3;
        tutorial[3] = R.drawable.tutorial_4;
        tutorial[4] = R.drawable.tutorial_5;
        tutorial[5] = R.drawable.tutorial_6;
        tutorial[6] = R.drawable.tutorial_7;
        tutorial[7] = R.drawable.tutorial_8;
        tutorial[8] = R.drawable.tutorial_9;
        tutorial[9] = R.drawable.tutorial_10;
        tutorial[10] = R.drawable.tutorial_11;
        tutorial[11] = R.drawable.tutorial_12;
//        tutorial[12] = R.drawable.tutorial_13;
//        tutorial[13] = R.drawable.tutorial_14;
        tutorial[12] = R.drawable.tutorial_15;
        tutorial[13] = R.drawable.tutorial_16;
        tutorial[14] = R.drawable.tutorial_17;
        tutorial[15] = R.drawable.tutorial_18;
    }

    @Override
    public Fragment getItem(int position) {
        TutorialFragment f = new TutorialFragment();
        Bundle b =new Bundle();
        b.putInt(CommonConstants.EXTRA_TUTORIAL_IMAGE, tutorial[position]);
        f.setArguments(b);
        return f;
    }

    @Override
    public int getCount() {
        return tutorial.length;
    }
}
