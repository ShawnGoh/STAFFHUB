//package com.example.blewifiterm5project.Adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.blewifiterm5project.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import java.util.ArrayList;
//
//public class UserActivityLogRecyclerAdapter extends RecyclerView.Adapter<UserActivityLogRecyclerAdapter.Viewholder> {
//    public static final int MSG_TYPE_LEFT = 0;
//    public static final int MSG_TYPE_RIGHT = 1;
//
//    private Context mcontext;
//    private ArrayList<Chat> mChat;
//    private String imageurlleft;
//    private String imageurlright;
//    FirebaseUser fuser;
//
//    public messageAdapter(Context mcontext, ArrayList<Chat> mChat, String imageurlleft, String imageurlright) {
//        this.mcontext = mcontext;
//        this.mChat = mChat;
//        this.imageurlleft = imageurlleft;
//        this.imageurlright = imageurlright;
//    }
//
//    public class Viewholder extends RecyclerView.ViewHolder{
//        public TextView show_message;
//        public ImageView profile_image;
//
//        public Viewholder(@NonNull View itemView) {
//            super(itemView);
//
//
//            show_message = itemView.findViewById(R.id.chatmessagemessagecontent);
//            profile_image = itemView.findViewById(R.id.chatmessageprofilepic);
//        }
//    }
//
//    @NonNull
//    @Override
//    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mcontext).inflate(R.layout.useractivityloglayout, parent, false);
//        return new Viewholder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
//
//
//
//
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return mChat.size();
//    }
//}