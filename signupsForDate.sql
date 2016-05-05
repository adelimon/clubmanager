select
j.id, j.title, j.point_value, j.cash_value, j.job_day, j.sort_order, j.reserved, j.work_leader_id, sd.date, et.type
from
job j, 
schedule_date sd, 
event_type et 
where 
j.event_type_id = sd.event_type_id and
et.id = j.event_type_id
order by date, type, j.title
