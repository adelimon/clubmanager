ALTER TABLE earned_points ADD COLUMN last_modified_date DATETIME;
ALTER TABLE member ADD COLUMN last_modified_date DATETIME;
ALTER TABLE job ADD COLUMN last_modified_date DATETIME;
ALTER TABLE signup ADD COLUMN last_modified_date DATETIME;

ALTER TABLE earned_points ADD COLUMN last_modified_by VARCHAR(255);
ALTER TABLE member ADD COLUMN last_modified_by VARCHAR(255);
ALTER TABLE job ADD COLUMN last_modified_by VARCHAR(255);
ALTER TABLE signup ADD COLUMN last_modified_by VARCHAR(255);
