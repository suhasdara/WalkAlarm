package com.suhasdara.walkalarm.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.suhasdara.walkalarm.activity.AlarmEditActivity;
import com.suhasdara.walkalarm.component.AlarmView;
import com.suhasdara.walkalarm.database.AlarmDatabaseHelper;
import com.suhasdara.walkalarm.model.Alarm;
import com.suhasdara.walkalarm.service.AlarmLoaderService;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private ArrayList<Alarm> alarms;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AlarmView view = new AlarmView(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Alarm alarm  = alarms.get(position);
        AlarmView view = viewHolder.getAlarmView();
        view.setData(alarm);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                Intent intent = new Intent(c, AlarmEditActivity.class);
                intent.putExtra(AlarmEditActivity.ALARM, alarm);
                c.startActivity(intent);
            }
        });
        view.getEnablerComponent().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                Context c = v.getContext();
                alarm.setEnabled(isChecked);
                AlarmDatabaseHelper.getInstance(c).updateAlarm(alarm);
                AlarmLoaderService.launchService(c);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarms == null ? 0 : alarms.size();
    }

    public void setAlarms(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private AlarmView view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = (AlarmView) itemView;
        }

        public AlarmView getAlarmView() {
            return view;
        }
    }
}
