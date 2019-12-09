package joshisen.abcc.aso.nekokan.ui.aircon;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import joshisen.abcc.aso.nekokan.R;

public class AirconFragment extends Fragment {

    private AirconViewModel airconViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        airconViewModel = ViewModelProviders.of(this).get(AirconViewModel.class);
        View root = inflater.inflate(R.layout.fragment_aircon, container, false);

        final TextView textView = root.findViewById(R.id.textView);
        final ImageView imageView = root.findViewById(R.id.imageView);
        final ToggleButton toggle = root.findViewById(R.id.toggleButton);

        // インスタンスの取得
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference refName = database.getReference("info");
        final DatabaseReference refName2 = database.getReference("info/air_conditioner/airconSwitch");

        refName.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                try {
                    air_conditioner aircon = dataSnapshot.getValue(air_conditioner.class);
                    int temperature = aircon.getTemperature();
                    int airconSwitch = aircon.getAirconSwitch();

                    if (airconSwitch == 0) {
                        toggle.setChecked(false);
                    } else {
                        toggle.setChecked(true);
                    }

                    textView.setText("室温："+temperature + "℃");
                    if (temperature >= 30) {
                        textView.setTextColor(Color.parseColor("#ff6347"));
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.atui));
                    } else if (temperature <= 22) {
                        textView.setTextColor(Color.parseColor("#4169e1"));
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.samui));
                    } else {
                        textView.setTextColor(Color.parseColor("#98fb98"));
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.best));
                    }
                }catch (Exception e){}
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggle.setBackgroundColor(Color.parseColor("#98fb98"));
                    Toast toast = Toast.makeText(getActivity(), "状態：ON", Toast.LENGTH_LONG);
                    refName2.setValue(1);

                    toast.show();

                } else {
                    toggle.setBackgroundColor(Color.parseColor("#dcdcdc"));
                    Toast toast = Toast.makeText(getActivity(), "状態：OFF", Toast.LENGTH_LONG);
                    refName2.setValue(0);
                    toast.show();

                }
            }
        });



        return root;
    }
}
