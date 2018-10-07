package com.betalent.betalent;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.betalent.betalent.Model.Campaign;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

class AssessmentsAdapter extends RecyclerView.Adapter<AssessmentsAdapter.ViewHolder> {

    List<Campaign> assessments;
    Context mContext;

    public AssessmentsAdapter(List<Campaign> assessments) {
        this.assessments = assessments;
    }

    @NonNull
    @Override
    public AssessmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentsAdapter.ViewHolder holder, int position) {
        Resources res = holder.itemView.getContext().getResources();

        //
        //  Campaign Name
        //

        holder.campaignName.setText(assessments.get(position).getCampaignName());

        //
        //  Complete by date
        //

        DateFormat df = android.text.format.DateFormat.getDateFormat(holder.itemView.getContext());
        String formattedDate = df.format(new Date(assessments.get(position).getEndDate()));
        holder.endDate.setText(res.getString(R.string.complete_by).replace("{0}", formattedDate));

        //
        //  Product logo
        //

        String filename = holder.itemView.getContext().getFilesDir() + "/ICON_" + assessments.get(position).getCampaignId() + ".jpg";
        Bitmap logo = BitmapFactory.decodeFile(filename);
        holder.productLogo.setImageBitmap(logo);

        //
        //  Assessee
        //

        if (assessments.get(position).getAssesseeForename() != "") {
            holder.assessee.setText(assessments.get(position).getAssesseeForename() + " " +
                    assessments.get(position).getAssesseeSurname());

        }


        holder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView campaignName;
        public TextView endDate;
        public ImageView productLogo;
        public TextView assessee;
        public RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            campaignName = itemView.findViewById(R.id.campaignName);
            endDate = itemView.findViewById(R.id.campaignEndDate);
            productLogo = itemView.findViewById(R.id.productLogo);
            assessee = itemView.findViewById(R.id.assessee);
            parentLayout = itemView.findViewById(R.id.assessmentHolder);
        }
    }
}
