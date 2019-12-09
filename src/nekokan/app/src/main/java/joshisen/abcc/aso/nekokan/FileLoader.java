package joshisen.abcc.aso.nekokan;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import joshisen.abcc.aso.nekokan.ui.camera.CameraFragment;

public class FileLoader extends AsyncTask<String, Void, Bitmap>{
    private Activity mActivity;

    public FileLoader(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bImage = null;
        try {
            URL url = new URL(params[0]);
            InputStream is = url.openStream();
            bImage = BitmapFactory.decodeStream(is);
            is.close();
            return bImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        try {
            ImageView imageView = this.mActivity.findViewById(R.id.imageView2);
            System.out.println("-----画像を更新-----");
            imageView.setImageBitmap(null);
            imageView.setImageBitmap(result);
        }catch (Exception e){}
    }
}
