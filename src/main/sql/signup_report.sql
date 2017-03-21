create or replace view signup_report as
select
(j.id + sd.id) id,
j.id job_id,
sd.id schedule_date_id,
concat(w.first_name,' ', w.last_name) name,
j.title, j.point_value, j.cash_value, j.reserved, j.job_day, wl.last_name leader,  sd.date
from
job j
left join signup s on (s.job_id = j.id  and s.schedule_date_id in (select id from schedule_date where date > now()))
inner join schedule_date sd on (sd.event_type_id = j.event_type_id and sd.date > now())
left join member w on w.id = s.worker_id
left join member  wl on wl.id = j.work_leader_id
where
date = (select date from schedule_date where year(date) = year(now()) and date > now() order by date limit 1)
order by sort_order
