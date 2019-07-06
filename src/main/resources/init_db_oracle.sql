-- ORACLE 创建用户
-- Create the user
-- create user DAZER
-- identified by dazer12e4
-- default tablespace USERS
-- temporary tablespace TEMP
-- profile DEFAULT
-- quota unlimited on cbl_emergency_data;
-- -- Grant/Revoke role privileges
-- grant connect to DAZER with admin option;
-- grant resource to DAZER with admin option;
-- -- Grant/Revoke system privileges
-- grant unlimited tablespace to DAZER with admin option;


-- 系统用户
-- Create table
CREATE TABLE sys_user (
  user_id NUMBER NOT NULL,
  username varchar(50) NOT NULL ,
  password varchar(100),
  email varchar(100),
  mobile varchar(100),
  status INT,
  create_user_id NUMBER(20),
  create_time TIMESTAMP,
  CONSTRAINT PK_SYS_USER PRIMARY KEY (user_id)
);
-- Add comments to the table
COMMENT ON TABLE sys_user IS '系统用户';
-- Add comments to the columns
comment on column sys_user.user_id is '用户id';
comment on column sys_user.username is '用户名';
comment on column sys_user.password is '密码';
comment on column sys_user.email is '邮箱';
comment on column sys_user.mobile is '手机号';
comment on column sys_user.status is '状态  0：禁用   1：正常';
comment on column sys_user.create_user_id is '创建者ID';
comment on column sys_user.create_time is '创建时间';
-- Create/Recreate primary, unique and foreign key constraints
-- Create/Recreate indexes
create unique index idx_sys_user on sys_user (username);
-- Create/Recreate sequence
create sequence SEQU_SYS_USER
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
nocache;

-- 创建自增触发器,取消掉
-- create or replace
-- trigger TRIG_SYS_USER_AUTOINC
-- before insert on sys_user   -- fangwuxinxibiao 是表名
-- for each row
--   declare
--     nextid number; -- 定义一个中间变量
--   begin
--     IF :new.user_id IS NULL or :new.user_id = 0 THEN --FANGWUID 是字段名 其中的new是当前使用的表
--       select SEQU_SYS_USER.nextval into nextid from dual; -- dual是一个伪表，百度便知
--       :new.user_id := nextid;
--     end if;
--   end;



-- 菜单
-- Create table
CREATE TABLE SYS_MENU(
  MENU_ID NUMBER NOT NULL,
  PARENT_ID NUMBER ,
  NAME VARCHAR2(50),
  URL VARCHAR2(200),
  PERMS VARCHAR2(500),
  TYPE INT,
  ICON VARCHAR2(50),
  ORDER_NUM INT,
  CONSTRAINT PK_SYS_MENU PRIMARY KEY(MENU_ID)
) ;
-- Add comments to the table
COMMENT ON TABLE SYS_MENU IS '菜单管理';
-- Add comments to the columns
comment on column SYS_MENU.MENU_ID is '菜单自增id';
comment on column SYS_MENU.PARENT_ID IS '父菜单ID，一级菜单为0';
comment on column SYS_MENU.NAME IS '菜单名称';
comment on column SYS_MENU.URL IS '菜单URL';
comment on column SYS_MENU.PERMS IS '授权(多个用逗号分隔，如：user:list,user:create)';
comment on column SYS_MENU.TYPE IS '类型   0：目录   1：菜单   2：按钮';
comment on column SYS_MENU.ICON IS '菜单图标';
comment on column SYS_MENU.ORDER_NUM IS '排序';
-- Create/Recreate primary, unique and foreign key constraints
-- Create/Recreate sequence
create sequence SEQU_SYS_MENU
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
nocache;



-- 菜单
-- Create table
CREATE TABLE SYS_MENU(
  MENU_ID NUMBER NOT NULL,
  PARENT_ID NUMBER ,
  NAME VARCHAR2(50),
  URL VARCHAR2(200),
  PERMS VARCHAR2(500),
  TYPE INT,
  ICON VARCHAR2(50),
  ORDER_NUM INT,
  CONSTRAINT PK_SYS_MENU PRIMARY KEY(MENU_ID)
) ;
-- Add comments to the table
COMMENT ON TABLE SYS_MENU IS '菜单管理';
-- Add comments to the columns
comment on column SYS_MENU.MENU_ID is '菜单自增id';
comment on column SYS_MENU.PARENT_ID IS '父菜单ID，一级菜单为0';
comment on column SYS_MENU.NAME IS '菜单名称';
comment on column SYS_MENU.URL IS '菜单URL';
comment on column SYS_MENU.PERMS IS '授权(多个用逗号分隔，如：user:list,user:create)';
comment on column SYS_MENU.TYPE IS '类型   0：目录   1：菜单   2：按钮';
comment on column SYS_MENU.ICON IS '菜单图标';
comment on column SYS_MENU.ORDER_NUM IS '排序';
-- SYS_MENU 使用随机主键

