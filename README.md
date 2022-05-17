# Google Fit Plugin

Capacitor plugin to retrieve data from Google Fit

### Install

```
npm i --save capacitor-google-fit
npx cap sync
```

### Android requirement

In order for your app to communicate properly with the Google Fitness API, you need to provide the SHA1 sum of the certificate used for signing your application to Google. This will enable the GoogleFit plugin to communicate with the Fit application in each smartphone where the application is installed.

To do this:

#### 1. Get your app's certificate information

1. Locate your debug keystore file. The file name is **debug.keystore**

   - macOS : ~/.android
   - Windows : C:\Users\your-user-name\.android\

2. List the SHA-1 fingerprint:

   - macOS

   ```
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```

   - Windows

   ```
   keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
   ```

The line that begins with SHA1 contains the certificate's SHA-1 fingerprint.

##### 2. Request an OAuth 2.0 client ID in the Google API Console :

1. Go to the [Google API Console](https://console.developers.google.com/flows/enableapi?apiid=fitness)
2. Create a project or choose existing project
3. Click Continue to enable the Fitness API.
4. Click Go to [credentials](https://console.cloud.google.com/apis/credentials)
5. Now add credential to your project.
   Note that If this is the first time you configure the project you should "Configure the OAuth consent screen" first
   Click New credentials, then select OAuth Client ID.
6. Under Application type select Android.
7. In the resulting dialog, enter your app's SHA-1 fingerprint and package name. For example:

   ```
   BB:0D:AC:74:D3:21:E1:43:67:71:9B:62:91:AF:A1:66:6E:44:5D:75

   com.example.android.fit-example
   ```

8. Click Create. Your new Android OAuth 2.0 Client ID and secret appear in the list of IDs for your project.
   An OAuth 2.0 Client ID is a string of characters, something like this:
   `780816631155-gbvyo1o7r2pn95qc4ei9d61io4uh48hl.apps.googleusercontent.com`

### Set up in Android

Register plugin inside your MainActivity

```
import com.adscientiam.capacitor.googlefit.GoogleFitPlugin;

...

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    registerPlugin(GoogleFitPlugin.class);
  }
```

### Import plugin

```
import { Plugins } from '@capacitor/core';
const { GoogleFit } = Plugins;
```

### Supported data types :

| Data Type | Unit         | Google Fit equivalent  |
| --------- | ------------ | ---------------------- |
| step      | count        | TYPE_STEP_COUNT_DELTA  |
| calories  | kcal         | TYPE_CALORIES_EXPENDED |
| distance  | m            | TYPE_DISTANCE_DELTA    |
| weight    | kg           | TYPE_WEIGHT            |
| height    | m            | TYPE_HEIGHT            |
| activity  | activityType | TYPE_ACTIVITY_SEGMENT  |

### Methods :

#### connectToGoogleFit()

Plugin method to connect to google fit service using user account.
It will first check if the user has permissions to talk to Fitness APIs,
otherwise authenticate the user and request required permissions.

#### getHistory(startTime: string, endTime: string)

A method which retrieve data from a specific time interval provided in parametre:

- startDate: {type: String}, start date from which to get data
- endDate: {type: String}, end data to which to get the data
  Example:

```
  async getHistory() {
    const today = new Date();
    const lastWeek = new Date(today);
    lastWeek.setDate(lastWeek.getDate() - 7);
    const result = await GoogleFit.getHistory({
      startTime: lastWeek,
      endTime: today
    });
    console.log(result);
  }
```

#### getHistoryActivity()

Same as getHistory() method but this time to retrieve activities
Returned objects contain a set of fixed fields for each activity:

- startDate: {type: String} a date indicating when an activity starts
- endDate: {type: String} a date indicating when an activity ends
