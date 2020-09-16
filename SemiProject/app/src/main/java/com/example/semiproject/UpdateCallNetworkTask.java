package com.example.semiproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.storage.StorageManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UpdateCallNetworkTask extends AsyncTask<Integer, String, Object> {
    Context context;
    String mAddr;
    ArrayList<Member> members;
    ProgressDialog progressDialog;

    public UpdateCallNetworkTask(Context context, String mAddr,ArrayList<Member> members) {
        this.context = context;
        this.mAddr = mAddr;
        this.members = new ArrayList<Member>();
    }



    @Override
    protected void onPreExecute() {
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

            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();// 한줄씩 가져오고
                inputStreamReader = new InputStreamReader(inputStream); // 하나씩 가져온걸 한꺼번에 포장을 하고
                bufferedReader = new BufferedReader(inputStreamReader); // 포장한것을 리스트에 올림

                while(true){
                    String strline = bufferedReader.readLine();
                    if(strline == null) break;
                    stringBuffer.append(strline + "\n");
                }

                Parser(stringBuffer.toString());
                Log.v("오류",stringBuffer.toString());

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(bufferedReader != null) bufferedReader.close();
                if(inputStreamReader != null) inputStreamReader.close();
                if(inputStream != null) inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return members;
    }
    public void Parser(String s){
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray =  new JSONArray(jsonObject.getString("address_info"));
            members.clear();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String name = jsonObject1.getString("name");
                String phone = jsonObject1.getString("phone");
                String address = jsonObject1.getString("address");
                String relation = jsonObject1.getString("relation");
                String img = jsonObject1.getString("img");

                Member member = new Member(name, phone, address, relation,img);

                members.add(member);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
