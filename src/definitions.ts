export interface GoogleFitPlugin {
  connectToGoogleFit(): Promise<void>;
  isAllowed(): Promise<IsAllowedResult>;
  getSteps(options: GetStepsOptions): Promise<StepsQueryResult>;
  getWeight(options: GetWeightOptions): Promise<WeightQueryResult>;
  getActivities(options: GetActivitiesOptions): Promise<ActivitiesQueryResult>;
}

export interface GetStepsOptions {
  startDate: string;
  endDate: string;
  timeUnit: string;
  bucketSize: number;
}

export interface GetWeightOptions {
  startDate: string;
  endDate: string;
}

export interface GetActivitiesOptions {
  startDate: string;
  endDate: string;
}

export interface IsAllowedResult {
  isAllowed: boolean;
}

export interface StepsQueryResult {
  data: {
    startDate: string;
    endDate: string;
    value: number;
  }[];
}

export interface WeightQueryResult {
  data: {
    startDate: string;
    endDate: string;
    value: number;
  }[];
}

export interface ActivitiesQueryResult {
  data: {
    startDate: string;
    endDate: string;
    activityType: string;
    activityTypeId: number;
    calories: number;
    steps: number;
    speed: number;
    distance: number;
    dataSource:
      | 'com.google.activity.segment'
      | 'com.google.activity.samples'
      | string;
  }[];
}

export interface SimpleData {
  startDate: string;
  endDate: string;
  value: number;
}

export interface ActivityData extends SimpleData {
  calories: number;
  name: string;
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
