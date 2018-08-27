package com.example.younes.authentificationproject.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.younes.authentificationproject.R;
import com.example.younes.authentificationproject.models.Organisation;

import java.util.List;

/**
 * Created by younes on 8/20/2018.
 */

public class OrganisationsRecyclerViewAdapter extends RecyclerView.Adapter<OrganisationsRecyclerViewAdapter.ViewHolder>   {
    private View cardView;
    List<Organisation> mOrganisations;
    Context mContext;
    private static int openedOrganisationDetailsposition;
    private static int itemToOpen;
    OrganisationsRecyclerViewAdapter.RecyclerViewClickListener listener;
    public OrganisationsRecyclerViewAdapter(List<Organisation> organisations,Context context,OrganisationsRecyclerViewAdapter.RecyclerViewClickListener listener){
        this.mOrganisations = organisations;
        this.mContext = context;
        this.listener = listener;
    }
    @Override
    public OrganisationsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        cardView = inflater.inflate(R.layout.organisations_item
                , parent, false);
        ViewHolder viewHolder = new ViewHolder(cardView,listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrganisationsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.fillData(mOrganisations.get(position));
        holder.setOrganisation(mOrganisations.get(position));
    }

    @Override
    public int getItemCount() {
        return mOrganisations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
//        ImageView offreView;
        private RecyclerViewClickListener mListener;
        TextView organisationNameTextView;
        ImageView detailsImageView;
        RelativeLayout organisationDetailsRelativeLayout;
        Organisation organisation;
        TextView companyPhoneTextView;
        TextView companyEmailTextView;
//        ViewHolder(View v, RecyclerViewClickListener listener) {
//            super(v);
//            mListener = listener;
//            v.setOnClickListener(this);
//        }

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView,RecyclerViewClickListener listener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.

            super(itemView);
            //if(openedOrganisationDetailsposition ==)
            organisationNameTextView = itemView.findViewById(R.id.organisation_name);
            mListener = listener;
            organisationDetailsRelativeLayout = itemView.findViewById(R.id.organisation_details);
            detailsImageView = itemView.findViewById(R.id.details_imageView);
            companyPhoneTextView = itemView.findViewById(R.id.company_phone);
            companyEmailTextView = itemView.findViewById(R.id.company_email);
            organisationNameTextView.setOnClickListener(this);

            //offerImageView = itemView.findViewById(R.id.offre);
        }
        public void fillData(final Organisation organisation){
            organisationNameTextView.setText(organisation.getName());
            companyPhoneTextView.setText(organisation.getPhone());
            companyEmailTextView.setText(organisation.getEmail());

            if(itemToOpen == organisation.getId()){
                if(organisationDetailsRelativeLayout.getVisibility() == View.GONE)
                    expand(organisationDetailsRelativeLayout);
                else
                    collapse(organisationDetailsRelativeLayout);
            }
            else {
                if(organisationDetailsRelativeLayout.getVisibility() == View.VISIBLE)
                    collapse(organisationDetailsRelativeLayout);
            }


            detailsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(organisationDetailsRelativeLayout.getVisibility() == View.VISIBLE)
                        itemToOpen = 0;
                    else
                        itemToOpen = organisation.getId();
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, organisation);
        }

        public void setOrganisation(Organisation organisation) {
            this.organisation = organisation;
        }
    }
    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(350);
        v.startAnimation(a);
    }
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(350);
        v.startAnimation(a);
    }
    public interface RecyclerViewClickListener {

        void onClick(View view, Organisation organisation);
    }
}