-- 角色
CREATE TABLE SYS_ROLE (
role_id NUMBER NOT NULL,
role_name varchar(100),
remark varchar(100),
create_user_id NUMBER(20),
create_time TIMESTAMP,
  CONSTRAINT PK_SYS_ROLE PRIMARY KEY (role_id)
) ;
-- Add comments to the table
COMMENT ON TABLE SYS_ROLE IS '角色';
-- Add comments to the columns
comment on column SYS_ROLE.role_id is '角色id';
comment on column SYS_ROLE.role_name is '角色名称';
comment on column SYS_ROLE.remark is '备注';
comment on column SYS_ROLE.create_user_id is '创建者ID';
comment on column SYS_ROLE.create_time is '创建时间';
-- Create/Recreate primary, unique and foreign key constraints
-- Create/Recreate indexes
create unique index idx_sys_role on SYS_ROLE (role_name);
-- Create/Recreate sequence
create sequence SEQU_SYS_ROLE
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
nocache;


-- 用户与角色对应关系
CREATE TABLE SYS_USER_ROLE (
id NUMBER NOT NULL,
user_id NUMBER,
role_id NUMBER,
 CONSTRAINT PK_SYS_USER_ROLE PRIMARY KEY (id)
);
-- Add comments to the table
COMMENT ON TABLE SYS_USER_ROLE IS '用户与角色对应关系';
-- Add comments to the columns
comment on column SYS_USER_ROLE.id is 'id';
comment on column SYS_USER_ROLE.user_id is '用户ID';
comment on column SYS_USER_ROLE.role_id is '角色ID';
-- Create/Recreate sequence
create sequence SEQU_SYS_USER_ROLE
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
nocache;

-- 创建自增触发器
create or replace
trigger TRIG_SYS_USER_ROLE_AUTOINC
before insert on SYS_USER_ROLE   -- fangwuxinxibiao 是表名
for each row
  declare
    nextid number; -- 定义一个中间变量
  begin
    IF :new.id IS NULL or :new.id = 0 THEN --FANGWUID 是字段名 其中的new是当前使用的表
      select SEQU_SYS_USER_ROLE.nextval into nextid from dual; -- dual是一个伪表，百度便知
      :new.id := nextid;
    end if;
  end;


-- 角色与菜单对应关系
CREATE TABLE SYS_ROLE_MENU (
  ID NUMBER NOT NULL ,
  ROLE_ID NUMBER,
  MENU_ID NUMBER,
  CONSTRAINT PK_SYS_ROLE_MENU PRIMARY KEY (ID)
);
-- Add comments to the table
COMMENT ON TABLE SYS_ROLE_MENU IS '角色与菜单对应关系';
-- Add comments to the columns
comment on column SYS_ROLE_MENU.ID is 'id';
comment on column SYS_ROLE_MENU.ROLE_ID is '角色ID';
comment on column SYS_ROLE_MENU.MENU_ID is '菜单ID';
-- Create/Recreate sequence
CREATE SEQUENCE SEQU_SYS_ROLE_MENU
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
nocache;
-- 创建自增触发器
create or replace
trigger TRIG_SYS_ROLE_MENU_AUTOINC
before insert on SYS_ROLE_MENU   -- fangwuxinxibiao 是表名
for each row
  declare
    nextid number; -- 定义一个中间变量
  begin
    IF :new.ID IS NULL or :new.ID = 0 THEN --FANGWUID 是字段名 其中的new是当前使用的表
      select SEQU_SYS_ROLE_MENU.nextval into nextid from dual; -- dual是一个伪表，百度便知
      :new.ID := nextid;
    end if;
  end;


-- 系统配置信息
CREATE TABLE SYS_CONFIG (
  ID VARCHAR2(50) default sys_guid() NOT NULL,
  key VARCHAR2(50),
  value VARCHAR2(2000),
  status INT DEFAULT 1,
  remark VARCHAR2(500),
  CONSTRAINT PK_SYS_CONFIG PRIMARY KEY (ID)
);


