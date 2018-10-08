package com.betalent.betalent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    TextView txtQuestion;
    private BeTalentDB betalentDb;

    private OnFragmentInteractionListener mListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(int campaignId) {
        QuestionFragment fragment = new QuestionFragment();

        Bundle args = new Bundle();
        args.putInt("CAMPAIGN_ID", campaignId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCampaignId = getArguments().getInt("CAMPAIGN_ID");
                Toast.makeText(this.getContext(), "Campaign Id: " + mCampaignId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        betalentDb = BeTalentDB.getInstance(this.getContext());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false);
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

//    private void GetQuestion() {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                System.out.println("Running Runnable ...");
//                Question question = betalentDb.getQuestionDao().getNextQuestion()
//
//                if (user == null) {
//                    System.out.println("No user found");
//
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    intent.putExtra("DataAvailable", false);
//                    startActivity(intent);
//
//                } else {
//                    System.out.println(user.toString());
//
//                    Intent intent;
//
//                    if (user.getLoggedIn()) {
//                        intent = new Intent(getApplicationContext(), MainActivity.class);
//                    } else {
//                        intent = new Intent(getApplicationContext(), LoginActivity.class);
//                        intent.putExtra("DataAvailable", true);
//                        intent.putExtra("EmailAddress", user.getEmailAddress());
//                        intent.putExtra("Password", user.getPassword());
//                    }
//
//                    startActivity(intent);
//                }
//
//            }
//        }) .start();
//    }
}
