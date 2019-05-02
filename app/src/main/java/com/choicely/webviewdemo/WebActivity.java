package com.choicely.webviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * Created by Tommy Eklund on 2019-05-02.
 */
public class WebActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);

        Intent intent = getIntent();
        if (intent == null) {
            showToast("No intent");
            finish();
            return;
        }
        String url = intent.getStringExtra(ChoicelyIntentKeys.URL);
        if (TextUtils.isEmpty(url)) {
            showToast("No URL");
            finish();
            return;
        }

        Fragment f = new ChoicelyWebFragment();
        Bundle data = new Bundle();
        data.putString(ChoicelyIntentKeys.URL, url);
        f.setArguments(data);
        changeFragment(R.id.web_activity_fragment_frame, f);
    }

}
