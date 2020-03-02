package com.example.experienceone.adapter.ticketadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.pojo.ticketdetails.Detail;

import java.util.List;

public class TicketDetailsAdapter extends RecyclerView.Adapter {
    private List<Detail> details;
    private String lyt, type;

    public TicketDetailsAdapter(List<Detail> details, String lyt, String type) {
        this.details = details;
        this.lyt = lyt;
        this.type = type;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (type.equalsIgnoreCase("Cab booking")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_details_travel_content, parent, false);
            return new TravelViewHolder(view);
        } else if (type.equalsIgnoreCase("Foreign Exchange")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_details_exchange_content, parent, false);
            return new ViewHolder(view);
        } else if (type.equalsIgnoreCase("Support")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_details_support_content, parent, false);
            return new SupportViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_details_recycler_content, parent, false);
            return new ViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (type) {
            case "Cab booking":
                ((TravelViewHolder) holder).bindView(position);
                break;
            case "Foreign Exchange":
                ((ViewHolder) holder).bindView(position);
                break;
            case "Support":
                ((SupportViewHolder) holder).bindView(position);
                break;
            default:
                ((ViewHolder) holder).bindView(position);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return details.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_service, tv_price, tv_quantity, tv_From_to;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_service = itemView.findViewById(R.id.tv_service);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_From_to = itemView.findViewById(R.id.tv_From_to);
        }

        void bindView(int position) {
            switch (lyt) {
                case "TBL-PO":
                    tv_quantity.setVisibility(View.GONE);
                    break;
                case "TBL-QO":
                    tv_price.setVisibility(View.GONE);
                    break;
                case "TBL-IO":
                    tv_price.setVisibility(View.GONE);
                    tv_quantity.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            if (type.equalsIgnoreCase("Foreign Exchange")) {
                if (position == 0) {
                    tv_From_to.setText("From");
                } else if (position == 1) {
                    tv_From_to.setText("To");
                }
            }
            tv_service.setText(details.get(position).getTitle());

            // holder.tv_service.setText(details.get(position).getTitle());
            if (!details.get(position).getQuantity().isEmpty()) {
                tv_quantity.setText("x" + details.get(position).getQuantity());
            } else {
                tv_quantity.setText("-");
            }
            if (!details.get(position).getPrice().isEmpty()) {
                tv_price.setText(details.get(position).getPrice() + details.get(position).getCurencySymbol());
            } else {
                tv_price.setText("-");
            }
        }
    }


    class TravelViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_service, tv_price, tv_quantity, tv_car_type, tv_car_model;

        TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_car_type = itemView.findViewById(R.id.tv_car_type);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_service = itemView.findViewById(R.id.tv_service);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_car_model = itemView.findViewById(R.id.tv_car_model);
        }

        void bindView(int position) {
            try {
                switch (lyt) {
                    case "TBL-PO":
                        tv_quantity.setVisibility(View.GONE);
                        break;
                    case "TBL-QO":
                        tv_price.setVisibility(View.GONE);
                        break;
                    case "TBL-IO":
                        tv_price.setVisibility(View.GONE);
                        tv_quantity.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                tv_service.setText(details.get(position).getTitle());

                tv_price.setText(details.get(position).getPrice());

                tv_quantity.setText("x" + details.get(position).getQuantity());

                String[] tag = details.get(position).getTags().split(",");

                tv_car_type.setText(tag[0]);
                tv_car_model.setText(tag[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    class SupportViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_tittle, tv_description;

        SupportViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_tittle = itemView.findViewById(R.id.tv_tittle);
        }

        void bindView(int position) {
            tv_tittle.setText(details.get(position).getTitle());
            tv_description.setText(details.get(position).getDescription());

        }
    }


}
