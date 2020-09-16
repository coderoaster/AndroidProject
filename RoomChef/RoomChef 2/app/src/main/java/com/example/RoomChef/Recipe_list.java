
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


public class Recipe_list extends Fragment {

    String urladdr;
    private String centIP = RecipeData.CENIP;
    ArrayList<RecipeData> list;
    RecipeRecyclerAdapter adapter;
    RecyclerView recyclerView;
    Context mcontext;
    String updateurladdr ;


    public Recipe_list(){

    }

    @Override
    public void onResume() {
        setList();
        super.onResume();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext =context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_recipe_list,container,false);
        recyclerView = rootView.findViewById(R.id.recuocler_list) ;
        setList();


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.

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
                updateurladdr ="http://172.30.1.33:8080/test/viewUpdate.jsp?seq=";
                updateurladdr = updateurladdr + list.get(position).getSeq()+"&view="+(Integer.parseInt(list.get(position).getView())+1);
                Log.v("url",updateurladdr);
                connectionInsertData();
            }
        });

        return rootView;

    }
    private void connectionInsertData() {
        try {
            InsNetworkTask insNetworkTask = new InsNetworkTask(mcontext, updateurladdr);
            insNetworkTask.execute().get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setList(){
        String search =  RecipeData.SEARCH;
        if (search==null){
            urladdr = "http://"+centIP+":8080/test/recipe_all.jsp?name=";
            Log.v("search",urladdr);
        }else {
            urladdr = "http://"+centIP+":8080/test/recipe_all.jsp?name=";
            urladdr = urladdr + search;
            Log.v("search",urladdr);

        }


        try{
            list = new ArrayList<RecipeData>();
            NetworkTask networkTask = new NetworkTask(mcontext,urladdr);
            list = (ArrayList<RecipeData>) networkTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}





