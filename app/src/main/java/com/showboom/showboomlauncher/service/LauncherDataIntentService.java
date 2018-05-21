package com.showboom.showboomlauncher.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.showboom.showboomlauncher.App;
import com.showboom.showboomlauncher.api.ApiService;
import com.showboom.showboomlauncher.bean.LauncherDataBean;
import com.showboom.showboomlauncher.green_dao.LauncherInfo;
import com.showboom.showboomlauncher.green_dao.LauncherInfoDaoUtils;
import com.showboom.showboomlauncher.net.HttpObserver;
import com.showboom.showboomlauncher.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class LauncherDataIntentService extends IntentService {
    private LauncherInfoDaoUtils infoDaoUtils;

    public LauncherDataIntentService() {
        super("Launcher_data_service");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, LauncherDataIntentService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (infoDaoUtils == null) {
            infoDaoUtils = new LauncherInfoDaoUtils(this);
        }
        getScreenData();
    }

    private void getScreenData() {

        Map<String, Object> map = new HashMap<>();
        map.put("service", "getDeviceScreen");
        map.put("imei", StringUtil.getPhoneImei());
        //map.put("imei", "865468030001997");
        ApiService.getApiService().getScreenData(new HttpObserver<LauncherDataBean>() {
            @Override
            public void onFinished() {

            }

            @Override
            public void getDisposable(Disposable disposable) {

            }

            @Override
            public void onNext(LauncherDataBean value) {
                if (value != null) {
                    insertDatabase(value);
                }
            }
        }, map);
    }

    private void insertDatabase(LauncherDataBean dataBean) {
        LauncherInfo info = new LauncherInfo();
        String version = dataBean.getVersion();
        String oldVersion = Settings.System.getString(getContentResolver(), "launcher_current_version");
        if (oldVersion != null && version != null && !oldVersion.isEmpty() && oldVersion.equals(version)) {
            Log.i(App.TAG, "load_launcher_data_old_version=" + oldVersion);
            return;
        }
        info.setVersion(version);
        info.setTime(System.currentTimeMillis());
        List<LauncherDataBean.DataBean> list = dataBean.getData();
        if (list != null && list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String screenOrder = list.get(i).getScreen_order();
                switch (screenOrder) {
                    case "-2":
                        info.setPage_minus_two(new Gson().toJson(list.get(i)));
                        break;
                    case "-1":
                        info.setPage_minus_one(new Gson().toJson(list.get(i)));
                        break;
                    case "0":
                        info.setPage_zero(new Gson().toJson(list.get(i)));
                        break;
                    case "1":
                        info.setPage_one(new Gson().toJson(list.get(i)));
                        break;
                    case "2":
                        info.setPage_two(new Gson().toJson(list.get(i)));
                        break;
                    case "3":
                        info.setPage_three(new Gson().toJson(list.get(i)));
                        break;
                    case "4":
                        info.setPage_four(new Gson().toJson(list.get(i)));
                        break;
                    case "5":
                        info.setPage_five(new Gson().toJson(list.get(i)));
                        break;
                    case "6":
                        info.setPage_six(new Gson().toJson(list.get(i)));
                        break;
                    case "7":
                        info.setPage_seven(new Gson().toJson(list.get(i)));
                        break;
                    case "8":
                        info.setPage_eight(new Gson().toJson(list.get(i)));
                        break;
                    case "9":
                        info.setPage_nine(new Gson().toJson(list.get(i)));
                        break;
                    case "10":
                        info.setPage_ten(new Gson().toJson(list.get(i)));
                        break;
                }
            }
        }
        insertInfoDao(info, oldVersion);
    }

    private void insertInfoDao(LauncherInfo info, String oldVersion) {
        Log.i(App.TAG, "load_launcher_data_version=" + info.getVersion());
        for (int i = 0; i < 5; i++) {
            boolean isInsert = infoDaoUtils.insertLauncherInfoDao(info);
            if (isInsert) {
                Settings.System.putString(getContentResolver(), "launcher_current_version", info.getVersion());
                if (oldVersion != null && !oldVersion.isEmpty()) {
                    Settings.System.putString(getContentResolver(), "launcher_old_version", oldVersion);
                    List<LauncherInfo> list = infoDaoUtils.queryLauncherInfoDaoByQueryBuilder(info.getVersion(), oldVersion);
                    if (list != null && list.size() > 0) {
                        for (LauncherInfo infoBean : list) {
                            infoDaoUtils.deleteLauncherInfoDao(infoBean);
                        }
                    }
                }
                return;
            }
        }
    }
}
