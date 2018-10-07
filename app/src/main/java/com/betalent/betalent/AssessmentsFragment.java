package com.betalent.betalent;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.betalent.betalent.Model.BeTalentDB;
import com.betalent.betalent.Model.Campaign;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AssessmentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssessmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView assessmentsRecycler;
    RecyclerView.Adapter adapter;

    BeTalentDB beTalentDb;

    List<Campaign> assessments;

    private OnFragmentInteractionListener mListener;

    public AssessmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssessmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssessmentsFragment newInstance(String param1, String param2) {
        AssessmentsFragment fragment = new AssessmentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity())
                .setActionBarTitle(getString(R.string.txt_assessments));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assessments, container, false);

        beTalentDb = BeTalentDB.getInstance(getActivity());

        List<Campaign> assessments = beTalentDb.getCampaignDao().getCampaigns();

        Log.d(TAG, "onCreateView: No. assessments" + assessments.size());
        assessmentsRecycler = view.findViewById(R.id.assessmentsRecycler);
        assessmentsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AssessmentsAdapter(assessments);
        assessmentsRecycler.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
