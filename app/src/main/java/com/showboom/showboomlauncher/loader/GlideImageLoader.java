package com.showboom.showboomlauncher.loader;

import android.content.Context;
import android.widget.ImageView;

import com.showboom.showboomlauncher.GlideApp;
import com.showboom.showboomlauncher.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by gaopeng on 2018/5/8.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        GlideApp.with(context.getApplicationContext()).load(path).placeholder(R.mipmap.banner_default_bg).into(imageView);
    }

//    @Override
//    public ImageView createImageView(Context context) {
//        //圆角
//        return new RoundAngleImageView(context);
//    }
}
