package com.walhalla.admin;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.walhalla.admin.adapter.AppAdapter;
import com.walhalla.admin.adapter.appitem.AppObj;
import com.walhalla.ui.DLog;

import java.util.ArrayList;
import java.util.List;


public class AppListFragment extends CompatFragment {
    AppAdapter configAdapter;
    private static final String KEY_VAR0 = AppListFragment.class.getSimpleName();
    private final List<AppObj> mRecyclerViewItems = new ArrayList<>();


    private String fileName;
    private Handler handler;

    public static Fragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_VAR0, s);
        AppListFragment fragment = new AppListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileName = getArguments().getString(KEY_VAR0);
        }
        handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        if (savedInstanceState == null) {
            configAdapter = new AppAdapter(getActivity(), mRecyclerViewItems);
            recyclerView.setAdapter(configAdapter);

        }
    }

    private void loadCategory() {
//        index = 0;
//        swipe.setRefreshing(false);

        try {
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
//            reference.onDisconnect().removeValue((error, reference1) -> {
//                if (error != null) {
//                    Log.d(TAG, "could not establish onDisconnect event:" + error.getMessage());
//                }
//            });
//            reference.onDisconnect().cancel((databaseError, databaseReference) -> {
//                M.d( "onComplete: 00000");
//            });
            try {
                reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {



                                //showLoader();
                                if (mainCallback != null) {
                                    mainCallback.hideLoader();
                                }
                                List<AppObj> tmp = new ArrayList<>();
                                for (DataSnapshot obj : snapshot.getChildren()) {
                                    try {
                                        AppObj category = obj.getValue(AppObj.class);
                                        DLog.d("@@@@"+category);
                                        tmp.add(category);

                                    } catch (Exception e) {
                                        DLog.handleException(e);
                                        onRetrievalFailed(
                                                "Failed to getUrl value." + e.getLocalizedMessage());
                                    }
                                }

                                if (!tmp.isEmpty()) {
                                    onMessageRetrieved(tmp);
                                } else {
                                    onRetrievalFailed("Database is empty, reinstall the Application");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                onRetrievalFailed(error.getMessage());
                            }
                        });

                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        DLog.d("@");
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        DLog.d("@");
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        DLog.d("@");
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        DLog.d("@");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        DLog.d("@");
                    }
                });
            } catch (Exception e) {
                onRetrievalFailed(e.getLocalizedMessage());
            }
        } catch (Exception e) {
            onRetrievalFailed("loadCategory: " + e.getLocalizedMessage());
        }

//        if (Config.ENABLE_NATIVE_ADS) {
//            this.nativeHelper.prefetchAds(getActivity());
//        }
    }
    private void onMessageRetrieved(List<AppObj> message) {
        handler.post(() -> {
            if (message != null && !message.isEmpty()) {
                mRecyclerViewItems.clear();
                mRecyclerViewItems.addAll(message);

                if (getContext() != null && isAdded()) {
                    //loadNativeAds();
                    //M.d( "loadMenu: " + mRecyclerViewItems.toString());

                    if (mRecyclerViewItems.isEmpty()) {
//                    empty1.setVisibility(View.VISIBLE);
//                    empty2.setVisibility(View.VISIBLE);

                        //recyclerView.setVisibility(View.GONE);
                    } else {
//                    empty1.setVisibility(View.GONE);
//                    empty2.setVisibility(View.GONE);

                        //recyclerView.setVisibility(View.VISIBLE);
                        configAdapter.swap(mRecyclerViewItems);
//                    if (mainCallback != null) {
//                        mainCallback.showMessage("ok");
//                    }
                    }
                }
            }
        });
    }

    private void onRetrievalFailed(String error) {
        DLog.d( "onRetrievalFailed: " + error);
        if (mainCallback != null) {
            mainCallback.hideLoader();
            mainCallback.showMessage(error);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategory();
    }
}
