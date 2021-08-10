package com.example.miniapptest.screens;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.miniapptest.screens.interfaces.IFragmentStart;
import com.example.miniapptest.R;

public class StartFragment extends Fragment {
    private IFragmentStart iStartFragment;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentStart) {
            iStartFragment = (IFragmentStart) context;
        } else
            throw new RuntimeException(context.toString() + "must implement IFragmentDataListener");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.buttonStartTest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iStartFragment.startTest();
            }
        });
    }
}
