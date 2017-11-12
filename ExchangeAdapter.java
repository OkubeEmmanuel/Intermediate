package com.example.odeh.intermediate;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by odeh on 11/4/17.
 */

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ExchangeRatesViewHolder> {

    CustomItemClickListener mListener;

    // Provide a reference to the views for each data item
    public static class ExchangeRatesViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView currency_text_view;
        TextView rate_text_view;
        public ExchangeRatesViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            currency_text_view = itemView.findViewById(R.id.currency);
            rate_text_view = itemView.findViewById(R.id.rate);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    List<ExchangeRates> mExchanges;

    public ExchangeAdapter(List<ExchangeRates> exchanges, CustomItemClickListener listener) {
        mExchanges = exchanges;
        mListener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExchangeAdapter.ExchangeRatesViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exchange_card_item, parent, false);
        final ExchangeRatesViewHolder vh = new ExchangeRatesViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, vh.getAdapterPosition());
            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ExchangeRatesViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.currency_text_view.setText(mExchanges.get(position).getCurrency());
        double exRate = mExchanges.get(position).getExchangeRate();
        holder.rate_text_view.setText(Double.toString(exRate));
    }

    // Return the size of your ExchangeRates rates (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mExchanges.size();
    }
}

