package com.idealcn.refresh;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * author:idealgn
 * date:16-10-22 下午5:37
 */
public class ResultAdapter extends BaseAdapter {

    private Context context;
    private List<LoveResult> results;
    private LayoutInflater inflater;
   public ResultAdapter(Context context, List<LoveResult> results){
        this.context = context;
       this.results = results;
       inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public LoveResult getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = inflater.inflate(R.layout.adapter,null);
        }

      TextView mTextId =  ViewHolder.get(convertView,R.id.item_id);
        TextView mTextTitle = ViewHolder.get(convertView,R.id.item_title);

        LoveResult loveResult = results.get(position);
        mTextId.setText(loveResult.getId()+"");
        mTextTitle.setText(loveResult.getTitle());
        return convertView;
    }


}
