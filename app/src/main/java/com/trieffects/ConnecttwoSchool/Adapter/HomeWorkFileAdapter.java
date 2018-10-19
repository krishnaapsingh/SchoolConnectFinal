package com.trieffects.ConnecttwoSchool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;

import com.trieffects.ConnecttwoSchool.R;

/**
 * Created by Trieffects on 24-Nov-17.
 */

public class HomeWorkFileAdapter extends BaseAdapter {
    private Context mContext;

    public HomeWorkFileAdapter(Context c) {
        mContext = c;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.file_view, null);
         //   WebView webView = (WebView) grid.findViewById(R.id.webview);


        } else {
            grid = (View) convertView;
        }

        return grid;
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }
    }
}
