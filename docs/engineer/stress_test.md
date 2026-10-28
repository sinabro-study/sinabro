## Lock 시간 집계

```sql
SHOW GLOBAL STATUS 
WHERE Variable_name IN ( 
  'Innodb_row_lock_time', 
  'Innodb_row_lock_waits', 
  'Innodb_row_lock_current_waits'  
);
```

## Deadlock

```sql
SELECT NAME, COUNT, COUNT_RESET, STATUS
FROM information_schema.INNODB_METRICS
WHERE NAME = 'lock_deadlocks';

SHOW ENGINE INNODB STATUS;
```
