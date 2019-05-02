package com.choicely.webviewdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Tommy Eklund on 2019-05-02.
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private static final String EXAMPLE_URL_1 = "https://amazon.com/loveisland";
    private static final String EXAMPLE_URL_2 = "choicely.com";

    private EditText urlEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlEdit = findViewById(R.id.main_url_edit);
    }

    public void onOpenInFragmentClick(View view) {
        String url = getUrl();
        Fragment f = new ChoicelyWebFragment();

        Bundle data = new Bundle();
        data.putString(ChoicelyIntentKeys.URL, url);

        f.setArguments(data);

        changeFragment(R.id.main_fragment_frame, f);
    }

    @Nullable
    private String getUrl() {
        String url = urlEdit.getText().toString();
        if (TextUtils.isEmpty(url)) {
            showToast("Url is empty");
            return null;
        }
        if (!url.startsWith("http")) {
            url = "https://" + url;
        } else if (url.startsWith("http://")) {
            url = url.replace("http://", "https://");
        }

        Log.d(TAG, "url: " + url);
        return url;
    }

    public void onOpenInBrowserClick(View view) {
        String url = getUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = null;
        try {
            uri = Uri.parse(url);
        } catch (Exception e) {
            Log.w(TAG, "Error parsing URL: " + url, e);
            showToast("Error parsing URL");
            return;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
    }

    public void onOpenInActivityClick(View view) {
        String url = getUrl();

        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(ChoicelyIntentKeys.URL, url);

        startActivity(intent);
    }


    public void onClearUrl(View view) {
        urlEdit.setText("");
    }

    public void onExampleClick(View view) {

        switch (view.getId()) {
            case R.id.main_button_example_1:
                urlEdit.setText(EXAMPLE_URL_1);
                break;
            case R.id.main_button_example_2:
                urlEdit.setText(EXAMPLE_URL_2);
                break;
        }

    }
}
