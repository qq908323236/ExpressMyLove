package fu.mr.expressmylove.fragment;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.view.CustomListView;
import fu.mr.expressmylove.view.CustomViewPager;

import static fu.mr.expressmylove.R.id.listview;
import static fu.mr.expressmylove.R.id.viewpager;


/**
 * A simple {@link Fragment} subclass.
 */
public class MsgFragment extends Fragment {



    private View rootView;


    public MsgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_msg, container, false);
            initUI(rootView);
            initListener();
            initData();
        }


        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }


    private void initUI(View view) {

    }

    private void initListener() {

    }

    private void initData() {

    }

}
