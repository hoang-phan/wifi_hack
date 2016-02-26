package vn.hoangphan.wifihack.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.hoangphan.wifihack.R;
import vn.hoangphan.wifihack.models.Wifi;

/**
 * Created by ea on 2/26/16.
 */
public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.WifiHolder> {
    private List<Wifi> mWifiList = new ArrayList<>();
    private OnClickListener mOnClickListener;

    @Override
    public WifiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi, parent, false);
        return new WifiHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiHolder holder, int position) {
        final Wifi wifi = mWifiList.get(position);
        holder.mSsidTv.setText(wifi.getSSID());
        holder.mConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(wifi);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWifiList.size();
    }

    public void addWifi(Wifi wifi) {
        mWifiList.add(wifi);
    }

    public void reset() {
        mWifiList.clear();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public static class WifiHolder extends RecyclerView.ViewHolder {
        private TextView mSsidTv;
        private Button mConnectBtn;

        public WifiHolder(View itemView) {
            super(itemView);
            mSsidTv = (TextView) itemView.findViewById(R.id.tv_ssid);
            mConnectBtn = (Button) itemView.findViewById(R.id.btn_connect);
        }
    }
}
