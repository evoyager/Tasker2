package com.gusar.tasker2.adapter;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
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
import com.gusar.tasker2.fragment.DoneTaskFragment;
import com.gusar.tasker2.model.Item;
import com.gusar.tasker2.model.ModelTask;

public class DoneTasksAdapter extends TaskAdapter {
    public DoneTasksAdapter(DoneTaskFragment taskFragment) {
        super(taskFragment);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_task, viewGroup, false);
        return new TaskViewHolder(v, (TextView) v.findViewById(R.id.tvTaskTitle), (TextView) v.findViewById(R.id.tvTaskDate), (CircleImageView) v.findViewById(R.id.cvTaskPriority));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Item item = (Item) this.items.get(position);
        if (item.isTask()) {
            viewHolder.itemView.setEnabled(true);
            final ModelTask task = (ModelTask) item;
            final TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;
            final View itemView = taskViewHolder.itemView;
            final Resources resources = itemView.getResources();
            taskViewHolder.title.setText(task.getTitle());
            if (task.getDate() != 0) {
                taskViewHolder.date.setText(Utils.getFullDate(task.getDate()));
            } else {
                taskViewHolder.date.setText(null);
            }
            itemView.setVisibility(0);
            taskViewHolder.priority.setEnabled(true);
            taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_disabled_material_light));
            taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_disabled_material_light));
            taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
            taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp);
            itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            DoneTasksAdapter.this.getTaskFragment().removeTaskDialog(taskViewHolder.getLayoutPosition());
                        }
                    }, 1000);
                    return true;
                }
            });
            taskViewHolder.priority.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    taskViewHolder.priority.setEnabled(false);
                    task.setStatus(1);
                    DoneTasksAdapter.this.getTaskFragment().activity.dbHelper.update().status(task.getTimeStamp(), 1);
                    taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_default_material_light));
                    taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
                    taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.priority, "rotationY", new float[]{180.0f, 0.0f});
                    taskViewHolder.priority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);
                    flipIn.addListener(new AnimatorListener() {
                        public void onAnimationStart(Animator animation) {
                        }

                        public void onAnimationEnd(Animator animation) {
                            if (task.getStatus() != 2) {
                                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView, "translationX", new float[]{0.0f, (float) (-itemView.getWidth())});
                                ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView, "translationX", new float[]{(float) (-itemView.getWidth()), 0.0f});
                                translationX.addListener(new AnimatorListener() {
                                    public void onAnimationStart(Animator animation) {
                                    }

                                    public void onAnimationEnd(Animator animation) {
                                        itemView.setVisibility(8);
                                        DoneTasksAdapter.this.getTaskFragment().moveTask(task);
                                        DoneTasksAdapter.this.removeItem(taskViewHolder.getLayoutPosition());
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
        }
    }
}
