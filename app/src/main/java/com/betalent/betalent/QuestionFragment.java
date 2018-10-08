package com.betalent.betalent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.betalent.betalent.Model.BeTalentDB;
import com.betalent.betalent.Model.Question;
import com.betalent.betalent.Model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {

    private int mCampaignId;
    private String mCampaignName;

    TextView txtQuestion;
    private BeTalentDB betalentDb;

    private OnFragmentInteractionListener mListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(int campaignId, String campaignName) {
        QuestionFragment fragment = new QuestionFragment();

        Bundle args = new Bundle();
        args.putInt("CAMPAIGN_ID", campaignId);
        args.putString("CAMPAIGN_NAME", campaignName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCampaignId = getArguments().getInt("CAMPAIGN_ID");
            mCampaignName = getArguments().getString("CAMPAIGN_NAME");
//            Toast.makeText(this.getContext(), "Campaign Id: " + mCampaignId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        betalentDb = BeTalentDB.getInstance(this.getContext());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        ((MainActivity) getActivity())
                .setActionBarTitle(mCampaignName);

        txtQuestion = view.findViewById(R.id.txtQuestion);

        getQuestion();

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

    private void getQuestion() {

        Question question = betalentDb.getQuestionDao().getNextQuestion(mCampaignId);

        if (question != null) {
            txtQuestion.setText(Html.fromHtml(question.getQuestionTextSelf()).toString());
        }

    }
}
