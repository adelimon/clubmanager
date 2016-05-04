select
"" worker, j.title, j.point_value, j.cash_value, j.job_day, sd.date, concat(wl.first_name, ' ', wl.last_name) leader
from
job j, 
schedule_date sd,
member wl
where 
sd.date = '2016-05-22' and
j.event_type_id = sd.event_type_id and 
wl.id = j.work_leader_id
order by sd.date, j.title