package com.betalent.betalent;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.betalent.betalent.Model.BeTalentDB;
import com.betalent.betalent.Model.Campaign;
import com.betalent.betalent.Model.Tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


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
    private int mNumCardsRejected = 0;
    private List<Tag> mTags;
    private Campaign mCampaign;
    private int mTagIndex = 0;
    private List<Tag> mSelectedTags = new ArrayList<Tag>();
    private List<Tag> mRejectedTags = new ArrayList<Tag>();
    private Iterator<Tag> mTagIterator;
    private boolean mflipped = false;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private ListIterator<Tag> mTagListIterator;
    private Tag mCurrentTag;

    Button btnPrevQuestion;
    TextView txtNumSelected;
    TextView txtNumRejected;
    ImageView imgCard;
    TextView txtTagName;
    TextView txtTagNameFront;
    TextView txtCardText;
    FrameLayout cardFront;
    FrameLayout cardBack;
    RelativeLayout cardHolder;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        betalentDb = BeTalentDB.getInstance(this.getContext());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_sort, container, false);

        ((MainActivity) getActivity())
                .setActionBarTitle(mCampaignName);

        btnPrevQuestion = view.findViewById(R.id.btnPrevQuestion);
        txtNumSelected = view.findViewById(R.id.txtNumSelected);
        txtNumRejected = view.findViewById(R.id.txtNumRejected);
        imgCard = view.findViewById(R.id.imgCard);
        cardHolder = view.findViewById(R.id.cardHolder);
        cardFront = view.findViewById(R.id.cardFront);
        cardBack = view.findViewById(R.id.cardBack);
        txtTagName = view.findViewById(R.id.txtTagName);
        txtTagNameFront = view.findViewById(R.id.txtTagNameFront);
        txtCardText = view.findViewById(R.id.txtCardText);

        loadAnimations();

//        imgCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        imgCard.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onTouch() {
                flipCard();
            }
            public void onSwipeLeft() {

                //
                //  Animate the view left
                //

                cardHolder.animate()
                        .translationX(0 - cardHolder.getWidth())
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                cardHolder.animate()
                                        .translationX(0)
                                        .setDuration(0)
                                        .setListener(null);
                                GetCard("Next");
                                //cardHolder.setVisibility(View.GONE);
                            }
                        });
            }
            public void onSwipeRight() {

                //
                //  Animate the view left
                //

                cardHolder.animate()
                        .translationX(cardHolder.getWidth())
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                cardHolder.animate()
                                        .translationX(0)
                                        .setDuration(0)
                                        .setListener(null);
                                GetCard("Previous");
                                //cardHolder.setVisibility(View.GONE);
                            }
                        });
            }
            public void onSwipeTop() {
//                Toast.makeText(getContext(), "selected", Toast.LENGTH_SHORT).show();
                mSelectedTags.add(mCurrentTag);
                mTagListIterator.remove();
                mNumCardsSelected++;
                txtNumSelected.setText(getString(R.string.num_cards_selected) + " " + mNumCardsSelected);

                //
                //  Animate the view up
                //

                cardHolder.animate()
                        .translationY(0 - cardHolder.getHeight())
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                cardHolder.animate()
                                        .translationY(0)
                                        .setDuration(0)
                                        .setListener(null);
                                GetCard("Next");
                                //cardHolder.setVisibility(View.GONE);
                            }
                        });


            }
            public void onSwipeBottom() {
//                Toast.makeText(getContext(), "rejected", Toast.LENGTH_SHORT).show();
                mRejectedTags.add(mCurrentTag);
                mTagListIterator.remove();
                mNumCardsRejected++;
                txtNumRejected.setText(getString(R.string.num_cards_rejected) + " " + mNumCardsRejected);

                //
                //  Animate the view up
                //

                cardHolder.animate()
                        .translationY(cardHolder.getHeight())
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                cardHolder.animate()
                                        .translationY(0)
                                        .setDuration(0)
                                        .setListener(null);
                                GetCard("Next");
                                //cardHolder.setVisibility(View.GONE);
                            }
                        });
            }

        });

        mCampaign = betalentDb.getCampaignDao().getCampaign(mCampaignId);
        mTags = betalentDb.getTagDao().getTags(mCampaign.getTagType());
        mTagIterator = mTags.iterator();
        mTagListIterator = mTags.listIterator();

        txtNumSelected.setText(getString(R.string.num_cards_selected) + " " + mNumCardsSelected);
        txtNumRejected.setText(getString(R.string.num_cards_rejected) + " " + mNumCardsRejected);

        GetCard("Next");

        return view;
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.in_animation);
    }

    private void GetCard(String direction) {

        mCurrentTag = null;

        if (direction == "Next") {

            if (mTagListIterator.hasNext()) {
                mCurrentTag = mTagListIterator.next();
            }
        } else {
            if (mTagListIterator.hasPrevious()) {
                mCurrentTag = mTagListIterator.previous();
            }
        }

        if (mCurrentTag != null) {

            String filename = getContext().getFilesDir() + "/CARD_" + mCurrentTag.getTagId() + ".jpg";

            Bitmap card = BitmapFactory.decodeFile(filename);
            imgCard.setImageBitmap(card);

            txtTagNameFront.setText(mCurrentTag.getTagName());
            txtTagName.setText(mCurrentTag.getTagName());
            txtCardText.setText(Html.fromHtml(mCurrentTag.getCardText()));

        } else {
                Toast.makeText(getContext(),
                        "No more cards",
                        Toast.LENGTH_LONG).show();
        }

    }

    private void flipCard() {

        if (!mflipped) {
            mSetRightOut.setTarget(cardFront);
            mSetLeftIn.setTarget(cardBack);
            mSetRightOut.start();
            mSetLeftIn.start();
            mflipped = true;
        } else {
            mSetRightOut.setTarget(cardBack);
            mSetLeftIn.setTarget(cardFront);
            mSetRightOut.start();
            mSetLeftIn.start();
            mflipped = false;
        }

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
