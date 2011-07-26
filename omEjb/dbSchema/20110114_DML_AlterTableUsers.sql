-- aku80 / 14.01.2011
-- MM
-- alter table users

alter table users
  add FIRSTNAME VARCHAR2(100 CHAR);

alter table users
  add LASTNAME VARCHAR2(100 CHAR);
  
alter table users
  add EMAIL VARCHAR2(150 CHAR);
  
alter table users
  add PHONE VARCHAR2(150 CHAR);
  
alter table users
  add ORGANIZATION_UNIT VARCHAR2(150 CHAR);