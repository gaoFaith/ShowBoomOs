package com.showboom.showboomlauncher.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.showboom.showboomlauncher.R;
import com.showboom.showboomlauncher.widget.ProgressButton;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class AppListAdapter extends BaseAdapter {
    public static final String TAG = "AppListAdapter";

    public static final int Type_App = 0;
    public static final int Type_Advert = 1;

    public static final int Advert_Open_Link = 0;
    public static final int Advert_Open_App = 1;

    public static final int Btn_State_NotInstall = 0;
    public static final int Btn_State_Downloading = 1;
    public static final int Btn_State_Pause = 2;
    public static final int Btn_State_Installing = 3;
    public static final int Btn_State_Failure = 4;

    public static final int MSG_UPDATE_PROGRESS = 1;
    public static final int MSG_INSATLL_APK = 2;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<AppListItem> mAppList;

    public static final String INSTALL_MAP_FILE = "install_map_file";
    private HashMap<String, HolderRecord> mAllHolder = new HashMap<>();
    private HashMap<String, String> mInstallMaps = new HashMap<>(); // K, packageName; Value, value1-value2, downloadId(Long)-state(Integer)
    private SharedPreferences mSP_Install;
    private HashMap<Long, HolderRecord> mHolderMaps = new HashMap<>();
    DownloadManager downloadManager;

    public AppListAdapter(Context context, List<AppListItem> appList) {
        mContext = context;
        this.mAppList = appList;
        mInflater = LayoutInflater.from(context);

        mSP_Install = mContext.getSharedPreferences(INSTALL_MAP_FILE, 0);
        //mInstallMaps = (HashMap<String, String>) mSP_Install.getAll();

        downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);

        mHandler = new MainHandler(mContext);
    }

    private static class MainHandler extends Handler {
        private final WeakReference<Context> contextWeakReference;

        public MainHandler(Context context) {
            contextWeakReference = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            Context context = contextWeakReference.get();
            if (context != null) {
                switch (msg.what) {
                    case MSG_UPDATE_PROGRESS:
                        HolderRecord holderRecord = (HolderRecord) msg.obj;
                        holderRecord.viewHolder.btnInstall.setProgress(holderRecord.progress);
                        break;
                    case MSG_INSATLL_APK:
                        HolderRecord holder = (HolderRecord) msg.obj;
                        String fileName = holder.fileName;
                        String pkgName = holder.pkgName;
                        holder.viewHolder.btnInstall.setText(R.string.app_btn_installing);
                        Log.d(TAG, "installApk file="+fileName+" brand="+ Build.BRAND);
                        if(fileName != null) {
                            if(Build.BRAND.equals("showboom")) {
                                Intent intent = new Intent("com.heimilink.action.install_or_delete");
                                intent.putExtra("event", "install");
                                intent.putExtra("pkgName", pkgName);
                                intent.putExtra("path", fileName);
                                context.sendBroadcast(intent);
                            } else {
                                File apkfile = new File(fileName);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void startTimer() {
        if(progressTimerTask == null) {
            progressTimerTask = new ProgressTimerTask();
        }
        if(progressTimer == null) {
            progressTimer = new Timer();
            progressTimer.schedule(progressTimerTask, 2000, 2000);
        }
    }

    public void finishTimer() {
        if(progressTimerTask != null) {
            progressTimerTask.cancel();
            progressTimerTask = null;
        }
        if(progressTimer != null) {
            progressTimer.cancel();
            progressTimer = null;
        }
    }

    private MainHandler mHandler = null;
    private Timer progressTimer = null;
    private ProgressTimerTask progressTimerTask = null;
    private class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            int size = mHolderMaps.size();
            Log.d(TAG, "ProgressTimerTask run HolderMaps size="+size);

            if(size > 0) {
                List<Long> idList = new ArrayList<>();
                for(Long key : mHolderMaps.keySet()) {
                    long id = key;
                    HolderRecord holderRecord = mHolderMaps.get(key);
                    Log.d(TAG, "ProgressTimerTask pkgName="+holderRecord.pkgName+" progress="+holderRecord.progress);
                    if(holderRecord.progress < 100) {
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(id);
                        Cursor cursor = downloadManager.query(query);
                        Log.d(TAG, "ProgressTimerTask cursor getCount="+cursor.getCount());
                        if(cursor.moveToFirst()) {
                            long downId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                            long bytes_downloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            long bytes_total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            String fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                            long progress =  bytes_downloaded *100 / bytes_total;
                            Log.d(TAG, "ProgressTimerTask id="+downId+" bytes_downloaded="+bytes_downloaded+" bytes_total="+bytes_total+" progress="+progress);

                            holderRecord.progress = (int)progress;
                            holderRecord.fileName = fileName;

                            if(progress > 7) {
                                Message msg = mHandler.obtainMessage();
                                msg.what = MSG_UPDATE_PROGRESS;
                                msg.obj = holderRecord;
                                msg.sendToTarget();
                            }
                            if(progress == 100) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                Message msg = mHandler.obtainMessage();
                                msg.what = MSG_INSATLL_APK;
                                msg.obj = holderRecord;
                                msg.sendToTarget();

                                idList.add(downId);
                            }
                        }
                        cursor.close();
                    }
                }
                int len = idList.size();
                if(len > 0) {
                    for(int i=0; i<len; i++) {
                        mHolderMaps.remove(idList.get(i));
                    }
                }
            } else {
                finishTimer();
            }
        }
    }

    @Override
    public int getCount() {
        if(mAppList != null) {
            return mAppList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mAppList != null) {
            return mAppList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(mAppList != null) {
            final ViewHolderApp viewHolderApp;
            final ViewHolderAdvert viewHolderAdvert;
            final AppListItem ListItem = mAppList.get(position);
            int type = ListItem.getType();
            if (convertView == null) {
                Log.d(TAG, "postion="+position+" type="+type+" convertView is null");
                switch (type) {
                    case AppListAdapter.Type_App:
                        viewHolderApp = new ViewHolderApp();
                        convertView = mInflater.inflate(R.layout.app_item_app_layout, parent, false);
                        viewHolderApp.imageViewIcon = convertView.findViewById(R.id.app_icon);
                        viewHolderApp.textViewTitle = convertView.findViewById(R.id.app_title);
                        viewHolderApp.textViewDesc = convertView.findViewById(R.id.app_desc);
                        viewHolderApp.textViewFavorites = convertView.findViewById(R.id.app_favorites);
                        viewHolderApp.textViewSize = convertView.findViewById(R.id.app_size);
                        viewHolderApp.btnOpen = convertView.findViewById(R.id.btn_open);
                        viewHolderApp.btnInstall = convertView.findViewById(R.id.btn_install);

                        viewHolderApp.textViewTitle.setText(ListItem.getApp().getTitle());
                        viewHolderApp.textViewDesc.setText(ListItem.getApp().getDesc());
                        viewHolderApp.textViewFavorites.setText(ListItem.getApp().getFavorites());
                        viewHolderApp.textViewSize.setText(ListItem.getApp().getSize());

                        loadAppIcon(mContext, ListItem.getApp().getIconUrl(), viewHolderApp.imageViewIcon);

                        final String pkgName = ListItem.getApp().getPkgName();
                        mAllHolder.put(pkgName, new HolderRecord(viewHolderApp, pkgName));

                        String values = mInstallMaps.get(pkgName);
                        InstallData installData = new InstallData(values);
                        long downId = installData.id;
                        int btnState = installData.state;

                        if(!pkgInstalled(mContext, pkgName)) {
                            //app未安装
                            viewHolderApp.btnInstall.setVisibility(View.VISIBLE);
                            viewHolderApp.btnOpen.setVisibility(View.GONE);

                            /*if(downId > -1 && btnState == Btn_State_Downloading) {
                                mHolderMaps.put(downId, new HolderRecord(viewHolderApp, pkgName));
                            }*/

                            /*if(downId > -1 && btnState == Btn_State_Installing) {
                                DownloadManager.Query query = new DownloadManager.Query();
                                query.setFilterById(downId);
                                Cursor cursor = downloadManager.query(query);
                                if(cursor.moveToFirst()) {
                                    String fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                                    if(status == DownloadManager.STATUS_SUCCESSFUL) {
                                        HolderRecord holderRecord = new HolderRecord(viewHolderApp, pkgName);
                                        holderRecord.fileName = fileName;

                                        Message msg = mHandler.obtainMessage();
                                        msg.what = MSG_INSATLL_APK;
                                        msg.obj = holderRecord;
                                        msg.sendToTarget();
                                    }
                                }
                                cursor.close();
                            }*/

                            if(btnState == Btn_State_NotInstall) {
                                viewHolderApp.btnInstall.setText(R.string.app_btn_install);
                            } else if(btnState == Btn_State_Downloading) {
                                viewHolderApp.btnInstall.setText(R.string.app_btn_downloading);
                            } else if(btnState == Btn_State_Pause) {
                                viewHolderApp.btnInstall.setText(R.string.app_btn_pause);
                            } else if(btnState == Btn_State_Installing) {
                                viewHolderApp.btnInstall.setText(R.string.app_btn_installing);
                            } else if(btnState == Btn_State_Failure) {
                                viewHolderApp.btnInstall.setText(R.string.app_btn_continue);
                            }

                            if(btnState != Btn_State_NotInstall) {
                                viewHolderApp.btnInstall.setTextColor(mContext.getResources().getColor(R.color.normal_text));
                            }

                        } else {
                            viewHolderApp.btnOpen.setVisibility(View.VISIBLE);
                            viewHolderApp.btnInstall.setVisibility(View.GONE);

                            //app已安装，清理mInstallMaps和mAppHolderMaps
                            if(downId > -1) {
                                downloadManager.remove(downId);
                                mInstallMaps.remove(pkgName);

                                SharedPreferences.Editor spInstallEditor = mSP_Install.edit();
                                spInstallEditor.remove(pkgName);
                                spInstallEditor.apply();
                                spInstallEditor.commit();
                            }
                        }

                        viewHolderApp.btnOpen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startApp(mContext, pkgName);
                            }
                        });

                        viewHolderApp.btnInstall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String values = mInstallMaps.get(pkgName);
                                InstallData installData = new InstallData(values);
                                long downId = installData.id;
                                int btnState = installData.state;

                                if(btnState == Btn_State_NotInstall) {
                                    viewHolderApp.btnInstall.setTextColor(mContext.getResources().getColor(R.color.normal_text));

                                    if(downId > -1) {
                                        DownloadManager.Query query = new DownloadManager.Query();
                                        query.setFilterById(downId);
                                        Cursor cursor = downloadManager.query(query);
                                        if(cursor.moveToFirst()) {
                                            String fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                                            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                                            if(status == DownloadManager.STATUS_SUCCESSFUL) {
                                                HolderRecord holderRecord = new HolderRecord(viewHolderApp, pkgName);
                                                holderRecord.fileName = fileName;

                                                Message msg = mHandler.obtainMessage();
                                                msg.what = MSG_INSATLL_APK;
                                                msg.obj = holderRecord;
                                                msg.sendToTarget();

                                                cursor.close();
                                                return;
                                            }
                                        }
                                        cursor.close();
                                    }

                                    //点击安装按钮，开始下载
                                    viewHolderApp.btnInstall.setText(R.string.app_btn_downloading);

                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(ListItem.getApp().getPkgUrl()));
                                    //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                                    long id = downloadManager.enqueue(request);
                                    Log.d(TAG, "downloadManager id="+id);

                                    mHolderMaps.put(id, new HolderRecord(viewHolderApp, pkgName));
                                    startTimer();

                                    InstallData data = new InstallData(id, Btn_State_Downloading);
                                    String syncStr = data.getSyncStr();
                                    mInstallMaps.put(pkgName, syncStr);

                                    SharedPreferences.Editor spInstallEditor = mSP_Install.edit();
                                    spInstallEditor.putString(pkgName, syncStr);
                                    spInstallEditor.apply();
                                    spInstallEditor.commit();
                                }
                            }
                        });

                        convertView.setTag(viewHolderApp);
                        break;
                    case AppListAdapter.Type_Advert:
                        viewHolderAdvert = new ViewHolderAdvert();
                        convertView = mInflater.inflate(R.layout.app_item_advert_layout, parent, false);
                        viewHolderAdvert.imageViewAdvert1 = convertView.findViewById(R.id.advert1);
                        viewHolderAdvert.imageViewAdvert2 = convertView.findViewById(R.id.advert2);

                        loadAdvertImage(mContext, ListItem.getAdvert1().getImageUrl(), viewHolderAdvert.imageViewAdvert1);
                        loadAdvertImage(mContext, ListItem.getAdvert2().getImageUrl(), viewHolderAdvert.imageViewAdvert2);

                        convertView.setTag(viewHolderAdvert);
                        break;
                    default:
                        break;
                }
            } else {
                Log.d(TAG, "postion="+position+" type="+type+" convertView not null");
                switch (type) {
                    case AppListAdapter.Type_App:
                        viewHolderApp = (ViewHolderApp)convertView.getTag();
                        break;
                    case AppListAdapter.Type_Advert:
                        viewHolderAdvert = (ViewHolderAdvert)convertView.getTag();
                        break;
                    default:
                        break;
                }
            }
            return convertView;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        List<AppListItem> appList = mAppList;
        if(appList != null) {
            AppListItem appItem = appList.get(position);
            return appItem.getType();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void downloadComplete(long download_id) {
        Log.d(TAG, "downloadComplete id="+download_id);
        HolderRecord holderRecord = mHolderMaps.get(download_id);
        if(holderRecord != null) {
            InstallData data = new InstallData(download_id, Btn_State_Installing);
            String syncStr = data.getSyncStr();
            mInstallMaps.put(holderRecord.pkgName, syncStr);

            SharedPreferences.Editor spInstallEditor = mSP_Install.edit();
            spInstallEditor.putString(holderRecord.pkgName, syncStr);
            spInstallEditor.apply();
            spInstallEditor.commit();
        }
    }

    public void installComplete(String pkgName) {
        Log.d(TAG, "installComplete pkgName="+pkgName);
        if(pkgName != null) {
            HolderRecord holderRecord = mAllHolder.get(pkgName);
            if(holderRecord != null) {
                holderRecord.viewHolder.btnInstall.setVisibility(View.GONE);
                holderRecord.viewHolder.btnOpen.setVisibility(View.VISIBLE);

                mInstallMaps.remove(pkgName);

                SharedPreferences.Editor spInstallEditor = mSP_Install.edit();
                spInstallEditor.remove(pkgName);
                spInstallEditor.apply();
                spInstallEditor.commit();
            }
        }
    }

    public void removeComplete(String pkgName) {
        Log.d(TAG, "removeComplete pkgName="+pkgName);
        if(pkgName != null) {
            HolderRecord holderRecord = mAllHolder.get(pkgName);
            if(holderRecord != null) {
                holderRecord.viewHolder.btnInstall.setVisibility(View.VISIBLE);
                holderRecord.viewHolder.btnOpen.setVisibility(View.GONE);

                holderRecord.viewHolder.btnInstall.setProgress(0);
                holderRecord.viewHolder.btnInstall.setText(R.string.app_btn_install);
            }
        }
    }

    class ViewHolderApp {
        ImageView imageViewIcon;
        TextView textViewTitle;
        TextView textViewDesc;
        TextView textViewFavorites;
        TextView textViewSize;
        Button btnOpen;
        ProgressButton btnInstall;
    }

    class ViewHolderAdvert {
        ImageView imageViewAdvert1;
        ImageView imageViewAdvert2;
    }

    class HolderRecord {
        public ViewHolderApp viewHolder;
        public String pkgName;
        public int progress = 0;
        public String fileName = null;

        public HolderRecord(ViewHolderApp viewHolder, String pkgName) {
            this.viewHolder = viewHolder;
            this.pkgName = pkgName;
        }
    }

    class InstallData {
        public long id = -1;
        public int state = 0;

        public InstallData(String syncStr) {
            if(syncStr != null) {
                String[] datas = syncStr.split("-");
                if(datas.length == 2) {
                    id = Long.parseLong(datas[0]);
                    state = Integer.parseInt(datas[1]);
                }
            }
        }

        public InstallData(long id, int state) {
            this.id = id;
            this.state = state;
        }

        public String getSyncStr() {
            String syncStr = Long.toString(id)+"-"+Integer.toString(state);
            return syncStr;
        }
    }

    public boolean pkgInstalled(Context context, String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }

        android.content.pm.ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(pkgName, 0);
            return info != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void startApp(Context context, String pkgName) {
        if(context == null) {
            Log.d(TAG, "startApp context == null");
            return;
        }

        try{
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            context.startActivity(intent);
        }catch(Exception e){
            Log.d(TAG, "startApp can't start pkgName: "+pkgName);
        }
    }

    public void loadAppIcon(Context context, String url, ImageView view) {
        if(context == null) {
            Log.d(TAG, "loadAppIcon context == null");
            return;
        }

        Glide.with(context).load(url).apply(bitmapTransform(new RoundedCornersTransformation(18, 0))).into(view);
    }

    public void loadAdvertImage(Context context, String url, ImageView view) {
        if(context == null) {
            Log.d(TAG, "loadAdvertImage context == null");
            return;
        }

        Glide.with(context).load(url).apply(bitmapTransform(new RoundedCornersTransformation(30, 0))).into(view);
    }

}
