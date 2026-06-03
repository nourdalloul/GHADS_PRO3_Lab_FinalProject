GHADS - Gaza Humanitarian Aid Distribution System
1. System Overview
GHADS is a centralized desktop application designed to streamline and coordinate humanitarian aid distribution for displaced families in Gaza. By providing a unified platform, the system ensures that aid reaches those most in need while preventing the common issue of overlapping or redundant assistance.

2. The Problem It Solves
Humanitarian organizations often work in silos, leading to a situation where some families receive duplicate aid while others are left without support. GHADS solves this by:

Maintaining a shared, centralized database accessible to all participating organizations.

Implementing automated duplication checks based on vulnerability levels and timeframes (30-day rule).

Ensuring fair and transparent resource allocation.

3. Technologies Used
Language: Java

GUI Framework: JavaFX

Database: MySQL

Connector: JDBC (MySQL Connector/J)

Development Environment: NetBeans IDE

4. Architecture Pattern
The project follows the MVC (Model-View-Controller) architectural pattern:

Model: Handles data structures and database connectivity.

View: FXML files define the user interface layouts.

Controller: Contains the logic to bridge the UI and the data, managing user events and input validation.

5. Key Features
Role-Based Access: Distinct interfaces and permissions for Admins (system-wide management) and Coordinators (organization-specific operations).

Vulnerability-Aware Distribution: The system automatically permits or blocks aid based on the family's vulnerability level and recent history.

Comprehensive Dashboards: Provides real-time statistics to help humanitarian workers make data-driven decisions.

Fairness Tracking: Easy identification of families who have not yet received any aid.

6. Screenshots
