package com.showboom.showboomlauncher.green_dao;

import android.content.Context;
import android.util.Log;

import com.showboom.showboomlauncher.App;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by gaopeng on 2018/3/20.
 */

public class LauncherInfoDaoUtils {
    
    private DaoManager mManager;

    public LauncherInfoDaoUtils(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成com.heimilink.showboomservice.Beans.HmReplyDao记录的插入，如果表未创建，先创建com.heimilink.showboomservice.Beans.HmReplyDao表
     *
     * @param info
     * @return
     */
    public boolean insertLauncherInfoDao(LauncherInfo info) {
        boolean flag = false;
        flag = mManager.getDaoSession().getLauncherInfoDao().insertOrReplace(info) != -1;
        Log.i(App.TAG, "insert LauncherInfo :" + flag + "-->" + info.toString());
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     *
     * @param infoList
     * @return
     */
    public boolean insertMultLauncherInfoDao(final List<LauncherInfo> infoList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (LauncherInfo info : infoList) {
                        mManager.getDaoSession().insertOrReplace(info);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     *
     * @param info
     * @return
     */
    public boolean updateLauncherInfoDao(LauncherInfo info) {
        boolean flag = false;
        try {
            mManager.getDaoSession().update(info);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     *
     * @param info
     * @return
     */
    public boolean deleteLauncherInfoDao(LauncherInfo info) {
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(info);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     *
     * @return
     */
    public boolean deleteAll() {
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().deleteAll(LauncherInfo.class);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<LauncherInfo> queryAllLauncherInfoDao() {
        return mManager.getDaoSession().loadAll(LauncherInfo.class);
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    public LauncherInfo queryLauncherInfoDaoById(long key) {
        return mManager.getDaoSession().load(LauncherInfo.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<LauncherInfo> queryLauncherInfoDaoByNativeSql(String sql, String[] conditions) {
        return mManager.getDaoSession().queryRaw(LauncherInfo.class, sql, conditions);
    }
    
    /**
     * 使用queryBuilder进行查询
     *
     * @return
     */
    public List<LauncherInfo> queryLauncherInfoDaoByQueryBuilder(String version,String oldVersion) {
        QueryBuilder<LauncherInfo> queryBuilder = mManager.getDaoSession().queryBuilder(LauncherInfo.class);
        return queryBuilder.where(LauncherInfoDao.Properties.Version.notEq(version),LauncherInfoDao.Properties.Version.notEq(oldVersion)).list();
    }
}
