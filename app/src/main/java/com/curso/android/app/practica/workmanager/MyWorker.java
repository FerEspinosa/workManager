package com.curso.android.app.practica.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    // doWork() runs asynchronosuly on a background thread provided by Workmanager
    @NonNull
    @Override
    public Result doWork() {

        // Getting data from inputData
        Data data = getInputData();
        int countingLimit = data.getInt("max_limit",0);

        // count to 1000
        for (int i=0; i<countingLimit; i++){
            Log.i("TAGY", "Count is: "+i);
        }

        // Sendind Data and Done Notification
        Data dataToSend = new Data.Builder()
                .putString("msg","Task done succesfully")
                .build();


        return Result.success(dataToSend);
    }
}
