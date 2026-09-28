# Smart IT Asset & Lifecycle Intelligence Platform

## Project Overview
Organizations manage large numbers of IT assets (laptops, desktops, monitors, network equipment). A basic asset register only tracks location and assignment, but fails to answer critical operational questions like: "Which assets are becoming risky, expensive to maintain, or close to replacement?"

## Solution
The Smart IT Asset & Lifecycle Intelligence Platform goes beyond basic CRUD by introducing an **Asset Health & Lifecycle Intelligence Engine**. It analyzes operational data—age, warranty, condition, usage, and maintenance history—to generate a deterministic health score. This allows IT administrators to proactively monitor assets, schedule maintenance, and plan replacements based on transparent business rules.

## Key Features
- **Asset Registry:** Track detailed asset attributes, assignments, and statuses.
- **Asset Health Engine:** Deterministic rule-based scoring of asset risk.
- **Lifecycle Recommendations:** Actionable intelligence derived from asset health (KEEP_IN_SERVICE, MONITOR, MAINTENANCE_REQUIRED, RETIRE).
- **Dashboard Summary:** High-level KPI metrics, dynamic charts (Chart.js), and Action Required alerts.
- **Total Cost of Ownership (TCO):** Automatic rollup of Purchase Cost + Maintenance Costs on asset details.
- **RESTful API:** Clean JSON API for integration.

## Asset Health Intelligence
The core differentiator of this platform is the `AssetHealthEngine`. It evaluates risk using the following scoring factors:
- **Age Risk:** Increased risk for assets older than 3 and 5 years.
- **Warranty Risk:** Penalties for expiring soon (<30 days) or expired warranties.
- **Maintenance Risk:** Higher risk associated with frequently maintained assets (>2 or >5 times).
- **Incident Risk:** Penalty for high incident counts (>3).
- **Condition Risk:** High penalty for POOR and CRITICAL conditions.
- **Usage Risk:** Small penalty for very high usage hours (>10,000).

The engine aggregates these factors into a transparent Health Score (0-100), which maps to:
- `HEALTHY` (Score 0-24) -> `KEEP_IN_SERVICE`
- `MONITOR` (Score 25-49) -> `MONITOR`
- `AT_RISK` (Score 50-74) -> `MAINTENANCE_REQUIRED`
- `REPLACEMENT_RECOMMENDED` (Score 75-100) -> `RETIRE`

## Architecture
HTML/CSS/JS (Vanilla + Chart.js) &rarr; REST API (JSON) &rarr; Service Layer &rarr; Repository (Spring Data JPA) &rarr; MySQL

## Technology Stack
Java 17, Spring Boot, Maven, MySQL, HTML, CSS, JavaScript, Chart.js.

## Database
Main tables include:
- `assets`: Core asset registry
- `employees`: User assignment references
- `asset_transfers`: Audit log of asset movements
- `maintenance_records`: History of asset maintenance and incidents

## API
- `GET /api/dashboard/*` - Dashboard KPIs, charts, and alerts.
- `GET|POST|PUT|DELETE /api/assets` - Core CRUD operations.
- `PUT /api/assets/{id}/assign` - Asset assignment.
- `PUT /api/assets/{id}/transfer` - Asset transfer.
- `GET|POST /api/assets/{id}/maintenance` - Log and view maintenance.
- `GET /api/assets/{id}/health` - Execute the Asset Health Engine dynamically.

## Setup
### Prerequisites
- Java 17
- Maven
- MySQL 8.0+

### Configuration
1. Copy `src/main/resources/application.properties.example` to `src/main/resources/application.properties` (or set environment variables `MYSQL_USER` and `MYSQL_PASSWORD`) to configure your local MySQL connection.
2. The application will auto-generate tables (`ddl-auto=update`) and populate realistic synthetic data from `data.sql`.

### Run Instructions
```bash
mvn clean compile
mvn clean package
java -jar target/asset-intelligence-0.0.1-SNAPSHOT.jar
```
Or run directly via maven:
```bash
./mvnw spring-boot:run
```

Access the UI at: `http://localhost:8080/index.html`

## Testing
Run unit and integration tests (which run in an isolated H2 environment using the `test` profile):
```bash
./mvnw clean test
```

## Future Enhancements
- OAuth2 authentication
- QR/barcode asset lookup
- Notification integrations
- Cloud deployment
- Predictive maintenance using historical data

---
*Disclaimer: This is a software development portfolio project and not an official enterprise asset-management system.*
