package com.suhasdara.walkalarm.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.suhasdara.walkalarm.R;
import com.suhasdara.walkalarm.activity.AlarmEditActivity;
import com.suhasdara.walkalarm.component.AlarmView;
import com.suhasdara.walkalarm.database.AlarmDatabaseHelper;
import com.suhasdara.walkalarm.model.Alarm;
import com.suhasdara.walkalarm.service.AlarmLoaderService;
import com.suhasdara.walkalarm.service.AlarmReceiver;

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
                enablerListener(v.getContext(), isChecked, alarm);
            }
        });
        view.getDeleterComponent().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleterListener(v.getContext(), alarm);
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

    private void enablerListener(Context context, boolean isChecked, Alarm alarm) {
        alarm.setEnabled(isChecked);
        AlarmDatabaseHelper.getInstance(context).updateAlarm(alarm);
        if(alarm.isEnabled()) {
            AlarmReceiver.scheduleAlarm(context, alarm);
        } else {
            AlarmReceiver.cancelAlarm(context, alarm);
        }
        AlarmLoaderService.launchService(context);
    }

    private void deleterListener(final Context context, final Alarm alarm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog)
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_content)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlarmDatabaseHelper.getInstance(context).deleteAlarm(alarm);
                        AlarmReceiver.cancelAlarm(context, alarm);
                        AlarmLoaderService.launchService(context);
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        builder.show();
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
