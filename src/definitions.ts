declare module "@capacitor/core" {
  interface PluginRegistry {
    GoogleFit: GoogleFitPlugin;
  }
}

class AccountData {
  displayName: string;
  email: string;
  weight: number;
  height: number
}

export interface GoogleFitPlugin {
  /**
   * Connect to Google Fit
   * @returns {Promise}
   * @resolve any
   */
  connectToGoogleFit(): Promise<void>;

  /**
   * Get account data
   * @returns {Promise}
   * @resolve AccountData
   */
  getAccountData(): Promise<AccountData>;

  /**
   * Get today data
   * @returns {Promise}
   * @resolve AccountData
   */
  getTodayData(): Promise<any>;

  /**
   * Get history
   * @returns {Promise}
   * @resolve AccountData
   */
  getHistory(): Promise<any>;

  /**
   * Get history activity
   * @returns {Promise}
   * @resolve AccountData
   */
  getHistoryActivity(): Promise<any>;
}
