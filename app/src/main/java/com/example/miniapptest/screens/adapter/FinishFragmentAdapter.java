package com.example.miniapptest.screens.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.recyclerview.widget.RecyclerView;
import com.example.miniapptest.screens.question.Question;
import com.example.miniapptest.R;
import java.util.List;

public class FinishFragmentAdapter extends RecyclerView.Adapter<FinishFragmentAdapter.FinishFragmentViewHolder> {
    private final List<Question> list;
    private final int TYPE_TRUE = 0;
    private final int TYPE_FALSE = 1;
    private final Function<Integer, Void> mListener;

    public FinishFragmentAdapter(List<Question> list, Function<Integer, Void> clickListener) {
        this.list = list;
        mListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isSolvedCorrectly()) return TYPE_TRUE;
        else return TYPE_FALSE;
    }

    @NonNull
    @Override
    public FinishFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_item_recycler_view, parent, false);
        return new FinishFragmentViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FinishFragmentAdapter.FinishFragmentViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                holder.textView.setText(String.valueOf(position));
                holder.textView.setBackground(holder.itemView.getResources().getDrawable(R.drawable.rounded_corner_true));
                break;
            case 1:
                holder.textView.setText(String.valueOf(position));
                holder.textView.setBackground(holder.itemView.getResources().getDrawable(R.drawable.rounded_corner_false));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FinishFragmentViewHolder extends RecyclerView.ViewHolder {
        FinishFragmentAdapter adapter;
        TextView textView;

        public FinishFragmentViewHolder(@NonNull View itemView, FinishFragmentAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            textView = itemView.findViewById(R.id.textViewItemQuestionFromFinishFragment);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.apply(FinishFragmentViewHolder.this.getLayoutPosition());
                }
            });
        }
    }
}
