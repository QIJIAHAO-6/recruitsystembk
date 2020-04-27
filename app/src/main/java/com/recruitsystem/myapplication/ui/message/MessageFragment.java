package com.recruitsystem.myapplication.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.recruitsystem.myapplication.R;

public class MessageFragment extends Fragment {


    static {

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, container, false);
        return root;
    }
    ListView listView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = getView().findViewById(R.id.lv);
        final SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.srl);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary),
                0xff000000
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
                initData();
            }
        });


        initData();
    }

    private void initData() {
     /*   listView.setAdapter(new BaseAdapter() {
            class ViewHolder {
                public View rootView;
                public ImageView bg;
                public TextView t1;
                public TextView t2;
                public TextView t3;
                public TextView q1;
                public TextView q2;

                public ViewHolder(View rootView) {
                    this.rootView = rootView;
                    this.bg = (ImageView) rootView.findViewById(R.id.bg);
                    this.t1 = (TextView) rootView.findViewById(R.id.t1);
                    this.t2 = (TextView) rootView.findViewById(R.id.t2);
                    this.t3 = (TextView) rootView.findViewById(R.id.t3);
                    this.q1 = (TextView) rootView.findViewById(R.id.q1);
                    this.q2 = (TextView) rootView.findViewById(R.id.q2);
                }

            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layoutcs, parent, false);
                final ViewHolder viewHolder = new ViewHolder(convertView);
                BHttp.getBG(new BHttp.CallBack() {
                    @Override
                    public void get(String s) {
                        Glide.with(getActivity()).load(s).into(viewHolder.bg);
                    }
                });
                final ZPBean ZPBean = list.get(position);
                viewHolder.t1.setText("" + ZPBean.q1 +"    "+ ZPBean.q2);
                viewHolder.t2.setText("" + ZPBean.q3);
                viewHolder.t3.setText("" + ZPBean.q4);
                //   viewHolder.t4.setText("" + ZPBean.q1);

                viewHolder.q2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     list.remove(position);
                     notifyDataSetChanged();
                     ToastUtils.show(getContext(),"已取消");
                    }
                });
                return convertView;
            }
        });*/
    }
}