CREATE OR REPLACE VIEW signup_report AS
    SELECT (`j`.`id` + `sd`.`id`) AS `id`,`j`.`id` AS `job_id`,`sd`.`id` AS `schedule_date_id`, CONCAT(`w`.`first_name`,' ',`w`.`last_name`) AS `name`,`j`.`title` AS `title`,`j`.`point_value` AS `point_value`,`j`.`cash_value` AS `cash_value`,`j`.`meal_ticket` AS `meal_ticket`,`j`.`reserved` AS `reserved`,`j`.`job_day` AS `job_day`,`wl`.`last_name` AS `leader`,`sd`.`date` AS `date`,`s`.`id` AS `signup_id`
    FROM ((((`job` `j`
        LEFT JOIN `signup` `s` ON(((`s`.`job_id` = `j`.`id`) AND (`s`.`schedule_date_id` = (
            SELECT `schedule_date`.`id`
            FROM `schedule_date`
            WHERE ((`schedule_date`.`event_type_id` IN (1,5,8)) AND (YEAR(`schedule_date`.`date`) = YEAR(NOW())) AND (`schedule_date`.`date` > NOW()))
            ORDER BY `schedule_date`.`date`
            LIMIT 1)))))
        JOIN `schedule_date` `sd` ON(((`sd`.`event_type_id` = `j`.`event_type_id`) AND (`sd`.`date` > NOW()))))
        LEFT JOIN `member` `w` ON((`w`.`id` = `s`.`worker_id`)))
        LEFT JOIN `member` `wl` ON((`wl`.`id` = `j`.`work_leader_id`)))
    WHERE (`sd`.`date` = (
        SELECT `schedule_date`.`date`
        FROM `schedule_date`
        WHERE ((`schedule_date`.`event_type_id` IN (1,5,8)) AND (YEAR(`schedule_date`.`date`) = YEAR(NOW())) AND (`schedule_date`.`date` > NOW()))
        ORDER BY `schedule_date`.`date`
        LIMIT 1))
    ORDER BY `j`.`sort_order`;
