package com.example.RoomChef;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class likelist extends Fragment {

    String urlAddr ;
    private String centIP = RecipeData.CENIP;
    String email = RecipeData.USERID;
    RecyclerView recyclerView;
    ArrayList<RecipeData> list;
    RecipeRecyclerAdapter adapter;
    Context mcontext;

    public static likelist newInstance() {
        return new likelist();
    }
    public likelist(){

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext=context;
    }

    //Fragment에서 실행될떄 MainActivity//onCreate와 같다고보면됨.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_recipe_list,container,false);
        recyclerView = rootView.findViewById(R.id.recuocler_list) ;
        connectionInsertData();

        recyclerView.setLayoutManager(new LinearLayoutManager(mcontext)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new RecipeRecyclerAdapter(list) ;
        recyclerView.setAdapter(adapter) ;

        //어뎁터 안에 만든 리스너를 불러와서 사용한다
        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent intent = new Intent(mcontext,SelectFood.class);
                intent.putExtra("seq",list.get(position).getSeq());
                intent.putExtra("view",list.get(position).getView());
                startActivity(intent);
                urlAddr ="http://192.168.0.148:8080/test/viewUpdate.jsp?seq=";
                urlAddr = urlAddr + list.get(position).getSeq()+"&view="+(Integer.parseInt(list.get(position).getView())+1);
                Log.v("url",urlAddr);
                connectionInsertData();
            }
        });


        return rootView;
    }
    private void connectionInsertData() {
        urlAddr ="";
        urlAddr = "http://" + centIP + ":8080/test/like_recipe.jsp?"; // centIP 는 항상 위에
        urlAddr = urlAddr + "email=" + email;
        try {
            NetworkTask networkTask = new NetworkTask(mcontext, urlAddr);
            Object obj = networkTask.execute().get();
            list = (ArrayList<RecipeData>) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}//-----
