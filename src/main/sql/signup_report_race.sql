create view signup_report_race as
select
concat(w.first_name,' ', w.last_name) name,
j.title, j.point_value, j.cash_value, j.reserved, j.job_day, wl.last_name leader,  sd.date
from
job j
left join signup s on (s.job_id = j.id  and s.schedule_date_id in (select id from schedule_date where date > now()))
inner join schedule_date sd on (sd.event_type_id = j.event_type_id and sd.date > now())
left join member w on w.id = s.worker_id
left join member  wl on wl.id = j.work_leader_id
where
j.event_type_id = (select id from event_type where type = 'RACE')
order by date, title
