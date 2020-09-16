package com.example.RoomChef;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class selectNetworkTask extends AsyncTask<Integer, String, Object> {
    Context context;
    String mAddr;
    ProgressDialog progressDialog;
    ArrayList<ingredient> list;

    public selectNetworkTask(Context context, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
        this.list = new ArrayList<ingredient>();
    }

    @Override
    protected void onPreExecute() {
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setTitle("Dialog");
//        progressDialog.setMessage("Get ....");
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

        return list;
    }

    private void Parser(String s){
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray =  new JSONArray(jsonObject.getString("ingredient_list"));
            list.clear();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int seq = jsonObject1.getInt("seq");
                String name = jsonObject1.getString("name");
                String date = jsonObject1.getString("date");


                ingredient ing = new ingredient(name, date, seq);
                list.add(ing);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


