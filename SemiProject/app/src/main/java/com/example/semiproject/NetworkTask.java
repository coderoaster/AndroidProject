package com.example.semiproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkTask extends AsyncTask<Integer, String, Integer> {

    final static String TAG = "NetworkTask : ";
    Context context;
    String mAddr;
    ProgressDialog progressDialog;
    String devicePath;
    ImageView imageView;

    public NetworkTask(Context context, String mAddr, ImageView imageView) {
        this.context = context;
        this.mAddr = mAddr;
        this.imageView = imageView;
    }

    // ProgressDialog 설정 --------------------------------------------
    // 데이터를 받고있을때
    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute");

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Dialogue()");
        progressDialog.setMessage("down.....");
        progressDialog.show();
    }

    // 데이터가 바뀌엇을때
    @Override
    protected void onProgressUpdate(String... values) {
        Log.v(TAG, "onProgressUpdate()");
        super.onProgressUpdate(values);

    }

    // 데이터가 완료되었을때
    @Override
    protected void onPostExecute(Integer integer) {
        Log.v(TAG, "onPostExecute");

        Bitmap bitmap = BitmapFactory.decodeFile(devicePath);
        imageView.setImageBitmap(bitmap);
        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        Log.v(TAG, "onCancelled()");

        super.onCancelled();
    }

    // ProgressDialog 설정끝 --------------------------------------------


    @Override
    protected Integer doInBackground(Integer... integers) {
        // /의 맨 마지막 의 값을 가져온다.
        int index = mAddr.lastIndexOf("/");
        String imgName = mAddr.substring(index + 1);

        devicePath = Environment.getDataDirectory().getAbsolutePath() + "/data/com.example.semiproject/files/" + imgName;
        Log.v(TAG, "핸드폰 절대주소 : " + Environment.getDataDirectory().getAbsolutePath());

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {

            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);

            // 총 데이터중에 지정한만큼식 가져와라
            int len = httpURLConnection.getContentLength();
            byte[] bytes = new byte[len];

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                fileOutputStream = context.openFileOutput(imgName, 0);

                while (true) {
                    int i = inputStream.read(bytes);
                    if (i < 0) {
                        break;
                    }

                    fileOutputStream.write(bytes, 0, i);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream == null) fileOutputStream.close();
                if (inputStream == null) inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
//[출처] AndroidStudio 서버에있는 Image 가져오기|작성자 피실


