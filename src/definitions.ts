declare module "@capacitor/core" {
  interface PluginRegistry {
    GoogleFit: GoogleFitPlugin;
  }
}

export interface GoogleFitPlugin {
  echo(options: { value: string }): Promise<{value: string}>;
}
