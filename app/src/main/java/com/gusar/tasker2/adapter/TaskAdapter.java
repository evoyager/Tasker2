package com.gusar.tasker2.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import com.gusar.tasker2.fragment.OldTaskFragment;
import com.gusar.tasker2.model.Item;
import com.gusar.tasker2.model.ModelSeparator;
import com.gusar.tasker2.model.ModelTask;
import java.util.ArrayList;
import java.util.List;

public abstract class TaskAdapter extends Adapter<ViewHolder> {
    public boolean containsSeparatorFuture;
    public boolean containsSeparatorOverdue;
    public boolean containsSeparatorToday;
    public boolean containsSeparatorTomorrow;
    List<Item> items = new ArrayList();
    OldTaskFragment taskFragment;

    protected class SeparatorViewHolder extends ViewHolder {
        protected TextView type;

        public SeparatorViewHolder(View itemView, TextView type) {
            super(itemView);
            this.type = type;
        }
    }

    protected class TaskViewHolder extends ViewHolder {
        protected TextView date;
        protected CircleImageView priority;
        protected TextView title;

        public TaskViewHolder(View itemView, TextView title, TextView date, CircleImageView priority) {
            super(itemView);
            this.title = title;
            this.date = date;
            this.priority = priority;
        }
    }

    public TaskAdapter(OldTaskFragment taskFragment) {
        this.taskFragment = taskFragment;
    }

    public Item getItem(int position) {
        return (Item) this.items.get(position);
    }

    public void updateTask(ModelTask newTask) {
        for (int i = 0; i < getItemCount(); i++) {
            if (getItem(i).isTask()) {
                if (newTask.getTimeStamp() == ((ModelTask) getItem(i)).getTimeStamp()) {
                    removeItem(i);
                    getTaskFragment().addTask(newTask, false);
                }
            }
        }
    }

    public void addItem(Item item) {
        this.items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(int location, Item item) {
        this.items.add(location, item);
        notifyItemInserted(location);
    }

    public void removeItem(int location) {
        if (location >= 0 && location <= getItemCount() - 1) {
            this.items.remove(location);
            notifyItemRemoved(location);
            if (location - 1 < 0 || location > getItemCount() - 1) {
                if (getItemCount() - 1 >= 0 && !getItem(getItemCount() - 1).isTask()) {
                    switch (((ModelSeparator) getItem(getItemCount() - 1)).getType()) {
                        case ModelSeparator.TYPE_FUTURE /*2131165243*/:
                            this.containsSeparatorFuture = false;
                            break;
                        case ModelSeparator.TYPE_OVERDUE /*2131165244*/:
                            this.containsSeparatorOverdue = false;
                            break;
                        case ModelSeparator.TYPE_TODAY /*2131165245*/:
                            this.containsSeparatorToday = false;
                            break;
                        case ModelSeparator.TYPE_TOMORROW /*2131165246*/:
                            this.containsSeparatorTomorrow = false;
                            break;
                    }
                    int loc = getItemCount() - 1;
                    this.items.remove(loc);
                    notifyItemRemoved(loc);
                }
            } else if (!getItem(location).isTask() && !getItem(location - 1).isTask()) {
                switch (((ModelSeparator) getItem(location - 1)).getType()) {
                    case ModelSeparator.TYPE_FUTURE /*2131165243*/:
                        this.containsSeparatorFuture = false;
                        break;
                    case ModelSeparator.TYPE_OVERDUE /*2131165244*/:
                        this.containsSeparatorOverdue = false;
                        break;
                    case ModelSeparator.TYPE_TODAY /*2131165245*/:
                        this.containsSeparatorToday = false;
                        break;
                    case ModelSeparator.TYPE_TOMORROW /*2131165246*/:
                        this.containsSeparatorTomorrow = false;
                        break;
                }
                this.items.remove(location - 1);
                notifyItemRemoved(location - 1);
            }
        }
    }

    public void removeAllItems() {
        if (getItemCount() != 0) {
            this.items = new ArrayList();
            notifyDataSetChanged();
            this.containsSeparatorOverdue = false;
            this.containsSeparatorToday = false;
            this.containsSeparatorTomorrow = false;
            this.containsSeparatorFuture = false;
        }
    }

    public int getItemCount() {
        return this.items.size();
    }

    public OldTaskFragment getTaskFragment() {
        return this.taskFragment;
    }
}
