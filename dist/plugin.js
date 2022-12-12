var capacitorGoogleFit = (function (exports, core) {
    'use strict';

    exports.TimeUnit = void 0;
    (function (TimeUnit) {
        TimeUnit["NANOSECONDS"] = "NANOSECONDS";
        TimeUnit["MICROSECONDS"] = "MICROSECONDS";
        TimeUnit["MILLISECONDS"] = "MILLISECONDS";
        TimeUnit["SECONDS"] = "SECONDS";
        TimeUnit["MINUTES"] = "MINUTES";
        TimeUnit["HOURS"] = "HOURS";
        TimeUnit["DAYS"] = "DAYS";
    })(exports.TimeUnit || (exports.TimeUnit = {}));

    const GoogleFit = core.registerPlugin('GoogleFit', {
        web: () => Promise.resolve().then(function () { return web; }).then(m => new m.GoogleFitWeb()),
    });

    class GoogleFitWeb extends core.WebPlugin {
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

    var web = /*#__PURE__*/Object.freeze({
        __proto__: null,
        GoogleFitWeb: GoogleFitWeb
    });

    exports.GoogleFit = GoogleFit;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
