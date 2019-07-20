
package ripe.ripe.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ripe.ripe.R;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {

    private ArrayList<PersonDataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRank;
        TextView textViewName;
        TextView textViewEmail;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewRank = (TextView) itemView.findViewById(R.id.person_rank);
            this.textViewName = (TextView) itemView.findViewById(R.id.person_name);
            this.textViewEmail = (TextView) itemView.findViewById(R.id.person_email);
        }
    }

    public LeaderboardAdapter(ArrayList<PersonDataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_leaderboard, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewRank = holder.textViewRank;
        TextView textViewName = holder.textViewName;
        TextView textViewEmail = holder.textViewEmail;

        textViewRank.setText(Integer.toString(dataSet.get(listPosition).getRank()));
        textViewName.setText(dataSet.get(listPosition).getName());
        textViewEmail.setText(Integer.toString(dataSet.get(listPosition).getScore()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
