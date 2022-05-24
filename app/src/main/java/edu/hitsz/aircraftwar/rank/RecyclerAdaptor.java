package edu.hitsz.aircraftwar.rank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import edu.hitsz.aircraftwar.R;
import edu.hitsz.aircraftwar.databinding.RankItemBinding;
import edu.hitsz.aircraftwar.rank.repository.dao.RankEntry;

public class RecyclerAdaptor extends RecyclerView.Adapter<RecyclerAdaptor.ViewHolder> {
    private final List<RankEntry> entries = new ArrayList<>();
    private View.OnClickListener mListener;

    public void update(List<RankEntry> e){
        entries.clear();
        entries.addAll(e);
        entries.sort(Comparator.comparingInt(RankEntry::getScore).reversed());
        notifyDataSetChanged();
    }

    public RankEntry getRankEntryAt(int index){
        return entries.get(index);
    }

    @Inject
    public RecyclerAdaptor(){

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                RankItemBinding.
                        inflate(LayoutInflater.from(parent.getContext()))
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getUserName().setText(entries.get(position).username + " ");
        holder.getScore().setText(String.format("%d", entries.get(position).getScore()) + " ");
        Date date = new Date(entries.get(position).timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        holder.getTime().setText(format.format(date) + " ");
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView score;
        private final TextView time;

        public ViewHolder(RankItemBinding binding) {
            super(binding.getRoot());
            userName = binding.userName;
            score = binding.score;
            time = binding.time;
            binding.deleteCheckBox.setChecked(false);
        }

        public TextView getUserName() {
            return userName;
        }

        public TextView getScore() {
            return score;
        }

        public TextView getTime() {
            return time;
        }
    }

}
