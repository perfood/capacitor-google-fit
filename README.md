# Google Fit Plugin
Capacitor plugin to retrieve data from Google Fit

### Install
```
npm i --save capacitor-google-fit
npx cap update

```

### Android
Register plugin inside you MainActivity.onCreate
```
this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
  add(GoogleFitPlugin.class);
}});

```
### Use plugin
Register the plugin by importing it. 
```
import "@example/capacitor-google-fit"

```

Use it 
```
import { Plugins } from '@capacitor/core';
const { GoogleFitPlugin } = Plugins;

```

### Supported data types :

| Data Type | Unit | Google Fit equivalent |
| --- | --- | --- |
| step | count | TYPE_STEP_COUNT_DELTA |
| calories | kcal | TYPE_CALORIES_EXPENDED |
| distance | m | TYPE_DISTANCE_DELTA |
| weight | kg | TYPE_WEIGHT |
| height | m | TYPE_HEIGHT |
| activity | activityType | TYPE_ACTIVITY_SEGMENT |


### Methods :

#### connectToGoogleFit()
Plugin method to connect to google fit service using user account.
It will first check if the user has permissions to talk to Fitness APIs,
otherwise authenticate the user and request required permissions.

#### getHistory()
A method which retrieve data from a specific time interval provided in parametre. 
- startDate: {type: String}, start date from which to get data
- endDate: {type: String}, end data to which to get the data
Example:
```
  async getHistory() {
    var lastWeek = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 7);
    this.startDate = lastWeek.toDateString();
    this.endDate = today.toDateString();
    let result = await GoogleFitPlugin.getHistory({
      startTime: this.startDate, 
      endTime: this.endDate 
    })
    console.log(result)
  }
    
```

Returned objects contain a set of fixed fields:

#### getHistoryActivity()
Same as getHistory() method but this time to retrieve activities
Returned objects contain a set of fixed fields for each activity:
- startDate: {type: String} a date indicating when an activity starts
- endDate: {type: String} a date indicating when an activity ends

#### getAccountData()
Methode to get user informations as shown in Google Fit profile page :
```
  async getAccountData() {
      let result = await SamsungHealthPlugin.getAccountData();
      console.log(result);
      this.displayName = result.displayName;
      this.email = result.email;
      this.weight = result.weight;
      this.height = result.height;
  }
```

#### getTodayData()
Method to get today's data such as total steps, calories and distances travelled.
```
  async getTodayData() {
    let result =  await GoogleFitPlugin.getTodayData();
    console.log(result);
    this.todayStep = result.todayStep;
    this.todayCal = result.todayCal;
    this.todayDist = result.todayDist;
  }
```

