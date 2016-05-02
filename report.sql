select 
w.first_name, w.last_name, j.title, j.job_day, j.point_value, j.cash_value, s.schedule_date_id
from 
signup s, job j, member w
where
s.job_id = j.id and
s.worker_id = w.id
order by j.sort_order


