package com.idealcn.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * author:idealgn
 * date:16-10-22 下午3:27
 */
public class RefreshListView extends ListView{

    private static final String TAG = "refresh";
    private LayoutInflater inflater;
    private int headerHeight;
    private   View headerView;
    private TextView mUpdateStatus;
    private TextView mUpdateTime;
    private ProgressBar mUpdateProgress;

    private OnRefreshListener listener;
    public interface OnRefreshListener{
        boolean onRefreshing();
    }

    public void setOnRefreshListener(OnRefreshListener l){
        this.listener = l;
    }

    public RefreshListView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        initHeaderView();
    }

    private void initHeaderView() {
        headerView = inflater.inflate(R.layout.layout_header,null,false);
        initViews();
        measureHeight(headerView);
        headerHeight = headerView.getMeasuredHeight();
        Log.d(TAG, "initHeaderView: "+headerHeight);
        setHeaderPadding(-headerHeight);
        addHeaderView(headerView);
    }

    private void initViews() {
        mUpdateStatus = (TextView) headerView.findViewById(R.id.update_status);
        mUpdateTime = (TextView) headerView.findViewById(R.id.update_time);
        mUpdateProgress = (ProgressBar) headerView.findViewById(R.id.update_progress);
    }

    private void measureHeight(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params==null)
            params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    200
            );

        int width = ViewGroup.getChildMeasureSpec(0,0,params.width);

        int height = params.height;

        if (height>0){
            height = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        }else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        view.measure(width,height);


    }

    private void setHeaderPadding(int height) {
        headerView.setPadding(headerView.getPaddingLeft(),height,
                headerView.getPaddingRight(),headerView.getPaddingBottom());
        headerView.invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float y = 0;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE: {
                float diffY = ev.getY() - y;
                if (diffY > 0) {
                    if (diffY >= headerHeight) {
                        break;
                    }
                    setHeaderPadding((int) -(headerHeight - diffY));
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                float diffY = ev.getY() - y;
                if (diffY<headerHeight/3){
                    setHeaderPadding(-headerHeight);
                }else {
                    setHeaderPadding(0);
                    if (listener!=null) {
                        boolean refresh = listener.onRefreshing();
                            updateHeaderStatus(refresh);
                    }
                }
            }
                break;
            default:break;
        }
        return super.onTouchEvent(ev);
    }

    private void updateHeaderStatus(boolean status) {
        if (status){
            mUpdateStatus.setText("刷新完成");
            mUpdateTime.setText(System.currentTimeMillis()+"");
        }else {
            mUpdateStatus.setText("刷新失败");
            mUpdateTime.setText(System.currentTimeMillis()+"");
        }
        setHeaderPadding(-headerHeight);
    }


}
