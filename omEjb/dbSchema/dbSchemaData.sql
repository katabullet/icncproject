Insert into TRANSACTIONSTATUS
   (ID)
 Values
   ('ALLOCATED');
Insert into TRANSACTIONSTATUS
   (ID)
 Values
   ('APPROVAL_PENDING');
Insert into TRANSACTIONSTATUS
   (ID)
 Values
   ('APPROVED');
Insert into TRANSACTIONSTATUS
   (ID)
 Values
   ('EDITING');
Insert into TRANSACTIONSTATUS
   (ID)
 Values
   ('NEW');
Insert into TRANSACTIONSTATUS
   (ID)
 Values
   ('REJECTED_FOR_DISCARD');
Insert into TRANSACTIONSTATUS
   (ID)
 Values
   ('REJECTED_FOR_REEDIT');

Insert into TRANSACTIONSTATUS_CHANGE
   (OLD_STATE, NEW_STATE)
 Values
   ('APPROVED', 'ALLOCATED');
Insert into TRANSACTIONSTATUS_CHANGE
   (OLD_STATE, NEW_STATE)
 Values
   ('EDITING', 'APPROVAL_PENDING');
Insert into TRANSACTIONSTATUS_CHANGE
   (OLD_STATE, NEW_STATE)
 Values
   ('NEW', 'APPROVAL_PENDING');
Insert into TRANSACTIONSTATUS_CHANGE
   (OLD_STATE, NEW_STATE)
 Values
   ('APPROVAL_PENDING', 'APPROVED');
Insert into TRANSACTIONSTATUS_CHANGE
   (OLD_STATE, NEW_STATE)
 Values
   ('REJECTED_FOR_REEDIT', 'EDITING');
Insert into TRANSACTIONSTATUS_CHANGE
   (OLD_STATE, NEW_STATE)
 Values
   ('APPROVAL_PENDING', 'REJECTED_FOR_DISCARD');
Insert into TRANSACTIONSTATUS_CHANGE
   (OLD_STATE, NEW_STATE)
 Values
   ('APPROVAL_PENDING', 'REJECTED_FOR_REEDIT');

Insert into MASTERDATASTATUS
   (ID)
 Values
   ('ALLOCATED');
Insert into MASTERDATASTATUS
   (ID)
 Values
   ('DEACTIVATED');
Insert into MASTERDATASTATUS
   (ID)
 Values
   ('EDITING');
Insert into MASTERDATASTATUS
   (ID)
 Values
   ('MARKED_FOR_DEACTIVATION');
Insert into MASTERDATASTATUS
   (ID)
 Values
   ('NEW');

Insert into ADD_DISCARD_TABLES
   (ENTITY_TYPE, TABLE_NAME)
 Values
   ('SHIPPER', 'SHIPPER');
Insert into ADD_DISCARD_TABLES
   (ENTITY_TYPE, TABLE_NAME)
 Values
   ('SHIPPER', 'SHIPPER_ADDRESS');
Insert into ADD_DISCARD_TABLES
   (ENTITY_TYPE, TABLE_NAME)
 Values
   ('SHIPPER', 'SHIPPER_SERVICE');
COMMIT;
