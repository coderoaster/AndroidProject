package com.example.semiproject;

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

public class AddlistNetworkTask extends AsyncTask<Integer, String, Object> {
//AsyncTask 에서 Object는 ArrayList이다. 그래야 Object가 리턴 값을 받는다.

    Context context;
    String mAddr;
    ProgressDialog progressDialog;
    ArrayList<AddressBook> members;
    private static String TAG = "mytag";


    public AddlistNetworkTask(Context context, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
        this.members = new ArrayList<AddressBook>();
        Log.v(TAG,"ppp");
    }

    //AsyncTask 에서 Object는 ArrayList이다. 그래야 Object가 리턴 값을 받는다.




    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Dialog");
        progressDialog.setMessage("Get....");
        progressDialog.show();
        Log.v(TAG,"ppp");
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.v(TAG,"ppp");


    }
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
        Log.v(TAG,"ppp");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.v(TAG,"ppp");
    }

    @Override
    protected Object doInBackground(Integer... integers) {
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            Log.v(TAG,mAddr);
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);

            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK ){
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true){
                    String strline = bufferedReader.readLine() ;
                    if ((strline) == null)  break;
                    stringBuffer.append(strline + "\n");
                }
                Parser(stringBuffer.toString());
            }



        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (inputStreamReader != null)inputStreamReader.close();
                if (inputStream != null) inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        return members;  //ArrayList로 간다.
    }

    private void Parser (String s) {
        Log.v(TAG,"ppp");
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("AddressBook_info"));
            members.clear();

            for (int i = 0; i < jsonArray.length(); i ++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String no = jsonObject1.getString("no");
                String name = jsonObject1.getString("name");
                String phone = jsonObject1.getString("phone");
                String address = jsonObject1.getString("relation");

                AddressBook member = new AddressBook(no, name, phone, address);
                members.add(member);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

