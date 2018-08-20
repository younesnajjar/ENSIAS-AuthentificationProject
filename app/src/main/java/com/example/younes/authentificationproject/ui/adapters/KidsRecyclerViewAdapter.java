package com.example.younes.authentificationproject.ui.adapters;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.example.younes.authentificationproject.R;
        import com.example.younes.authentificationproject.models.Kids;
        import com.example.younes.authentificationproject.models.Organisation;

        import java.util.List;

/**
 * Created by younes on 8/20/2018.
 */

public class KidsRecyclerViewAdapter extends RecyclerView.Adapter<KidsRecyclerViewAdapter.ViewHolder> {
    private View cardView;
    List<Kids> mKids;
    Context mContext;
    public KidsRecyclerViewAdapter(List<Kids> kids, Context context){
        this.mKids = kids;
        this.mContext = context;
    }
    @Override
    public KidsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        cardView = inflater.inflate(R.layout.list_item
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

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.

            super(itemView);
            organisationNameTextView = itemView.findViewById(R.id.organisation_name);

            //offerImageView = itemView.findViewById(R.id.offre);
        }
        public void fillData(Kids kid){
            organisationNameTextView.setText(kid.getFirstName()+" - "+kid.getLastName());
        }
    }
}

