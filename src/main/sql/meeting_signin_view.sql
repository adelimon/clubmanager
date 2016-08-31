create or replace view meeting_signin as
select
m.id member_id, m.last_name, m.first_name, sd.id meeting_id, sd.date meeting_date
from
member m, schedule_date sd
where
m.status != 9 and
sd.event_type_id = 7 and
sd.date > now() and
sd.date < DATE_ADD(now(), INTERVAL 14 DAY)
order by
m.last_name
