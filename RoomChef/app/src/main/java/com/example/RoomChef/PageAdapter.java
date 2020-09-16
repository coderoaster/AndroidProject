package com.example.RoomChef;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter  extends FragmentPagerAdapter {

    int Tabnum;


    public PageAdapter(@NonNull FragmentManager fm , int Tabnum) {
        super(fm);
        this.Tabnum =Tabnum;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Fragment fragment = new Recipe_list();
                return fragment;
            case 1:
                Fragment grape = new ingredientFragment();

                return grape;
            case 2:
                Fragment Mypage = new Mypage();
                return Mypage;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return Tabnum;
    }
}