-- Add comments to the table
COMMENT ON TABLE SYS_CONFIG IS '系统配置信息表';
-- Add comments to the columns
comment on column SYS_CONFIG.ID is 'id';
comment on column SYS_CONFIG.key is 'key';
comment on column SYS_CONFIG.value is 'value';
comment on column SYS_CONFIG.status is '状态   0：隐藏   1：显示';
comment on column SYS_CONFIG.remark is '备注';

-- 系统日志
CREATE TABLE SYS_LOG (
  ID VARCHAR2(50) NOT NULL ,
  username VARCHAR2(50),
  operation VARCHAR2(50),
  method varchar(200),
  params VARCHAR2(4000),
  ip VARCHAR2(64),
  create_date TIMESTAMP,
  PRIMARY KEY (ID)
);
-- Add comments to the table
COMMENT ON TABLE SYS_LOG IS '系统日志';
-- Add comments to the columns
comment on column SYS_LOG.ID is 'id';
comment on column SYS_LOG.username is '用户名';
comment on column SYS_LOG.operation is '用户操作';
comment on column SYS_LOG.method is '请求方法';
comment on column SYS_LOG.params is '备注';
comment on column SYS_LOG.ip is 'IP地址';
comment on column SYS_LOG.create_date is '创建时间';


-- 初始数据
INSERT INTO sys_user (user_id, username, password, email, mobile, status, create_time) VALUES ('1', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '100@qq.com', '13666666666', '1', to_timestamp('2017-06-01 15:33:20', 'yyyy-mm-dd hh24:mi:ss'));
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('1', '0', '系统管理', NULL, NULL, '0', 'fa fa-cog', '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('2', '1', '用户管理', 'sys/user.html', NULL, '1', 'fa fa-user', '1');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('3', '1', '角色管理', 'sys/Sysrole.html', NULL, '1', 'fa fa-user-secret', '2');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('4', '1', '菜单管理', 'sys/menu.html', NULL, '1', 'fa fa-th-list', '3');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('5', '1', 'SQL监控', 'druid/sql.html', NULL, '1', 'fa fa-bug', '4');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('15', '2', '查看', NULL, 'sys:user:list,sys:user:info', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('16', '2', '新增', NULL, 'sys:user:save,sys:role:select', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('17', '2', '修改', NULL, 'sys:user:update,sys:role:select', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('18', '2', '删除', NULL, 'sys:user:delete', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('19', '3', '查看', NULL, 'sys:role:list,sys:role:info', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('20', '3', '新增', NULL, 'sys:role:save,sys:menu:perms', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('21', '3', '修改', NULL, 'sys:role:update,sys:menu:perms', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('22', '3', '删除', NULL, 'sys:role:delete', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('23', '4', '查看', NULL, 'sys:menu:list,sys:menu:info', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('24', '4', '新增', NULL, 'sys:menu:save,sys:menu:select', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('25', '4', '修改', NULL, 'sys:menu:update,sys:menu:select', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('26', '4', '删除', NULL, 'sys:menu:delete', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('27', '1', '参数管理', 'sys/config.html', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', '1', 'fa fa-sun-o', '6');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('29', '1', '系统日志', 'sys/log.html', 'sys:log:list', '1', 'fa fa-file-text-o', '7');


INSERT INTO sys_config (id, key, value, status, remark) VALUES ('1', 'CLOUD_STORAGE_CONFIG_KEY',
                                                            '{\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"aliyunDomain\":\"\",\"aliyunEndPoint\":\"\",\"aliyunPrefix\":\"\",\"qcloudBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuBucketName\":\"ios-app\",\"qiniuDomain\":\"http://7xqbwh.dl1.z0.glb.clouddn.com\",\"qiniuPrefix\":\"upload\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"type\":1}',
                                                            '0',
                                                            '云存储配置信息');


