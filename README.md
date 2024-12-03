# workManager

Work Manager App


1- Put WorkManager dependencies in gradle file and sync project
get the dependencies forme here: https://developer.android.com/jetpack/androidx/releases/work
2- Cretae a java class called MyWorker that extends Worker (androidx.work)
3- implement the suggested method
4- Inside the doWork method, put the code that should be run asynchronously.  for example:
// count to 1000
for (int i=0; i<1000; i++){
Log.i("TAGY", "Count is: "+i);
}
return Result.success();
5- Create constructor matching super (as suggested by the IDE)

6- make a button in the layout file (assign an id)
7- initialize it in MainActivity
8- in MainActivity, create a worker:
WorkRequest w = new OneTimeWorkRequest.Builder(MyWorker.class).build();
(make sure to use androidx.work.oneTimeBuilder)
9- Enqueue the request inside the Button onClickListener:
btn.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
WorkManager.getInstance(
getApplicationContext()).enqueue(wr);
}
});
10- Change the target API in manifest to 33


11-
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
                       }
                   }
               });


12- Set constraints before creating the worker (done in step 8):
// Constraints
Constraints constraints = new Constraints
.Builder()
.setRequiresCharging(true)
.build();
13- add a setConstraints command when creating the worker (created in step 8)
WorkRequest w = new OneTimeWorkRequest
.Builder(MyWorker.class)
.setConstraints(constraints)
.build();
Sending data from the main activity to the worker
14- create the data
// Data
Data data = new Data.Builder()
.putInt("max_limit",500)
.build();
15- Add it to the code block where the worker is created (in step 8):
// making use of worker
WorkRequest wr = new OneTimeWorkRequest
.Builder(MyWorker.class)
.setConstraints(constraints)
.setInputData(data)      // added this line
.build();
16- In MyWorker java class, inside the doWork method:
// Getting data from inputData
Data data = getInputData();
int countingLimit = data.getInt("max_limit",0);
17- inside the "count to 1000" code block (created in step 4), change the number 1000 in the for loop, with the countingLimit variable
(int i=0; i< countingLimit ; i++)
18- Inside the doWork method, right before the return statement
// Sendind Data and Done Notification
Data dataToSend = new Data.Builder()
.putString("msg","Task done succesfully")
.build();
19- Inside the doWork, change the return statement to include the data:
return Result.success(dataToSend);
20- In MainActivity, in the onChanged method, right after the Toast inside the if loop:
if (workInfo != null) {
Toast.makeText(MainActivity.this,
"status: "+workInfo.getState().name(),
Toast.LENGTH_SHORT).show();

// add this code
if (workInfo.getState().isFinished()){
Data data1 = workInfo.getOutputData();
Toast.makeText(MainActivity.this,
""+data1.getString("msg"),
Toast.LENGTH_SHORT).show();
}
}


