import { WebPlugin } from '@capacitor/core';
import { GoogleFitPlugin } from './definitions';

export class GoogleFitWeb extends WebPlugin implements GoogleFitPlugin {
  constructor() {
    super({
      name: 'GoogleFit',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return options;
  }
}

const GoogleFit = new GoogleFitWeb();

export { GoogleFit };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(GoogleFit);