-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- API接口相关SQL，如果不使用api模块，则不用执行下面SQL -------------------------------------------------------------------------------------------------------------
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- 用户表
CREATE TABLE XA_TB_USER (
  USER_ID NUMBER NOT NULL ,
  USERNAME VARCHAR2(50) NOT NULL ,
  MOBILE VARCHAR2(20) ,
  PASSWORD VARCHAR2(64) NOT NULL ,
  CREATE_TIME TIMESTAMP,
  CONSTRAINT PK_TB_USER PRIMARY KEY (USER_ID)
);
COMMENT ON TABLE  XA_TB_USER IS '用户';
COMMENT ON COLUMN XA_TB_USER.USER_ID IS '用户名';
COMMENT ON COLUMN XA_TB_USER.USERNAME IS '手机号';
COMMENT ON COLUMN XA_TB_USER.MOBILE IS '密码';
COMMENT ON COLUMN XA_TB_USER.PASSWORD IS '创建时间';
COMMENT ON COLUMN XA_TB_USER.CREATE_TIME IS '创建时间';


-- 用户Token表
CREATE TABLE TB_TOKEN(
  USER_ID NUMBER NOT NULL,
  TOKEN VARCHAR2(100) NOT NULL ,
  EXPIRE_TIME TIMESTAMP,
  UPDATE_TIME TIMESTAMP,
  CONSTRAINT PK_TB_TOKEN PRIMARY KEY (USER_ID)
);
COMMENT ON TABLE XA_TB_TOKEN IS '用户Token';
CREATE UNIQUE INDEX idx_tb_token ON XA_TB_TOKEN (TOKEN);
COMMENT ON COLUMN XA_TB_TOKEN.USER_ID IS '用户id';
COMMENT ON COLUMN XA_TB_TOKEN.TOKEN IS 'token';
COMMENT ON COLUMN XA_TB_TOKEN.EXPIRE_TIME IS '过期时间';
COMMENT ON COLUMN XA_TB_TOKEN.UPDATE_TIME IS '更新时间';
COMMIT ;

