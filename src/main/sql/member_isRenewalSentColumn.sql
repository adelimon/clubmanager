ALTER TABLE `member` ADD COLUMN `renewal_sent` BIT NOT NULL AFTER `current_year_points`;
update member set renewal_sent = 0;
