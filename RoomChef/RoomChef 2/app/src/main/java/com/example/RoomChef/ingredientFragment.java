package com.example.RoomChef;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ingredientFragment extends Fragment {

    Context mContext;
    ArrayList<ingredient> data;
    ingredientAdapter adapter;
    ListView listView;
    String name, date;
    EditText ed_name, ed_date;
    Button btn_add;
    private String centIP = RecipeData.CENIP;
    String urlAddr;
    String email = RecipeData.USERID;
    InputMethodManager mimm;
    int seq;
    int position1;

    public static ingredientFragment newInstance() {
        return new ingredientFragment();
    }
    public ingredientFragment(){

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext =context;
        Log.v("mContext","onAttach"+mContext.toString());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("mContext","onCreate"+mContext.toString());


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v("mContext","onViewCreated"+mContext.toString());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.v("mContext","onActivityCreated"+mContext.toString());

    }

    @Override
    public void onResume() {
        super.onResume();
        connectGetData(); // 입력 후 리스트 새로 불러옴
        Log.v("mContext","onResume"+ mContext.toString());

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.ingredient,container,false);
        listView = rootView.findViewById(R.id.lv_ingredient);
        btn_add = rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(onClickListener); //입력버튼 누리면 입력 다이얼로그
        listView.setOnItemLongClickListener(longClickListener); //길게누르면 삭제 다이얼로그
        urlAddr = "http://" + centIP + ":8080/test/select_ingredient.jsp?";
        urlAddr = urlAddr + "email=" + email;

        return rootView;

    }

    View.OnClickListener onClickListener = new View.OnClickListener() { // 식재료 입력받습니다.
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view1 = inflater.inflate(R.layout.insert_layout, null);
            ed_name = view1.findViewById(R.id.ed_ingredient);
            ed_date = view1.findViewById(R.id.ed_expire);

            builder.setView(view1)
                    .setTitle("식재료를 입력해주세요")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                        @Override

                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                name = ed_name.getText().toString().trim();
                                date = ed_date.getText().toString().trim();
                                urlAddr = "http://" + centIP + ":8080/test/insert_ingredient.jsp?";
                                urlAddr = urlAddr + "email=" + email + "&date=" + date + "&name=" + name;
                                connectionInsertData();
                                onResume(); // 리스트 새로 불러오기
                                Toast.makeText(getContext(),"입력되었습니다.",Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("취소", null)
                    .show();


        }

    };

    protected void connectGetData() { // 리스트 불러오기
        try {
            urlAddr = "http://" + centIP + ":8080/test/select_ingredient.jsp?";
            urlAddr = urlAddr + "email=" + email;

            selectNetworkTask networkTask = new selectNetworkTask(mContext, urlAddr);
            // execute() java 파일안의 메소드 한번에 동작시키기, 메소드를 사용하면 HttpURLConnection 이 제대로 작동하지않는다.
            Object obj = networkTask.execute().get();
            data = (ArrayList<ingredient>) obj;

            adapter = new ingredientAdapter(mContext, R.layout.listview, data);
            listView.setAdapter(adapter);
            listView.setOnItemLongClickListener(longClickListener);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void connectionInsertData(){ // 리스트 입력 삭제
        try {
            InsNetworkTask insNetworkTask = new InsNetworkTask(mContext, urlAddr);
            insNetworkTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            seq = data.get(position).getSeq();
            //openDialog();
            position1 = position;

            new AlertDialog.Builder(getContext())
                    .setTitle("데이터를 삭제하시겠습니까?")
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)   // 배경 눌러도 안닫힘
                    .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            urlAddr = "http://" + centIP + ":8080/test/delete_ingredient.jsp?";
                            urlAddr = urlAddr + "seq=" + seq;
                            connectionInsertData();


                            // data ArrayList 안에있는 데이터 삭제후 ListView 새로고침.
                            data.remove(position1);
                            listView.clearChoices();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton("취소", null)
                    .show();

            return true;
        }
    };

}
