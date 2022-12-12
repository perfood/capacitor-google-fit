import { registerPlugin } from '@capacitor/core';
const GoogleFit = registerPlugin('GoogleFit', {
    web: () => import('./web').then(m => new m.GoogleFitWeb()),
});
export * from './definitions';
export { GoogleFit };
//# sourceMappingURL=index.js.map