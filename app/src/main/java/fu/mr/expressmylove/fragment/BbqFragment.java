package fu.mr.expressmylove.fragment;


import android.graphics.Color;
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

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

import fu.mr.expressmylove.R;

import static fu.mr.expressmylove.R.id.indicator;

public class BbqFragment extends Fragment {

    private View rootView;    //缓存Fragment View

    /**
     * Tab标题
     */
    private static final String[] TITLE = new String[] { "最近", "本周", "本月"};

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

    private void initUI(View view) {
        ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        viewpager.setAdapter(new MyPagerAdapter());

//        UnderlinePageIndicator indicator = (UnderlinePageIndicator) view.findViewById(R.id.indicator);
//        indicator.setFades(false); //设置线是否消失
//        indicator.setSelectedColor(Color.parseColor("#FF4081"));
//        indicator.setViewPager(viewpager);

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
    }

    private void initListener() {

    }

    private void initData() {

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
            TextView text = (TextView) view.findViewById(R.id.text);
            text.setText("viewpage:" + position);

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
