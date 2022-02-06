# Internet-bank application
<br>
<hr>

## Description
Internet-bank is a server-side REST-application that is implemented with using<br> 
the Spring boot framework version 2.6.2.<br>
Its current capabilities allow a remote user to conduct the following<br> 
operations with a bank account: creation, deletion, receipt balance,<br>
withdrawal of money, replenishment.<br>
To interact with a remote user, the application implements REST API. Data<br>
exchange takes place in .JSON format.<br>
All account information is stored in a database managed by the PostgreSQL DBMS.<br>
In case of conflict situations in the application, they are processed using<br>
the exception mechanism. The result of exception handling allows you to issue<br> 
to the remote user exhaustive answers.<br>
All these situations, as well as successful completion of operations, are<br> 
logged to files, located in the project folder<br> 
internet-bank/src/main/resources/logs.<br><br> 
<hr>

## Structural elements

* REST API implemented in package ru.dreremin.internetbank.controllers.<br>
  Classes responsible for implementation:<br>
  InternetBankController, ManagementBankController,OperationsController;<br>
* The service layer is represented by the package<br> 
  ru.dreremin.internetbank.services with classes:<br>
  BankAccountService, BankAccountManager, OperationService,<br>
  OperationDescriptionService, TransferRecepientService;<br>
* Models that display database entities are contained in the package<br> 
  ru.dreremin.internetbank.models. They represented by classes: BankAccount,<br>
  Client, Operation, OperationType, TransferRecipient. Class-model<br>
  OperationDescription don't is mapping any a table from database.<br>
  This is a model of a string transmitted to the client with information<br>
  about the operation with the account;<br>
* In package ru.dreremin.internetbank.repositories placed interfaces<br>
  inherited from JpaRepository<T,ID>: BankAccountRepository,<br>
  ClientRepository, OperationDescriptionRepository, OperationRepository,<br>
  TransferRecipientRepository. They are necessary for direct interaction with<br>
  the database through the mechanism Spring Data JPA;<br>
* In package ru.dreremin.internetbank.exceptions placed custom exception<br>
  classes: DataMissingException, DateTimeOutOfBoundsException,<br>
  IncorrectNumberException, NotEnoughMoneyException, SameIdException,<br>
  UniquenessViolationException. They are necessary for handle especial<br>
  conflict situations and representation full information to client.<br>
  To manage exceptions was provided the class<br>
  ru.dreremin.internetbank.controllers.ExceptionController;<br>
* To receive and transmit data in .JSON format, was implemented classes<br>
  located in the package ru.dreremin.internetbank.dto: StatusOperationDTO,<br>
  OperationListDTO, DateTimeOfPeriodWithZoneIdDto, BankAccountDTO,<br>
  BalanceDTO, SenderIdAndMoneyAndRecipientIdDTO, ClientIdDTO,<br>
  ClientIdAndMoneyDTO.<br><br>
<hr>

## App interaction points

App interaction points represented methods of classes InternetBankController,<br>
ManagementBankAccountController, OperationsController:<br>
* getBalance() - accepts the client's ID, returns the amount on the account of<br>
  this client;<br>
* putMoney() - accepts the client's ID and amount, adds specified value of<br> 
  amount to amount on account of client with specified ID;<br>
* takeMoney() - accepts the client's ID and amount, substracts specified value<br>
  of amount to amount on account of client with specified ID;<br>
* transferMoney() - accepts the sender's ID, recipient's ID and amount,<br>
  substracts specified value of amount to amount on account of sender<br>
  with specified ID, then adds specified value of amount to amount on<br>
  account of recipient with specified ID;<br>
* getOperationList() - accepts two dates and a time zone, returns the operation<br>
  list in the specified dates range. If one or both dates are null then returns<br>
  a list of all operations;<br>
* createAccount() - accepts the client's ID, created the a new account with the<br>
  specified client's ID and an amount equal 0.0;<br>
