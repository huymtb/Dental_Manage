package com.prostage.dental_manage.base;

import android.app.Dialog;
import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.prostage.dental_manage.R;
import com.prostage.dental_manage.utils.OnSingleClickListener;
import com.prostage.dental_manage.utils.Utils;

/**
 * Created by congnc on 5/18/17.
 */

public class QuestionDialog extends Dialog {
    private TextView titleTextView;
    private TextView denyTextView;
    private TextView allowTextView;
    private QuestionRequestDialogDelegate delegate;
    private String tag;
    private OnSingleClickListener mySingleListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View view) {
            switch (view.getId()) {
                case R.id.deny_txt: {
                    dismiss();
                    if (delegate != null) {
                        delegate.clickAllow(false, tag);
                    }
                    break;
                }
                case R.id.allow_txt: {
                    dismiss();
                    if (delegate != null) {
                        delegate.clickAllow(true, tag);
                    }
                }
            }
        }
    };

    public QuestionDialog(Context context, String title, Spanned alert) {
        super(context);
        showInfoDialog(title, alert);
    }


    public QuestionDialog(Context context, String title, String alert) {
        super(context);
        showInfoDialog(title, Utils.fromHtml(alert));
    }

    private void showInfoDialog(String title, Spanned alert) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_question_request);

        titleTextView = (TextView) findViewById(R.id.title_txt);
        denyTextView = (TextView) findViewById(R.id.deny_txt);
        TextView questionTextView = (TextView) findViewById(R.id.question_txt);
        allowTextView = (TextView) findViewById(R.id.allow_txt);

        if (TextUtils.isEmpty(title)) {
            titleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }
        questionTextView.setText(alert);

        denyTextView.setOnClickListener(mySingleListener);
        allowTextView.setOnClickListener(mySingleListener);
    }

    public void setColorTitle(int color) {
        allowTextView.setTextColor(color);
        titleTextView.setTextColor(color);
    }

    public void setTitleButtonOK(String text) {
        allowTextView.setText(text.toUpperCase());
    }

    public void setTitleButtonCancel(String text) {
        denyTextView.setText(text.toUpperCase());
    }

    public void setDelegate(QuestionRequestDialogDelegate fragment, String tag) {
        delegate = fragment;
        this.tag = tag;
    }

    public interface QuestionRequestDialogDelegate {
        void clickAllow(Boolean bAllow, String type);
    }
}
