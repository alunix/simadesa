package com.agunwgn.simadesa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agunwgn.simadesa.adapter.StrukturAdapter;
import com.agunwgn.simadesa.api.ApiEndPoint;
import com.agunwgn.simadesa.api.ApiService;
import com.agunwgn.simadesa.model.ResponseModel;
import com.agunwgn.simadesa.model.StrukturModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StrukturFragment extends Fragment {

    private StrukturAdapter viewAdapter;
    private List<StrukturModel> mItems = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_struktur,null);

        recyclerView = view.findViewById(R.id.recyclerStruktur);
        progressBar = view.findViewById(R.id.progress_bar);

        viewAdapter = new StrukturAdapter(getContext(), mItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);

        loadDataStruktur();

        return view;
    }

    private void loadDataStruktur() {

        ApiService api = ApiEndPoint.getClient().create(ApiService.class);
        Call<ResponseModel> call = api.getStruktur();
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                String statusCode = response.body().getStatusCode();
                progressBar.setVisibility(View.GONE);
                if (statusCode.equals("200")) {
                    mItems = response.body().getsResult();
                    viewAdapter = new StrukturAdapter(getContext(), mItems);
                    recyclerView.setAdapter(viewAdapter);
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), "Oops, Tidak Ada Koneksi Internet!! ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