-- 账号：13888888888  密码：admin
INSERT INTO XA_TB_USER (username, mobile, password, create_time) VALUES ('test', '13888888888', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',  to_timestamp('2017-03-23 22:37:41','yyyy-mm-dd hh24:mi:ss'));

-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- 代码生成器相关SQL，如果不使用gen模块，则不用执行下面SQL -------------------------------------------------------------------------------------------------------------
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('28', '1', '代码生成器', 'sys/generator.html', 'sys:generator:list,sys:generator:code', '1', 'fa fa-rocket', '8');

-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- 定时任务相关表结构，如果不使用schedule模块，则不用执行下面SQL -------------------------------------------------------------------------------------------------------------
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- 初始化菜单数据
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('6', '1', '定时任务管理', 'sys/schedule.html', NULL, '1', 'fa fa-tasks', '5');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('7', '6', '查看', NULL, 'sys:schedule:list,sys:schedule:info', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('8', '6', '新增', NULL, 'sys:schedule:save', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('9', '6', '修改', NULL, 'sys:schedule:update', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('10', '6', '删除', NULL, 'sys:schedule:delete', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('11', '6', '暂停', NULL, 'sys:schedule:pause', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('12', '6', '恢复', NULL, 'sys:schedule:resume', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('13', '6', '立即执行', NULL, 'sys:schedule:run', '2', NULL, '0');
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('14', '6', '日志列表', NULL, 'sys:schedule:log', '2', NULL, '0');

-- 定时任务
CREATE TABLE XA_SCHEDULE_JOB(
  job_Id NUMBER NOT NULL,
  bean_name VARCHAR2(200) DEFAULT NULL ,
  method_name VARCHAR2(100) DEFAULT NULL ,
  params VARCHAR2(2000) DEFAULT NULL ,
  cron_expression VARCHAR2(100) DEFAULT NULL ,
  status INT DEFAULT NULL ,
  remark VARCHAR2(255) DEFAULT NULL ,
  create_time TIMESTAMP DEFAULT NULL ,
  CONSTRAINT PK_SCHEDULE_JOB PRIMARY KEY (job_Id)
);
COMMENT ON TABLE XA_SCHEDULE_JOB IS '定时任务';
COMMENT ON COLUMN XA_SCHEDULE_JOB.job_Id IS '任务id';
COMMENT ON COLUMN XA_SCHEDULE_JOB.bean_name IS 'spring bean名称';
COMMENT ON COLUMN XA_SCHEDULE_JOB.method_name IS '方法名';
COMMENT ON COLUMN XA_SCHEDULE_JOB.params IS '参数';
COMMENT ON COLUMN XA_SCHEDULE_JOB.cron_expression IS 'cron表达式';
COMMENT ON COLUMN XA_SCHEDULE_JOB.status IS '任务状态';
COMMENT ON COLUMN XA_SCHEDULE_JOB.remark IS '备注';
COMMENT ON COLUMN XA_SCHEDULE_JOB.create_time IS '创建时间';
CREATE SEQUENCE SEQU_SCHEDULE_JOB
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
nocache;
-- 创建自增触发器
create or replace
trigger TRIG_SCHEDULE_JOB_AUTOINC
before insert on XA_SCHEDULE_JOB   -- fangwuxinxibiao 是表名
for each row
  declare
    nextid number; -- 定义一个中间变量
  begin
    IF :new.job_Id IS NULL or :new.job_Id = 0 THEN --FANGWUID 是字段名 其中的new是当前使用的表
      select SEQU_SYS_LOG.nextval into nextid from dual; -- dual是一个伪表，百度便知
      :new.job_Id := nextid;
    end if;
  end;

-- 定时任务日志
CREATE TABLE XA_SCHEDULE_JOB_LOG(
  log_id NUMBER NOT NULL,
  job_id VARCHAR2(200) DEFAULT NULL ,
  bean_name VARCHAR2(100) DEFAULT NULL ,
  method_name VARCHAR2(2000) DEFAULT NULL ,
  params VARCHAR2(100) DEFAULT NULL ,
  status INT DEFAULT NULL ,
  error VARCHAR2(255) DEFAULT NULL ,
  times NUMBER DEFAULT NULL ,
  create_time TIMESTAMP DEFAULT NULL ,
  CONSTRAINT SCHEDULE_JOB_LOG_JOB PRIMARY KEY (log_id)
);
COMMENT ON TABLE  XA_SCHEDULE_JOB_LOG IS '定时任务日志';
COMMENT ON COLUMN XA_SCHEDULE_JOB_LOG.log_id IS '任务日志id';
COMMENT ON COLUMN XA_SCHEDULE_JOB_LOG.job_id IS '任务id';
COMMENT ON COLUMN XA_SCHEDULE_JOB_LOG.bean_name IS 'spring bean名称';
COMMENT ON COLUMN XA_SCHEDULE_JOB_LOG.method_name IS '方法名';
COMMENT ON COLUMN XA_SCHEDULE_JOB_LOG.params IS '参数';
COMMENT ON COLUMN XA_SCHEDULE_JOB_LOG.status IS '任务状态    0：成功    1：失败';
COMMENT ON COLUMN XA_SCHEDULE_JOB_LOG.error IS '失败信息';
COMMENT ON COLUMN XA_SCHEDULE_JOB_LOG.times IS '耗时(单位：毫秒)';
COMMENT ON COLUMN XA_SCHEDULE_JOB_LOG.create_time IS '创建时间';
CREATE SEQUENCE SEQU_SCHEDULE_JOB_LOG
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
nocache;

-- 创建自增触发器
create or replace
trigger TRIG_SCHEDULE_JOB_LOG_AUTOINC
before insert on XA_SCHEDULE_JOB_LOG   -- fangwuxinxibiao 是表名
for each row
  declare
    nextid number; -- 定义一个中间变量
  begin
    IF :new.log_id IS NULL or :new.log_id = 0 THEN --FANGWUID 是字段名 其中的new是当前使用的表
      select SEQU_SCHEDULE_JOB_LOG.nextval into nextid from dual; -- dual是一个伪表，百度便知
      :new.log_id := nextid;
    end if;
  end;


INSERT INTO XA_SCHEDULE_JOB (bean_name, method_name, params, cron_expression, status, remark, create_time) VALUES ('testTask', 'test', 'para', '0 0/30 * * * ?', '0', '有参数测试',  to_timestamp('2017-06-01 23:16:46','yyyy-mm-dd hh24:mi:ss'));
INSERT INTO XA_SCHEDULE_JOB (bean_name, method_name, params, cron_expression, status, remark, create_time) VALUES ('testTask', 'test2', NULL, '0 0/30 * * * ?', '1', '无参数测试', to_timestamp('2017-06-03 14:55:56','yyyy-mm-dd hh24:mi:ss'));

--  quartz自带表结构，请在官网进行下载
-- delete from qrtz_fired_triggers;
-- delete from qrtz_simple_triggers;
-- delete from qrtz_simprop_triggers;
-- delete from qrtz_cron_triggers;
-- delete from qrtz_blob_triggers;
-- delete from qrtz_triggers;
-- delete from qrtz_job_details;
-- delete from qrtz_calendars;
-- delete from qrtz_paused_trigger_grps;
-- delete from qrtz_locks;
-- delete from qrtz_scheduler_state;
--
-- drop table qrtz_calendars;
-- drop table qrtz_fired_triggers;
-- drop table qrtz_blob_triggers;
-- drop table qrtz_cron_triggers;
-- drop table qrtz_simple_triggers;
-- drop table qrtz_simprop_triggers;
-- drop table qrtz_triggers;
-- drop table qrtz_job_details;
-- drop table qrtz_paused_trigger_grps;
-- drop table qrtz_locks;
-- drop table qrtz_scheduler_state;


CREATE TABLE qrtz_job_details
(
  SCHED_NAME VARCHAR2(120) NOT NULL,
  JOB_NAME  VARCHAR2(200) NOT NULL,
  JOB_GROUP VARCHAR2(200) NOT NULL,
  DESCRIPTION VARCHAR2(250) NULL,
  JOB_CLASS_NAME   VARCHAR2(250) NOT NULL,
  IS_DURABLE VARCHAR2(1) NOT NULL,
  IS_NONCONCURRENT VARCHAR2(1) NOT NULL,
  IS_UPDATE_DATA VARCHAR2(1) NOT NULL,
  REQUESTS_RECOVERY VARCHAR2(1) NOT NULL,
  JOB_DATA BLOB NULL,
  CONSTRAINT QRTZ_JOB_DETAILS_PK PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);
CREATE TABLE qrtz_triggers
(
  SCHED_NAME VARCHAR2(120) NOT NULL,
  TRIGGER_NAME VARCHAR2(200) NOT NULL,
  TRIGGER_GROUP VARCHAR2(200) NOT NULL,
  JOB_NAME  VARCHAR2(200) NOT NULL,
  JOB_GROUP VARCHAR2(200) NOT NULL,
  DESCRIPTION VARCHAR2(250) NULL,
  NEXT_FIRE_TIME NUMBER(13) NULL,
  PREV_FIRE_TIME NUMBER(13) NULL,
  PRIORITY NUMBER(13) NULL,
  TRIGGER_STATE VARCHAR2(16) NOT NULL,
  TRIGGER_TYPE VARCHAR2(8) NOT NULL,
  START_TIME NUMBER(13) NOT NULL,
  END_TIME NUMBER(13) NULL,
  CALENDAR_NAME VARCHAR2(200) NULL,
  MISFIRE_INSTR NUMBER(2) NULL,
  JOB_DATA BLOB NULL,
  CONSTRAINT QRTZ_TRIGGERS_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  CONSTRAINT QRTZ_TRIGGER_TO_JOBS_FK FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
  REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
);
CREATE TABLE qrtz_simple_triggers
(
  SCHED_NAME VARCHAR2(120) NOT NULL,
  TRIGGER_NAME VARCHAR2(200) NOT NULL,
  TRIGGER_GROUP VARCHAR2(200) NOT NULL,
  REPEAT_COUNT NUMBER(7) NOT NULL,
  REPEAT_INTERVAL NUMBER(12) NOT NULL,
  TIMES_TRIGGERED NUMBER(10) NOT NULL,
  CONSTRAINT QRTZ_SIMPLE_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  CONSTRAINT QRTZ_SIMPLE_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_cron_triggers
(
  SCHED_NAME VARCHAR2(120) NOT NULL,
  TRIGGER_NAME VARCHAR2(200) NOT NULL,
  TRIGGER_GROUP VARCHAR2(200) NOT NULL,
  CRON_EXPRESSION VARCHAR2(120) NOT NULL,
  TIME_ZONE_ID VARCHAR2(80),
  CONSTRAINT QRTZ_CRON_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  CONSTRAINT QRTZ_CRON_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_simprop_triggers
(
  SCHED_NAME VARCHAR2(120) NOT NULL,
  TRIGGER_NAME VARCHAR2(200) NOT NULL,
  TRIGGER_GROUP VARCHAR2(200) NOT NULL,
  STR_PROP_1 VARCHAR2(512) NULL,
  STR_PROP_2 VARCHAR2(512) NULL,
  STR_PROP_3 VARCHAR2(512) NULL,
  INT_PROP_1 NUMBER(10) NULL,
  INT_PROP_2 NUMBER(10) NULL,
  LONG_PROP_1 NUMBER(13) NULL,
  LONG_PROP_2 NUMBER(13) NULL,
  DEC_PROP_1 NUMERIC(13,4) NULL,
  DEC_PROP_2 NUMERIC(13,4) NULL,
  BOOL_PROP_1 VARCHAR2(1) NULL,
  BOOL_PROP_2 VARCHAR2(1) NULL,
  CONSTRAINT QRTZ_SIMPROP_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  CONSTRAINT QRTZ_SIMPROP_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_blob_triggers
(
  SCHED_NAME VARCHAR2(120) NOT NULL,
  TRIGGER_NAME VARCHAR2(200) NOT NULL,
  TRIGGER_GROUP VARCHAR2(200) NOT NULL,
  BLOB_DATA BLOB NULL,
  CONSTRAINT QRTZ_BLOB_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  CONSTRAINT QRTZ_BLOB_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_calendars
(
  SCHED_NAME VARCHAR2(120) NOT NULL,
  CALENDAR_NAME  VARCHAR2(200) NOT NULL,
  CALENDAR BLOB NOT NULL,
  CONSTRAINT QRTZ_CALENDARS_PK PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);
CREATE TABLE qrtz_paused_trigger_grps
(
  SCHED_NAME VARCHAR2(120) NOT NULL,
  TRIGGER_GROUP  VARCHAR2(200) NOT NULL,
  CONSTRAINT QRTZ_PAUSED_TRIG_GRPS_PK PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_fired_triggers
(
  SCHED_NAME VARCHAR2(120) NOT NULL,
  ENTRY_ID VARCHAR2(95) NOT NULL,
  TRIGGER_NAME VARCHAR2(200) NOT NULL,
  TRIGGER_GROUP VARCHAR2(200) NOT NULL,
  INSTANCE_NAME VARCHAR2(200) NOT NULL,
  FIRED_TIME NUMBER(13) NOT NULL,
  SCHED_TIME NUMBER(13) NOT NULL,
  PRIORITY NUMBER(13) NOT NULL,
  STATE VARCHAR2(16) NOT NULL,
  JOB_NAME VARCHAR2(200) NULL,
  JOB_GROUP VARCHAR2(200) NULL,
  IS_NONCONCURRENT VARCHAR2(1) NULL,
  REQUESTS_RECOVERY VARCHAR2(1) NULL,
  CONSTRAINT QRTZ_FIRED_TRIGGER_PK PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);
CREATE TABLE qrtz_scheduler_state
(
  SCHED_NAME VARCHAR2(120) NOT NULL,
  INSTANCE_NAME VARCHAR2(200) NOT NULL,
  LAST_CHECKIN_TIME NUMBER(13) NOT NULL,
  CHECKIN_INTERVAL NUMBER(13) NOT NULL,
  CONSTRAINT QRTZ_SCHEDULER_STATE_PK PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);
CREATE TABLE qrtz_locks
(
  SCHED_NAME VARCHAR2(120) NOT NULL,
  LOCK_NAME  VARCHAR2(40) NOT NULL,
  CONSTRAINT QRTZ_LOCKS_PK PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

create index idx_qrtz_j_req_recovery on qrtz_job_details(SCHED_NAME,REQUESTS_RECOVERY);
create index idx_qrtz_j_grp on qrtz_job_details(SCHED_NAME,JOB_GROUP);

create index idx_qrtz_t_j on qrtz_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_qrtz_t_jg on qrtz_triggers(SCHED_NAME,JOB_GROUP);
create index idx_qrtz_t_c on qrtz_triggers(SCHED_NAME,CALENDAR_NAME);
create index idx_qrtz_t_g on qrtz_triggers(SCHED_NAME,TRIGGER_GROUP);
create index idx_qrtz_t_state on qrtz_triggers(SCHED_NAME,TRIGGER_STATE);
create index idx_qrtz_t_n_state on qrtz_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_qrtz_t_n_g_state on qrtz_triggers(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_qrtz_t_next_fire_time on qrtz_triggers(SCHED_NAME,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st on qrtz_triggers(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_misfire on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st_misfire on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
create index idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers(SCHED_NAME,INSTANCE_NAME);
create index idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
create index idx_qrtz_ft_j_g on qrtz_fired_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_qrtz_ft_jg on qrtz_fired_triggers(SCHED_NAME,JOB_GROUP);
create index idx_qrtz_ft_t_g on qrtz_fired_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
create index idx_qrtz_ft_tg on qrtz_fired_triggers(SCHED_NAME,TRIGGER_GROUP);