package com.prostage.dental_manage.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prostage.dental_manage.DentalManageApp;
import com.prostage.dental_manage.core.model.AdminModel;
import com.prostage.dental_manage.core.IHttpResponse;
import com.prostage.dental_manage.core.NetworkManager;
import com.prostage.dental_manage.base.MainActivity;
import com.prostage.dental_manage.R;
import com.prostage.dental_manage.base.BaseFragment;
import com.prostage.dental_manage.core.model.AccessModel;
import com.prostage.dental_manage.utils.AppConstants;
import com.prostage.dental_manage.utils.Utils;
import com.prostage.dental_manage.views.ExtendedEditText;
import com.prostage.dental_manage.views.TitleBar;

import static com.prostage.dental_manage.utils.AppConstants.ID_ADMIN;
import static com.prostage.dental_manage.utils.Utils.PREF_ADMIN_DATA;
import static com.prostage.dental_manage.utils.Utils.PREF_ADMIN_ID;
import static com.prostage.dental_manage.utils.Utils.PREF_PASSWORD;
import static com.prostage.dental_manage.utils.Utils.PREF_REMEMBER;
import static com.prostage.dental_manage.utils.Utils.PREF_USER_NAME;

public class AuthenticationFragment extends BaseFragment implements View.OnClickListener,
        IHttpResponse {
    public static final String TAG = AuthenticationFragment.class.getSimpleName();
    public static final String ARG_USER = "arg_user";
    private TitleBar titleBar;
    private ExtendedEditText tvAccount;
    private ExtendedEditText tvPassword;
    private CheckBox cbRemember;
    private Button btLogin;
    private ImageView ivBackground;
    NetworkManager networkManager;
    Animation shakeAnimation;

    private String account = "";
    private String password = "";

    public static AuthenticationFragment newInstance() {
        Bundle args = new Bundle();
        AuthenticationFragment fragment = new AuthenticationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkManager = new NetworkManager(getActivity(), this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authentication, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        titleBar = view.findViewById(R.id.title_bar);
        tvAccount = view.findViewById(R.id.tv_account);
        tvPassword = view.findViewById(R.id.tv_password);
        cbRemember = view.findViewById(R.id.cb_remember);
        ivBackground = view.findViewById(R.id.iv_background);
        btLogin = view.findViewById(R.id.bt_login);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleBar.setTitle(getString(R.string.title_login));
        titleBar.setSubtitle(getString(R.string.subtitle_login));
        tvAccount.setHint(getString(R.string.account));
        tvAccount.addTextChangedListener(new GenericTextWatcher(tvAccount));

        Utils.setImageById(getActivity(), ivBackground, R.drawable.waitingroom);
        tvPassword.setHint(getString(R.string.password));
        tvPassword.addTextChangedListener(new GenericTextWatcher(tvPassword));

        btLogin.setOnClickListener(this);

        if (Utils.getInt(getActivity(), PREF_REMEMBER) == 1) {
            cbRemember.setChecked(true);
            account = Utils.getString(getActivity(), PREF_USER_NAME);
            password = Utils.getString(getActivity(), PREF_PASSWORD);
            networkManager.requestApi(networkManager.login(account, password),
                    AppConstants.ID_LOGIN);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                Utils.hideSoftKeyboard(getActivity());
                if (validate()) {
                    networkManager.requestApi(networkManager.login(account, password),
                            AppConstants.ID_LOGIN);
                }
                break;
        }
    }

    @Override
    public void onHttpComplete(String response, int idRequest) {
        Gson gson = new Gson();
        switch (idRequest) {
            case AppConstants.ID_LOGIN:
                AccessModel accessModel = gson.fromJson(response, AccessModel.class);
                if (accessModel != null) {
                    loadAdminInfo(accessModel);
                }
                break;
            case AppConstants.ID_ADMIN:
                AdminModel model = gson.fromJson(response, AdminModel.class);
                Utils.saveString(getActivity(), model.toString(), PREF_ADMIN_DATA);
                loginSuccess();
                break;
        }
    }

    private void loadAdminInfo(AccessModel accessModel) {
        DentalManageApp.setToken(accessModel.getToken());
        Utils.saveInt(getActivity(), PREF_ADMIN_ID, accessModel.getId());
        if (cbRemember.isChecked()) {
            Utils.saveInt(getActivity(), PREF_REMEMBER, 1);
            Utils.saveString(getActivity(), PREF_USER_NAME, account);
            Utils.saveString(getActivity(), PREF_PASSWORD, password);
        }
        networkManager.requestApi(networkManager.getAdminInfoById(accessModel.getId()), ID_ADMIN);
    }

    private void loginSuccess() {
        Bundle bundle = new Bundle();
        startNewActivity(MainActivity.class, bundle);
        ActivityCompat.finishAffinity(getActivity());
    }

    @Override
    public void onHttpError(String response, int idRequest, int errorCode) {
        switch (idRequest) {
            case AppConstants.ID_LOGIN:
                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = s.toString();
            switch (view.getId()) {
                case R.id.tv_account:
                    account = text;
                    break;
                case R.id.tv_password:
                    password = text;
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private boolean validate() {
        boolean isValidate = true;

        if (TextUtils.isEmpty(account)) {
            tvAccount.startAnimation(shakeAnimation);
            tvAccount.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        if (TextUtils.isEmpty(password)) {
            tvPassword.startAnimation(shakeAnimation);
            tvPassword.setError(getString(R.string.blank_field));
            isValidate = false;
        }
        return isValidate;
    }
}
