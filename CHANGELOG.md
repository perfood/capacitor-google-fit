# [0.2.3](https://github.com/Ad-Scientiam/capacitor-google-fit/compare/v0.2.2...v0.2.3) (2020-10-30)

### Update

- **android:** remove isexpired() check

# [0.2.2](https://github.com/Ad-Scientiam/capacitor-google-fit/compare/v0.2.1...v0.2.2) (2020-10-30)

### Update

- **android:** improve connectToGoogleFit and add security in case of no account

# [0.2.1](https://github.com/Ad-Scientiam/capacitor-google-fit/compare/v0.2.0...v0.2.1) (2020-10-19)

### Fixed

- **android:** prevent null exception in isAllowed()

# [0.2.0](https://github.com/Ad-Scientiam/capacitor-google-fit/compare/v0.1.1...v0.2.0) (2020-10-19)

### Added

- **android:** added isAllowed() method which return a boolean

### Fixed

- **android:** getHistoryActivity() resolve and returns list of activities
- **android:** getHistory() resolve and returns a list of daily aggregated data

### Removed

- **android:** Remove getAccountData() and getTodayData() due to lack of support, feel free to contribute if needed!

# [0.1.1](https://github.com/Ad-Scientiam/capacitor-google-fit/compare/v0.1.0...v0.1.1) (2020-10-14)

### WIP

- **android:** getHistoryActivity() resolve and try to fetch sessions (WIP)

# [0.1.0](https://github.com/Ad-Scientiam/capacitor-google-fit/compare/v0.0.6...v0.1.0) (2020-08-28)

### Fixed

- **android:** connectToGoogleFit() resolve promise normally

# [0.0.6](https://github.com/Ad-Scientiam/capacitor-google-fit/compare/v0.0.5...v0.0.6) (2020-01-07)

### Fixed

- **ios:** Remove iOS from capacitor to enable iOS usage without error
