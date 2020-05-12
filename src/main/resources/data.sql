insert into employee (id, version, deleted, employee_code, full_name, last_name, email, physical_address, postal_address, start_date, end_date, occupation, status,created_by,created_date) values
('65ed6644718b11eabc550242ac130003',0,false,'emp1','John', 'Travolta', 'JohnT@hollywood.com','Fancy Mansion, Hollywood', 'VIP Postal Box',CURRENT_DATE, NULL,'Actor','Active','system',CURRENT_TIMESTAMP),
('1061cfca718c11eabc550242ac130003',0,false,'emp2','Cristiano', 'Ronaldo', 'CR7@bestplayer.com','Real Madrid, Spain', 'Ballondor, France',CURRENT_DATE, NULL,'Soccer Player','Active','system', CURRENT_TIMESTAMP);
insert into project (id, version, code, name, client_code, start_date, end_date, type, deleted,created_by,created) values
('d5436fd9ec184fe4a0402d66992be407',0,'ECS','Equities Clearing System', 'JSE', CURRENT_DATE,NULL, 'BILLABLE',FALSE,'system',CURRENT_TIMESTAMP),
('0b5af6c17acb4ab0a3812a670daa1e42',0,'RSM','Resource Management POC', 'PSY', CURRENT_DATE, DATEADD('DAY',14, CURRENT_DATE), 'NON_BILLABLE', FALSE,'system',CURRENT_TIMESTAMP);
insert into task (id, version, code, name, project_id, deleted, created_by,created) values
('2e60b4f5f4f74dd1a62d92c2ea864be7',0,'DEV_ECS','Development', 'd5436fd9ec184fe4a0402d66992be407', FALSE,'system',CURRENT_TIMESTAMP),
('e3140fd9abbc4b348de5414b4a530f2e',0,'ANL_ECS','Analysis', 'd5436fd9ec184fe4a0402d66992be407', FALSE,'system',CURRENT_TIMESTAMP),
('636bb7841b98443aa4f6de90158a6952',0,'DEV_RSM','Development', '0b5af6c17acb4ab0a3812a670daa1e42', FALSE,'system',CURRENT_TIMESTAMP),
('163d87495fd04e67aaad6ec6233b8dda',0,'ANL_RSM','Analysis', '0b5af6c17acb4ab0a3812a670daa1e42', FALSE,'system',CURRENT_TIMESTAMP);

insert into allocation (id, version, employee_id, project_id, created_by, created, updated, updated_by, deleted) values
('d5436fd9ec184fe4a0402d66992be407', 0, '65ed6644718b11eabc550242ac130003', 'd5436fd9ec184fe4a0402d66992be407', 'mahlori', CURRENT_DATE, CURRENT_DATE, 'mahlori', FALSE),
('0b5af6c17acb4ab0a3812a670daa1e42', 0, '65ed6644718b11eabc550242ac130003', '0b5af6c17acb4ab0a3812a670daa1e42', 'mahlori', CURRENT_DATE, CURRENT_DATE, 'mahlori', FALSE),
('163d87495fd04e67aaad6ec6233b8dda', 0, '1061cfca718c11eabc550242ac130003', '0b5af6c17acb4ab0a3812a670daa1e42', 'mahlori', CURRENT_DATE, CURRENT_DATE, 'mahlori', FALSE);
insert into Time_Entry (id, version, employee_id, task_id, status, status_reason, description, period, date, deleted, created_by, created) values
('56269d672e0b44e58c9541d2b20d7a75',0,'65ed6644718b11eabc550242ac130003', '2e60b4f5f4f74dd1a62d92c2ea864be7', 'NEW','Dummy data','Batch and Support',500, CURRENT_DATE, FALSE,'system',CURRENT_TIMESTAMP),
('94b97da909e44aab9816913e109463a7',0,'65ed6644718b11eabc550242ac130003', 'e3140fd9abbc4b348de5414b4a530f2e', 'NEW','Dummy data','Batch and Support',1800, CURRENT_DATE, FALSE,'system',CURRENT_TIMESTAMP),
('68cb92b9e00b413a963298f575543428',0,'1061cfca718c11eabc550242ac130003', '636bb7841b98443aa4f6de90158a6952', 'NEW', 'Dummy data',NULL,36000, CURRENT_DATE, FALSE,'system', CURRENT_TIMESTAMP);
insert into Status_History (id, version, status, status_update_reason, entry_id, time_stamp, deleted, created_by, created) values
('082042d0f5d34b7f949c33d112d95847',0,'NEW','Dummy data','56269d672e0b44e58c9541d2b20d7a75', CURRENT_TIMESTAMP, FALSE, 'system', CURRENT_TIMESTAMP),
('79f111acdb0d4959b1fd2544fca91b71',0,'NEW','Dummy data','94b97da909e44aab9816913e109463a7', CURRENT_TIMESTAMP, FALSE, 'system', CURRENT_TIMESTAMP),
('78f58a50ecb3462f9cd22d91d42604f9',0,'NEW','Dummy data','68cb92b9e00b413a963298f575543428', CURRENT_TIMESTAMP, FALSE, 'system', CURRENT_TIMESTAMP);
