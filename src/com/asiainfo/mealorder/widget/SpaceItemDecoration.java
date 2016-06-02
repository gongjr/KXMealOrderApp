package com.asiainfo.mealorder.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/1 上午9:28
 * <p/>
 * recyclerview item之间的间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildPosition(view);
        int i = position % 2;
        if (position == 1) {
            outRect.left = space;
        } else if (i != 0 & position != 0) {
            outRect.right = space;
        }
        outRect.top = space;

    }
}
