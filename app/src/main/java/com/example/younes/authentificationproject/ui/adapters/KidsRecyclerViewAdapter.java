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
        import com.example.younes.authentificationproject.models.Kids;

        import java.util.List;

/**
 * Created by younes on 8/20/2018.
 */

public class KidsRecyclerViewAdapter extends RecyclerView.Adapter<KidsRecyclerViewAdapter.ViewHolder> {
    private View cardView;
    List<Kids> mKids;
    Context mContext;
    private static int itemToOpen;
    public KidsRecyclerViewAdapter(List<Kids> kids, Context context){
        this.mKids = kids;
        this.mContext = context;
    }
    @Override
    public KidsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        cardView = inflater.inflate(R.layout.kid_item
                , parent, false);
        ViewHolder viewHolder = new ViewHolder(cardView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(KidsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.fillData(mKids.get(position));
    }

    @Override
    public int getItemCount() {
        return mKids.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
//        ImageView offreView;
        TextView organisationNameTextView;
        ImageView detailsImageView;
        RelativeLayout kidDetailsRelativeLayout;
        TextView kidParentEmailTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.

            super(itemView);
            organisationNameTextView = itemView.findViewById(R.id.organisation_name);
            detailsImageView = itemView.findViewById(R.id.details_imageView);
            kidDetailsRelativeLayout = itemView.findViewById(R.id.kid_details);
            kidParentEmailTextView = itemView.findViewById(R.id.company_email);


            //offerImageView = itemView.findViewById(R.id.offre);
        }
        public void fillData(final Kids kid){
            organisationNameTextView.setText(kid.getFirstName()+" - "+kid.getLastName());
            kidParentEmailTextView.setText(kid.getParentEmail());
            if(itemToOpen == kid.getId()){
                if(kidDetailsRelativeLayout.getVisibility() == View.GONE)
                    expand(kidDetailsRelativeLayout);
                else
                    collapse(kidDetailsRelativeLayout);
            }
            else {
                if(kidDetailsRelativeLayout.getVisibility() == View.VISIBLE)
                    collapse(kidDetailsRelativeLayout);
            }
            detailsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(kidDetailsRelativeLayout.getVisibility() == View.VISIBLE)
                        itemToOpen = 0;
                    else
                        itemToOpen = kid.getId();
                    notifyDataSetChanged();
                }
            });
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
}

