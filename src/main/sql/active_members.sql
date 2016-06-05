ALTER TABLE `member` ADD COLUMN `is_active` BIT NOT NULL AFTER `current_year_points`;
-- anyone with points is active
update member set is_active = 1 where current_year_points > 0;
-- as well as senior and on deck members, and paid labor
update member set is_active = 1 where status in (4,7,9);
-- and the 2016 board members that are both life members and actually doing stuff
update member set is_active = 1 where id in (22, 32);
