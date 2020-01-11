package org.workholic.widget;

import android.content.Context;
import android.widget.ScrollView;

public class CustomeScrollviewLayout extends ScrollView {

    public CustomeScrollviewLayout(Context context){
        super(context);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return super.canScrollHorizontally(direction);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return super.canScrollVertically(direction);
    }
}
