package com.gusar.tasker2.adapter;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import com.gusar.tasker2.R;
import com.gusar.tasker2.Utils;
import com.gusar.tasker2.fragment.CurrentTaskFragment;
import com.gusar.tasker2.model.Item;
import com.gusar.tasker2.model.ModelSeparator;
import com.gusar.tasker2.model.ModelTask;
import java.util.Calendar;

public class CurrentTasksAdapter extends TaskAdapter {
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_TASK = 0;

    public CurrentTasksAdapter(CurrentTaskFragment taskFragment) {
        super(taskFragment);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ModelTask.PRIORITY_LOW /*0*/:
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_task, viewGroup, false);
                return new TaskViewHolder(v, (TextView) v.findViewById(R.id.tvTaskTitle), (TextView) v.findViewById(R.id.tvTaskDate), (CircleImageView) v.findViewById(R.id.cvTaskPriority));
            case TYPE_SEPARATOR /*1*/:
                View separator = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_separator, viewGroup, false);
                return new SeparatorViewHolder(separator, (TextView) separator.findViewById(R.id.tvSeparatorName));
            default:
                return null;
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Item item = (Item) this.items.get(position);
        final Resources resources = viewHolder.itemView.getResources();
        if (item.isTask()) {
            viewHolder.itemView.setEnabled(true);
            final ModelTask task = (ModelTask) item;
            final TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;
            final View itemView = taskViewHolder.itemView;
            taskViewHolder.title.setText(task.getTitle());
            if (task.getDate() != 0) {
                taskViewHolder.date.setText(Utils.getFullDate(task.getDate()));
            } else {
                taskViewHolder.date.setText(null);
            }
            itemView.setVisibility(View.VISIBLE);
            taskViewHolder.priority.setEnabled(true);
            if (task.getDate() == 0 || task.getDate() >= Calendar.getInstance().getTimeInMillis()) {
                itemView.setBackgroundColor(resources.getColor(R.color.gray_50));
            } else {
                itemView.setBackgroundColor(resources.getColor(R.color.white));
            }
            taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_default_material_light));
            taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
            taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
//            taskViewHolder.priority.setColorFilter(Color.BLUE);
            taskViewHolder.priority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_24dp);
            itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            CurrentTasksAdapter.this.getTaskFragment().removeTaskDialog(taskViewHolder.getLayoutPosition());
                        }
                    }, 1000);
                    return true;
                }
            });
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    CurrentTasksAdapter.this.getTaskFragment().showTaskEditDialog(task);
                }
            });
            taskViewHolder.priority.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    taskViewHolder.priority.setEnabled(false);
                    task.setStatus(2);
                    CurrentTasksAdapter.this.getTaskFragment().activity.dbHelper.update().status(task.getTimeStamp(), 2);
                    taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_disabled_material_light));
                    taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_disabled_material_light));
                    taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
//                    taskViewHolder.priority.setColorFilter(Color.RED);
                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.priority, "rotationY", new float[]{-180.0f, 0.0f});
                    flipIn.addListener(new AnimatorListener() {
                        public void onAnimationStart(Animator animation) {
                        }

                        public void onAnimationEnd(Animator animation) {
                            if (task.getStatus() == 2) {
                                taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_24dp);
                                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView, "translationX", new float[]{0.0f, (float) itemView.getWidth()});
                                ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView, "translationX", new float[]{(float) itemView.getWidth(), 0.0f});
                                translationX.addListener(new AnimatorListener() {
                                    public void onAnimationStart(Animator animation) {
                                    }

                                    public void onAnimationEnd(Animator animation) {
                                        itemView.setVisibility(View.VISIBLE);
                                        CurrentTasksAdapter.this.getTaskFragment().moveTask(task);
                                        CurrentTasksAdapter.this.removeItem(taskViewHolder.getLayoutPosition());
                                    }

                                    public void onAnimationCancel(Animator animation) {
                                    }

                                    public void onAnimationRepeat(Animator animation) {
                                    }
                                });
                                AnimatorSet translationSet = new AnimatorSet();
                                translationSet.play(translationX).before(translationXBack);
                                translationSet.start();
                            }
                        }

                        public void onAnimationCancel(Animator animation) {
                        }

                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    flipIn.start();
                }
            });
            return;
        }
        ((SeparatorViewHolder) viewHolder).type.setText(resources.getString(((ModelSeparator) item).getType()));
    }

    public int getItemViewType(int position) {
        if (getItem(position).isTask()) {
            return 0;
        }
        return TYPE_SEPARATOR;
    }
}
