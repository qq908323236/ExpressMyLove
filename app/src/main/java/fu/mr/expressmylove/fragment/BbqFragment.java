package fu.mr.expressmylove.fragment;


import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.adapter.BbqListViewAdapter;
import fu.mr.expressmylove.view.WaterDropListView;

import static fu.mr.expressmylove.R.id.listview;

public class BbqFragment extends Fragment {

    private View rootView;    //缓存Fragment View
    private WaterDropListView[] listViews = new WaterDropListView[3];

    private int firstVisiblePositions[] = new int[3]; // listView第一个可见的item的位置，即在数据集合中的位置position
    private int firstVisiblePositionTops[] = new int[3]; // listView第一可见的item距离父布局的top

    /**
     * Tab标题
     */
    private static final String[] TITLE = new String[] { "最近", "本周", "本月"};
    private TextView tv_recent;
    private TextView tv_week;
    private TextView tv_month;
    private ViewPager viewpager;
    private MyPagerAdapter pgerAdapter;

    public BbqFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_bbq, container, false);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStart() {
        super.onStart();
        // 恢复现在listView的位置，(上一次保存的位置)
        for (int i = 0; i < 3; i++){
            if (listViews[i] != null){
                listViews[i].setSelectionFromTop(firstVisiblePositions[i], firstVisiblePositionTops[i]);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveListViewPositionAndTop();
    }

    /**
     * 保存当前页签listView的第一个可见的位置和top
     */
    private void saveListViewPositionAndTop() {

        for (int i = 0;i < 3;i++){
            if (listViews[i] != null){
                firstVisiblePositions[i] = listViews[i].getFirstVisiblePosition();
                View item = listViews[i].getChildAt(0);
                firstVisiblePositionTops[i] = (item == null) ? 0 : item.getTop();
            }
        }
    }

    private void initUI(View view) {
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        viewpager.setOffscreenPageLimit(2);
        pgerAdapter = new MyPagerAdapter();
        viewpager.setAdapter(pgerAdapter);
        UnderlinePageIndicator indicator = (UnderlinePageIndicator) view.findViewById(R.id.indicator);
        indicator.setFades(false); //设置线是否消失
        indicator.setSelectedColor(Color.parseColor("#FF4081"));
        indicator.setViewPager(viewpager);

        view.findViewById(R.id.fabt_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "点击了，发帖", Toast.LENGTH_SHORT).show();
            }
        });

//        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        tv_recent = (TextView) view.findViewById(R.id.tv_recent);
        tv_week = (TextView) view.findViewById(R.id.tv_week);
        tv_month = (TextView) view.findViewById(R.id.tv_month);
    }

    private void initListener() {
        tv_recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(0);
            }
        });
        tv_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(1);
            }
        });
        tv_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpager.setCurrentItem(2);
            }
        });
    }

    private void initData() {

    }

    public List<String> getData() {

        List<String> data = new ArrayList<String>();
        data.add("To see a world in a grain of sand,");
        data.add("And a heaven in a wild flower,");
        data.add("Hold  infinity in the palm of your hand,");
        data.add("And eternity in an hour.");
        data.add("To see a world in a grain of sand,");
        data.add("And a heaven in a wild flower,");
        data.add("Hold infinity in the palm of your hand,");
        data.add("我喜欢你1.");
        data.add("我喜欢你2.");
        data.add("我喜欢你3.");
        data.add("我喜欢你4.");
        data.add("我喜欢你5.");
        data.add("我喜欢你6.");
        return data;
    }

    private class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return TITLE.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position];
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(getActivity(), R.layout.item_pager_bbq, null);
            listViews[position]= (WaterDropListView) view.findViewById(listview);
            BbqListViewAdapter adapter = new BbqListViewAdapter(getActivity(), getData());
            listViews[position].setAdapter(adapter);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //这个super 要注释掉
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
