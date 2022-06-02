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
   * Get step history
   * @returns {Promise}
   * @resolve StepQueryResult
   */
  getSteps(call: StepQueryInput): Promise<StepQueryResult>;

  /**
   * Get weight history
   * @returns {Promise}
   * @resolve WeightQueryResult
   */
  getWeight(call: QueryInput): Promise<WeightQueryResult>;

  /**
   * Get Activites
   * @returns {Promise}
   * @resolve StepQueryResult
   */
  getActivities(call: QueryInput): Promise<any>;

  /**
   * Get history activity
   * @returns {Promise}
   * @resolve AccountData
   */
  getHistoryActivity(call: QueryInput): Promise<ActivityContainer>;
}

export interface PermissionData {
  allowed: boolean;
}

export interface QueryInput {
  startTime: Date;
  endTime: Date;
}

export interface StepQueryInput extends QueryInput {
  bucketSize: number;
  timeUnit: TimeUnit;
}

export interface ActivityContainer {
  activities: HistoryActivityData[];
}

export interface DayContainer {
  days: HistoryData[];
}

/**
 * The results of a WeightQuery.
 * The @param value inside of SimpleData has the unit kilograms
 */
export interface WeightQueryResult {
  weights: SimpleData[];
}

/**
 * The results of a StepQuery.
 * The @param value inside of SimpleData always represents a count
 */
export interface StepQueryResult {
  steps: SimpleData[];
}
export interface SimpleData {
  startTime: string;
  endTime: string;
  value: number;
}

export interface HistoryData {
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

export interface HistoryActivityData {
  start: string;
  end: string;
  distance?: string;
  speed?: string;
  calories?: string;
  activity?: string;
  weight?: string;
  steps?: string;
}

export interface AllowedResult {
  allowed: boolean;
}

export enum TimeUnit {
  NANOSECONDS = 'NANOSECONDS',
  MICROSECONDS = 'MICROSECONDS',
  MILLISECONDS = 'MILLISECONDS',
  SECONDS = 'SECONDS',
  MINUTES = 'MINUTES',
  HOURS = 'HOURS',
  DAYS = 'DAYS',
}
