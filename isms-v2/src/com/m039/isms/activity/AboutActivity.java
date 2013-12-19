/** AboutActivity.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.activity;

import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.m039.mqst.R;

/**
 *
 *
 * Created: 12/20/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class AboutActivity extends BaseActivity {

    WebView mWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWeb = new WebView(this);
        mWeb.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if(url.startsWith("mailto:")) {
                        MailTo mt = MailTo.parse(url);
                        Intent i = newEmailIntent(AboutActivity.this,
                                                  mt.getTo(),
                                                  mt.getSubject(),
                                                  mt.getBody(),
                                                  mt.getCc());
                        startActivity(i);
                        view.reload();

                    } else if(url.startsWith("http://")) {

                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    } else {

                        view.loadUrl(url);
                    }

                    return true;
                }

            });
        mWeb.loadData(getString(R.string.a_about__html), null, "UTF8");

        setContentView(mWeb);
    }

    public static Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.setType("message/rfc822");
        return intent;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mWeb != null) {
            mWeb.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mWeb != null) {
            mWeb.onPause();
        }
    }

} // AboutActivity
