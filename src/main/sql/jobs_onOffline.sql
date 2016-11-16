ALTER TABLE `job` ADD COLUMN `online` BIT NULL DEFAULT b'1' AFTER `work_leader_id`;
