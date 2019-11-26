package com.example.trippe.ui.trips;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trippe.R;

public class TripsViewHolder extends RecyclerView.ViewHolder {

    private CardView tripsCardView;
    private ImageView destinationFlag;
    private TextView destination;
    private TextView fromDate;
    private TextView toDate;
    private TextView milesAwayFromHome;
    private TextView timeZone;
    private TextView currency;
    private TextView languages;


    public TripsViewHolder(View itemView) {
        super(itemView);
        setTripsCardView((CardView) itemView.findViewById(R.id.tripCard));
        setDestinationFlag((ImageView) itemView.findViewById(R.id.destinationFlag));
        setDestination((TextView) itemView.findViewById(R.id.tripDestination));
        setFromDate((TextView) itemView.findViewById(R.id.fromDate));
        setToDate((TextView) itemView.findViewById(R.id.toDate));
        setMilesAwayFromHome((TextView) itemView.findViewById(R.id.milesAwayFromHome));
        setTimeZone((TextView) itemView.findViewById(R.id.timeZone));
        setCurrency((TextView) itemView.findViewById(R.id.currency));
        setLanguages((TextView)itemView.findViewById(R.id.languages));
    }

    public CardView getTripsCardView() {
        return tripsCardView;
    }

    public void setTripsCardView(CardView tripsCardView) {
        this.tripsCardView = tripsCardView;
    }

    public ImageView getDestinationFlag() {
        return destinationFlag;
    }

    public void setDestinationFlag(ImageView destinationFlag) {
        this.destinationFlag = destinationFlag;
    }

    public TextView getDestination() {
        return destination;
    }

    public void setDestination(TextView destination) {
        this.destination = destination;
    }

    public TextView getFromDate() {
        return fromDate;
    }

    public void setFromDate(TextView fromDate) {
        this.fromDate = fromDate;
    }

    public TextView getToDate() {
        return toDate;
    }

    public void setToDate(TextView toDate) {
        this.toDate = toDate;
    }

    public TextView getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TextView timeZone) {
        this.timeZone = timeZone;
    }

    public TextView getCurrency() {
        return currency;
    }

    public void setCurrency(TextView currency) {
        this.currency = currency;
    }

    public TextView getLanguages() {
        return languages;
    }

    public void setLanguages(TextView languages) {
        this.languages = languages;
    }

    public TextView getMilesAwayFromHome() {
        return milesAwayFromHome;
    }

    public void setMilesAwayFromHome(TextView milesAwayFromHome) {
        this.milesAwayFromHome = milesAwayFromHome;
    }
}
