package com.coolwallpaper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coolwallpaper.R;
import com.coolwallpaper.bean.BaseRequestParam;
import com.coolwallpaper.bean.PictureBean;
import com.coolwallpaper.bean.WallPaperRequetParam;
import com.coolwallpaper.constant.TestURL;
import com.coolwallpaper.utils.ImageUtil;
import com.coolwallpaper.utils.PictureParseUtil;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

/**
 * 最底层的单个Fragement，只显示图片列表
 * Created by fuchao on 2015/11/18.
 */
public class ShowPictureListFragment extends BaseFragment {

    private String TAG = "[ShowPictureListFragment]";
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private PictureGridAdapter adapter;
    private BaseRequestParam requetParam;
    private ProgressDialog progressDialog;
    private int currentPage;
    private List<PictureBean> beanList;
    private String tag;
    private String tag3;
    private OkHttpClient okHttpClient;//网络访问采用okhttp
    private HttpUtils httpUtils;
    private

    @ViewInject(R.id.gv_pic)
    PullToRefreshGridView gridView;

    /**
     * 创建实例方法
     *
     * @param tag  二级标题,例如:风景
     * @param tag3 三级标题,例如:自然风光
     * @return
     */
    public static ShowPictureListFragment newInstance(String tag, String tag3) {
        ShowPictureListFragment fragment = new ShowPictureListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TAG", tag);
        bundle.putString("TAG3", tag3);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pic_grid, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        this.tag = bundle.getString("TAG");
        this.tag3 = bundle.getString("TAG3");
        //初始化
        this.init();
        //添加监听器
        this.addListener();
        //        if(beanList == null || beanList.size() == 0){
        //            //查询数据
        //            this.queryPicture();
        //        }
        //测试代码
        this.gridView.setAdapter(new TestAdapter());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    //初始化
    private void init() {
        this.imageLoader.init(ImageUtil.getInstance().getImageLoaderConfiguration());
        this.options = ImageUtil.getInstance().getDisplayImageOptions();
        this.httpUtils = new HttpUtils();
        this.okHttpClient = new OkHttpClient();
        this.requetParam = new WallPaperRequetParam();
        this.requetParam.setTag(tag);
        this.requetParam.setTag3(tag3);
        //this.gridView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        this.gridView.setMode(PullToRefreshBase.Mode.DISABLED);
        ILoadingLayout loadLayout = gridView.getLoadingLayoutProxy();
        loadLayout.setPullLabel("上拉加载");
        loadLayout.setRefreshingLabel("正在加载...");
        loadLayout.setReleaseLabel("释放加载");
    }

    //从数据库中查询图片
    private void queryDB() {
        //使用GreenDAO来查询数据库
        //DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), ConstDB.DB_PICTURE, null);

    }

    //查询图片
    private void queryPicture() {
        //        final Request request = new Request.Builder().url(requetParam.getUrl()).build();
        //        Calsl call = okHttpClient.newCall(request);
        //        call.enqueue(new Callback() {
        //            @Override
        //            public void onFailure(Request request, IOException e) {
        //
        //            }
        //
        //            @Override
        //            public void onResponse(Response response) throws IOException {
        //                String jsonStr = response.body().string();
        //                //解析数据
        //                beanList = PictureParseUtil.parse(jsonStr);
        //                //更新ui
        //                getActivity().runOnUiThread(new Runnable() {
        //                    @Override
        //                    public void run() {
        //                        showPicture(beanList);
        //                        //停止刷新
        //                        gridView.onRefreshComplete();
        //                    }
        //                });
        //            }
        //        });

        this.httpUtils.send(HttpRequest.HttpMethod.GET, requetParam.getUrl(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String jsonStr = responseInfo.result;
                //解析数据
                beanList = PictureParseUtil.parse(jsonStr);
                showPicture(beanList);
                //关闭对话框
                //if (progressDialog != null && progressDialog.isShowing()) {
                //    progressDialog.dismiss();
                //}
                //停止刷新
                gridView.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }

            @Override
            public void onStart() {
                //显示进度条
                //progressDialog = ProgressDialog.show(ShowPictureListActivity.this, "正在加载", "请等待....");
                //progressDialog.show();
            }
        });
    }

    //添加监听器
    private void addListener() {
        //this.gridView.setOnScrollListener(new AutoLoadListener());
        //        //添加刷新监听
        //        this.gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
        //            @Override
        //            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
        //                requetParam.setPn(++currentPage);
        //                queryPicture();
        //            }
        //        });
        //        //添加item监听
        //        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //                // 跳转到图片详情
        //                PictureBean tmpBean = adapter.getBeanList().get(position);
        //                ShowPictureDetailActivity.startActivity(getActivity(), tmpBean, beanList);
        //            }
        //        });
    }

    //显示图片
    private void showPicture(List<PictureBean> beanList) {
        //若为空则创建adaper
        if (adapter == null) {
            adapter = new PictureGridAdapter(getActivity(), beanList);
            gridView.setAdapter(adapter);
        }
        //已经创建了adapter
        else {
            //添加数据
            adapter.getBeanList().addAll(beanList);
            adapter.notifyDataSetChanged();
        }
    }

    //适配器
    private class PictureGridAdapter extends BaseAdapter {

        private List<PictureBean> beanList;
        private Context context;

        //构造函数
        public PictureGridAdapter(Context context, List<PictureBean> beanList) {
            this.context = context;
            this.beanList = beanList;
        }

        @Override
        public int getCount() {
            if (beanList == null) {
                return 0;
            }
            return beanList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.picture_item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            holder = (ViewHolder) view.getTag();
            PictureBean bean = beanList.get(position);
            //绑定数据
            imageLoader.displayImage(bean.getSmallImageUrl(), holder.ivPic, options);
            holder.tvDesc.setText(bean.getDesc());
            return view;
        }

        class ViewHolder implements ImageLoadingListener {

            public ImageView ivPic;
            public TextView tvDesc;
            public ProgressBar progressBar;

            public ViewHolder(View view) {
                ivPic = (ImageView) view.findViewById(R.id.iv_pic);
                tvDesc = (TextView) view.findViewById(R.id.tv_desc);
                progressBar = (ProgressBar) view.findViewById(R.id.progress);
                //默认隐藏ProgressBar
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingStarted(String s, View view) {
                this.ivPic.setImageDrawable(null);
                this.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                this.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }

        }

        public void setBeanList(List<PictureBean> beanList) {
            this.beanList = beanList;
        }

        public List<PictureBean> getBeanList() {
            return beanList;
        }
    }

    //测试用适配器
    private class TestAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return TestURL.urls.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.picture_item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            holder = (ViewHolder) view.getTag();
            //加载图片
            String url = TestURL.urls[position];
            imageLoader.displayImage(url, holder.ivPic, options);
            return view;
        }

        private class ViewHolder {

            public ImageView ivPic;
            public TextView tvDesc;
            public ProgressBar progressBar;

            public ViewHolder(View view) {
                ivPic = (ImageView) view.findViewById(R.id.iv_pic);
                tvDesc = (TextView) view.findViewById(R.id.tv_desc);
                progressBar = (ProgressBar) view.findViewById(R.id.progress);
                //默认隐藏ProgressBar
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
