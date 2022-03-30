package com.example.EV3_controller;

import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.example.EV3_controller.databinding.FragmentTwoBinding;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentTwo extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private FragmentTwoBinding binding;
    int power = 75;
    private MyFragmentDataPassListener cv_listener;
    private RobotConnection robot;
    private ImageButton U,D,L,R;
    private Button navDrive, navConnect, soon;
    private TextView driveStatus, powerLevel;
    private SeekBar powerBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyFragmentDataPassListener) {
            cv_listener = (MyFragmentDataPassListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must MyFragmentDataPassListener");
        }
    }

    public void getConnectionInfo(RobotConnection rb) {
        robot = rb;
        if(robot.isConnected()) {
            driveStatus.setText("Connected to " +robot.getDevice().getName());
            powerBar.setProgress(50);
            U.setEnabled(true);
            D.setEnabled(true);
            L.setEnabled(true);
            R.setEnabled(true);

        }
        else {
            driveStatus.setText("Not Connected");
            U.setEnabled(false);
            D.setEnabled(false);
            L.setEnabled(false);
            R.setEnabled(false);
            powerBar.setProgress(0);
            powerBar.setEnabled(false);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTwoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        U = binding.driveU; D = binding.driveD; L = binding.driveL; R = binding.driveR;
        navConnect = binding.navConnect; navDrive = binding.navDrive; soon = binding.comingSoon;
        driveStatus = binding.driveStatus; powerLevel = binding.powerText;
        powerLevel.setText("Power: " +binding.powerBar.getProgress());
        powerBar = binding.powerBar;
        powerBar.setOnSeekBarChangeListener(this);
        navDrive.setEnabled(false);


        navConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_listener.switchScreen(1, robot);
            }
        });

        soon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "More features coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        U.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String err = robot.drive('u',powerBar.getProgress());
                if(!err.isEmpty())
                    driveStatus.setText(err);
            }
        }));
        D.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String err = robot.drive('d',powerBar.getProgress());
                if(!err.isEmpty())
                    driveStatus.setText(err);
            }
        }));
        L.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String err = robot.drive('l',powerBar.getProgress());
                if(!err.isEmpty())
                    driveStatus.setText(err);
            }
        }));
        R.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String err = robot.drive('r',powerBar.getProgress());
                if(!err.isEmpty())
                    driveStatus.setText(err);
            }
        }));

        return view;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        power = progress;
        powerLevel.setText("Power: " +power);
    }
}