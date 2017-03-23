package com.bitlove.fetlife.view.screen.resource.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitlove.fetlife.FetLifeApplication;
import com.bitlove.fetlife.R;
import com.bitlove.fetlife.event.ServiceCallFinishedEvent;
import com.bitlove.fetlife.model.service.FetLifeApiIntentService;
import com.bitlove.fetlife.view.adapter.StatusesRecyclerAdapter;
import com.bitlove.fetlife.view.screen.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class StatusesFragment extends BaseFragment {

    private static final String ARG_MEMBER_ID = "ARG_MEMBER_ID";
    private RecyclerView recyclerView;

    public static StatusesFragment newInstance(String memberId) {
        //TODO(profile): make it work with current user too (mergeSave user as member and keep only id in other table)
        StatusesFragment statusesFragment = new StatusesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEMBER_ID, memberId);
        statusesFragment.setArguments(args);
        return statusesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_recycler,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getFetLifeApplication());
        recyclerView.setLayoutManager(recyclerLayoutManager);
        StatusesRecyclerAdapter adapter = new StatusesRecyclerAdapter(getArguments().getString(ARG_MEMBER_ID));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServiceCallFinished(ServiceCallFinishedEvent serviceCallFinishedEvent) {
        if (serviceCallFinishedEvent.getServiceCallAction().equals(FetLifeApiIntentService.ACTION_APICALL_MEMBER_STATUSES)) {
            refresh();
        }
    }

    public void refresh() {
        if (recyclerView != null) {
            StatusesRecyclerAdapter recyclerViewAdapter = (StatusesRecyclerAdapter) recyclerView.getAdapter();
            recyclerViewAdapter.refresh();
        }
    }


    private FetLifeApplication getFetLifeApplication() {
        return (FetLifeApplication) getActivity().getApplication();
    }
}
