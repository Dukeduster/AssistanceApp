package co.edu.udea.arqsoft.assistapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.edu.udea.arqsoft.assistapp.R;
import co.edu.udea.arqsoft.assistapp.dtos.Course;

/**
 * Created by AW 13 on 26/11/2017.
 */

 public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.RequestViewHolder> implements RecyclerView.OnItemTouchListener {

    private Context context;
    static List<Course> courses;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public CourseAdapter(List<Course> courses, Context context) {
        CourseAdapter.courses = courses;
        this.context = context;
    }

    @Override
    public CourseAdapter.RequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_card, viewGroup, false);
        return new CourseAdapter.RequestViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final CourseAdapter.RequestViewHolder viewHolder, final int i) {
        viewHolder.courseName.setText(courses.get(i).getName());
        viewHolder.dateExpire.setText(courses.get(i).getFechaExpiracion());
        // Log.e("statusRoute", routes.get())
        viewHolder.description.setText(courses.get(i).getDescripcion());
        final Integer index = i;
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("Indice", String.valueOf(index));
                //Toast.makeText(context,"indice: "+String.valueOf(index),Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(context, MapsActivity.class);
                //intent.putExtra("route",(Parcelable) routes.get(index));
                //context.startActivity(intent);
                Intent in = new Intent("LOADSESSIONS");
                in.putExtra("course",courses.get(index).getId());
                context.sendBroadcast(in);
            }
        });

        setAnimation(viewHolder.viewParent);

    }

    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.left_animation);
        viewToAnimate.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_course_card)
        public TextView courseName;
        @BindView(R.id.date_course_card)
        public TextView dateExpire;
        @BindView(R.id.desc_course_card)
        public TextView description;
        @BindView(R.id.button_card)
        public Button button;

        View viewParent;

        RequestViewHolder(View view, Context context) {
            super(view);
            ButterKnife.bind(this, view);
            viewParent = view;
        }
    }
}
