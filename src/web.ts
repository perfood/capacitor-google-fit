import { WebPlugin } from '@capacitor/core';

import type { GoogleFitPlugin } from './definitions';

export class GoogleFitWeb extends WebPlugin implements GoogleFitPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
