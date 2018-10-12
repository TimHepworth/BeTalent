package com.betalent.betalent;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.betalent.betalent.Model.BeTalentDB;
import com.betalent.betalent.Model.Campaign;
import com.betalent.betalent.Model.Tag;

import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardSortFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardSortFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardSortFragment extends Fragment {

    private int mCampaignId;
    private String mCampaignName;
    private int mNumCardsSelected = 0;
    private List<Tag> mTags;
    private Campaign mCampaign;
    private int mTagIndex = 0;
    private List<Tag> mSelectedTags;
    private List<Tag> mRejectedTags;
    private Iterator<Tag> mTagIterator;
    private boolean mflipped = false;

    Button btnPrevQuestion;
    TextView txtQuestionProgress;
    ImageView imgCard;
    TextView txtTagName;
    TextView txtCardText;
    LinearLayout cardFront;
    LinearLayout cardBack;

    private BeTalentDB betalentDb;

    private OnFragmentInteractionListener mListener;

    public CardSortFragment() {
        // Required empty public constructor
    }

    public static CardSortFragment newInstance(int campaignId, String campaignName) {
        CardSortFragment fragment = new CardSortFragment();

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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_sort, container, false);

        ((MainActivity) getActivity())
                .setActionBarTitle(mCampaignName);

        btnPrevQuestion = view.findViewById(R.id.btnPrevQuestion);
        txtQuestionProgress = view.findViewById(R.id.txtQuestionProgress);
        imgCard = view.findViewById(R.id.imgCard);
        cardFront = view.findViewById(R.id.cardFront);
        cardBack = view.findViewById(R.id.cardBack);
        txtTagName = view.findViewById(R.id.txtTagName);
        txtCardText = view.findViewById(R.id.txtCardText);

//        imgCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        imgCard.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onTouch() {
                flipCard();
            }
            public void onSwipeRight() {
                GetCard();
            }
            public void onSwipeLeft() {
                GetCard();
            }
//            public void onSwipeBottom() {
//                Toast.makeText(getContext(), "bottom", Toast.LENGTH_SHORT).show();
//            }

        });

        mCampaign = betalentDb.getCampaignDao().getCampaign(mCampaignId);
        mTags = betalentDb.getTagDao().getTags(mCampaign.getTagType());
        mTagIterator = mTags.iterator();

        txtQuestionProgress.setText(getString(R.string.num_cards_selected) + " " + mNumCardsSelected);

        GetCard();

        return view;
    }

    private void GetCard() {

        if (mTagIterator.hasNext()) {

            Tag tag = mTagIterator.next();

            String filename = getContext().getFilesDir() + "/CARD_" + tag.getTagId() + ".jpg";

            Bitmap card = BitmapFactory.decodeFile(filename);
            imgCard.setImageBitmap(card);

            txtTagName.setText(tag.getTagName());
            txtCardText.setText(tag.getCardText());

        } else {
                Toast.makeText(getContext(),
                        "No more cards",
                        Toast.LENGTH_LONG).show();
        }

    }

    private void flipCard() {

        ObjectAnimator flip;

        if (mflipped) {
            flip = ObjectAnimator.ofFloat(cardFront, "rotationY", 180f, 0f);
        } else {
            flip = ObjectAnimator.ofFloat(cardFront, "rotationY", 0f, 180f);
            flip.setDuration(500);
            flip.start();
            cardFront.setVisibility(View.INVISIBLE);
            cardBack.setVisibility(View.VISIBLE);
            flip = ObjectAnimator.ofFloat(cardBack, "rotationY", 90f, 0f);
            flip.setDuration(500);
            flip.start();
        }


        mflipped = !mflipped;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
