package com.example.semiproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectAllActivity extends Activity {

    String urlAddr;
    ArrayList<AddressBook> members;
    AddressBookAdapter adapter;
    ListView listView;
    String centIP;
    private final static String TAG = "my tag";
    Button btn_search;
    Button btn_insert;
    EditText edit_search;
    String keyword;
    Intent intent;
    String userseq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlist);
        Log.v(TAG,"ppp");
        intent = getIntent();
        userseq = intent.getStringExtra("email");

        btn_search = findViewById(R.id.btn_search);
        btn_insert = findViewById(R.id.btn_add1);
        edit_search = findViewById(R.id.edit_search);


        centIP = "192.168.0.89";
        urlAddr = "http://" + centIP + ":8080/test/addressBook_query_all.jsp?id="+userseq;
        connectGetDate();
        btn_search.setOnClickListener(onClickListener);
        btn_insert.setOnClickListener(onClickListener);
        listView.setOnItemClickListener(ItemClickListener);
    }

    AdapterView.OnItemClickListener ItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            intent = new Intent(SelectAllActivity.this,UpdateDeleteActivity.class);
            intent.putExtra("seq",members.get(i).getNo());
            intent.putExtra("userseq",userseq);
            startActivity(intent);

        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            keyword = edit_search.getText().toString();

            switch (view.getId()) {
                case R.id.btn_add1:
                    intent = new Intent( SelectAllActivity.this, InsertActivity.class);
                    intent.putExtra("userseq",userseq);
                    startActivity( intent );
                    break;
                case R.id.btn_search:
                    urlAddr = "http://192.168.0.89:8080/test/addressBook_search.jsp?keyword="+keyword+"&id="+userseq;
                    Log.v("key",keyword);
                    connectGetDate();
                    break;
            }

        }
    };

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(SelectAllActivity.this)
                .setTitle("주소록을 종료하시겠습니까?")
                .setCancelable(false)
                .setNegativeButton("취소", null)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .show();



    }

    @Override
    protected void onResume() {
        super.onResume();
        connectGetDate();
    }

    private void connectGetDate(){
        Log.v(TAG,"ppp");
        try {
        AddlistNetworkTask networkTask = new AddlistNetworkTask(SelectAllActivity.this, urlAddr);
        Object obj = networkTask.execute().get();
        members=(ArrayList<AddressBook>) obj;
        adapter = new AddressBookAdapter(SelectAllActivity.this,R.layout.addrlistview,members);
        listView = findViewById(R.id.lv_address);
        listView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();

        }
    }
}