package com.example.instructorhelpdome;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.instructorhelpdome.JavaBean.HomeIconInfo;
import com.example.instructorhelpdome.adapter.MyGridAdapter;
import com.example.instructorhelpdome.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;


public class HomeFragment extends Fragment implements View.OnClickListener {

    SliderLayout sliderLayout;
    PagerIndicator indicator;
    private List<View> mViews = new ArrayList<>();
    private List<HomeIconInfo> mPagerOneData = new ArrayList<>();
    private List<HomeIconInfo> mPagerTwoData = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        initImageSlider(view);
        initData();
        initGridView(view);
        return view;
    }

    private void initView(View view) {
    }

    /**
     * 初始化首页广告条
     */
    private void initImageSlider(View v) {
        sliderLayout = (SliderLayout) v.findViewById(R.id.slider);
        indicator = (PagerIndicator) v.findViewById(R.id.custom_indicator);
        //准备好要显示的数据
        List<String> imageUrls = new ArrayList<>();
        final List<String> descriptions = new ArrayList<>();
        //传到服务器上
        imageUrls.add("http://pic.96weixin.com/ueditor/20190412/1555053050158909.jpg");
        imageUrls.add("http://pic.96weixin.com/ueditor/20190412/1555053050428485.jpg");
        imageUrls.add("http://pic.96weixin.com/ueditor/20190412/1555053050723139.jpg");
        descriptions.add("两学一做");
        descriptions.add("十九大精神");
        descriptions.add("思想政治工作");


        for (int i = 0; i < imageUrls.size(); i++) {
            //新建三个展示View，并且添加到SliderLayout
            TextSliderView tsv = new TextSliderView(getActivity());
            tsv.image(imageUrls.get(i)).description(descriptions.get(i));
            final int finalI = i;
            tsv.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Toast.makeText(getActivity(), descriptions.get(finalI), Toast.LENGTH_SHORT).show();
                    if (finalI==0){
                        Intent intent=new Intent(getActivity(),WebviewActivity.class);
                        Bundle B=new Bundle();
                        B.putString("name","两学一做");
                        B.putString("url","http://www.bhu.edu.cn/newsgl/page/lxyz/");
                        intent.putExtra("data",B);
                        startActivity(intent);

                    }else if (finalI==2){
                        Intent intent=new Intent(getActivity(),WebviewActivity.class);
                        Bundle B=new Bundle();
                        B.putString("name","思想政治工作");
                        B.putString("url","http://www.bhu.edu.cn/newsgl/page/szgz/");
                        intent.putExtra("data",B);
                        startActivity(intent);

                    }else if (finalI==1) {
                        Intent intent=new Intent(getActivity(),WebviewActivity.class);
                        Bundle B=new Bundle();
                        B.putString("name","十九大精神");
                        B.putString("url","http://www.bhu.edu.cn/newsgl/page/19d/");
                        intent.putExtra("data",B);
                        startActivity(intent);

                    }

                }
            });
            sliderLayout.addSlider(tsv);
        }
        //对SliderLayout进行一些自定义的配置
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setDuration(3000);
        sliderLayout.setCustomIndicator(indicator);
    }

    private void initData() {
        String[] iconName = getResources().getStringArray(R.array.home_bar_labels);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.home_bar_icon);

        for (int i = 0; i < iconName.length; i++) {
            if (i < 8) {
                mPagerOneData.add(new HomeIconInfo(iconName[i], typedArray.getResourceId(i, 0)));
            } else {
                mPagerTwoData.add(new HomeIconInfo(iconName[i], typedArray.getResourceId(i, 0)));
            }
        }
    }

    private void initGridView( View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        View pagerOne = getActivity().getLayoutInflater().inflate(R.layout.home_gridview, null);
        GridView gridView01 = (GridView) pagerOne.findViewById(R.id.gridView);
        gridView01.setAdapter(new MyGridAdapter(mPagerOneData, this.getActivity()));
        gridView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","数字渤大");
                    B.putString("url","http://cas.bhu.edu.cn/cas/login?service=http://i.bhu.edu.cn:80/dcp/index.jsp");
                    intent.putExtra("data",B);
                    startActivity(intent);
                }
                if(i==1&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","执行测评");
                    B.putString("url","http://210.47.176.33:8080/Default.aspx?url=s_App8.aspx");
                    intent.putExtra("data",B);
                    startActivity(intent);
                }
                if(i==2&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","综合教务系统");
                    B.putString("url","http://jw.bhu.edu.cn/");
                    intent.putExtra("data",B);
                    startActivity(intent);
                }
                if(i==3&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","公交时刻");
                    B.putString("url","https://mp.weixin.qq.com/s/M57gThPoBixlHqCFOqrd2g");
                    intent.putExtra("data",B);
                    startActivity(intent);
                }
                if(i==4&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","校历");
                    B.putString("url","https://weixiao.qq.com/weixin_project/calendar/schoolCalendar.html?weixinOpenInfo=gh_edfb3f596431");
                    intent.putExtra("data",B);
                    startActivity(intent);
                }
                if(i==5&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","在校证明");
                    B.putString("url","http://210.47.176.33:8080/Default.aspx?url=s_App7.aspx&nsukey=0t5FPq0C4ZcXLfKAlr3WyZ1qaKLbnSUGTrn64jUQVO0b8WPB1gqS8saWGQKiW66y1bvwnoWeSYnZuP8EsUEYqu4a9Za0QDicer72mY6qtY%2Fed3e0nQLIspoxF4X1CDn1BtxmiAhDoEET7O%2BPr7%2Fher9DMcdkhIzxTAIIqM%2BQbiy301NeB%2BKnWVxs%2BnMabpSh3uCmzIcH9mOxBjVEfUl%2B8A%3D%3D");
                    intent.putExtra("data",B);
                    startActivity(intent);
                }
                if(i==6&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","空教室查询");
                    B.putString("url","http://210.47.176.3/page/depart/bdlab/zxscx/index.asp?nsukey=WuYMrfUaRBDgGFs8jRN/tGxrHzwWHxSOIFTGAOgV4wMZSsyDgyiF4kuzqbNf7uTlBf21xSxwzLMJRtCxBXNYT9rU8v3hQT81oPeD+IHJ+hvk2PBaNEBElKQWfLLkzuSpc1Sm3MY4ZXETOeGGYnY2pEbkhwJsAtHu1IXobnsHgsxiG0nyZXl6L5EL/3svB0zmEYf61lkOFbehHr7w0LcxaQ==");
                    intent.putExtra("data",B);
                    startActivity(intent);
                }
                if(i==7&&(int)l==0) {

                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","后勤报修");
                    B.putString("url","http://www.bhu.edu.cn/newsgl/depart/hq/jlhd.asp");
                    intent.putExtra("data",B);
                    startActivity(intent);
                }

            }
        });

        View pagerTwo =getActivity().getLayoutInflater().inflate(R.layout.home_gridview, null);
        GridView gridView02 = (GridView) pagerTwo.findViewById(R.id.gridView);
        gridView02.setAdapter(new MyGridAdapter(mPagerTwoData,this.getActivity()));
        gridView02.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","图书查询");
                    B.putString("url","http://agentdockingopac.featurelib.libsou.com/showhome/search/showSearch?schoolId=2598");
                    intent.putExtra("data",B);
                    startActivity(intent);
                }
                if(i==1&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","网费（滨海）");
                    B.putString("url","http://sf1.bhu.edu.cn:8800/");
                    intent.putExtra("data",B);
                    startActivity(intent);
                }
                if(i==2&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","网费（松山）");
                    B.putString("url","http://sf.bhu.edu.cn/");
                    intent.putExtra("data",B);
                    startActivity(intent);
                }
                if(i==3&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","自助缴费");
                    B.putString("url","http://jf.bhu.edu.cn/zzjf/index/index_index.html");
                    intent.putExtra("data",B);
                    startActivity(intent);

                }
                if(i==4&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","渤大图书馆");
                    B.putString("url","http://210.47.177.84/reader/redr_verify.php");
                    intent.putExtra("data",B);
                    startActivity(intent);

                }
                if(i==5&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","渤海说吧");
                    B.putString("url","https://boda360.kuaizhan.com/33/6/p24984441932969");
                    intent.putExtra("data",B);
                    startActivity(intent);

                }
                if(i==6&&(int)l==0) {
                    Intent intent=new Intent(getActivity(),WebviewActivity.class);
                    Bundle B=new Bundle();
                    B.putString("name","高考录取通知");
                    B.putString("url","http://notice.woai662.net/studentTzs/appLogic.php?type=trigger&media_id=gh_edfb3f596431");
                    intent.putExtra("data",B);
                    startActivity(intent);

                }
                if(i==7&&(int)l==0) {

                }
            }
        });

        mViews.add(pagerOne);
        mViews.add(pagerTwo);
        viewPager.setAdapter(new MyPagerAdapter(mViews));


    }


    @Override
    public void onClick(View view) {

    }
}
