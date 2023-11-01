# @perfood/capacitor-google-fit

Capacitor plugin to read data from Google Fit.

## Install

```bash
npm install @perfood/capacitor-google-fit
npx cap sync
```

## API

<docgen-index>

* [`connectToGoogleFit()`](#connecttogooglefit)
* [`isAllowed()`](#isallowed)
* [`getSteps(...)`](#getsteps)
* [`getWeight(...)`](#getweight)
* [`getActivities(...)`](#getactivities)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### connectToGoogleFit()

```typescript
connectToGoogleFit() => Promise<void>
```

--------------------


### isAllowed()

```typescript
isAllowed() => Promise<IsAllowedResult>
```

**Returns:** <code>Promise&lt;<a href="#isallowedresult">IsAllowedResult</a>&gt;</code>

--------------------


### getSteps(...)

```typescript
getSteps(options: GetStepsOptions) => Promise<StepsQueryResult>
```

| Param         | Type                                                        |
| ------------- | ----------------------------------------------------------- |
| **`options`** | <code><a href="#getstepsoptions">GetStepsOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#stepsqueryresult">StepsQueryResult</a>&gt;</code>

--------------------


### getWeight(...)

```typescript
getWeight(options: GetWeightOptions) => Promise<WeightQueryResult>
```

| Param         | Type                                                          |
| ------------- | ------------------------------------------------------------- |
| **`options`** | <code><a href="#getweightoptions">GetWeightOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#weightqueryresult">WeightQueryResult</a>&gt;</code>

--------------------


### getActivities(...)

```typescript
getActivities(options: GetActivitiesOptions) => Promise<ActivitiesQueryResult>
```

| Param         | Type                                                                  |
| ------------- | --------------------------------------------------------------------- |
| **`options`** | <code><a href="#getactivitiesoptions">GetActivitiesOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#activitiesqueryresult">ActivitiesQueryResult</a>&gt;</code>

--------------------


### Interfaces


#### IsAllowedResult

| Prop            | Type                 |
| --------------- | -------------------- |
| **`isAllowed`** | <code>boolean</code> |


#### StepsQueryResult

| Prop       | Type                                                                  |
| ---------- | --------------------------------------------------------------------- |
| **`data`** | <code>{ startDate: string; endDate: string; value: number; }[]</code> |


#### GetStepsOptions

| Prop             | Type                |
| ---------------- | ------------------- |
| **`startDate`**  | <code>string</code> |
| **`endDate`**    | <code>string</code> |
| **`timeUnit`**   | <code>string</code> |
| **`bucketSize`** | <code>number</code> |


#### WeightQueryResult

| Prop       | Type                                                                  |
| ---------- | --------------------------------------------------------------------- |
| **`data`** | <code>{ startDate: string; endDate: string; value: number; }[]</code> |


#### GetWeightOptions

| Prop            | Type                |
| --------------- | ------------------- |
| **`startDate`** | <code>string</code> |
| **`endDate`**   | <code>string</code> |


#### ActivitiesQueryResult

| Prop       | Type                                                                                                                                                                                       |
| ---------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **`data`** | <code>{ startDate: string; endDate: string; activityType: string; activityTypeId: number; calories: number; steps: number; speed: number; distance: number; dataSource: string; }[]</code> |


#### GetActivitiesOptions

| Prop            | Type                |
| --------------- | ------------------- |
| **`startDate`** | <code>string</code> |
| **`endDate`**   | <code>string</code> |

</docgen-api>
