-- Insert sample employees
INSERT IGNORE INTO employees (employee_id, name, department, email) VALUES ('E001', 'Alice Smith', 'Engineering', 'alice@example.com');
INSERT IGNORE INTO employees (employee_id, name, department, email) VALUES ('E002', 'Bob Jones', 'Marketing', 'bob@example.com');
INSERT IGNORE INTO employees (employee_id, name, department, email) VALUES ('E003', 'Charlie Brown', 'HR', 'charlie@example.com');

-- Insert sample assets
-- 1. Healthy Asset
INSERT IGNORE INTO assets (asset_code, asset_type, brand, model, serial_number, purchase_date, purchase_cost, warranty_end_date, assigned_to, employee_id, department, location, status, condition_state, usage_hours, maintenance_count, incident_count, last_maintenance_date, created_at, updated_at) 
VALUES ('A001', 'LAPTOP', 'Dell', 'XPS 13', 'SN001', '2024-05-01', 1200.00, '2025-05-01', 'E001', 'E001', 'Engineering', 'NY', 'ASSIGNED', 'EXCELLENT', 100, 0, 0, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 2. Monitor Asset
INSERT IGNORE INTO assets (asset_code, asset_type, brand, model, serial_number, purchase_date, purchase_cost, warranty_end_date, assigned_to, employee_id, department, location, status, condition_state, usage_hours, maintenance_count, incident_count, last_maintenance_date, created_at, updated_at) 
VALUES ('A002', 'MONITOR', 'LG', '27UN850', 'SN002', '2022-01-01', 400.00, '2025-01-01', 'E002', 'E002', 'Marketing', 'CA', 'ASSIGNED', 'GOOD', 4000, 1, 0, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. At-Risk Asset
INSERT IGNORE INTO assets (asset_code, asset_type, brand, model, serial_number, purchase_date, purchase_cost, warranty_end_date, assigned_to, employee_id, department, location, status, condition_state, usage_hours, maintenance_count, incident_count, last_maintenance_date, created_at, updated_at) 
VALUES ('A003', 'LAPTOP', 'Apple', 'MacBook Pro', 'SN003', '2020-01-01', 2000.00, '2021-01-01', 'E003', 'E003', 'HR', 'NY', 'ASSIGNED', 'POOR', 8000, 3, 2, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 4. Replacement Recommended Asset
INSERT IGNORE INTO assets (asset_code, asset_type, brand, model, serial_number, purchase_date, purchase_cost, warranty_end_date, assigned_to, employee_id, department, location, status, condition_state, usage_hours, maintenance_count, incident_count, last_maintenance_date, created_at, updated_at) 
VALUES ('A004', 'DESKTOP', 'HP', 'EliteDesk', 'SN004', '2018-01-01', 1000.00, '2021-01-01', NULL, NULL, 'IT', 'Storage', 'AVAILABLE', 'CRITICAL', 15000, 6, 4, '2023-01-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 5. Under Maintenance Asset
INSERT IGNORE INTO assets (asset_code, asset_type, brand, model, serial_number, purchase_date, purchase_cost, warranty_end_date, assigned_to, employee_id, department, location, status, condition_state, usage_hours, maintenance_count, incident_count, last_maintenance_date, created_at, updated_at) 
VALUES ('A005', 'PRINTER', 'Epson', 'WorkForce', 'SN005', '2023-01-01', 300.00, '2025-01-01', NULL, NULL, 'Engineering', 'NY', 'UNDER_MAINTENANCE', 'FAIR', 500, 2, 1, '2024-01-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
