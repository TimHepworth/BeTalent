package com.betalent.betalent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.betalent.betalent.Model.BeTalentDB;
import com.betalent.betalent.Model.Question;
import com.betalent.betalent.Model.QuestionChoice;
import com.betalent.betalent.Model.User;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

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
    private int mQuestionNo;
    private int mNumQuestions;

    Button btnPrevQuestion;
    TextView txtQuestion;
    TextView txtQuestionProgress;
    LinearLayout buttonContainer;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        betalentDb = BeTalentDB.getInstance(this.getContext());

        mNumQuestions = betalentDb.getQuestionDao().getNumQuestions(mCampaignId);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        ((MainActivity) getActivity())
                .setActionBarTitle(mCampaignName);

        btnPrevQuestion = view.findViewById(R.id.btnPrevQuestion);
        txtQuestionProgress = view.findViewById(R.id.txtQuestionProgress);
        txtQuestion = view.findViewById(R.id.txtQuestion);
        buttonContainer = view.findViewById(R.id.buttonContainer);

        getQuestion();

        return view;
    }

    private void getQuestion() {

        Question question = betalentDb.getQuestionDao().getNextQuestion(mCampaignId);

        if (question != null) {

            mQuestionNo = question.getQuestionNo();

            btnPrevQuestion.setOnClickListener((getOnClickListener(btnPrevQuestion, -1)));

            txtQuestionProgress.setText(mQuestionNo + " / " + mNumQuestions);

            //
            //  Show the question text
            //

            txtQuestion.setText(Html.fromHtml(question.getQuestionTextSelf()).toString());

            //
            //  Depending on the question type show the answer buttons
            //

            buttonContainer.removeAllViews();

            switch(question.getQuestionType()) {

                case "SCALE":
                    List<QuestionChoice> choices = betalentDb.getQuestionChoiceDao().getScaleQuestionChoices(question.getScaleId());

                    for (int i = 0; i < choices.size(); i++) {

                        Button button = new Button(this.getContext());

                        button.setText(choices.get(i).getChoiceText());
                        button.setOnClickListener((getOnClickListener(button, 1)));

                        buttonContainer.addView(button);

                    }

                    break;

                case "ORDER":
                    break;

                case "CHOICE":
                    break;

                case "TEXT":
                    break;
            }
        }

    }

    View.OnClickListener getOnClickListener(final Button button, final int questionDelta)  {
        return new View.OnClickListener() {
            public void onClick(View v) {

                if (((questionDelta < 0) && (mQuestionNo > 1)) || ((questionDelta > 0) && (mQuestionNo < mNumQuestions)) ){
                    betalentDb.getCampaignDao().setNextQuestion(mCampaignId, questionDelta);
                }

                getQuestion();
            }
        };
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
