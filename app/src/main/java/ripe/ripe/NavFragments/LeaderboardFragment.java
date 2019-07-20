package ripe.ripe.NavFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ripe.ripe.Utils.LeaderboardAdapter;
import ripe.ripe.Utils.PersonDataModel;
import ripe.ripe.R;

public class LeaderboardFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<PersonDataModel> data;
    private final int LEADER_COUNT = 7;

    private Context context;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    public static LeaderboardFragment newInstance(String param1, String param2) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard_main, container, false);
        context = this.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.leader_content);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //HARDCODED LEADERBOARD
        String[] names = {"Travis Scott", "Kanye West", "Kendrick Lamar", "Drake", "Asap Rocky", "Isaiah Rashad", "Skepta"};
        String[] emails = {"goat@gmail.com", "yeezy@gmail.com", "comp10@gmail.com", "drakeazz@gmail.com", "a$ap@gmail.com", "albumnever@gmail.com", "greaze@gmail.com"};
        int[] images = {R.drawable.leader_1_trav, R.drawable.leader_2_ye, R.drawable.leader_3_kenny, R.drawable.leader_4_drake, R.drawable.leader_5_rocky, R.drawable.leader_6_rashad, R.drawable.leader_7_skepta};

        data = new ArrayList<>();
        for (int i = 0; i <LEADER_COUNT; ++i){
            data.add(new PersonDataModel(
                    i+1,
                    names[i],
                    emails[i],
                    images[i]
            ));
        }

        adapter = new LeaderboardAdapter(data);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
