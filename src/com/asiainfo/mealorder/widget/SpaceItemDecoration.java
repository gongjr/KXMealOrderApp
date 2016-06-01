package com.asiainfo.mealorder.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/1 上午9:28
 *
 * recyclerview item之间的间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildPosition(view) % 2 == 0 || parent.getChildPosition(view) != 0) {
            outRect.left = space;
        }
        if (parent.getChildPosition(view) != 0 || parent.getChildPosition(view) != 1) {
            outRect.top = space;
        }
    }
}
