package joshisen.abcc.aso.nekokan.ui.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import joshisen.abcc.aso.nekokan.FileLoader;
import joshisen.abcc.aso.nekokan.MainActivity;
import joshisen.abcc.aso.nekokan.R;

public class CameraFragment extends Fragment{
    Map<String,String> data;
    SimpleAdapter adapter;
    ArrayList<Map<String, String>> list = new ArrayList<>();

    //データベース参照[Firebaseデータベースのpictureテーブルを参照する]
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("picture");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera, container, false);
        setAdapter(root);
        setImage(0);

        return root;
    }

    /**
     * SimpleAdapterに使うListを取得する
     * @return
     */
    public void setAdapter(View root) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        int minusMin = 0;

        Query query = databaseReference.limitToLast(15);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    //ダウンロードURL
                    String date = "";

                    //データがあるかどうかを判定
                    if (dataSnapshot.exists()) {
                        //データがある場合にはデータスナップショットから１件ずつissueに登録する
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            date = (String) issue.child("date").getValue();
                            data = new HashMap();
                            data.put("date", date);
                            list.add(0,data);
                        }
                    }

                    ListView pictureListView = (ListView) getActivity().findViewById(R.id.listView_pictures);

                    adapter = new SimpleAdapter(
                            getActivity(),
                            list,   //ArrayList
                            R.layout.camera_list,   //1 行レイアウト
                            new String[]{"date"},  //map のキーどの項目を
                            new int[]{R.id.textView_date}   //どの id 項目に入れるか
                    );

                    pictureListView.setAdapter(adapter);

                    pictureListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            setImage(position);
                            System.out.println("ポディション:" + position);

                        }
                    });
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setImage(int position){

        //クエリの作成[最後にpictureデーブルに追加された5件を取得する]
        Query query = databaseReference.limitToLast(position+1);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    //ダウンロードURL
                    String downloadUrl = "";

                    //データがあるかどうかを判定
                    if (dataSnapshot.exists()) {
                        //データがある場合にはデータスナップショットから１件ずつissueに登録する
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            downloadUrl = (String) issue.child("downloadUrl").getValue();
                            break;
                        }
                    }

                    System.out.println("URL::" + downloadUrl);

                    FileLoader task = new FileLoader(getActivity());
                    task.execute(downloadUrl);
                }catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
