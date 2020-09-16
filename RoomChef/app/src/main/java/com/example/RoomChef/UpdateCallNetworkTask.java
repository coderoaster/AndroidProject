package com.example.RoomChef;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UpdateCallNetworkTask extends AsyncTask<Integer, String, Object> {

    Context context;
    String mAddr;
    ArrayList<UserInfo> beans;
    ProgressDialog progressDialog;


    public UpdateCallNetworkTask(Context context, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
        this.beans = new ArrayList<UserInfo>();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Dialog");
        progressDialog.setMessage("Loading ....");
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
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

        try{
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();// 한 줄씩 가져온다.
                inputStreamReader = new InputStreamReader(inputStream);//하나씩 가져온걸 한 번에 포장을 한다.
                bufferedReader = new BufferedReader(inputStreamReader); //포장한것을 리스트에 띄운다.

                while(true){
                    String strline =bufferedReader.readLine();
                    if(strline == null) break;
                    stringBuffer.append(strline + "\n");
                }
            Parser(stringBuffer.toString());                //파싱이란 어떤 데이터를 원하는 모양으로 만들어 내는것.
                Log.v("오류",stringBuffer.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
                try {
                    if(bufferedReader != null) bufferedReader.close();
                    if(inputStreamReader != null) inputStreamReader.close();
                    if(inputStream != null) inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return beans;
    }
    public void Parser(String s) {
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("user_info")); // jsp 안에 있는 이름하고 같아야한다.
            beans.clear();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                String phone = jsonObject1.getString("phone");
                String pw = jsonObject1.getString("password");
                String image = jsonObject1.getString("image");

                UserInfo bean = new UserInfo(phone, pw, image);

                beans.add(bean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}//------
