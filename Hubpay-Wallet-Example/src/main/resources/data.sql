INSERT INTO HUBPAY_USER(ID,USER_NAME,USER_EMAIL,USER_MOBILE) VALUES
('HP1234','HUBPAY_USER1','HUBPAY_USER1@GMAIL.COM','07404338267'),
('HP4567','HUBPAY_USER2','HUBPAY_USER2@GMAIL.AE','0505957324'),
('HP7890','HUBPAY_USER3','HUBPAY_USER3@GMAIL.UK','8886356111');

INSERT INTO HUBPAY_WALLET(USER_ID,BALANCE,IS_ACTIVE,VERSION) VALUES
('HP1234',0,TRUE,CURRENT_DATE),
('HP4567',0,TRUE,CURRENT_DATE),
('HP7890',0,TRUE,CURRENT_DATE);
