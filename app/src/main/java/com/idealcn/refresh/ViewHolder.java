package com.idealcn.refresh;

import android.util.SparseArray;
import android.view.View;

/**
 * author:idealgn
 * date:16-10-22 上午10:14
 */
public class ViewHolder {


    public static <T extends View> T get(View view, int id) {
        SparseArray<T> holder = (SparseArray) view.getTag();
        if (holder == null) {
            holder = new SparseArray();
            view.setTag(holder);
        }
        T childView = holder.get(id);
        if (childView == null) {
            childView = (T) view.findViewById(id);
            holder.put(id, childView);
        }
        return childView;
    }

}
