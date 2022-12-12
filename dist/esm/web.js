import { WebPlugin } from '@capacitor/core';
export class GoogleFitWeb extends WebPlugin {
    constructor() {
        super({
            name: 'GoogleFit',
            platforms: ['web'],
        });
    }
    async connectToGoogleFit() {
        throw new Error('Method not implemented.');
    }
    async isAllowed() {
        throw new Error('Method not implemented.');
    }
    async getSteps() {
        throw new Error('Method not implemented.');
    }
    async getWeight() {
        throw new Error('Method not implemented.');
    }
    async getActivities() {
        throw new Error('Method not implemented.');
    }
}
//# sourceMappingURL=web.js.map