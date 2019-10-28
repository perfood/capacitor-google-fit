import { WebPlugin } from '@capacitor/core';
import { GoogleFitPlugin } from './definitions';

class AccountData {
  displayName: string;
  email: string;
  weight: number;
  height: number
}

export class GoogleFitWeb extends WebPlugin implements GoogleFitPlugin {
  constructor() {
    super({
      name: 'GoogleFit',
      platforms: ['web']
    });
  }
  
  async connectToGoogleFit(): Promise<void> {
    throw new Error("Method not implemented.");
  }
  async getAccountData(): Promise<AccountData> {
    throw new Error("Method not implemented.");
  }
  async getTodayData(): Promise<any> {
    throw new Error("Method not implemented.");
  }
  async getHistory(): Promise<any> {
    throw new Error("Method not implemented.");
  }
  async getHistoryActivity(): Promise<any> {
    throw new Error("Method not implemented.");
  }
}

const GoogleFit = new GoogleFitWeb();

export { GoogleFit };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(GoogleFit);