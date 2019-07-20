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
import java.util.List;

import ripe.ripe.APIUtils.RipeUserService;
import ripe.ripe.Utils.LeaderboardAdapter;
import ripe.ripe.Utils.PersonDataModel;
import ripe.ripe.R;

public class LeaderboardFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
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

        final ArrayList<PersonDataModel> data = new ArrayList<>();

        RipeUserService userService = new RipeUserService();
        userService.getLeaderboard(new RipeUserService.LeaderboardCallback() {
            @Override
            public void populateLeaderboard(List<List<String>> leaderboard) {
                int i = 1;
                for(List<String> userContent: leaderboard){
                    int score = Integer.parseInt(userContent.get(1));
                    data.add(new PersonDataModel(i, userContent.get(0), score));
                    i++;
                }

                adapter = new LeaderboardAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        });

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
