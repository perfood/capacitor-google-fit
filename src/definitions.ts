declare module "@capacitor/core" {
  interface PluginRegistry {
    GoogleFit: GoogleFitPlugin;
  }
}

export interface GoogleFitPlugin {
  /**
   * Connect to Google Fit
   * @returns {Promise}
   * @resolve any
   */
  connectToGoogleFit(): Promise<void>;

  /**
   * Returns wether the permissions are ok or not
   * @returns {Promise}
   * @resolve AllowedResult
   */
  isAllowed(): Promise<AllowedResult>;

  /**
   * Get history
   * @returns {Promise}
   * @resolve AccountData
   */
  getHistory(call: QueryInput): Promise<DayContainer>;

  /**
   * Get history activity
   * @returns {Promise}
   * @resolve AccountData
   */
  getHistoryActivity(call: QueryInput): Promise<ActivityContainer>;
}


export interface PermissionData{
  allowed: boolean;
}

export interface QueryInput{
  startTime: Date;
  endTime: Date;
}

export interface ActivityContainer{
  activities: HistoryActivityData[]
}


export interface DayContainer{
  days: HistoryData[]
}

export interface HistoryData{
  start: string;
  end: string;
  /**
  Distance travelled in meters.
  Valid range: 0—100 meters per second
   */
  distance: string;
  /**meters per second */
  speed: string;
  /*
  This data type captures the total calories (in kilocalories) burned by the user, including calories burned at rest (BMR or Basalrate)!
  */
  calories: string;
}

export interface HistoryActivityData{
  start: string;
  end: string;
  distance?: string;
  speed?: string;
  calories?: string;
  activity?: string;
  weight?: string;
}

export interface AllowedResult{
  allowed: boolean;
}