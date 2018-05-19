package com.cuit.monitorapp.main.cuit.monitorapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cuit.monitorapp.R;
import com.cuit.monitorapp.main.cuit.monitorapp.model.Trace;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TraceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<Trace> traceList = new ArrayList<>(1);

    public TraceListAdapter(Context context, List<Trace> traceList) {
        inflater = LayoutInflater.from(context);
        this.traceList = traceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_trace, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        // Trace t = traceList.get(position);
        itemHolder.tvTopLine.setVisibility(View.VISIBLE);
        // 第一行头的竖线不显示
        // itemHolder.tvTopLine.setVisibility(View.INVISIBLE);
        // 字体颜色加深
        itemHolder.tvAcceptTime.setTextColor(0xff555555);
        itemHolder.tvAcceptContent.setTextColor(0xff555555);
        // itemHolder.tvAcceptPic.setImageURI(Uri.parse(trace.getAcceptPic()));
        // itemHolder.tvAcceptPic.setImageResource(t.getAcceptPic());
        itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);

        itemHolder.bindHolder(traceList.get(position));
    }

    @Override
    public int getItemCount() {
        return traceList.size();
    }

    // @Override
    // public int getItemViewType(int position) {
    //     if (position == 0) {
    //         return TYPE_TOP;
    //     }
    //     return TYPE_NORMAL;
    // }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAcceptTime, tvAcceptContent;
        private TextView tvTopLine, tvDot;
        private ImageView tvAcceptPic;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAcceptTime = (TextView) itemView.findViewById(R.id.tvAcceptTime);
            tvAcceptContent = (TextView) itemView.findViewById(R.id.tvAcceptContent);
            tvAcceptPic = (ImageView) itemView.findViewById(R.id.ivPic);
            tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
            tvDot = (TextView) itemView.findViewById(R.id.tvDot);
        }

        public void bindHolder(final Trace trace) {
            tvAcceptTime.setText(trace.getAcceptTime());
            tvAcceptContent.setText(trace.getAcceptContent());
            // System.out.println(Uri.parse(trace.getAcceptPic()));
            // tvAcceptPic.setImageURI(Uri.parse("http://t11.baidu.com/it/u=3644064505,2332498428&fm=173&s=E0C2B341DAE7B76E1ED1A507000030C2&w=640&h=674&img.JPEG"));
            // tvAcceptPic.setImageResource(R.drawable.aaa);
            //得到可用的图片
            // Bitmap bitmap = getHttpBitmap("http://lc-fQeWhHtq.cn-n1.lcfile.com/AiIy2jdFHboFGrfh4q7LMObhJKaZhS6opepCw1pK");
            // tvAcceptPic.setImageBitmap(bitmap);
            // Drawable drawable = loadImageFromNetwork("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526721788894&di=ea3ecdd8e536e9c84cf9d55b6c6707ab&imgtype=0&src=http%3A%2F%2Fp8.qhimg.com%2Ft015a0da40a3150c7bb.jpg");
            // tvAcceptPic.setImageDrawable(drawable) ;

            // new Thread(new Runnable(){
            //     // Drawable drawable = loadImageFromNetwork("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526721788894&di=ea3ecdd8e536e9c84cf9d55b6c6707ab&imgtype=0&src=http%3A%2F%2Fp8.qhimg.com%2Ft015a0da40a3150c7bb.jpg");
            //     Drawable drawable = loadImageFromNetwork(trace.getAcceptPic());
            //     @Override
            //     public void run() {
            //         tvAcceptPic.post(new Runnable(){
            //             @Override
            //             public void run() {
            //                 tvAcceptPic.setImageDrawable(drawable) ;
            //             }}) ;
            //     }
            //
            // }).start()  ;

            // new DownloadImageTask().execute("http://lc-fQeWhHtq.cn-n1.lcfile.com/AiIy2jdFHboFGrfh4q7LMObhJKaZhS6opepCw1pK") ;
            String url = trace.getAcceptPic();
            if (url != null) {
                new DownloadImageTask().execute(url) ;
            }

        }

        private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {

            protected Drawable doInBackground(String... urls) {
                return loadImageFromNetwork(urls[0]);
            }

            protected void onPostExecute(Drawable result) {
                tvAcceptPic.setImageDrawable(result);
            }
        }

        private Drawable loadImageFromNetwork(String imageUrl) {
            Drawable drawable = null;
            try {
                // 通过文件名来判断，是否本地有此图片
                drawable = Drawable.createFromStream(
                        new URL(imageUrl).openStream(), "image.jpg");
            } catch (IOException e) {
                Log.d("test", e.getMessage());
            }
            if (drawable == null) {
                Log.d("test", "null drawable");
            } else {
                Log.d("test", "not null drawable");
            }

            return drawable ;
        }
    }
}