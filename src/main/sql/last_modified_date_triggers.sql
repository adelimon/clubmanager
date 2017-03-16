create trigger earnedpoints_last_modified before update on earned_points for each row set new.last_modified_date = now();

create trigger member_last_modified before update on member for each row set new.last_modified_date = now();
