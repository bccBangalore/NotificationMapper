create database notification_sms;
use notification_sms;

create table notification_tracker (id int , notificationId varchar(50),configuredRetries int, 
notificationPayload JSON, sourceSystem varchar(20), processedRetries int, status varchar(20)
,primary key(id));
