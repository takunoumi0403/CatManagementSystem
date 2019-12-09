package joshisen.abcc.aso.nekokan.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import joshisen.abcc.aso.nekokan.R;
import joshisen.abcc.aso.nekokan.ui.aircon.air_conditioner;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //
        final TextView textView = root.findViewById(R.id.textView7);
        final TextView textView2 = root.findViewById(R.id.textView8);

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


                    textView.setText("室温："+temperature + "℃");
                    if (temperature >= 30) {
                        textView.setTextColor(Color.parseColor("#ff6347"));
                    } else if (temperature <= 22) {
                        textView.setTextColor(Color.parseColor("#4169e1"));
                    } else {
                        textView.setTextColor(Color.parseColor("#98fb98"));
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

        Date date = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        int intDate = Integer.parseInt(sdf2.format(date));

        Query query = refName.orderByChild("date").equalTo(intDate);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    int weight = 0;
                    int count = 0;
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            weight += (Integer) issue.child("weight").getValue(Integer.class);
                            count++;
                        }
                    }

                    textView2.setText("トイレ量："+weight + "g");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;

    }
}