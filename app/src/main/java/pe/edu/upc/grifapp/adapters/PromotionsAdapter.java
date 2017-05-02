package pe.edu.upc.grifapp.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import java.util.List;

import pe.edu.upc.grifapp.GrifApp;
import pe.edu.upc.grifapp.R;
import pe.edu.upc.grifapp.activities.PromotionActivity;
import pe.edu.upc.grifapp.models.Promotion;

/**
 * Created by Nic Marcelo on 2/05/2017.
 */

public class PromotionsAdapter extends
        RecyclerView.Adapter<PromotionsAdapter.ViewHolder> {

    private List<Promotion> promotions;

    @Override
    public PromotionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_promotion, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PromotionsAdapter.ViewHolder holder, final int position) {
        holder.pictureCardANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.pictureCardANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.pictureCardANImageView.setImageUrl(promotions.get(position).getPathImage());
        holder.nameCardTextView.setText(promotions.get(position).getName());
        holder.promotionCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrifApp.getInstance().setCurrentPromotion(promotions.get(position));
                v.getContext().startActivity(new Intent(
                        v.getContext(), PromotionActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    public List<Promotion> getPromotions(){
        return promotions;
    }
    public void setPromotions(List<Promotion> promotions){
        this.promotions = promotions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView promotionCardView;
        TextView nameCardTextView;
        ANImageView pictureCardANImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            nameCardTextView = (TextView) itemView.findViewById(R.id.nameCardTextView);
            pictureCardANImageView = (ANImageView) itemView.findViewById(R.id.pictureCardANImageView);
            promotionCardView = (CardView) itemView.findViewById(R.id.promotionCardView);
        }
    }
}
