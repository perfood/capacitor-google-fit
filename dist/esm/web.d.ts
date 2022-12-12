import { WebPlugin } from '@capacitor/core';
import type { AllowedResult, GoogleFitPlugin, StepQueryResult } from './definitions';
export declare class GoogleFitWeb extends WebPlugin implements GoogleFitPlugin {
    constructor();
    connectToGoogleFit(): Promise<void>;
    isAllowed(): Promise<AllowedResult>;
    getSteps(): Promise<StepQueryResult>;
    getWeight(): Promise<any>;
    getActivities(): Promise<any>;
}
