package com.prostage.dental_manage.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prostage.dental_manage.R;

import static com.prostage.dental_manage.R.id.tv_content;

public class TextViewEdit extends RelativeLayout implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView ivEdit;
    private TextView tvContent;
    private ExtendedEditText etContent;
    private ImageView ivCheck;
    private ImageView ivDelete;
    private String textContent;
    TextViewEditListener listener;
    int tag;

    public TextViewEdit(Context context) {
        super(context);
        init(context);
    }

    public TextViewEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.view_textview_edit, this);
        tvTitle = rootView.findViewById(R.id.tv_title);
        ivEdit = rootView.findViewById(R.id.iv_edit);
        tvContent = rootView.findViewById(tv_content);
        ivCheck = rootView.findViewById(R.id.iv_check);
        ivDelete = rootView.findViewById(R.id.iv_delete);
        etContent = rootView.findViewById(R.id.et_content);
        ivEdit.setOnClickListener(this);
        ivCheck.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        etContent.addTextChangedListener(new GenericTextWatcher());
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setContent(String content) {
        tvContent.setText(content);
        textContent = content;
        etContent.setText(content);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit:
                ivCheck.setVisibility(VISIBLE);
                ivDelete.setVisibility(VISIBLE);
                ivEdit.setVisibility(GONE);
                tvContent.setVisibility(GONE);
                etContent.setVisibility(VISIBLE);
                break;
            case R.id.iv_check:
                ivCheck.setVisibility(GONE);
                ivDelete.setVisibility(GONE);
                ivEdit.setVisibility(VISIBLE);
                tvContent.setVisibility(VISIBLE);
                etContent.setVisibility(GONE);
                textContent = etContent.getText().toString();
                setContent(textContent);
                break;
            case R.id.iv_delete:
                ivCheck.setVisibility(GONE);
                ivDelete.setVisibility(GONE);
                ivEdit.setVisibility(VISIBLE);
                tvContent.setVisibility(VISIBLE);
                etContent.setVisibility(GONE);
                textContent = tvContent.getText().toString();
                etContent.setText(textContent);
                break;
        }
    }

    private class GenericTextWatcher implements TextWatcher {

        private GenericTextWatcher() {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (listener != null) {
                if (TextUtils.isEmpty(s)) {
                    listener.onTextViewEditChanged(tag, "");
                } else {
                    listener.onTextViewEditChanged(tag, s.toString());
                }
            }
        }
    }

    public void setListener(int tag, TextViewEditListener listener) {
        this.tag = tag;
        this.listener = listener;
    }

    public interface TextViewEditListener {
        void onTextViewEditChanged(int tag, String text);
    }
}
