package com.choicely.webviewdemo;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Created by Tommy on 2019-05-02.
 */
public class ChoicelyWebFragment extends Fragment {

    private static final String TAG = "ChoicelyWebFragment";
    private static final String ENCODING = "UTF-8";
    private static final String MIME_TYPE = "text/html";
    private static final String ASSETS_DIRECTORY = "file:///android_asset/";
    public static final String SHOW_LOADING_INDICATOR = "intent_show_loading_indicator";
    private static final String EMPTY_PAGE = "<!DOCTYPE html><html lang=\"en\"><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"><title>Choicely Video</title></head><body></body></html>";

    private View layout;
    private WebView webView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.web_fragment, container, false);

        webView = layout.findViewById(R.id.web_activity_webview);
        progressBar = layout.findViewById(R.id.web_activity_progressbar);

        webView.setWebChromeClient(new WebChromeClient() {
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        //noinspection deprecation
        webView.getSettings().setPluginState(PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        Bundle bundle = getArguments();
        if (bundle == null) {
            Log.w(TAG, "No intent");
            finish();
            return layout;
        }
        progressBar.setVisibility(bundle.getBoolean(SHOW_LOADING_INDICATOR, false) ? View.VISIBLE : View.GONE);

        String url = bundle.getString(ChoicelyIntentKeys.URL);
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        } else {
            String data = bundle.getString(ChoicelyIntentKeys.WEB_EMBED);
            if (!TextUtils.isEmpty(data)) {
//                data = replaceWidth(data);
//                data = replaceHeight(data);
                if (!data.contains("<html")) {
                    data = EMPTY_PAGE.replace("<body></body>", "<body>" + data + "</body>");
                }
                Log.d(TAG, String.format("modifiedData:\n%s", data));

                webView.loadDataWithBaseURL(ASSETS_DIRECTORY, data, MIME_TYPE, ENCODING, null);
            } else {
                Log.w(TAG, "No Url or Data");
                finish();
            }
        }

        updateContent();
        return layout;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

//    private String replaceWidth(String data) {
//        String replacement = "width=\"" + (ChoicelyStaticUtils.getWindowWidth(this) - getResources().getDimensionPixelSize(R.dimen.margin_quad)) / getResources().getDisplayMetrics().density + "px\"";
////        String replacement = "width=\"100%\"";
//        String regex = "width=\"[0-9]*(%|px)?\"";
//        return data.replaceAll(regex, replacement);
//    }
//
//    private String replaceHeight(String data) {
//        String replacement = "height=\"" + (ChoicelyStaticUtils.getWindowHeight(this) - 2 * getResources().getDimensionPixelSize(R.dimen.margin_quad)) / getResources().getDisplayMetrics().density + "px\"";
////        String replacement = "height=\"100%\"";
//        String regex = "height=\"[0-9]*(%|px)?\"";
//        return data.replaceAll(regex, replacement);
//    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.loadUrl("about:blank");
        }

        super.onDestroy();
    }

    protected void updateContent(@NonNull Activity act, @NonNull View v, @Nullable Bundle data) {
    }


    protected final void updateContent() {
        Activity act = getActivity();
        Bundle b = getArguments();
        if (act == null || layout == null) {
            return;
        }

        updateContent(act, layout, b);
    }

    protected void finish() {
        Activity act = getActivity();
        if (act != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                act.finishAfterTransition();
            } else {
                act.finish();
            }
        }
    }
}
