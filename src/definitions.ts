declare module '@capacitor/core' {
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
   * @resolve any
   */
  isAllowed(): Promise<any>;

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
