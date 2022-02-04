declare module "@capacitor/core" {
  interface PluginRegistry {
    GoogleFit: GoogleFitPlugin;
  }
}

export interface GoogleFitPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;

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
  getHistory(call:{startTime: Date, endTime: Date}): Promise<any>;

  /**
   * Get history activity
   * @returns {Promise}
   * @resolve AccountData
   */
  getHistoryActivity(): Promise<any>;
}
