package com.curso.android.app.practica.workmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class MainActivity extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn = findViewById(R.id.btn);


        // Data
        Data data = new Data.Builder()
                .putInt("max_limit",500)
                .build();

        // Constraints
        Constraints constraints = new Constraints
                .Builder()
                .setRequiresCharging(true)
                .build();

        // making use of worker
        WorkRequest wr = new OneTimeWorkRequest
                .Builder(MyWorker.class)
                .setConstraints(constraints)
                .setInputData(data)
                .build();


        // Enqueue the request with WorkManager when the user clicks on the button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance(
                        getApplicationContext()).enqueue(wr);
            }
        });

        // monitoring the status of the work manager
        WorkManager.getInstance(getApplicationContext())

            .getWorkInfoByIdLiveData(wr.getId())

            .observe(this,

                new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null) {
                            Toast.makeText(MainActivity.this,
                                    "status: "+workInfo.getState().name(),
                                    Toast.LENGTH_SHORT).show();

                            if (workInfo.getState().isFinished()){
                                Data data1 = workInfo.getOutputData();
                                Toast.makeText(MainActivity.this,
                                        ""+data1.getString("msg"),
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
            );
    }
}