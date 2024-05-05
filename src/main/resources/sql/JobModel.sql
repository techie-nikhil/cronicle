
CREATE TABLE IF NOT EXISTS `cron_jobs` (
   `jobid` bigint(20) NOT NULL AUTO_INCREMENT,
   `job_name` varchar(255) NOT NULL,
   `description` varchar(255) DEFAULT NULL,
   `cron_expression` varchar(255) NOT NULL,
   `is_active` tinyint(1) DEFAULT '1',
   `request_type` enum('GET','POST','PUT','DELETE') NOT NULL,
   `request_url` varchar(255) NOT NULL,
   `headers` varchar(2048) DEFAULT NULL,
   `param` varchar(2048) DEFAULT NULL,
   `body` varchar(2048) DEFAULT NULL,
   `callback_url` varchar(255) DEFAULT NULL,
   `delay_between_retries` int(11) DEFAULT NULL,
   `retry_count` int(11) DEFAULT '0',
   `is_deleted` tinyint(1) DEFAULT '0',
   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`jobid`)
   );

  CREATE TABLE IF NOT EXISTS `cron_audit` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `job_id` bigint(20) NOT NULL,
    `job_name` varchar(255) NOT NULL,
    `start_time` datetime(6) DEFAULT NULL,
    `end_time` datetime(6) DEFAULT NULL,
    `status` enum('SUCCESS','FAILURE','PENDING') DEFAULT 'PENDING',
    `retry_number` int(11) NOT NULL DEFAULT '0',
    `last_failure_reason` varchar(255) DEFAULT NULL,
    `response_body` varchar(2048) DEFAULT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `IDX_JOB_ID` (`job_id`)
  );