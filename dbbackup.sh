#!/bin/bash
mysqldump --no-create-info -c -u root clubmanager earned_points event_type job member schedule_date signup > ../dbdump.sql
