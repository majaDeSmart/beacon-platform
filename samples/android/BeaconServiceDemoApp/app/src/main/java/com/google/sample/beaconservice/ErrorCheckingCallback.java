package com.google.sample.beaconservice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

class ErrorCheckingCallback implements ResultCallback<Status> {
    private final String method;
    private final Runnable runOnSuccess;
    private Context context;

    public ErrorCheckingCallback(String method) {
        this(method, null, null);
    }

    public ErrorCheckingCallback(String method, @Nullable Runnable runOnSuccess, Context context) {
        this.method = method;
        this.runOnSuccess = runOnSuccess;
        this.context = context;
    }

    @Override
    public void onResult(@NonNull Status status) {
        Toast.makeText(context, status.toString(), Toast.LENGTH_SHORT).show();

        if (status.isSuccess()) {
            if (runOnSuccess != null) {
                runOnSuccess.run();
            }
        }
    }
}
