import { WebPlugin } from '@capacitor/core';

import type { GoogleFitPlugin } from './definitions';

export class GoogleFitWeb extends WebPlugin implements GoogleFitPlugin {
  async connectToGoogleFit(): Promise<void> {
    console.log('connectToGoogleFit');
  }

  async isAllowed(): Promise<any> {
    console.log('isAllowed');
  }

  async getSteps(options: {
    startDate: string;
    endDate: string;
  }): Promise<any> {
    console.log('getSteps', options);
  }

  async getWeight(options: {
    startDate: string;
    endDate: string;
  }): Promise<any> {
    console.log('getWeight', options);
  }

  async getActivities(options: {
    startDate: string;
    endDate: string;
  }): Promise<any> {
    console.log('getActivities', options);
  }
}
