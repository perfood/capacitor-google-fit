# Google Fit Plugin

v2 is going to be a nearly complete rewrite of the plugin and still under heavy development. Things might not work just yet, but we are on it!

**TODO list:**

- [x] Import Steps
- [x] Import Weight
      <<<<<<< HEAD
- [x] # Import Activities
  > > > > > > > origin/next
- [ ] Import Activities
- [ ] Import Sleep
- [ ] Import Pulse?

## Install

```
npm i --save @perfood/capacitor-google-fit
npx cap sync
```

### Android requirement

In order for your app to communicate properly with the Google Fitness API, you need to provide the SHA1 sum of the certificate used for signing your application to Google. This will enable the GoogleFit plugin to communicate with the Fit application in each smartphone where the application is installed.

To do this:

#### 1. Get your app's certificate information

1. Locate your debug keystore file. The file name is **debug.keystore**

   - macOS : ~/.android
   - Windows : C:\Users\your-user-name\.android\

2. List the SHA-1 fingerprint:!

   - macOS

   ```
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```

   - Windows

   ```
   keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
   ```

The line that begins with SHA1 contains the certificate's SHA-1 fingerprint.

#### 2. Request an OAuth 2.0 client ID in the Google API Console :

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
   `780816531155-gbvyo1o7r2pn95tc4ei9d61io4uh48hl.apps.googleusercontent.com`

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

## Usage

### First time Usage

Before you can query for Data you have to connect to Google Fit you first have to import the GoogleFit Plugin and connect the User to Google Fit. An Example Service could look like this:

#### **`example.service.ts`**

```ts
import { GoogleFit } from '@perfood/capacitor-google-fit';

export class ExampleService {
  constructor() {}

  public async connect(): Promise<void> {
    await GoogleFit.connectToGoogleFit();
  }
}
```

### Query for Steps

To query for steps, you need to define the following Parameteres, also defined in ExtendedQueryInput:

```
startTime: Date;
endTime: Date;
bucketSize: number;
timeUnit: TimeUnit; //p.e. "DAYS","HOURS","MINUTES",...
```

The bucketSize and timeUnit will define in what chunks Google fit will deliver your data.
an Example function to get Step Data in chunks of hours, looks like this:

#### **`example.service.ts`**

```ts
import { GoogleFit, SimpleData } from '@perfood/capacitor-google-fit';

export class ExampleService {
  constructor() {}

  public async getSteps(): Promise<SimpleData[]> {
    const today = new Date();
    const lastWeek = new Date();

    lastWeek.setDate(
      today.getFullYear(),
      today.getMonth(),
      today.getDate() - 7,
    );

    const result = await GoogleFit.getSteps({
      startTime: lastWeek,
      endTime: today,
      timeUnit: TimeUnit.HOURS,
      bucketSize: 1,
    });

    return result.steps;
  }
}
```

### Query for Weight

To query for weight, you need to define the following Parameteres, also defined in QueryInput:

```
startTime: Date;
endTime: Date;
```

an example function to get Weight Data, looks like this:

#### **`example.service.ts`**

```ts
import { GoogleFit, SimpleData } from '@perfood/capacitor-google-fit';

export class ExampleService {
  constructor() {}

  public async getWeight(): Promise<SimpleData[]> {
    const today = new Date();
    const lastWeek = new Date();

    lastWeek.setDate(
      today.getFullYear(),
      today.getMonth(),
      today.getDate() - 7,
    );

    const result = await GoogleFit.getWeight({
      startTime: lastWeek,
      endTime: today,
    });

    return result.weights;
  }
}
```

### Query for Activities

To query for actvities, you need to define the following Parameteres, also defined in ExtendedQueryInput:

```
startTime: Date;
endTime: Date;
bucketSize: number;
timeUnit: TimeUnit; //p.e. "DAYS","HOURS","MINUTES",...
```

The bucketSize and timeUnit will define the minimum length of an activity.

Many activities will have the type unknown or in_vehicle - you will have to filter those out manually.

an Example function to get Activities, looks like this:

#### **`example.service.ts`**

```ts
import { GoogleFit, StepData } from '@perfood/capacitor-google-fit';
export class ExampleService {
  constructor() {}
  public async getActivities(): Promise<ActivityData[]> {
    const today = new Date();
    const lastWeek = new Date();
    lastWeek.setDate(
      today.getFullYear(),
      today.getMonth(),
      today.getDate() - 7,
    );
    const result = await GoogleFit.getSteps({
      startTime: lastWeek,
      endTime: today,
      timeUnit: TimeUnit.MINUTES,
      bucketSize: 1,
    });
    return result.activities;
  }
}
```
