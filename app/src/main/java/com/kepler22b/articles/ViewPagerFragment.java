package com.kepler22b.articles;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.kepler22b.articles.databinding.FragmentFullscreenPageBinding;
import com.kepler22b.articles.model.Article;




import java.util.List;



public class ViewPagerFragment extends DialogFragment {

    private ViewPager mViewPager;
    private TextView mTimeStamp;
    private List<Article> mArticleList;
    private int mCurrentPos;

    public static ViewPagerFragment newInstance() {
        ViewPagerFragment fragment = new ViewPagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make it fullscreen
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fullscreen, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_fullscreen);

        mTimeStamp = (TextView) view.findViewById(R.id.tv_time);


        //get the data passed from the MainActivity.
        mArticleList = (List<Article>) getArguments().getSerializable("list");
        mCurrentPos = getArguments().getInt("position");

        //attach adapter.
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        adapter.setMovieInfoList(mArticleList);
        mViewPager.setAdapter(adapter);

        //attach listener
        mViewPager.addOnPageChangeListener(new ViewPagerListener());

        //show the item that is clicked instead of showing the first item by default.
        setTheCurrentItem(mCurrentPos);

        return view;
    }

    private void setTheCurrentItem(int currentPos) {
        mTimeStamp.setText(mArticleList.get(currentPos).publishedAt);
        mViewPager.setCurrentItem(currentPos);
    }







    //adapter
    private class ViewPagerAdapter extends PagerAdapter {

        private List<Article> mArticleList;
        private LayoutInflater mInflater;


        //DATA BINDING
        private FragmentFullscreenPageBinding mPageBinding;

        void setMovieInfoList(List<Article> movieInfoList) {
            mArticleList = movieInfoList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {



            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            //data binding
            mPageBinding = DataBindingUtil.inflate(mInflater, R.layout.fragment_fullscreen_page, container, false);
            View view = mPageBinding.getRoot();
            mPageBinding.setImgUrl(mArticleList.get(position).urlToImage);
            mPageBinding.setTitle(mArticleList.get(position));
            mPageBinding.setDescription(mArticleList.get(position));

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mArticleList == null ? 0 : mArticleList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //listener
    private class ViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setTheCurrentItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }



}
