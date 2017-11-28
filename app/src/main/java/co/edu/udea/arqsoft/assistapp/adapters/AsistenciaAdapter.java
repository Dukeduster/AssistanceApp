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
import co.edu.udea.arqsoft.assistapp.AddSessionActivity;
import co.edu.udea.arqsoft.assistapp.MainActivity;
import co.edu.udea.arqsoft.assistapp.MapsActivity;
import co.edu.udea.arqsoft.assistapp.R;
import co.edu.udea.arqsoft.assistapp.dtos.Asistencia;

/**
 * Created by AW 13 on 28/11/2017.
 */

public class AsistenciaAdapter extends RecyclerView.Adapter<AsistenciaAdapter.RequestViewHolder> implements RecyclerView.OnItemTouchListener {

    private Context context;
    static List<Asistencia> asistencias;

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

    public AsistenciaAdapter(List<Asistencia> asistencias, Context context) {
        AsistenciaAdapter.asistencias = asistencias;
        this.context = context;
    }

    @Override
    public AsistenciaAdapter.RequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.assist_card, viewGroup, false);
        return new AsistenciaAdapter.RequestViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final AsistenciaAdapter.RequestViewHolder viewHolder, int i) {
        viewHolder.sessionName.setText(asistencias.get(i).getSesion());
        viewHolder.assistDate.setText(asistencias.get(i).getFechaAsistencia());
        viewHolder.studentId.setText(String.valueOf(asistencias.get(i).getEstudiante()));
        viewHolder.assistLat.setText(asistencias.get(i).getLatitud().toString());
        viewHolder.assistLon.setText(asistencias.get(i).getLongitud().toString());
        final Integer index = i;
        viewHolder.buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO mapActivitty
                Intent i = new Intent(context, MapsActivity.class);
                i.putExtra("lat",asistencias.get(index).getLatitud());
                i.putExtra("lon",asistencias.get(index).getLongitud());
                context.startActivity(i);

            }
        });
        viewHolder.buttonCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlQr+sessions.get(index).getId()));
                //context.startActivity(browserIntent);
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
        return asistencias.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.session_assist_card)
        public TextView sessionName;
        @BindView(R.id.date_assist_card)
        public TextView assistDate;
        @BindView(R.id.stu_assist_card)
        public TextView studentId;
        @BindView(R.id.lat_assist_card)
        public TextView assistLat;
        @BindView(R.id.lon_assist_card)
        public TextView assistLon;
        @BindView(R.id.button_assist_course)
        public Button buttonCourse;
        @BindView(R.id.button_assist_map)
        public Button buttonMap;

        View viewParent;

        RequestViewHolder(View view, Context context) {
            super(view);
            ButterKnife.bind(this, view);
            viewParent = view;
        }
    }
}