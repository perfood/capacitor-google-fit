export interface GoogleFitPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
