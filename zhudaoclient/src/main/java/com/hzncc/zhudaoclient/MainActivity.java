package com.hzncc.zhudaoclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzncc.zhudaoclient.serverutil.JsonUtil;
import com.hzncc.zhudaoclient.serverutil.ServerInterface;
import com.hzncc.zhudaoclient.serverutil.SocketManager;
import com.hzncc.zhudaoclient.ui.GalleryActivity;
import com.hzncc.zhudaoclient.utils.FileUtil;
import com.hzncc.zhudaoclient.utils.WifiApAdmin;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    public static SocketManager socketManager;
    public static String device = "";
    private String ap_name = "zhudao_wifi";
    private String ap_password = "12345678";
    private WifiApAdmin wifiAp;
    private ListView deviceList;
    private TextView title, edit;
    private Button deleteAll, downloadAll;
    private CheckBox checkAll;
    private MyAdapter arrayAdapter;
    private List<String> devices;
    private PageType pageType = PageType.DEVICES;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SocketManager.RESULT_MSG_SUCCESS:
                    reply(msg.obj.toString());
                    break;
                case SocketManager.RESULT_FILE_SUCCESS:
                    arrayAdapter.notifyDataSetChanged();
                    break;
                case SocketManager.RESULT_ALL_FILE_SUCCESS:
                    arrayAdapter.notifyDataSetChanged();
                    break;
                case SocketManager.RESULT_FAILED:
                    break;
            }
        }
    };

    private void reply(String result) {
        switch (JsonUtil.getAction(result)) {
            case ServerInterface.CONNECT:
                boolean succ = JsonUtil.isSuccess(result);
                if (succ) {
                    showFiles(JsonUtil.getFilesByObject(result));
                } else {
                    Toast.makeText(MainActivity.this, succ ? "获取成功" : "获取失败，请检查热点连接", Toast.LENGTH_SHORT).show();
                }
                break;
            case ServerInterface.GET_IMAGE:
                break;
            case ServerInterface.GET_IMAGES:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        wifiAp = new WifiApAdmin(this);
//        wifiAp.startWifiAp(ap_name, ap_password);
        FileUtil.checkPermission(this);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        socketManager = new SocketManager(handler);
        deviceList = (ListView) findViewById(R.id.device_list);
        title = (TextView) findViewById(R.id.title);
        edit = (TextView) findViewById(R.id.edit);
        Button search = (Button) findViewById(R.id.search_btn);
        deleteAll = (Button) findViewById(R.id.delete_all);
        downloadAll = (Button) findViewById(R.id.download_all);
        checkAll = (CheckBox) findViewById(R.id.check_all);

        devices = WifiApAdmin.getConnectedIP();
        arrayAdapter = new MyAdapter(devices);
        deviceList.setAdapter(arrayAdapter);
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (pageType == PageType.DEVICES) {
                    device = devices.get(position);
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("from_ip", getWifiApIpAddress());
                    hashMap.put("type", String.valueOf(SocketManager.REPLY_NORMAL_MSG));
                    socketManager.sendMessage(JsonUtil.getRequestString(ServerInterface.CONNECT, hashMap), device, handler);
                } else if (pageType == PageType.FILE_LIST) {
                    String filename = devices.get(position);
                    if (new File(filename).exists()) {
                        //查看图片
                        Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                        intent.putExtra("curr", position);
                        intent.putStringArrayListExtra("list", (ArrayList<String>) devices);
                        startActivity(intent);
                    } else {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("file_name", devices.get(position));
                        hashMap.put("type", String.valueOf(SocketManager.REPLY_NORMAL_MSG));
                        socketManager.sendMessage(JsonUtil.getRequestString(ServerInterface.GET_IMAGE, hashMap), device, handler);
                    }
                } else {
                    arrayAdapter.changeCheck(position);
                }
            }
        });

        edit.setOnClickListener(this);
        search.setOnClickListener(this);
        deleteAll.setOnClickListener(this);
        downloadAll.setOnClickListener(this);
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arrayAdapter.checkAll();
                } else {
                    arrayAdapter.clearChecked();
                }
            }
        });
    }

    public String getWifiApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            Log.d("MainActivity", inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("MainActivity", ex.toString());
        }
        return null;
    }

    private void showFiles(List<String> list) {
        pageType = PageType.FILE_LIST;
        edit.setVisibility(View.VISIBLE);
        refreshListView(list);
    }

    private void showEditFiles() {
        pageType = PageType.EDIT_FILE_LIST;
        deleteAll.setVisibility(View.VISIBLE);
        downloadAll.setVisibility(View.VISIBLE);
        checkAll.setVisibility(View.VISIBLE);
        arrayAdapter.notifyDataSetChanged();
    }

    private void hideEditFiles() {
        pageType = PageType.FILE_LIST;
        deleteAll.setVisibility(View.GONE);
        downloadAll.setVisibility(View.GONE);
        checkAll.setVisibility(View.GONE);
        arrayAdapter.notifyDataSetChanged();
    }


    private void showDevices(List<String> list) {
        pageType = PageType.DEVICES;
        edit.setVisibility(View.GONE);
        refreshListView(list);
    }

    private void refreshListView(List<String> list) {
        devices.clear();
        devices.addAll(list);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        socketManager.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                showDevices(WifiApAdmin.getConnectedIP());
                break;
            case R.id.edit:
                if (pageType == PageType.FILE_LIST) {
                    showEditFiles();
                    arrayAdapter.clearChecked();
                    edit.setText("完成");
                } else {
                    hideEditFiles();
                    edit.setText("编辑");
                }
                break;
            case R.id.delete_all:
                arrayAdapter.deleteCheckedFiles();
                break;
            case R.id.download_all:
                List<String> checkedFiles = arrayAdapter.getCheckedFiles();
                socketManager.sendMessage(JsonUtil.getRequestMulFileString(ServerInterface.GET_IMAGES, checkedFiles), device, handler);
                arrayAdapter.clearChecked();
                break;
        }
    }

    private enum PageType {
        DEVICES, FILE_LIST, EDIT_FILE_LIST;
    }

    private class MyAdapter extends BaseAdapter {
        private List<String> data;
        private List<Integer> check;


        MyAdapter(List<String> list) {
            this.data = list;
            check = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public List<String> getCheckedFiles() {
            List<String> checked = new ArrayList<>();
            for (int i = 0; i < check.size(); i++) {
                checked.add(data.get(check.get(i)));
            }
            return checked;
        }

        public void deleteCheckedFiles() {
            for (int i = 0; i < check.size(); i++) {
                File file = new File(data.get(check.get(i)));
                if (file.exists()) {
                    file.delete();
                }
            }
            arrayAdapter.clearChecked();
            notifyDataSetChanged();
        }

        public void clearChecked() {
            check.clear();
            notifyDataSetChanged();
        }

        public void changeCheck(Integer integer) {
            if (check.contains(integer)) {
                unCheckInt(integer);
            } else {
                checkInt(integer);
            }
            notifyDataSetChanged();
        }

        public void checkInt(Integer integer) {
            if (!check.contains(integer)) {
                check.add(integer);
            }
            notifyDataSetChanged();
        }

        public void unCheckInt(Integer integer) {
            if (check.contains(integer)) {
                check.remove(integer);
            }
            notifyDataSetChanged();
        }

        public void checkAll() {
            clearChecked();
            for (int i = 0; i < getCount(); i++) {
                checkInt(i);
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_list_item, null, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.item_title);
                viewHolder.preview = (ImageView) convertView.findViewById(R.id.item_preview);
                viewHolder.check = (CheckBox) convertView.findViewById(R.id.item_check);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String filename = getItem(position);
            if (pageType == PageType.DEVICES) {
                viewHolder.title.setText(filename);
                viewHolder.preview.setVisibility(View.GONE);
            } else {
                String name = filename.substring(filename.lastIndexOf("/") + 1);
                viewHolder.title.setText(name);
                if (new File(filename).exists()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inDensity = 4;
                    Bitmap bitmap = BitmapFactory.decodeFile(filename, options);
                    if (null != bitmap) {
                        viewHolder.preview.setVisibility(View.VISIBLE);
                        viewHolder.preview.setImageBitmap(bitmap);
                    } else {
                        viewHolder.preview.setImageResource(R.mipmap.ic_launcher);
                    }
                } else {
                    viewHolder.preview.setVisibility(View.GONE);
                }
            }
            viewHolder.check.setVisibility(pageType == PageType.EDIT_FILE_LIST ? View.VISIBLE : View.GONE);
            viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkInt(position);
                    } else {
                        unCheckInt(position);
                    }
                }
            });
            viewHolder.check.setChecked(check.contains(position));
            return convertView;
        }

        private class ViewHolder {
            TextView title;
            ImageView preview;
            CheckBox check;
        }
    }
}