* deleteAccount() - accepts the client's ID, deleted the an account with the<br>
  specified client's ID.<br>

The getBalance(), putMoney(), takeMoney and transferMoney() methods also<br>
receive the date, time and time zone without fail. This data is required<br>
because all of these methods create a new entry in the database table each<br>
time they succeed.<br>

<hr>

## Particularities of the application  starting

1. To run the application, you need to specify the VM option<br>
  -Dspring.profiles.active=main. This is necessary because the active profile<br> 
  by default set for testing.<br>
2. It is necessary to set the absolute path to the project main folder in the<br>
   LOG_DIR environment variable (/...absolute_path.../internet-bank/src).<br>
   This is required to set up entries in the log files.

<hr>

## Database
DBMS - PostgreSQL<br>
Сurrently the database is represented by the following tables:<br>

<table>
    <thead>
        <tr>
            <th colspan="4">bank_account</th>
        </tr>
        <tr>
            <th>Attribute</th>
            <th>Data type</th>
            <th>Constraints</th>
            <th>Default</th>
        </tr>
    </thead>
    <tbody>
    <tr>
        <td>id</td>
        <td>bigserial</td>
        <td>not null, primary key, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>client_id</td>
        <td>bigint</td>
        <td>not null, client_id > 0, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>current_balance</td>
        <td>numeric(20.2)</td>
        <td>not null, current_balance >= 0</td>
        <td>0.00</td>
    </tr>
    <tr>
      <th colspan="4"></th>
    </tr>
    </tbody>
    <thead>
        <tr>
            <th colspan="4">operation</th>
        </tr>
        <tr>
            <th>Attribute</th>
            <th>Data type</th>
            <th>Constraints</th>
            <th>Default</th>
        </tr>
    </thead>
    <tbody>
    <tr>
        <td>id</td>
        <td>bigserial</td>
        <td>not null, primary key, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>account_id</td>
        <td>bigint</td>
        <td>not null, account_id > 0, foreign key</td>
        <td></td>
    </tr>
    <tr>
        <td>operation_type_id</td>
        <td>integer</td>
        <td>not null, operation_type_id > 0, foreign key</td>
        <td></td>
    </tr>
    <tr>
        <td>date_time</td>
        <td>timestamptz</td>
        <td>not null</td>
        <td></td>
    </tr>
    <tr>
        <td>transaction_amount</td>
        <td>numeric(20,2)</td>
        <td>not null, transaction_amount >= 0</td>
        <td>0.0</td>
    </tr>
    </tbody>
    <tr>
      <th colspan="4"></th>
    </tr>
    <thead>
        <tr>
            <th colspan="4">operation_type</th>
        </tr>
        <tr>
            <th>Attribute</th>
            <th>Data type</th>
            <th>Constraints</th>
            <th>Default</th>
        </tr>
    </thead>
    <tbody>
    <tr>
        <td>id</td>
        <td>serial</td>
        <td>not null, primary key, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>operation_name</td>
        <td>varchar(100)</td>
        <td>not null</td>
        <td></td>
    </tr>
    </tbody>
    <tr>
      <th colspan="4"></th>
    </tr>
    <thead>
        <tr>
            <th colspan="4">transfer_recipient</th>
        </tr>
        <tr>
            <th>Attribute</th>
            <th>Data type</th>
            <th>Constraints</th>
            <th>Default</th>
        </tr>
    </thead>
    <tbody>
    <tr>
        <td>recipient_account_id</td>
        <td>bigint</td>
        <td>not null, recipient_account_id > 0, foreign key</td>
        <td></td>
    </tr>
    <tr>
        <td>operation_id</td>
        <td>bigint</td>
        <td>not null, recipient_account_id > 0, foreign key</td>
        <td></td>
    </tr>
    <tr>
        <td></td>
        <td></td>
        <td>unique(recipient_account_id, operation_id)</td>
        <td></td>
    </tr>
    </tbody>
    <tr>
      <th colspan="4"></th>
    </tr>
    <thead>
        <tr>
            <th colspan="4">client</th>
        </tr>
        <tr>
            <th>Attribute</th>
            <th>Data type</th>
            <th>Constraints</th>
            <th>Default</th>
        </tr>
    </thead>
    <tbody>
    <tr>
        <td>id</td>
        <td>bigserial</td>
        <td>not null, primary key, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>firstname</td>
        <td>text</td>
        <td>not null, must contain only letters</td>
        <td></td>
    </tr>
    <tr>
        <td>lastname</td>
        <td>text</td>
        <td>not null, must contain only letters</td>
        <td></td>
    </tr>
    <tr>
        <td>patronymic</td>
        <td>text</td>
        <td>not null, must contain only letters</td>
        <td></td>
    </tr>
    <tr>
        <td>birthday</td>
        <td>date</td>
        <td>not null</td>
        <td></td>
    </tr>
    <tr>
        <td>address</td>
        <td>text</td>
        <td>not null</td>
        <td></td>
    </tr>
    </tbody>
    <tr>
      <th colspan="4"></th>
    </tr>
    <thead>
        <tr>
            <th colspan="4">email</th>
        </tr>
        <tr>
            <th>Attribute</th>
            <th>Data type</th>
            <th>Constraints</th>
            <th>Default</th>
        </tr>
    </thead>
    <tbody>
    <tr>
        <td>id</td>
        <td>bigserial</td>
        <td>not null, primary key, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>email_address</td>
        <td>text</td>
        <td>not null, ssss@ssss.lll, unique</td>
        <td></td>
    </tr>
    </tbody>
    <tr>
      <th colspan="4"></th>
    </tr>
    <thead>
        <tr>
            <th colspan="4">email_client</th>
        </tr>
        <tr>
            <th>Attribute</th>
            <th>Data type</th>
            <th>Constraints</th>
            <th>Default</th>
        </tr>
    </thead>
    <tbody>
    <tr>
        <td>email_id</td>
        <td>bigint</td>
        <td>not null, foreign key, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>client_id</td>
        <td>bigint</td>
        <td>not null, foreign key</td>
        <td></td>
    </tr>
    </tbody>
    <tr>
      <th colspan="4"></th>
    </tr>
    <thead>
        <tr>
            <th colspan="4">phone</th>
        </tr>
        <tr>
            <th>Attribute</th>
            <th>Data type</th>
            <th>Constraints</th>
            <th>Default</th>
        </tr>
    </thead>
    <tbody>
    <tr>
        <td>id</td>
        <td>bigserial</td>
        <td>not null, primary key, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>phone_number</td>
        <td>varchar(16)</td>
        <td>not null, +7(ddd)ddd-dd-dd, unique</td>
        <td></td>
    </tr>
    </tbody>
    <tr>
      <th colspan="4"></th>
    </tr>
    <thead>
        <tr>
            <th colspan="4">phone_client</th>
        </tr>
        <tr>
            <th>Attribute</th>
            <th>Data type</th>
            <th>Constraints</th>
            <th>Default</th>
        </tr>
    </thead>
    <tbody>
    <tr>
        <td>phone_id</td>
        <td>bigint</td>
        <td>not null, foreign key, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>client_id</td>
        <td>bigint</td>
        <td>not null, foreign key</td>
        <td></td>
    </tr>
    </tbody>
    </tr>
    </tbody>
    <tr>
      <th colspan="4"></th>
    </tr>
    <thead>
        <tr>
            <th colspan="4">passport</th>
        </tr>
        <tr>
            <th>Attribute</th>
            <th>Data type</th>
            <th>Constraints</th>
            <th>Default</th>
        </tr>
    </thead>
    <tbody>
    <tr>
        <td>id</td>
        <td>bigserial</td>
        <td>not null, primary key, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>client_id</td>
        <td>bigint</td>
        <td>not null, foreign key, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>series</td>
        <td>varchar(4)</td>
        <td>not null, dddd</td>
        <td></td>
    </tr>
    <tr>
        <td>passport_number</td>
        <td>varchar(6)</td>
        <td>not null, dddddd</td>
        <td></td>
    </tr>
    <tr>
        <td>date_issue</td>
        <td>date</td>
        <td>not null</td>
        <td></td>
    </tr>
    <tr>
        <td>whom_issued_by</td>
        <td>text</td>
        <td>not null</td>
        <td></td>
    </tr>
    <tr>
        <td>code_division</td>
        <td>varchar(7)</td>
        <td>not null, ddd-ddd</td>
        <td></td>
    </tr>
    <tr>
        <td></td>
        <td></td>
        <td>unique(series, passport_number)</td>
        <td></td>
    </tr>
    </tbody>
