# OneMAC Automation Framework

This repository contains **OneMAC** automation scripts for the OneMAC application, featuring **ExtentReports** for clear reporting. **GitHub Actions** is configured to run the tests in CI/CD pipelines.

## Framework Overview
The OneMAC Automation framework leverages **Selenium WebDriver** and **TestNG** to perform end-to-end UI testing of the OneMAC application. It uses a **modular, data-driven structure**, where configuration parameters such as browser type, headless mode, and environment settings are maintained in `config.properties`. Test execution is managed via **Maven**, with **ExtentReports** providing rich, HTML-based reports and failure screenshots. Continuous Integration is handled through **GitHub Actions**, enabling automated test runs on schedule or when changes are pushed to the repository.

## Directory Structure
```text
OneMAC-Automation/
├── .github/
│   └── workflows/               # GitHub Actions CI pipelines
│
├── extent-report/               # Extent HTML reports + screenshots
├── logs/                        # Test execution logs
│
├── src/
│   ├── main/java/gov/cms/onemac/
│   │   ├── pages/               # Page Object Model (POM)
│   │   │   ├── LoginPage.java
│   │   │   ├── DashboardPage.java
│   │   │   ├── SubmitMedicaidSpaPage.java
│   │   │   ├── WaiverSubmissionPages...
│   │   │
│   │   ├── models/              # SPA & Waiver POJOs
│   │   │   ├── BasePackage.java
│   │   │   ├── SpaPackage.java
│   │   │   └── WaiverPackage.java
│   │   │
│   │   └── utils/               # Framework utilities
│   │       ├── DriverFactory.java
│   │       ├── ConfigReader.java
│   │       ├── PageFactory.java
│   │       ├── UIElementUtils.java
│   │       ├── SpaIdGenerator.java
│   │       ├── WaiverIdGenerator.java
│   │       ├── StateCounterTracker.java
│   │       ├── ExcelPackageTracker.java
│   │       ├── ExcelPackageSelector.java
│   │       └── ScreenshotUtil.java
│   │
│   ├── test/java/gov/cms/onemac/
│   │   ├── base/
│   │   │   └── BaseTest.java
│   │   ├── tests/
│   │   │   ├── AuthenticationTests.java
│   │   │   └── DynamicWaiverTests.java
│   │   └── utils/
│   │       ├── TestListener.java
│   │       └── PackagePoolGenerator.java
│   │
│   └── resources/
│       ├── config.properties
│       ├── log4j2.xml
│       ├── packages.xlsx               # Generated test data (SPA + Waivers)
│       └── state_counters.xlsx         # Controlled increment for each state
│
├── pom.xml
└── README.md


```
## 1. Prerequisites

Before running the tests, make sure you have the following installed:

* **Java 17**
* **Maven**
* **Git**
* Chrome or Firefox (for local runs)



## 2. Configuration: Browser and Headless Mode

The browser and headless mode are configured in the `config.properties` file located at:

`src/test/resources/config.properties`

**Example `config.properties`:**

```properties
browser=chrome
headless=true
```
* browser can be chrome or firefox

* headless can be true or false

## 3. Clone Repository

Clone the repository to your local machine:

`git clone https://github.com/jad248/onemac-automation.git`

`cd onemac-automation`

## 4. Running Tests Locally

Run tests using Maven:

`mvn clean test`


* The browser and headless settings are taken from config.properties.

* ExtentReports will be generated in extent-report/.

* Screenshots of failures will be saved in extent-report/screenshots/.

## 5. GitHub Actions (CI)

* Workflow is defined in .github/workflows/selenium-testng.yml.

* On push to main or manual trigger, GitHub Actions will:

  * Set up Java 17

  * Install Chrome (or Firefox if configured)

  * Run Maven tests

  * Upload ExtentReports and screenshots as artifacts

**Note:** For stability, Chrome is recommended on CI.

## 6. Reports

* ExtentReports are generated at: `extent-report/OneMACTestReport.html`

* Screenshots of failed tests: `extent-report/screenshots/`

* Reports are uploaded as artifacts in GitHub Actions for each workflow run.

## 7. Git Operations

   **Adding Changes**

   `git add .`

   `git commit -m "Your commit message"`

  `git push origin main`


* Ensure your Personal Access Token (PAT) is set up for GitHub authentication.

**Creating/Updating Workflows**

* `.github/workflows/selenium-testng.yml` defines the CI workflow.

* Any changes to workflow files require a PAT with workflow scope.

## 8. Troubleshooting

* **SessionNotCreatedException / Marionette errors:**

  * Ensure the Firefox or Chrome driver version matches the browser version.

  * Headless mode arguments for CI:
  
```properties
--headless=new
--width=1920
--height=1080
--no-sandbox
--disable-dev-shm-usage
--disable-gpu

```

* **Browser installed incorrectly on CI:**

  * Firefox may fail on Linux runners; Chrome is recommended.

* **SLF4J warnings:**

  * These are logging warnings and can be ignored unless logging is required.

* **ExtentReports not uploading:**

  * Make sure `path` in `upload-artifact` action matches `extent-report/**` and `extent-report/screenshots/**`.

  * Ensure if: `always()` is used in workflow step to upload reports even on test failures.

* **Running a single browser:**

Use the browser specified in `config.properties` and pass it as a system property if needed:

mvn clean test -Dbrowser=firefox -Dheadless=true

## 9. Additional Notes

* Keep `config.properties` updated for headless or browser preferences.

* CI workflow is currently optimized for Linux runners.

* Ensure GitHub Actions runner has network access to download browsers and WebDriver binaries.


