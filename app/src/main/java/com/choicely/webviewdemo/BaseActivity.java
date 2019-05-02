package com.choicely.webviewdemo;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Tommy on 2019-05-02.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    @Nullable
    protected Fragment currentFragment;

    public void changeFragment(int resID, Fragment frag) {
        changeFragment(resID, frag, false);
    }

    public void changeFragment(int resID, Fragment frag, boolean addToBackStack) {
        changeFragment(resID, frag, null, null, addToBackStack);
    }

    public void changeFragment(int resID, Fragment frag, Integer enterAnimation, Integer exitAnimation) {
        changeFragment(resID, frag, enterAnimation, exitAnimation, false);
    }

    public void changeFragment(int resID, Fragment frag, Integer enterAnimation, Integer exitAnimation, boolean addToBackStack) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (enterAnimation != null || exitAnimation != null) {
                ft.setCustomAnimations(enterAnimation, exitAnimation);
            }
            if (frag != null) {
                ft.replace(resID, frag);
                ft.detach(frag);
                ft.attach(frag);
                if (addToBackStack) {
                    ft.addToBackStack(frag.getTag());
                }
            }
            ft.commit();
            currentFragment = frag;
        } catch (IllegalStateException e) {
            Log.w(TAG, "Could not perform fragment change", e);
        }
    }

    protected void showToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}
