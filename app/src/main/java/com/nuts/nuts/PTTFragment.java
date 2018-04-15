package com.nuts.nuts;
/* Created by petingo on 2018/3/17. */


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class PTTFragment extends Fragment {
    private Context context;
    private WebView webView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_ptt, container, false);
        this.context = container.getContext();
        return view;
    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        webView = (WebView) view.findViewById(R.id.webViewPtt);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.ptt.cc/bbs/NTU/index.html");

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP ) {
                    Log.e("back","pressed");
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }
                    return true;
                }
                return false;
            }
        });

    }

}
