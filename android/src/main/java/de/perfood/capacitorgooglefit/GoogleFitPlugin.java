package de.perfood.capacitorgooglefit;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.Manifest;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.FitnessActivities;

import com.google.android.gms.fitness.request.DataReadRequest;
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
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;



@CapacitorPlugin(name = "GoogleFit")
@NativePlugin(requestCodes = { GoogleFitPlugin.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, GoogleFitPlugin.RC_SIGN_IN })
public class GoogleFitPlugin extends Plugin {

    public static final String TAG = "capacitor-google-fit";
    static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 19849;
    static final int RC_SIGN_IN = 1337;

    private static final int REQUEST_OAUTH_REQUEST_CODE = 1;

    FitnessOptions fitnessOptions = FitnessOptions.builder()
      .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
      .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.TYPE_SPEED, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_READ)
      .build();

    @PluginMethod
    public void connectToGoogleFit(PluginCall call) {
      if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACTIVITY_RECOGNITION)
        != PackageManager.PERMISSION_GRANTED) {

        ActivityCompat.requestPermissions(
          getActivity(),
          new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
          1
        );
      }


      if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(getActivity()), fitnessOptions)) {
        GoogleSignIn.requestPermissions(
          getActivity(),
          REQUEST_OAUTH_REQUEST_CODE,
          GoogleSignIn.getLastSignedInAccount(getActivity()),
          fitnessOptions);
      }

      call.resolve();
    }

    @PluginMethod
    public void isAllowed(PluginCall call) {
        GoogleSignInAccount account = getSignedInAccount();

        final JSObject result = new JSObject();

        if (account != null && GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            result.put("isAllowed", true);
        } else {
            result.put("isAllowed", false);
        }

        call.resolve(result);
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);

        PluginCall savedCall = getSavedCall();

        if (resultCode == Activity.RESULT_OK) {

          if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
            savedCall.resolve();
          }
        } else {
          savedCall.reject("no permissions");
        }
    }

    @PluginMethod
    public Task<DataReadResponse> getSteps(final PluginCall call) throws ParseException {
        final GoogleSignInAccount account = getSignedInAccount();

        if (account == null) {
            call.reject("No access");
            return null;
        }

        long startDate = dateToTimestamp(call.getString("startDate"));
        long endDate = dateToTimestamp(call.getString("endDate"));

        String timeUnitInput = call.getString("timeUnit", "HOURS");

        TimeUnit timeUnit = stringToTimeUnit(timeUnitInput);

        int bucketSize = call.getInt("bucketSize", 1);

        if (startDate == -1 || endDate == -1 || bucketSize == -1) {
            call.reject("Must provide a start time and end time");

            return null;
        }

        DataReadRequest readRequest = new DataReadRequest.Builder()
            .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startDate, endDate, TimeUnit.MILLISECONDS)
            .bucketByTime(bucketSize, timeUnit)
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

                        JSONArray steps = new JSONArray();

                        for (Bucket bucket : buckets) {
                            String bucketStart = timestampToDate(bucket.getStartTime(TimeUnit.MILLISECONDS));
                            String bucketEnd = timestampToDate(bucket.getEndTime(TimeUnit.MILLISECONDS));
                            
                            Integer stepsInBucket = 0;

                            for (DataSet dataSet : bucket.getDataSets()) {
                                for (DataPoint dp : dataSet.getDataPoints()) {
                                    stepsInBucket += dp.getValue(dp.getDataType().getFields().get(0)).asInt();
                                }
                            }

                            JSONObject stepEntry = new JSONObject();

                            try {
                                stepEntry.put("startDate", bucketStart);
                                stepEntry.put("endDate", bucketEnd);
                                stepEntry.put("value", stepsInBucket);
                                steps.put(stepEntry);
                            } catch (JSONException e) {
                                call.reject(e.getMessage());
                                return;
                            }
                        }

                        JSObject result = new JSObject();
                        result.put("data", steps);

                        call.resolve(result);
                    }
                }
            );
    }

    @PluginMethod
    public Task<DataReadResponse> getWeight(final PluginCall call) throws ParseException {
        final GoogleSignInAccount account = getSignedInAccount();

        if (account == null) {
            call.reject("No access");
            return null;
        }

        long startDate = dateToTimestamp(call.getString("startDate"));
        long endDate = dateToTimestamp(call.getString("endDate"));

        if (startDate == -1 || endDate == -1) {
            call.reject("Must provide a start time and end time");

            return null;
        }
        DataReadRequest readRequest = new DataReadRequest.Builder()
            .read(DataType.TYPE_WEIGHT)
            .setTimeRange(startDate, endDate, TimeUnit.MILLISECONDS)
            .enableServerQueries()
            .build();

        return Fitness
            .getHistoryClient(getActivity(), account)
            .readData(readRequest)
            .addOnSuccessListener(
                new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        DataSet weightDataSet = dataReadResponse.getDataSet(DataType.TYPE_WEIGHT);

                        JSONArray weights = new JSONArray();
                        for (DataPoint dp : weightDataSet.getDataPoints()) {
                            for (Field field : dp.getDataType().getFields()) {
                                JSONObject weightEntry = new JSONObject();
                                try {
                                    weightEntry.put("startDate", timestampToDate(dp.getStartTime(TimeUnit.MILLISECONDS)));
                                    weightEntry.put("endDate", timestampToDate(dp.getEndTime(TimeUnit.MILLISECONDS)));
                                    weightEntry.put("value", dp.getValue(field).asFloat());
                                    weights.put(weightEntry);
                                } catch (JSONException e) {
                                    call.reject(e.getMessage());
                                    return;
                                }
                            }
                        }
                        JSObject result = new JSObject();
                        result.put("data", weights);
                        call.resolve(result);
                    }
                }
            );
    }

    @PluginMethod
    public void getActivities(final PluginCall call) throws ParseException {
        final GoogleSignInAccount account = getSignedInAccount();

        if (account == null) {
            call.reject("No access");
            return;
        }

        long startDate = dateToTimestamp(call.getString("startDate"));
        long endDate = dateToTimestamp(call.getString("endDate"));


        if (startDate == -1 || endDate == -1) {
            call.reject("Must provide a start time and end time");

            return;
        }

        DataReadRequest readRequest = new DataReadRequest.Builder()
          .read(DataType.TYPE_ACTIVITY_SEGMENT)
          .read(DataType.TYPE_CALORIES_EXPENDED)
          .read(DataType.TYPE_STEP_COUNT_DELTA)
          .read(DataType.TYPE_DISTANCE_DELTA)
          .read(DataType.TYPE_SPEED)
          .setTimeRange(startDate, endDate, TimeUnit.MILLISECONDS)
          .bucketByActivitySegment(1, TimeUnit.MINUTES)
          .build();

        Fitness.getHistoryClient(getContext(), GoogleSignIn.getLastSignedInAccount(getContext()))
          .readData(readRequest)
          .addOnSuccessListener(response -> {
            JSObject result = processData(response);
            call.resolve(result);
          })
          .addOnFailureListener(e -> {
            call.reject("Failed to read data: " + e.getMessage());
          });

    }

    private JSObject processData(DataReadResponse response) {
      List<Bucket> buckets = response.getBuckets();
      JSONArray activities = new JSONArray();
      for (Bucket bucket : buckets) {
        String activityType = bucket.getActivity();

        if ("unknown".equals(activityType)) {
          continue;
        }

        JSONObject summary = new JSONObject();
        Integer activityTypeId = null;  // Initialize it to null

        try {
          summary.put("activityType", activityType);
          summary.put("startDate", timestampToDate(bucket.getStartTime(TimeUnit.MILLISECONDS)));
          summary.put("endDate", timestampToDate(bucket.getEndTime(TimeUnit.MILLISECONDS)));

          List<DataSet> dataSets = bucket.getDataSets();

          for (DataSet dataSet : dataSets) {
            if (dataSet.getDataPoints().size() > 0) {
              DataPoint dataPoint = dataSet.getDataPoints().get(0);

                // Check for the activity segment and extract activity type ID
                if (dataSet.getDataType().equals(DataType.TYPE_ACTIVITY_SEGMENT)) {
                    activityTypeId = dataPoint.getValue(Field.FIELD_ACTIVITY).asInt();
                    summary.put("activityTypeId", activityTypeId);
                }

              summary.put("dataSource", dataPoint.getOriginalDataSource().getDataType().getName());

              switch (dataSet.getDataType().getName()) {
                  case "com.google.activity.summary":
                      summary.put("activityTypeId", dataPoint.getValue(Field.FIELD_ACTIVITY).asInt());
                      break;

                case "com.google.distance.delta":
                  summary.put("distance", dataPoint.getValue(Field.FIELD_DISTANCE).asFloat());
                  break;

                case "com.google.speed.summary":
                  summary.put("speed", dataPoint.getValue(Field.FIELD_AVERAGE).asFloat());
                  break;

                case "com.google.calories.expended":
                  summary.put("calories", dataPoint.getValue(Field.FIELD_CALORIES).asFloat());
                  break;

                case "com.google.step_count.delta":
                  summary.put("steps", dataPoint.getValue(Field.FIELD_STEPS).asInt());
                  break;

                default:
                  Log.i(TAG, "Unhandled: " + dataSet.getDataType().getName());
              }
            }
          }
        } catch (JSONException e) {
            // TODO: handle exception
        }

        activities.put(summary);
      }
      JSObject result = new JSObject();
      result.put("data", activities);

      return result;
    }

    private GoogleSignInAccount getSignedInAccount() {
      return GoogleSignIn.getLastSignedInAccount(getActivity());
    }

    private void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dp.getStartTime(TimeUnit.MILLISECONDS));
            Log.i(TAG, "\tEnd: " + dp.getEndTime(TimeUnit.MILLISECONDS));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() + "   Value: " + dp.getValue(field));
            }
        }
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

    private TimeUnit stringToTimeUnit(String inputString) {
        switch (inputString) {
            case "NANOSECONDS":
                return TimeUnit.NANOSECONDS;
            case "MICROSECONDS":
                return TimeUnit.MICROSECONDS;
            case "MILLISECONDS":
                return TimeUnit.MILLISECONDS;
            case "SECONDS":
                return TimeUnit.SECONDS;
            case "MINUTES":
                return TimeUnit.MINUTES;
            case "HOURS":
                return TimeUnit.HOURS;
            case "DAYS":
                return TimeUnit.DAYS;
            default:
                return TimeUnit.HOURS;
        }
    }
}
