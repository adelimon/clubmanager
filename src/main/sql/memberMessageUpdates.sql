ALTER TABLE `member_message`
	CHANGE COLUMN `message_text` `message_text` VARCHAR(6000) NOT NULL AFTER `subject`;

ALTER TABLE `member_message`
	ADD COLUMN `send_date` DATE NULL AFTER `message_text`;
