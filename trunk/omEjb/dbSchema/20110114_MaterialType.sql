CREATE SEQUENCE material_type_seq;

CREATE TABLE material_type
(
   ID               NUMBER CONSTRAINT material_type_pk PRIMARY KEY,
   client_id        VARCHAR2( 20 CHAR )
         NOT NULL CONSTRAINT material_type_f0 REFERENCES client( ID ),
   material_type_id VARCHAR2( 10 CHAR )
         NOT NULL CONSTRAINT material_type_u0 UNIQUE,
   NAME             VARCHAR2( 128 CHAR ) NOT NULL,
   STATUS           VARCHAR2( 30 CHAR )
         NOT NULL
         CONSTRAINT material_type_f1 REFERENCES masterdatastatus( ID ),
   TRANSACTION_ID   NUMBER
         CONSTRAINT material_type_f2
             REFERENCES transaction( ID ) ON DELETE SET NULL,
   VERSION          NUMBER NOT NULL,
   USERNAME         VARCHAR2( 30 CHAR ),
   MACHINE          VARCHAR2( 64 CHAR ),
   PROGRAM          VARCHAR2( 48 CHAR ),
   SECURITY_LABEL   VARCHAR2( 30 CHAR ) NOT NULL,
   CONSTRAINT material_type_f3 FOREIGN KEY
      ( client_id, security_label )
       REFERENCES security_labels( client_id, ID )
);

CREATE OR REPLACE TRIGGER MATERIAL_TYPE_T0
   BEFORE INSERT
   ON MATERIAL_TYPE
   REFERENCING NEW AS NEW OLD AS OLD
   FOR EACH ROW
DECLARE
   l_nId   PLS_INTEGER;
BEGIN
   IF ( :NEW.ID IS NULL )
   THEN
      SELECT MATERIAL_TYPE_seq.NEXTVAL INTO l_nId FROM DUAL;

      :NEW.ID := l_nId;
   END IF;
END MATERIAL_TYPE_T0;

CREATE OR REPLACE TRIGGER MATERIAL_TYPE_T1
   AFTER DELETE OR INSERT OR UPDATE
   ON MATERIAL_TYPE
   REFERENCING NEW AS NEW OLD AS OLD
   FOR EACH ROW
BEGIN
   TRANSACTION_PKG.ENTITY_TX_TRIGGER_TEMPLATE( INSERTING,
                                               UPDATING,
                                               DELETING,
                                               :OLD.transaction_id,
                                               :NEW.transaction_id,
                                               'MATERIAL_TYPE',
                                               :NEW.CLIENT_ID,
                                               :OLD.STATUS,
                                               :NEW.STATUS );
END MATERIAL_TYPE_T1;

CREATE OR REPLACE TRIGGER MATERIAL_TYPE_T2
   BEFORE INSERT OR UPDATE
   ON MATERIAL_TYPE
   REFERENCING NEW AS NEW OLD AS OLD
   FOR EACH ROW
BEGIN
   :NEW.username :=
      SYS_CONTEXT( SECURITY_PKG.GET_CTX_NAME, 'APPLICATION_USER' );
   :NEW.machine := SYS_CONTEXT( 'USERENV', 'HOST' );
   :NEW.PROGRAM := SYS_CONTEXT( 'USERENV', 'MODULE' );
END MATERIAL_TYPE_T2;

CREATE OR REPLACE TRIGGER MATERIAL_TYPE_T3
   BEFORE INSERT OR UPDATE OR DELETE
   ON MATERIAL_TYPE
   FOR EACH ROW
BEGIN
   IF INSERTING OR UPDATING
   THEN
      IF :NEW.transaction_id IS NOT NULL
      THEN
         UPDATE transaction
            SET entity_count = entity_count + 1
          WHERE ID = :NEW.transaction_id;
      END IF;
   END IF;

   IF UPDATING OR DELETING
   THEN
      IF :OLD.transaction_id IS NOT NULL
      THEN
         UPDATE transaction
            SET entity_count = entity_count - 1
          WHERE ID = :OLD.transaction_id;
      END IF;
   END IF;
END MATERIAL_TYPE_T3;

ALTER TABLE material_type FLASHBACK ARCHIVE  mddev_arch;
EXEC DBMS_RLS.add_policy(object_schema=>'MDDEV', object_name=>'MATERIAL_TYPE', policy_name=>'SECURITY_LABEL_FILTERING', policy_function=>'VPD_PKG.SECURITY_LABEL_FILTERING');
EXEC dbms_rls.add_policy(object_schema=>'MDDEV', object_name=>'MATERIAL_TYPE', policy_name=>'SECURITY_LABEL_INSERTING', policy_function=>'VPD_PKG.SECURITY_LABEL_INSERTING', statement_types=>'INSERT');
EXEC dbms_rls.add_policy(object_schema=>'MDDEV', object_name=>'MATERIAL_TYPE', policy_name=>'SECURITY_LABEL_UPDATING', policy_function=>'VPD_PKG.SECURITY_LABEL_UPDATING', statement_types=>'UPDATE');
EXEC dbms_rls.add_policy(object_schema=>'MDDEV', object_name=>'MATERIAL_TYPE', policy_name=>'SECURITY_LABEL_DELETING', policy_function=>'VPD_PKG.SECURITY_LABEL_DELETING', statement_types=>'DELETE');

INSERT INTO VALIDATION_TABLES( table_name, msg_key )
     VALUES ( 'MATERIAL_TYPE', 'commonMaterialType' );

INSERT INTO VALIDATION_COLUMNS( table_name, column_name, msg_key )
     VALUES ( 'MATERIAL_TYPE', 'MATERIAL_TYPE_ID', 'commonMaterialType' );

INSERT INTO VALIDATION_COLUMNS( table_name, column_name, msg_key )
     VALUES ( 'MATERIAL_TYPE', 'NAME', 'partnerName' );

COMMIT;
EXEC VALIDATION_PKG.REBUILD_VALIDATION;

ALTER TRIGGER costcenter_t1 DISABLE;
EXEC DBMS_RLS.drop_policy('MDDEV', 'COSTCENTER', 'SECURITY_LABEL_FILTERING');
EXEC DBMS_RLS.drop_policy('MDDEV', 'COSTCENTER', 'SECURITY_LABEL_DELETING');

DELETE FROM costcenter;

COMMIT;
EXEC DBMS_RLS.add_policy(object_schema=>'MDDEV', object_name=>'COSTCENTER', policy_name=>'SECURITY_LABEL_FILTERING', policy_function=>'VPD_PKG.SECURITY_LABEL_FILTERING');
EXEC dbms_rls.add_policy(object_schema=>'MDDEV', object_name=>'COSTCENTER', policy_name=>'SECURITY_LABEL_DELETING', policy_function=>'VPD_PKG.SECURITY_LABEL_DELETING', statement_types=>'DELETE');
EXEC GWBOSS.DROPFBAFORTABLE('MDDEV', 'COSTCENTER');
ALTER TABLE costcenter DROP COLUMN MATERIAL_TYPE;
ALTER TABLE costcenter ADD (material_type_id NUMBER CONSTRAINT costcenter_f4 REFERENCES material_type(ID));
ALTER TABLE costcenter FLASHBACK ARCHIVE mddev_arch;

DELETE FROM VALIDATION_COLUMNS c
      WHERE C.TABLE_NAME = 'COSTCENTER' AND C.COLUMN_NAME = 'MATERIAL_TYPE';

COMMIT;
EXEC VALIDATION_PKG.REBUILD_VALIDATION;