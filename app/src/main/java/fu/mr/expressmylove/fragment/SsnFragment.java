package fu.mr.expressmylove.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fu.mr.expressmylove.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SsnFragment extends Fragment {


    public SsnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ssn, container, false);
    }

}
