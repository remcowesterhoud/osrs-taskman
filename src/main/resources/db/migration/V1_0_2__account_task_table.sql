CREATE TABLE IF NOT EXISTS account_task (

    id SERIAL NOT NULL PRIMARY KEY,
    account_id int NOT NULL REFERENCES account,
    task_id int NOT NULL REFERENCES task,
    start_time timestamp with time zone NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC'),
    end_time timestamp with time zone NULL
);