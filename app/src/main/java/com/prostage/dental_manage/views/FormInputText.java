package com.prostage.dental_manage.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prostage.dental_manage.R;
import com.prostage.dental_manage.utils.Utils;

/**
 * Created by Linh on 3/29/2017.
 */

public class FormInputText extends RelativeLayout implements View.OnFocusChangeListener {
    View rootView;
    View lineView;
    ExtendedEditText editText;
    TextView titleInputText;
    TextView issueText;

    String tag;
    int colorFocus;
    int colorLine;

    private OnFormInputTextListener listener;

    public FormInputText(Context context) {
        super(context);
        init(context);
    }

    public FormInputText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FormInputText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_form_input_text, this);
        lineView = rootView.findViewById(R.id.line_maintabbar_view);
        lineView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lineView));
        editText = (ExtendedEditText) rootView.findViewById(R.id.edit_text);
        titleInputText = (TextView) rootView.findViewById(R.id.tv_title_input);
        issueText = (TextView) rootView.findViewById(R.id.issue_txt);

        colorFocus = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        colorLine = ContextCompat.getColor(getContext(), R.color.lineView);
        editText.setOnFocusChangeListener(this);
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (listener != null) {
                        listener.onReturn(tag);
                    }
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (listener != null) {
                    if (TextUtils.isEmpty(s)) {
                        listener.onValueChange(tag, "");
                    } else {
                        listener.onValueChange(tag, s.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setTitleInputText(String text) {
        titleInputText.setVisibility(VISIBLE);
        titleInputText.setText(text);
    }

    public void setInputType(Boolean bSecure) {
        if (bSecure) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTransformationMethod(null);
        }
    }

    public void setIssue(String issue) {
        if (!TextUtils.isEmpty(issue)) {

        }
    }

    public void setFocus() {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void setListener(OnFormInputTextListener listener, String tag) {
        this.listener = listener;
        this.tag = tag;
    }

    public interface OnFormInputTextListener {
        void onValueChange(String tag, String text);

        void onReturn(String tag);

        void onTextFocused(Boolean b, String tag);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view.getId() == R.id.edit_text) {
            if (b) {
                lineView.setBackgroundColor(colorFocus);
                titleInputText.setTextColor(colorFocus);
            } else {
                lineView.setBackgroundColor(colorLine);
                titleInputText.setTextColor(Utils.getColor(getContext(), R.color.medium_dark_gray));
            }
            if (listener != null) {
                listener.onTextFocused(b, tag);
            }
        }
    }
}
