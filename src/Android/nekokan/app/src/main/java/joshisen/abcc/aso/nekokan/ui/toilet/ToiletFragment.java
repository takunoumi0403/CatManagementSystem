package joshisen.abcc.aso.nekokan.ui.toilet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

import joshisen.abcc.aso.nekokan.R;

public class ToiletFragment extends Fragment {
    ArrayList<BarEntry> entries = new ArrayList<>();
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("toilet");
    int weight[] = {0,0,0,0,0,0,0,0,0,0};
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    Query query;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_toilet, container, false);

        int dateFrom = getToDate(1);
        int dateTo = getToDate(10);
        try {
            setTodayDate(root);
            System.out.println("差異:"+(dateFrom-dateTo));
            query = databaseReference.orderByChild("date").startAt(dateTo).endAt(dateFrom);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Date dateTo = new Date();
                        Date dateFrom = new Date();
                        dateFrom = sdf.parse(sdf.format(dateFrom));
                        long longDateFrom = dateFrom.getTime();
                        long longDateTo = 0;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                dateTo = sdf.parse(String.valueOf((Integer)issue.child("date").getValue(Integer.class)));
                                longDateTo = dateTo.getTime();
                                int index = (int)(( longDateFrom - longDateTo  ) / (1000 * 60 * 60 * 24 ))-1;
                                System.out.println("index===" + index);
                                weight[index] += (Integer) issue.child("weight").getValue(Integer.class);
                            }
                        }
                        int j = 10;
                        for (int i = 0; i < 10; i++) {
                            System.out.println("WEIGHT:::" + weight[i]);
                            entries.add(new BarEntry(j, weight[i]));
                            j--;
                            if (i == 9) {
                                List<IBarDataSet> bars = new ArrayList<>();
                                BarDataSet dataSet = new BarDataSet(entries, "bar");

                                //整数で表示
                                dataSet.setValueFormatter(new IValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                                        return "" + (int) value;
                                    }
                                });
                                //ハイライトさせない
                                dataSet.setHighlightEnabled(false);

                                //Barの色をセット
                                dataSet.setColors(new int[]{R.color.cornflowerblue, R.color.lightskyblue}, getActivity());
                                bars.add(dataSet);

                                BarChart chart = (BarChart) getActivity().findViewById(R.id.chart);

                                //表示データ取得
                                BarData data = new BarData(bars);
                                chart.setData(data);

                                //Y軸(左)
                                YAxis left = chart.getAxisLeft();
                                left.setAxisMinimum(0);
                                left.setAxisMaximum(300);
                                left.setLabelCount(10);
                                left.setDrawTopYLabelEntry(true);
                                //整数表示に
                                left.setValueFormatter(new IAxisValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value, AxisBase axis) {
                                        return "" + (int) value + "g";
                                    }
                                });

                                //Y軸(右)
                                YAxis right = chart.getAxisRight();
                                right.setDrawLabels(false);
                                right.setDrawGridLines(false);
                                right.setDrawZeroLine(false);
                                right.setDrawTopYLabelEntry(true);

                                //X軸
                                XAxis xAxis = chart.getXAxis();
                                //X軸に表示するLabelのリスト(最初の""は原点の位置)
                                final String[] labels = {"", "10日前", "9日前", "8日前", "7日前", "6日前", "5日前", "4日前", "3日前", "2日前", "1日前"};
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                                XAxis bottomAxis = chart.getXAxis();
                                bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                bottomAxis.setDrawLabels(true);
                                bottomAxis.setDrawGridLines(false);
                                bottomAxis.setDrawAxisLine(true);
                                bottomAxis.setLabelCount(10);

                                //グラフ上の表示
                                chart.setDrawValueAboveBar(true);
                                chart.getDescription().setEnabled(false);
                                chart.setClickable(false);

                                //凡例
                                chart.getLegend().setEnabled(false);

                                chart.setScaleEnabled(false);
                                //アニメーション
                                chart.animateY(2000, Easing.EasingOption.Linear);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return root;
    }

    /**
     * 現在の日付からi日引いた値を返すメソッド
     * @param i
     * @return
     */
    public int getToDate(int i){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -i);
        int toDate = Integer.parseInt(sdf.format(calendar.getTime()));

        return toDate;
    }

    /**
     *
     */
    public void setTodayDate(View root){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(date);
        int intDate = Integer.parseInt(sdf2.format(date));
        TextView textView = root.findViewById(R.id.text_todayDate);
        textView.setText("今日("+today+")");

        query = databaseReference.orderByChild("date").equalTo(intDate);
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
                    TextView textTimes = (TextView) getActivity().findViewById(R.id.text_todayTimes);
                    TextView textWeight = (TextView) getActivity().findViewById(R.id.text_todayWeight);

                    textTimes.setText(count + "回");
                    textWeight.setText(weight + "g");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
