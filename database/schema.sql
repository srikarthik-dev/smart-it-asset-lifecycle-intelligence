CREATE DATABASE IF NOT EXISTS it_asset_intelligence;
USE it_asset_intelligence;

-- Tables will be created automatically by Hibernate ddl-auto=update in development.
-- Here is the reference schema for production:

CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    department VARCHAR(255),
    email VARCHAR(255)
);

CREATE TABLE assets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_code VARCHAR(255) UNIQUE NOT NULL,
    asset_type VARCHAR(255),
    brand VARCHAR(255),
    model VARCHAR(255),
    serial_number VARCHAR(255) UNIQUE,
    purchase_date DATE,
    purchase_cost DOUBLE,
    warranty_end_date DATE,
    assigned_to VARCHAR(255),
    employee_id VARCHAR(255),
    department VARCHAR(255),
    location VARCHAR(255),
    status VARCHAR(255),
    condition_state VARCHAR(255),
    usage_hours INT DEFAULT 0,
    maintenance_count INT DEFAULT 0,
    incident_count INT DEFAULT 0,
    last_maintenance_date DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE asset_transfers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_id BIGINT NOT NULL,
    previous_employee VARCHAR(255),
    previous_employee_id VARCHAR(255),
    new_employee VARCHAR(255),
    new_employee_id VARCHAR(255),
    previous_location VARCHAR(255),
    new_location VARCHAR(255),
    reason VARCHAR(255),
    transfer_date TIMESTAMP,
    FOREIGN KEY (asset_id) REFERENCES assets(id)
);

CREATE TABLE maintenance_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_id BIGINT NOT NULL,
    type VARCHAR(255),
    description VARCHAR(255),
    reported_date DATE,
    completed_date DATE,
    cost DOUBLE,
    severity VARCHAR(255),
    technician VARCHAR(255),
    status VARCHAR(255),
    FOREIGN KEY (asset_id) REFERENCES assets(id)
);
