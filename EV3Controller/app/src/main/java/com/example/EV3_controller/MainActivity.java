package com.example.EV3_controller;

import com.example.EV3_controller.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MyFragmentDataPassListener {
    FragmentOne frag01;
    FragmentTwo frag02;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        frag01 = new FragmentOne();
        frag02 = new FragmentTwo();
        setContentFragment(1);

    }

    public static void loadFragment(AppCompatActivity activity, int containerId, Fragment fragment, String tag) {
        activity.getSupportFragmentManager().beginTransaction().
                replace(containerId, fragment, tag).commitAllowingStateLoss();
    }

    public void setContentFragment(int id) {
        switch (id) {
            case 1:
                loadFragment(this, binding.screenContainer.getId(), frag01, "fragment1");
                break;
            case 2:
                loadFragment(this, binding.screenContainer.getId(), frag02, "fragment2");
                break;
        }
    }



    @Override
    public void switchScreen(int dest, RobotConnection rb) {

            setContentFragment(dest);
            getSupportFragmentManager().executePendingTransactions();
            switch(dest) {
                case 1:
                    frag01.getConnectionInfo(rb);
                    break;
                case 2:
                    frag02.getConnectionInfo(rb);
                    break;
            }
    }
}