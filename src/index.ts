import { registerPlugin } from '@capacitor/core';

import type { GoogleFitPlugin } from './definitions';

const GoogleFit = registerPlugin<GoogleFitPlugin>('GoogleFit', {
  web: () => import('./web').then(m => new m.GoogleFitWeb()),
});

export * from './definitions';
export { GoogleFit };