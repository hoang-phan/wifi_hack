package vn.hoangphan.wifihack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import vn.hoangphan.wifihack.adapters.OnClickListener;
import vn.hoangphan.wifihack.adapters.WifiAdapter;
import vn.hoangphan.wifihack.models.Wifi;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private WifiManager mWifiManager;
    private List<ScanResult> mResults;
    private WifiAdapter mWifiAdapter;
    private BroadcastReceiver mReceiver;
    private LinearLayoutManager mLayoutManager;

    private FloatingActionButton mRefeshBtn;
    private RecyclerView mWifiRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        bindComponents();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_refresh:
                populateWifiResults();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void initComponents() {
        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        mWifiAdapter = new WifiAdapter();
        mLayoutManager = new LinearLayoutManager(this);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mResults = mWifiManager.getScanResults();
            }
        };

        mWifiRv = (RecyclerView) findViewById(R.id.rv_wifi);
        mRefeshBtn = (FloatingActionButton) findViewById(R.id.fab_refresh);
    }

    private void bindComponents() {
        mWifiAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Wifi wifi) {
                for (int i = 0; i < 10; i++) {
                    addWifiConfig(wifi, "0944924186");
                }
            }
        });
        mWifiRv.setAdapter(mWifiAdapter);
        mWifiRv.setLayoutManager(mLayoutManager);
        mRefeshBtn.setOnClickListener(this);
        registerReceiver(mReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    private void populateWifiResults() {
        mWifiAdapter.reset();
        mWifiManager.startScan();

        if (mResults != null) {
            for (ScanResult result : mResults) {
                mWifiAdapter.addWifi(new Wifi(result.SSID, result.capabilities));
            }
        }

        mWifiAdapter.notifyDataSetChanged();
    }

    private void addWifiConfig(Wifi wifi, String password) {
        WifiConfiguration conf = new WifiConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            conf.SSID = wifi.getSSID();
        } else {
            conf.SSID = "\"" + wifi.getSSID() + "\"";
        }
        if (wifi.getCapabilities().contains("WEP")) {
            conf.wepKeys[0] = password;
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (wifi.getCapabilities().contains("NONE")) {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else if (wifi.getCapabilities().contains("WPA")) {
            conf.preSharedKey = "\"" + password + "\"";
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        }
        if (wifi.getCapabilities().contains("TKIP")) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
        } else if (wifi.getCapabilities().contains("AES")) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
        } else if (wifi.getCapabilities().contains("WEP")) {
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (wifi.getCapabilities().contains("NONE")) {
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);
        }

        int newNetworkId = mWifiManager.addNetwork(conf);
        mWifiManager.enableNetwork(newNetworkId, true);
        mWifiManager.saveConfiguration();
        mWifiManager.setWifiEnabled(true);
    }
}
