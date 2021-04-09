package com.example.chatee;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    public SectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                request request = new request();
                return request;
            case 1:
                chat chat = new chat();
                return chat;
            case 2:
                friends friends = new friends();
                return friends;
            default:
                return null;



        }


    }

    @Override
    public int getCount() {
        return 3;


    }


public CharSequence getPagetitle(int position){
    switch (position) {

        case 0:
            return "REQUEST";
        case 1:
            return "CHAT";
        case 2:
            return "FRIENDS";
        default:
            return null;



    }

}


}
