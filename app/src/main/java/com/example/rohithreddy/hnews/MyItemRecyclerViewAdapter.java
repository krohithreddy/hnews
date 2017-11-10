package com.example.rohithreddy.hnews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final Context mListener;
    private List<mapuser> mapuserList;


    public MyItemRecyclerViewAdapter(List<mapuser> MapuserList, Context listener) {
        System.out.println("----------------------------"+MapuserList);
        mapuserList = MapuserList;
        System.out.println(MapuserList);
        mListener = listener;
    }

    @Override
    public MyItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new MyItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyItemRecyclerViewAdapter.ViewHolder holder, final int position) {
//        holder.mItem = mValues.get(position);
        holder.outletname.setText(mapuserList.get(position).gettitle());
        holder.username.setText(mapuserList.get(position).gettimestamp());
        holder.phonenum.setText(mapuserList.get(position).getmessage());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // System.out.println(mapuserList.get(position).getPhonenumber());

                Intent intent = new Intent(mListener, Notification.class);
                System.out.println("holder"+mapuserList.get(position).getimageUrl());
                intent.putExtra("url",mapuserList.get(position).getimageUrl());
                intent.putExtra("title",mapuserList.get(position).gettitle());
                intent.putExtra("message",mapuserList.get(position).getmessage());
                mListener.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mapuserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View v;
        public final TextView outletname,username,phonenum;

        public ViewHolder(View view) {
            super(view);
            v = view;
            outletname=(TextView) v.findViewById(R.id.outletname);
            username=(TextView) v.findViewById(R.id.username);
            phonenum=(TextView) v.findViewById(R.id.phonenum);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + outletname.getText() + "'";
        }
    }
}
