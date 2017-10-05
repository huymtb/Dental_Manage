package com.prostage.dental_manage.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.prostage.dental_manage.R;
import com.prostage.dental_manage.base.BaseFragment;
import com.prostage.dental_manage.views.TitleBar;

/**
 * Created by Linh on 3/30/2017.
 */

public class SearchUserFragment extends BaseFragment implements View.OnClickListener {
    private TitleBar titleBar;
    private Button btSearch;

    public static SearchUserFragment newInstance() {

        Bundle args = new Bundle();

        SearchUserFragment fragment = new SearchUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        btSearch = (Button) view.findViewById(R.id.bt_search);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleBar.setTitle(getString(R.string.title_user));
        btSearch.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search:
                UserListFragment fragment = UserListFragment.newInstance();
                add(fragment, UserListFragment.TAG, true, true, R.id.flContainer);
        }
    }
}