</table>

### Logical data model
![diagram](screenshots/dlm.png)

## Exceptions
<table>
  <thead>
    <tr>
      <th>№</th>
      <th>Description</th>
      <th>Return code</th>
      <th>Exception</th>
      <th>HTTP code</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td></td>
      <td>Success in all methods</td>
      <td align="center">1</td>
      <td></td>
      <td align="center">200</td>
    </tr>
    <tr>
      <th colspan="5" align="left">BankAccountController.getBalance()</th>
    </tr>
    <tr>
      <td align="center">1</td>
      <td>Incorrect client ID format</td>
      <td align="center">-1</td>
      <td>IncorrectNumberException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">2</td>
      <td>Incorrect date/time format</td>
      <td align="center">-1</td>
      <td>DateTimeParseException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">3</td>
      <td>Incorrect timezone format</td>
      <td align="center">-1</td>
      <td>DateTimeException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">4</td>
      <td>Time zone not found</td>
      <td align="center">0</td>
      <td>ZoneRulesException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <td align="center">5</td>
      <td>Client with this ID not found</td>
      <td align="center">0</td>
      <td>DataMissingException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <th colspan="5" align="left">BankAccountController.takeMoney()</th>
    </tr>
    <tr>
      <td align="center">1</td>
      <td>Incorrect client ID format</td>
      <td align="center">-1</td>
      <td>IncorrectNumberException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">2</td>
      <td>Incorrect date/time format</td>
      <td align="center">-1</td>
      <td>DateTimeParseException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">3</td>
      <td>Incorrect timezone format</td>
      <td align="center">-1</td>
      <td>DateTimeException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">4</td>
      <td>Incorrect amount format</td>
      <td align="center">-1</td>
      <td>IncorrectNumberException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">5</td>
      <td>Time zone not found</td>
      <td align="center">0</td>
      <td>ZoneRulesException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <td align="center">6</td>
      <td>Client with this ID not found</td>
      <td align="center">0</td>
      <td>DataMissingException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <td align="center">7</td>
      <td>Not enough funds on the account</td>
      <td align="center">0</td>
      <td>NotEnoughMoneyException</td>
      <td align="center">422</td>
    </tr>
    <tr>
      <th colspan="5" align="left">BankAccountController.putMoney()</th>
    </tr>
    <tr>
      <td align="center">1</td>
      <td>Incorrect client ID format</td>
      <td align="center">-1</td>
      <td>IncorrectNumberException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">2</td>
      <td>Incorrect date/time format</td>
      <td align="center">-1</td>
      <td>DateTimeParseException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">3</td>
      <td>Incorrect timezone format</td>
      <td align="center">-1</td>
      <td>DateTimeException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">4</td>
      <td>Incorrect amount format</td>
      <td align="center">-1</td>
      <td>IncorrectNumberException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">5</td>
      <td>Time zone not found</td>
      <td align="center">0</td>
      <td>ZoneRulesException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <td align="center">6</td>
      <td>Client with this ID not found</td>
      <td align="center">0</td>
      <td>DataMissingException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <th colspan="5" align="left">BankAccountController.transferMoney()</th>
    </tr>
    <tr>
      <td align="center">1</td>
      <td>Invalid sender ID format</td>
      <td align="center">-1</td>
      <td>IncorrectNumberException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">2</td>
      <td>Invalid recipient ID format</td>
      <td align="center">-1</td>
      <td>IncorrectNumberException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">3</td>
      <td>Incorrect date/time format</td>
      <td align="center">-1</td>
      <td>DateTimeParseException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">4</td>
      <td>Incorrect timezone format</td>
      <td align="center">-1</td>
      <td>DateTimeException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">5</td>
      <td>Incorrect amount format</td>
      <td align="center">-1</td>
      <td>IncorrectNumberException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">6</td>
      <td>Time zone not found</td>
      <td align="center">0</td>
      <td>ZoneRulesException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <td align="center">7</td>
      <td>Sender with this ID not found</td>
      <td align="center">0</td>
      <td>DataMissingException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <td align="center">8</td>
      <td>Recipient with this ID not found</td>
      <td align="center">0</td>
      <td>DataMissingException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <td align="center">9</td>
      <td>The sender's ID is equal to the recipient's ID</td>
      <td align="center">0</td>
      <td>SameIdException</td>
      <td align="center">422</td>
    </tr>
    <tr>
      <td align="center">10</td>
      <td>Insufficient funds in sender's account</td>
      <td align="center">0</td>
      <td>NotEnoughMoneyException</td>
      <td align="center">422</td>
    </tr>
    <tr>
      <th colspan="5" align="left">OperationsController.getOperationList()</th>
    </tr>
    <tr>
      <td align="center">1</td>
      <td>Incorrect date/time format</td>
      <td align="center">-1</td>
      <td>DateTimeParseException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">2</td>
      <td>Incorrect timezone format</td>
      <td align="center">-1</td>
      <td>DateTimeException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">3</td>
      <td>Time zone not found</td>
      <td align="center">0</td>
      <td>ZoneRulesException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <td align="center">4</td>
      <td>Start point of time greater or equal than to end</td>
      <td align="center">0</td>
      <td>DateTimeOutOfBoundsException</td>
      <td align="center">422</td>
    </tr>
    <tr>
      <th colspan="5" align="left">ManagementBankAccountController.createAccount()</th>
    </tr>
    <tr>
      <td align="center">1</td>
      <td>Incorrect client ID format</td>
      <td align="center">-1</td>
      <td>IncorrectNumberException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">2</td>
      <td>Incorrect date/time format</td>
      <td align="center">-1</td>
      <td>DateTimeParseException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">3</td>
      <td>Incorrect timezone format</td>
      <td align="center">-1</td>
      <td>DateTimeException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">4</td>
      <td>Time zone not found</td>
      <td align="center">0</td>
      <td>ZoneRulesException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <td align="center">5</td>
      <td>Client with this ID not found</td>
      <td align="center">0</td>
      <td>DataMissingException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <td align="center">6</td>
      <td>Client account with this ID already exists</td>
      <td align="center">0</td>
      <td>UniquenessViolationException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <th colspan="5" align="left">ManagementBankAccountController.deleteAccount()</th>
    </tr>
    <tr>
      <td align="center">1</td>
      <td>Incorrect client ID format</td>
      <td align="center">-1</td>
      <td>IncorrectNumberException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">2</td>
      <td>Incorrect date/time format</td>
      <td align="center">-1</td>
      <td>DateTimeParseException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">3</td>
      <td>Incorrect timezone format</td>
      <td align="center">-1</td>
      <td>DateTimeException</td>
      <td align="center">400</td>
    </tr>
    <tr>
      <td align="center">4</td>
      <td>Time zone not found</td>
      <td align="center">0</td>
      <td>ZoneRulesException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <td align="center">5</td>
      <td>Client with this ID not found</td>
      <td align="center">0</td>
      <td>DataMissingException</td>
      <td align="center">404</td>
    </tr>
  </tbody>
</table>

