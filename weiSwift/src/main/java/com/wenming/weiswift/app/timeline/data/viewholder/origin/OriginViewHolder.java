package com.wenming.weiswift.app.timeline.data.viewholder.origin;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.timeline.adapter.TimeLineImageAdapter;
import com.wenming.weiswift.app.timeline.adapter.VideoImgAdapter;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineContract;
import com.wenming.weiswift.app.timeline.data.viewholder.base.BaseTimeLineViewHolder;
import com.wenming.weiswift.widget.emojitextview.EmojiTextView;
import com.wenming.weiswift.widget.emojitextview.WeiBoContentTextUtil;

public class OriginViewHolder extends BaseTimeLineViewHolder implements OriginContract.View {
    private LinearLayout mStatusContainerLl;
    private EmojiTextView mTextContentTv;
    private RecyclerView mImgListRlv;
    private OriginContract.Presenter mPresenter;

    public OriginViewHolder(Context context, View v) {
        super(context, v);
    }

    @Override
    protected void prepareView() {
        super.prepareView();
        mStatusContainerLl = (LinearLayout) findViewById(R.id.timeline_origin_pictext_container_ll);
        mTextContentTv = (EmojiTextView) findViewById(R.id.timeline_origin_pictext_etv);
        mImgListRlv = (RecyclerView) findViewById(R.id.timeline_origin_pictext_rlv);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initListener() {
        super.initListener();
        //微博背景的点击事件
        mStatusContainerLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.goToStatusDetailActivity();
            }
        });
    }

    @Override
    public void setPresenter(BaseTimeLineContract.Presenter presenter) {
        mPresenter = (OriginContract.Presenter) presenter;
    }

    @Override
    public void setImgListContent(String text) {
        mTextContentTv.setText(WeiBoContentTextUtil.getWeiBoContent(text, mContext, mTextContentTv));
    }

    @Override
    public void setImgListContent(Status status) {
        GridLayoutManager gridLayoutManager = initGridLayoutManager(status.bmiddle_pic_urls);
        TimeLineImageAdapter imgListAdapter = new TimeLineImageAdapter(status, mContext);
        mImgListRlv.setHasFixedSize(true);
        mImgListRlv.setAdapter(imgListAdapter);
        mImgListRlv.setLayoutManager(gridLayoutManager);
        imgListAdapter.setData(status);
        mImgListRlv.setNestedScrollingEnabled(false);
    }

    @Override
    public void setVideoImg(String videoImg) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        VideoImgAdapter videoImgAdapter = new VideoImgAdapter(videoImg, mContext);
        mImgListRlv.setHasFixedSize(true);
        mImgListRlv.setAdapter(videoImgAdapter);
        mImgListRlv.setLayoutManager(gridLayoutManager);
        mImgListRlv.setNestedScrollingEnabled(false);
    }

    @Override
    public void setImgListVisible(boolean visible) {
        if (visible) {
            mImgListRlv.setVisibility(View.VISIBLE);
        } else {
            mImgListRlv.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isImgListVisble() {
        return !(mImgListRlv.getVisibility() == View.GONE || mImgListRlv.getVisibility() == View.INVISIBLE);
    }

    @Override
    public void resetView() {
        super.resetView();
    }
}