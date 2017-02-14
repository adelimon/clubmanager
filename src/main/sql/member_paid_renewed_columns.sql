ALTER TABLE `member` ADD COLUMN `current_year_paid` BIT NOT NULL AFTER `current_year_points`;
update member set current_year_paid = 0;
ALTER TABLE `member` ADD COLUMN `current_year_renewed` BIT NOT NULL AFTER `current_year_points`;
update member set current_year_renewed = 0;
