package meu.app.apprecipes.ui.login.fragmentos;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import meu.app.apprecipes.R;
import meu.app.apprecipes.adapters.AdapterAuxiliar;
import meu.app.apprecipes.models.Auxiliar;


public class ViewDirectionsFragment extends Fragment {

    private List<Auxiliar> directionList;
    private AdapterAuxiliar directionAdapter;

    private RecyclerView directionRecyclerView;
    private TextView emptyView;

    public ViewDirectionsFragment() {
        // Required empty public constructor
    }

    public static ViewDirectionsFragment newInstance(List<Auxiliar> directions) {
        ViewDirectionsFragment fragment = new ViewDirectionsFragment();
        if (directions == null)
            directions = new ArrayList<>();
        Bundle args = new Bundle();
        args.putParcelableArrayList("directions", (ArrayList<Auxiliar>) directions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_directions, container, false);

        Bundle args = getArguments();
        if (args != null)
            directionList = args.getParcelableArrayList("directions");

        directionRecyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.empty_view);

        directionAdapter = new AdapterAuxiliar(getActivity(), directionList, false);
        toggleEmptyView();

        directionRecyclerView.setHasFixedSize(true);
        directionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        directionRecyclerView.setAdapter(directionAdapter);

        return view;
    }

    private void toggleEmptyView() {
        if (directionList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            directionRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            directionRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
