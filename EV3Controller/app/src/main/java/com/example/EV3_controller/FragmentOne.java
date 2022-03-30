package com.example.EV3_controller;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.example.EV3_controller.databinding.FragmentOneBinding;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentOne extends Fragment {
    private FragmentOneBinding binding;
    private MyFragmentDataPassListener cv_listener;
    private RobotConnection robot = new RobotConnection();
    private TextView connStatus, batteryLevel, connDeviceText;
    private EditText robotName;
    private Button connect, disconnect, navConnect, navDrive, soon;
    private ImageView btIcon;
    private ProgressBar batteryBar;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOneBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        connStatus = binding.connectStatus; batteryLevel = binding.batteryText;
        connect = binding.connect; disconnect = binding.disconnect;
        navConnect = binding.navConnect; navDrive = binding.navDrive;
        soon = binding.comingSoon;
        connDeviceText = binding.connectedDeviceText;
        robotName = binding.editTextRobotName;
        btIcon = binding.btIcon;
        batteryBar = binding.batteryBar;
        disconnect.setEnabled(false);
        btIcon.setImageResource(R.drawable.bluetooth_disconnected);
        navConnect.setEnabled(false);

        soon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "More features coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] pairSearchResults = robot.connectToRobot(robotName.getText().toString());
                connStatus.setText(pairSearchResults[1]);
                if(pairSearchResults[0].equals("false"))
                    return;
                connStatus.setText(robot.finishConnecting());
                if(robot.isConnected()){
                    connect.setText("Connected");
                    connDeviceText.setText("Connected to "+robot.getDevice().getName());
                    connDeviceText.setTextColor(Color.argb(255, 237, 129, 21));
                    connect.setEnabled(false);
                    disconnect.setEnabled(true);
                    btIcon.setImageResource(R.drawable.bluetooth_connected);
                }
            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connStatus.setText(robot.disconnectFromRobot());
                if(!robot.isConnected()) {
                    disconnect.setEnabled(false);
                    connect.setEnabled(true);
                    connDeviceText.setText("Not Connected");
                    connDeviceText.setTextColor(Color.argb(255, 125, 125, 125));
                    connect.setText("Connect");
                    btIcon.setImageResource(R.drawable.bluetooth_disconnected);
                }
            }
        });

        navDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_listener.switchScreen(2, robot);
            }
        });

        return view;
    }

    public void getConnectionInfo(RobotConnection rb) {
        robot = rb;
        if(robot.isConnected()) {
            connect.setText("Connected");
            connect.setEnabled(false);
            btIcon.setImageResource(R.drawable.bluetooth_connected);
            connStatus.setText("Connected to " + robot.getDevice().getName() + " at " + robot.getDevice().getAddress());
            disconnect.setEnabled(true);
            connDeviceText.setText("Connected to "+robot.getDevice().getName());
            connDeviceText.setTextColor(Color.argb(255, 237, 129, 21));

        }
    }
}