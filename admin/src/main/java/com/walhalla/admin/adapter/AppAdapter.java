package com.walhalla.admin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.admin.R;
import com.walhalla.admin.adapter.appitem.AppObj;
import com.walhalla.admin.adapter.appitem.AppViewHolder;
import com.walhalla.ui.DLog;
import com.walhalla.ui.Module_U;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private final Context context;
    private final List<AppObj> appList = new ArrayList<>();

    public AppAdapter(Context context, List<AppObj> fileName) {
        this.context = context;
        try {
            appList.addAll(fileName);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_other, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        try {
            DLog.d("000000000000000000000000");
            AppObj app = appList.get(position);
            holder.titleTextView.setText("@@@" + app.country);
            holder.rateTextView.setText("@@@" + app.lang);
            holder.countTextView.setText("@@@" + app.entity.toString());

            holder.itemView.setOnClickListener(v ->
            {

                //Module_U.openMarketApp(context, app.url);
            });

            holder.itemView.setOnLongClickListener(v -> {
                showPopupMenu(v);
                return true;
            });
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }


    @SuppressLint("NonConstantResourceId")
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_app_item, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_option1:
                    // Обработка действия всплывающего меню
                    return true;
                case R.id.menu_option2:
                    // Обработка действия всплывающего меню
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }

    public void swap(List<AppObj> message) {
        appList.clear();
        appList.addAll(message);
        notifyDataSetChanged();
    }
}

