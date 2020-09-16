package com.example.RoomChef;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LikeCallNetworkTask extends AsyncTask<Integer, String , Object> {
    Context context;
    String mAddr;
    ProgressDialog progressDialog;
    String  like;

    public LikeCallNetworkTask(Context context, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
    }

    @Override
    protected void onPreExecute() {
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setTitle("Dialog");
//        progressDialog.setMessage("Loading .....");
//        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
//        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }



    @Override
    protected Object doInBackground(Integer... integers) {
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream(); // 데이터 가져오기
                inputStreamReader = new InputStreamReader(inputStream); // 가져온 데이터를 리더에 넣기
                bufferedReader = new BufferedReader(inputStreamReader); // 버퍼드리더에 넣기

                while (true){
                    String strline = bufferedReader.readLine();
                    if (strline == null) break; // 브레이크 만나면 와일문 빠져나감
                    stringBuffer.append(strline + "\n");
                } // 와일문 끝나면 다 가져왔다
                Parser(stringBuffer.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return like; // 좋아요 리턴시키고 엑티비티에서 받음
    }
    private void Parser(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            like = jsonObject.getString("liked"); // 디비에서 좋아요 받아옴
            Log.v("좋아요", like);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
