package com.example.RoomChef;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;


public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RecipeData> mData = null ;
    private RequestManager manager;
    private String urlAddr;
    private String centIP = RecipeData.CENIP;
    private String user_email = RecipeData.USERID;
    private String recipeSeq ="2";
    private String liked;

    //ClickEvent 이름을 정하고 인터페이스로 선언

    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);
    }
    //만든 인터페이스를 전역변수로 선언
    private OnItemClickListener mListener = null;


    //OnItemClickListener 를 객체 생성할때 값을 넣어주기 위해서 선언
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ,textView2;
        ImageView imageView;
        Button like_button, unlike_button2;

        ViewHolder(View itemView) {
            super(itemView) ;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos =getAdapterPosition();
                    if (pos!=RecyclerView.NO_POSITION){
                        // 선택한 position 값 구하기 (번호)
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            if (mListener != null) {
                                mListener.onItemClickListener(view, position);
                            }
                        }
                    }
                }
            });



            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.card_view_text) ;
            textView2 = itemView.findViewById(R.id.recipe_view);
            imageView = itemView.findViewById(R.id.card_view_img);
            like_button = itemView.findViewById(R.id.list_btn_like);
            unlike_button2 = itemView.findViewById(R.id.list_btn_unlike);

            like_button.setOnClickListener(onClickListener);
            unlike_button2.setOnClickListener(onClickListener);



        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                recipeSeq = mData.get(getAdapterPosition()).getSeq();
                switch (view.getId()){
                    case R.id.list_btn_unlike:
                        if(user_email == null || user_email.length() == 0 || user_email.equals("")){ // 이메일값이 없으면(비회원) like 사용 안되게
                            new AlertDialog.Builder(context)
                                    .setTitle("알림")
                                    .setMessage("로그인 후 이용 가능합니다.")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setCancelable(false)
                                    .setPositiveButton("확인", null)
                                    .show();
                        }else { // 이메일 값이 있으면
                            like_button.setVisibility(View.VISIBLE);
                            unlike_button2.setVisibility(View.INVISIBLE);
                            urlAddr = "";
                            urlAddr = "http://" + centIP + ":8080/test/like.jsp?"; // like 테이블에 해당 user의 like 업데이트
                            urlAddr = urlAddr + "email=" + user_email + "&recipeSeq=" + recipeSeq;// email, recipeSeq 들고 jsp로 슝
                            Log.v("TAG", urlAddr);
                            connectLikeData();
                        }
                        break;
                    case R.id.list_btn_like:
                        like_button.setVisibility(View.INVISIBLE);
                        unlike_button2.setVisibility(View.VISIBLE);
                        urlAddr = "";
                        urlAddr = "http://" + centIP + ":8080/test/unlike.jsp?"; // like 테이블에서 해당 user의 like 삭제
                        urlAddr = urlAddr + "email=" + user_email + "&recipeSeq=" + recipeSeq;// email, recipeSeq 들고 jsp로 슝
                        Log.v("TAG", urlAddr);
                        connectLikeData();
                        break;
                }
            }
        };
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecipeRecyclerAdapter(ArrayList<RecipeData> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecipeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.activity_recycler_item, parent, false) ;
        RecipeRecyclerAdapter.ViewHolder vh = new RecipeRecyclerAdapter.ViewHolder(view) ;
        //선언한 context parent.getContext 값을 넣어준다 (호출한 Context)
        context = parent.getContext();
        //Glide 라이브러리의 Context 값을 미리 넣어준다 ! 넣어주지 않고 한번에 호출해서 불러오면 값을 불러오기전에 CONTEXT 가 닫힘)
        manager = Glide.with(context);


        return vh ;
    }


    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RecipeRecyclerAdapter.ViewHolder holder, int position) {
        String text = mData.get(position).getName();
        String imgurl =mData.get(position).getImgurl();
        String view = mData.get(position).getView();

        recipeSeq = mData.get(position).getSeq();
        urlAddr = "";
        urlAddr = "http://" + centIP + ":8080/test/like_call.jsp?"; // centIP 는 위에 지정되어있음 해당아이피로 바꿔야함.
        urlAddr = urlAddr + "email=" + user_email + "&recipeSeq=" + recipeSeq;// email, recipeSeq 들고 jsp로 슝
        Log.v("TAG", urlAddr);
        connectLikCalleData(); // like 되어있는지 여부를 가져옴
        if(liked.equals("1")){ // like 가 되어있으면
            holder.like_button.setVisibility(View.VISIBLE); // 빨간색하트 나오고
            holder.unlike_button2.setVisibility(View.INVISIBLE); // 검정색하트 들어가고
        }else { // 그렇지 않으면(unlike 거나 비회원)
            holder.like_button.setVisibility(View.INVISIBLE); // 빨간색하트 들어가고
            holder.unlike_button2.setVisibility(View.VISIBLE); // 검정색하트 나오고
        }

        manager.load(imgurl).into(holder.imageView);
        holder.textView1.setText(text) ;
        holder.textView2.setText("조회수 " + view);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    private void connectLikeData() { // like 업데이트
        try {
            InsNetworkTask likeNetworkTask = new InsNetworkTask(context, urlAddr);
            likeNetworkTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void connectLikCalleData() { // like 여부 불러오기
        try {
            LikeCallNetworkTask likeCallNetworkTask = new LikeCallNetworkTask(context, urlAddr);
            liked = likeCallNetworkTask.execute().get().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
