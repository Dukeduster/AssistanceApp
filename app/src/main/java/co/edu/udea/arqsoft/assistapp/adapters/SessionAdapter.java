package co.edu.udea.arqsoft.assistapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import co.edu.udea.arqsoft.assistapp.dtos.Session;

/**
 * Created by AW 13 on 26/11/2017.
 */

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.RequestViewHolder> implements RecyclerView.OnItemTouchListener {

    private Context context;
    static List<Session> sessions;
    public final String urlQr="https://chart.googleapis.com/chart?cht=qr&chs=250x250&chl=";

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

    public SessionAdapter(List<Session> sessions, Context context) {
        SessionAdapter.sessions = sessions;
        this.context = context;
    }

    @Override
    public SessionAdapter.RequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.session_card, viewGroup, false);
        return new SessionAdapter.RequestViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final SessionAdapter.RequestViewHolder viewHolder, int i) {
        viewHolder.sessionName.setText(sessions.get(i).getName());
        viewHolder.sessionDate.setText(sessions.get(i).getFechaSesion());
        // Log.e("statusRoute", routes.get())
        viewHolder.sessionDescription.setText(sessions.get(i).getDescripcion());
        final Integer index = i;
        viewHolder.buttonAssist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("Indice", String.valueOf(index));
                //Toast.makeText(context,"indice: "+String.valueOf(index),Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(context, MapsActivity.class);
                //intent.putExtra("route",(Parcelable) routes.get(index));
                //context.startActivity(intent);

            }
        });
        viewHolder.buttonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("Indice", String.valueOf(index));
                //Toast.makeText(context,"indice: "+String.valueOf(index),Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(context, MapsActivity.class);
                //intent.putExtra("route",(Parcelable) routes.get(index));
                //context.startActivity(intent);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlQr+sessions.get(index).getId()));
                context.startActivity(browserIntent);
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
        return sessions.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_session_card)
        public TextView sessionName;
        @BindView(R.id.date_session_card)
        public TextView sessionDate;
        @BindView(R.id.desc_session_card)
        public TextView sessionDescription;
        @BindView(R.id.button_assist)
        public Button buttonAssist;
        @BindView(R.id.button_link)
        public Button buttonLink;

        View viewParent;

        RequestViewHolder(View view, Context context) {
            super(view);
            ButterKnife.bind(this, view);
            viewParent = view;
        }
    }
}
