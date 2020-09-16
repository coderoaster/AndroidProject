package com.example.RoomChef;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;


public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {
    private  Context mContext;
    private  ArrayList<RecipeData> mdata = null ;
    private  Button likebtn,unlikebtn,reply_insert,reply_list;
    private  String urlAddr;
    private String centIP = RecipeData.CENIP;
    private  String user_email = RecipeData.USERID;
    private  String recipeSeq =null;
    private  String liked;
    private RequestManager manager;
    private TextView textView1,textView2,textView3,viewtext;
    private ImageView imageView;

    public SelectAdapter(ArrayList<RecipeData> data) {
        mdata = data;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            textView1 = itemView.findViewById(R.id.select_recipe_name);
            textView2 = itemView.findViewById(R.id.select_recipe_food);
            textView3 = itemView.findViewById(R.id.select_recipe_how);
            imageView = itemView.findViewById(R.id.select_recipe_img);
            viewtext = itemView.findViewById(R.id.select_recipe_view);
            unlikebtn = itemView.findViewById(R.id.btn_unlike);
            likebtn = itemView.findViewById(R.id.btn_like);
            reply_insert= itemView.findViewById(R.id.reply_insert);
            reply_list=itemView.findViewById(R.id.reply_view);

            likebtn.setOnClickListener(onClickListener);
            unlikebtn.setOnClickListener(onClickListener);
            reply_insert.setOnClickListener(onClickListener);
            reply_list.setOnClickListener(onClickListener);
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            Intent intent;
            @Override

            public void onClick(View view) {
                recipeSeq = mdata.get(getAdapterPosition()).getSeq();
                switch (view.getId()){
                    case R.id.btn_unlike:
                        user_email.trim();
                        if(user_email == null || user_email.length() == 0 || user_email.equals("")){ // 이메일값이 없으면(비회원) like 사용 안되게
                            new AlertDialog.Builder(mContext)
                                    .setTitle("알림")
                                    .setMessage("로그인 후 이용 가능합니다.")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setCancelable(false)
                                    .setPositiveButton("확인", null)
                                    .show();
                        }else { // 이메일 값이 있으면
                            likebtn.setVisibility(View.VISIBLE);
                            unlikebtn.setVisibility(View.INVISIBLE);
                            urlAddr = "";
                            urlAddr = "http://" + centIP + ":8080/test/like.jsp?"; // like 테이블에 해당 user의 like 업데이트
                            urlAddr = urlAddr + "email=" + user_email + "&recipeSeq=" + recipeSeq;// email, recipeSeq 들고 jsp로 슝
                            Log.v("TAG", urlAddr);
                            connectLikeData();
                        }
                        break;
                    case R.id.btn_like:
                        likebtn.setVisibility(View.INVISIBLE);
                        unlikebtn.setVisibility(View.VISIBLE);
                        urlAddr = "";
                        urlAddr = "http://" + centIP + ":8080/test/unlike.jsp?"; // like 테이블에서 해당 user의 like 삭제
                        urlAddr = urlAddr + "email=" + user_email + "&recipeSeq=" + recipeSeq;// email, recipeSeq 들고 jsp로 슝
                        Log.v("TAG", urlAddr);
                        connectLikeData();
                        break;
                    case R.id.reply_insert:
                        intent = new Intent(mContext,ReviewActivity.class);
                        intent.putExtra("seq",recipeSeq);
                        mContext.startActivity(intent);
                        break;
                    case R.id.reply_view:
                        intent = new Intent(mContext,SelectReviewList.class);
                        intent.putExtra("name",mdata.get(getAdapterPosition()).getName());
                        intent.putExtra("seq",recipeSeq);
                        mContext.startActivity(intent);
                        break;
                }
            }
        };
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.activity_food_item, parent, false) ;
        SelectAdapter.ViewHolder vh = new SelectAdapter.ViewHolder(view) ;



        //선언한 context parent.getContext 값을 넣어준다 (호출한 Context)
        mContext = parent.getContext();
        //Glide 라이브러리의 Context 값을 미리 넣어준다 ! 넣어주지 않고 한번에 호출해서 불러오면 값을 불러오기전에 CONTEXT 가 닫힘)
        manager = Glide.with(mContext);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //좋아요 여부 체크
        recipeSeq = mdata.get(position).getSeq();
        urlAddr = "";
        urlAddr = "http://" + centIP + ":8080/test/like_call.jsp?"; // centIP 는 위에 지정되어있음 해당아이피로 바꿔야함.
        urlAddr = urlAddr + "email=" + user_email + "&recipeSeq=" + recipeSeq;// email, recipeSeq 들고 jsp로 슝
        Log.v("TAG", urlAddr);
        connectLikCalleData(); // like 되어있는지 여부를 가져옴
        if(liked.equals("1")){ // like 가 되어있으면
            likebtn.setVisibility(View.VISIBLE); // 빨간색하트 나오고
            unlikebtn.setVisibility(View.INVISIBLE); // 검정색하트 들어가고
        }else { // 그렇지 않으면(unlike 거나 비회원)
            likebtn.setVisibility(View.INVISIBLE); // 빨간색하트 들어가고
            unlikebtn.setVisibility(View.VISIBLE); // 검정색하트 나오고
        }


        //레시피 이미지
        String imgurl = mdata.get(position).getImgurl();
        Glide.with(mContext).load(imgurl).into(imageView);
        //레시피 이름
        textView1.setText(mdata.get(position).getName());
        //식재료 순서대로 잘라서 다시 붙이는 작업
        String food = mdata.get(position).getFood();
        String[] foodlist;
        foodlist = food.split("#");
        food="";
        for (int i=0; i < foodlist.length; i++){
            food = food+foodlist[i]+"\n";
        }
        textView2.setText(food);
//        조리법 순서대로 가져와서 자른후 다시 붙이는 작업

        String how = mdata.get(position).getHow();
        String[] howlist = how.split("#");
        how="";
        for(int i=0; i<howlist.length; i++){
            how = how + howlist[i]+"\n";
        }
        textView3.setText(how);
        viewtext.setText("조회수"+"\n"+mdata.get(position).getView());

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
    private void connectLikCalleData() { // like 여부 불러오기
        try {
            LikeCallNetworkTask likeCallNetworkTask = new LikeCallNetworkTask(mContext, urlAddr);
            liked = likeCallNetworkTask.execute().get().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void connectLikeData() { // like 업데이트
        try {
            InsNetworkTask likeNetworkTask = new InsNetworkTask(mContext, urlAddr);
            likeNetworkTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}