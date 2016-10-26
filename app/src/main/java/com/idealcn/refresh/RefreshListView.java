package com.idealcn.refresh;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * author:idealgn
 * date:16-10-22 下午3:27
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener{

    private static final String TAG = "refresh";
    private LayoutInflater inflater;
    private int headerHeight;
    private   View headerView;
    private TextView mUpdateStatus;
    private TextView mUpdateTime;
    private ProgressBar mUpdateProgress;
    /*
    第一个可见的item
     */
    private int mFirstVisibleItem;
    /*
    按下时的起始y位置
     */
    private int startY;
    /*
    标记是否处在屏幕顶部位置
     */
    private boolean isOnTop = false;

    /**
     * 头布局默认状态
     */
    private  static  final int STATE_NORMAL = 0;
    /*
    释放状态
     */
    private static final int STATE_RELEASE = 1;
    /*
    下拉状态
     */
    private static final int STATE_PULL = 2;
    /*
    正在刷新状态
     */
    private static final int STATE_REFRESHING = 3;
    /*
    刷新成功
     */
    private static final int STATE_REFRESH_SUCCESS = 4;
    /*
    刷新失败
     */
    private static final int STATE_REFRESH_ERROR = 5;

    private int state = STATE_NORMAL;

    private OnRefreshListener listener;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.mFirstVisibleItem = firstVisibleItem;
    }

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
                    300
//                    ViewGroup.LayoutParams.MATCH_PARENT
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
        headerView.setPadding(0,height,
                0,0);
        headerView.invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if (mFirstVisibleItem==0){
                    isOnTop = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE: {
                onMove(ev);
            }
            break;
            case MotionEvent.ACTION_UP: {
                onMove(ev);
            }
                break;
            default:break;
        }
        return super.onTouchEvent(ev);
    }

    private void onMove(MotionEvent ev) {
        if (!isOnTop)return;
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_MOVE: {
                int diffY = (int) (ev.getY() - startY);
                if (diffY <= 0) {

                } else {
                    setHeaderPadding(-(headerHeight-diffY));
                    state = STATE_PULL;
                    setHeaderState(state);
                    if (diffY>=headerHeight){
                        state = STATE_RELEASE;
                        setHeaderState(state);
                    }
                }
            }
                break;
            case MotionEvent.ACTION_UP: {
                int diffY = (int) (ev.getY() - startY);
                if (diffY>=headerHeight){
                    state = STATE_REFRESHING;
                    setHeaderState(state);
                    setHeaderPadding(headerHeight);
                    if (listener!=null) {
                        boolean refreshing = listener.onRefreshing();
                        if (refreshing){
                            state = STATE_REFRESH_SUCCESS;
                            setHeaderState(state);
                        }else {
                            state = STATE_REFRESH_ERROR;
                            setHeaderState(state);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setHeaderPadding(-headerHeight);
                            }
                        },1500);
                    }
                }
            }
                break;
            default:break;
        }
    }

    /**
     * 设置头布局状态数据
     */
    private void setHeaderState(int state) {
        switch (state){
            case STATE_PULL:
                mUpdateStatus.setText("下拉刷新");
                break;
            case STATE_RELEASE:
                mUpdateStatus.setText("释放刷新");
                break;
            case STATE_REFRESHING:
                mUpdateStatus.setText("正在刷新");
                break;
            case STATE_REFRESH_SUCCESS:
                mUpdateStatus.setText("刷新成功");
                break;
            case STATE_REFRESH_ERROR:
                mUpdateStatus.setText("刷新失败");
                break;
            default:
                mUpdateStatus.setText("下拉刷新");
        }
    }




}
