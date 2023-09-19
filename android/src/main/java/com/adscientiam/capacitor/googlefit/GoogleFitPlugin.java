package com.adscientiam.capacitor.googlefit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import androidx.annotation.NonNull;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.SleepStages;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "GoogleFit")
@NativePlugin(requestCodes = { GoogleFitPlugin.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, GoogleFitPlugin.RC_SIGN_IN })
public class GoogleFitPlugin extends Plugin {

    public static final String TAG = "HistoryApi";
    static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 19849;
    static final int RC_SIGN_IN = 1337;

    private FitnessOptions getFitnessSignInOptions() {
        // FitnessOptions instance, declaring the Fit API data types
        // and access required
        return FitnessOptions
            .builder()
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_SPEED, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ) // 体重
            .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_WRITE) // 体重
            .addDataType(DataType.TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_READ) // 睡眠
            .addDataType(DataType.TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_WRITE) // 睡眠
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ) // 歩数
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE) // 歩数
            .build();
    }

    private GoogleSignInAccount getAccount() {
        return GoogleSignIn.getLastSignedInAccount(getActivity());
    }

    private void requestPermissions() {
        GoogleSignIn.requestPermissions(getActivity(), GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, getAccount(), getFitnessSignInOptions());
    }

    // @Override
    // public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //     super.onActivityResult(requestCode, resultCode, data);

    //     if (requestCode == RC_SIGN_IN) {
    //         if (resultCode == Activity.RESULT_OK) {
    //             // ログイン成功時の処理
    //             GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this.getActivity());
    //             requestPermissions(); // ログイン成功時にパーミッションをリクエストする
    //         }
    //         // else {
    //         //     // ログインキャンセルまたは失敗時の処理
    //         //     // エラーを適切に処理するか、必要に応じて追加の処理を実行する
    //         // }
    //     }
    // }

    @PluginMethod
    public void logoutGoogleFit(PluginCall call) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this.getActivity(), gso);
        signInClient.signOut();
        call.resolve();
    }

    @PluginMethod
    public void connectToGoogleFit(PluginCall call) {
        GoogleSignInAccount account = getAccount();
        if (account == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            GoogleSignInClient signInClient = GoogleSignIn.getClient(this.getActivity(), gso);
            Intent signInIntent = signInClient.getSignInIntent();
            startActivityForResult(call, signInIntent, RC_SIGN_IN);
        } else {
            this.requestPermissions();
        }
        call.resolve();
    }

    @PluginMethod
    public void isGoogleFitInstalled(PluginCall call) {
        Context context = bridge.getActivity().getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        JSObject result = new JSObject();
        try {
            packageManager.getPackageInfo("com.google.android.apps.fitness", PackageManager.GET_ACTIVITIES);
            result.put("value", true);
        } catch (PackageManager.NameNotFoundException e) {
            result.put("value", false);
        }
        call.resolve(result);
    }

    @PluginMethod
    public void isAllowed(PluginCall call) {
        final JSObject result = new JSObject();
        GoogleSignInAccount account = getAccount();
        if (account != null && GoogleSignIn.hasPermissions(account, getFitnessSignInOptions())) {
            result.put("allowed", true);
        } else {
            result.put("allowed", false);
        }
        call.resolve(result);
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                // ログインが成功した場合に、権限をリクエスト
                this.requestPermissions();
            } else {
                // ログインが失敗した場合の処理
                // ここにエラーハンドリングを追加できます
            }
        }
    }

    // @Override
    // protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
    //     super.handleOnActivityResult(requestCode, resultCode, data);
    //     PluginCall savedCall = getSavedCall();

    //     if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
    //         savedCall.resolve();
    //     } else if (requestCode == RC_SIGN_IN) {
    //         if (!GoogleSignIn.hasPermissions(this.getAccount(), getFitnessSignInOptions())) {
    //             this.requestPermissions();
    //         } else {
    //             savedCall.resolve();
    //         }
    //     }
    // }

    @PluginMethod
    public Task<DataReadResponse> getHistory(final PluginCall call) throws ParseException {
        GoogleSignInAccount account = getAccount();

        if (account == null) {
            call.reject("No access");
            return null;
        }

        long startTime = dateToTimestamp(call.getString("startTime"));
        long endTime = dateToTimestamp(call.getString("endTime"));

        if (startTime == -1 || endTime == -1) {
            call.reject("Must provide a start time and end time");
            return null;
        }

        DataReadRequest readRequest = new DataReadRequest.Builder()
            .aggregate(DataType.TYPE_DISTANCE_DELTA)
            .aggregate(DataType.AGGREGATE_DISTANCE_DELTA)
            .aggregate(DataType.TYPE_SPEED)
            .aggregate(DataType.TYPE_CALORIES_EXPENDED)
            .aggregate(DataType.AGGREGATE_CALORIES_EXPENDED)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            // .bucketByTime(1, TimeUnit.DAYS)
            // .bucketByTime(1, TimeUnit.HOURS)
            .bucketByActivitySegment(30, TimeUnit.MINUTES)
            .enableServerQueries()
            .build();

        return Fitness
            .getHistoryClient(getActivity(), account)
            .readData(readRequest)
            .addOnSuccessListener(
                new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        List<Bucket> buckets = dataReadResponse.getBuckets();
                        JSONArray days = new JSONArray();
                        for (Bucket bucket : buckets) {
                            JSONObject summary = new JSONObject();
                            try {
                                summary.put("start", timestampToDate(bucket.getStartTime(TimeUnit.MILLISECONDS)));
                                summary.put("end", timestampToDate(bucket.getEndTime(TimeUnit.MILLISECONDS)));
                                List<DataSet> dataSets = bucket.getDataSets();
                                for (DataSet dataSet : dataSets) {
                                    if (dataSet.getDataPoints().size() > 0) {
                                        switch (dataSet.getDataType().getName()) {
                                            case "com.google.distance.delta":
                                                summary.put("distance", dataSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE));
                                                break;
                                            case "com.google.speed.summary":
                                                summary.put("speed", dataSet.getDataPoints().get(0).getValue(Field.FIELD_AVERAGE));
                                                break;
                                            case "com.google.calories.expended":
                                                summary.put("calories", dataSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES));
                                                break;
                                            default:
                                                Log.i(TAG, "need to handle " + dataSet.getDataType().getName());
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                call.reject(e.getMessage());
                                return;
                            }
                            days.put(summary);
                        }
                        JSObject result = new JSObject();
                        result.put("days", days);
                        call.resolve(result);
                    }
                }
            )
            .addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        call.reject(e.getMessage());
                    }
                }
            );
    }

    @PluginMethod
    public Task<DataReadResponse> getHistoryActivity(final PluginCall call) throws ParseException {
        final GoogleSignInAccount account = getAccount();
        if (account == null) {
            call.reject("No access");
            return null;
        }
        long startTime = dateToTimestamp(call.getString("startTime"));
        long endTime = dateToTimestamp(call.getString("endTime"));

        if (startTime == -1 || endTime == -1) {
            call.reject("Must provide a start time and end time");
            return null;
        }

        // https://developers.google.com/android/reference/com/google/android/gms/fitness/request/DataReadRequest.Builder
        DataReadRequest readRequest = new DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA) // 歩数
            .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .aggregate(DataType.TYPE_DISTANCE_DELTA)
            .aggregate(DataType.AGGREGATE_DISTANCE_DELTA)
            .aggregate(DataType.TYPE_SPEED)
            .aggregate(DataType.TYPE_CALORIES_EXPENDED)
            .aggregate(DataType.AGGREGATE_CALORIES_EXPENDED)
            .aggregate(DataType.TYPE_WEIGHT)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByActivitySegment(1, TimeUnit.MINUTES)
            .enableServerQueries()
            // .bucketByTime(1, TimeUnit.HOURS)
            .build();

        return Fitness
            .getHistoryClient(getActivity(), account)
            .readData(readRequest)
            .addOnSuccessListener(
                new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        List<Bucket> buckets = dataReadResponse.getBuckets();
                        JSONArray activities = new JSONArray();
                        for (Bucket bucket : buckets) {
                            JSONObject summary = new JSONObject();
                            try {
                                summary.put("start", timestampToDate(bucket.getStartTime(TimeUnit.MILLISECONDS)));
                                summary.put("end", timestampToDate(bucket.getEndTime(TimeUnit.MILLISECONDS)));

                                List<DataSet> dataSets = bucket.getDataSets();

                                for (DataSet dataSet : dataSets) {
                                    if (dataSet.getDataPoints().size() > 0) {
                                        switch (dataSet.getDataType().getName()) {
                                            case "com.google.distance.delta":
                                                summary.put("distance", dataSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE));
                                                break;
                                            case "com.google.speed.summary":
                                                summary.put("speed", dataSet.getDataPoints().get(0).getValue(Field.FIELD_AVERAGE));
                                                break;
                                            case "com.google.calories.expended":
                                                summary.put("calories", dataSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES));
                                                break;
                                            case "com.google.weight.summary":
                                                summary.put("weight", dataSet.getDataPoints().get(0).getValue(Field.FIELD_AVERAGE));
                                                break;
                                            case "com.google.step_count.delta":
                                                summary.put("steps", dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS));
                                                break;
                                            default:
                                                Log.i(TAG, "need to handle " + dataSet.getDataType().getName());
                                        }
                                    }
                                }
                                summary.put("activity", bucket.getActivity());
                            } catch (JSONException e) {
                                call.reject(e.getMessage());
                                return;
                            }
                            activities.put(summary);
                        }

                        JSObject result = new JSObject();
                        result.put("activities", activities);
                        call.resolve(result);
                    }
                }
            )
            .addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        call.reject(e.getMessage());
                    }
                }
            );
    }

    @PluginMethod
    public Task<DataReadResponse> readSleepData(final PluginCall call) throws ParseException {
        final GoogleSignInAccount account = getAccount();

        if (account == null) {
            call.reject("No access");
            return null;
        }

        long startTime = dateToTimestamp(call.getString("startTime"));
        long endTime = dateToTimestamp(call.getString("endTime"));

        DataReadRequest readRequest = new DataReadRequest.Builder()
            .read(DataType.TYPE_SLEEP_SEGMENT)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build();

        Fitness
            .getHistoryClient(getActivity(), account)
            .readData(readRequest)
            .addOnSuccessListener(
                new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        List<DataSet> dataSets = dataReadResponse.getDataSets();
                        JSONArray sleeps = new JSONArray();
                        for (DataSet dataSet : dataSets) {
                            for (DataPoint dp : dataSet.getDataPoints()) {
                                JSONObject summary = new JSONObject();
                                try {
                                    summary.put("start", timestampToDate(dp.getStartTime(TimeUnit.MILLISECONDS)));
                                    summary.put("end", timestampToDate(dp.getEndTime(TimeUnit.MILLISECONDS)));
                                    summary.put("sleep", dp.getValue(Field.FIELD_SLEEP_SEGMENT_TYPE));
                                } catch (JSONException e) {
                                    call.reject(e.getMessage());
                                    return;
                                }
                                sleeps.put(summary);
                            }
                        }
                        JSObject result = new JSObject();
                        result.put("sleeps", sleeps);
                        call.resolve(result);
                    }
                }
            )
            .addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        call.reject(e.getMessage());
                    }
                }
            );
        return null;
    }

    @PluginMethod
    public Task<DataReadResponse> setWriteSleepData(final PluginCall call) throws ParseException {
        final GoogleSignInAccount account = getAccount();

        if (account == null) {
            call.reject("No access");
            return null;
        }

        long startTime = dateToTimestamp(call.getString("startTime"));
        long endTime = dateToTimestamp(call.getString("endTime"));
        int sleepStage = call.getInt("sleepStage");

        // int sleep;

        // switch (sleepStage) {
        //     case -1:
        //         sleep = SleepStages.AWAKE;
        //         break;
        //     case 0:
        //         sleep = SleepStages.SLEEP_REM;
        //         break;
        //     case 1:
        //     case 2:
        //         sleep = SleepStages.SLEEP_LIGHT;
        //         break;
        //     case 3:
        //         sleep = SleepStages.SLEEP_DEEP;
        //         break;
        //     default:
        //         sleep = SleepStages.OUT_OF_BED;
        //         break;
        // }

        //        .setAppPackageName(getString(R.string.package_name))
        DataSource sleepDataSource = new DataSource.Builder().setDataType(DataType.TYPE_SLEEP_SEGMENT).setType(DataSource.TYPE_RAW).build();

        Session session = new Session.Builder()
            .setName("Sleep session")
            .setDescription("Sleep data from my app")
            .setIdentifier("UniqueIdentifierHere")
            .setActivity(FitnessActivities.SLEEP)
            .setStartTime(startTime, TimeUnit.MILLISECONDS)
            .setEndTime(endTime, TimeUnit.MILLISECONDS)
            .setActivity(FitnessActivities.SLEEP)
            .setIdentifier("UniqueIdentifierHere")
            .build();

        DataPoint sleepStageDataPoint = DataPoint
            .builder(sleepDataSource)
            .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
            // .setField(Field.FIELD_SLEEP_SEGMENT_TYPE, sleep)

            // inbed
            .setField(Field.FIELD_SLEEP_SEGMENT_TYPE, SleepStages.IN_BED)
            .build();

        DataSet sleepStageDataSet = DataSet.builder(sleepDataSource).add(sleepStageDataPoint).build();

        SessionInsertRequest request = new SessionInsertRequest.Builder().setSession(session).addDataSet(sleepStageDataSet).build();

        JSObject ret = new JSObject();
        ret.put("value", "success");

        Fitness
            .getSessionsClient(getActivity(), account)
            .insertSession(request)
            .addOnSuccessListener(session1 -> call.resolve(ret))
            .addOnFailureListener(e -> call.reject(e.getMessage()));

        return null;
    }

    private String timestampToDate(long timestamp) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return df.format(cal.getTime());
    }

    private long dateToTimestamp(String date) {
        if (date.isEmpty()) {
            return -1;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        try {
            return f.parse(date).getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    @PluginMethod
    public Task<DataReadResponse> settingSleepSegment(final PluginCall call) throws ParseException {
        final GoogleSignInAccount account = getAccount();

        if (account == null) {
            call.reject("No access");
            return null;
        }

        long startTime = dateToTimestamp(call.getString("startTime"));
        long endTime = dateToTimestamp(call.getString("endTime"));

        // 睡眠セッションの書き込み
        // 睡眠セッションの書き込み
        SessionInsertRequest.Builder insertRequestBuilder = new SessionInsertRequest.Builder();

        // 睡眠セッションの設定
        Session session = new Session.Builder()
            .setName("Sleep Session") // セッションの名前
            .setStartTime(startTime, TimeUnit.MILLISECONDS) // 開始時間
            .setEndTime(endTime, TimeUnit.MILLISECONDS) // 終了時間
            .setActivity(FitnessActivities.SLEEP) // アクティビティを睡眠に設定
            .build();

        // 睡眠セッションを書き込む
        Task<Void> insertSessionTask = Fitness.getSessionsClient(getActivity(), account).insertSession(insertRequestBuilder.build());

        insertSessionTask.addOnSuccessListener(
            new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    JSObject ret = new JSObject();
                    ret.put("value", "success");
                    // 睡眠セッションの書き込みに成功した場合の処理
                    call.resolve(ret);
                }
            }
        );

        insertSessionTask.addOnFailureListener(
            new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // 睡眠セッションの書き込みに失敗した場合の処理
                    call.reject(e.getMessage());
                    // call.reject("Failed to add sleep session: " + e.getMessage());
                }
            }
        );

        return null;
    }

    @PluginMethod
    public Task<DataReadResponse> writeStepCountData(final PluginCall call) throws ParseException {
        final GoogleSignInAccount account = getAccount();

        if (account == null) {
            call.reject("No access");
            return null;
        }

        long startTime = dateToTimestamp(call.getString("startTime"));
        long endTime = dateToTimestamp(call.getString("endTime"));
        int stepCount = call.getInt("value");

        DataSource stepCountDataSource = new DataSource.Builder()
            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .setType(DataSource.TYPE_RAW)
            .setAppPackageName(getActivity()) // これを追加
            .build();

        DataPoint stepCountDataPoint = DataPoint
            .builder(stepCountDataSource)
            .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
            .setField(Field.FIELD_STEPS, stepCount)
            .build();

        DataSet stepCountDataSet = DataSet.builder(stepCountDataSource).add(stepCountDataPoint).build();

        JSObject ret = new JSObject();
        ret.put("value", "success");

        Fitness
            .getHistoryClient(getActivity(), account)
            .insertData(stepCountDataSet)
            .addOnSuccessListener(session1 -> call.resolve(ret))
            .addOnFailureListener(e -> call.reject(e.getMessage()));

        return null;
    }
}
