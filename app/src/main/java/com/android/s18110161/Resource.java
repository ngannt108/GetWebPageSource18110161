package com.android.s18110161;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
public class Resource extends AsyncTaskLoader<String>{
    private String queryString;
    private String transferProtocol;
    private Context context;

    public Resource(String queryString, String transferProtocol, Context context) {
        super(context);
        this.queryString = queryString;
        this.transferProtocol = transferProtocol;
        this.context = context;
    }


    @Nullable
    @Override
    public String loadInBackground() {
        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
