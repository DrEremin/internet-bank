# Internet-bank
<br>
<hr>

## Description
Internet-bank is a server-side REST-application that is implemented with using<br> 
the Spring boot framework version 2.6.2.<br>
Its current capabilities allow a remote user to conduct the following<br> 
operations with a bank account: creation, deletion, receipt balance,<br>
withdrawal of money, replenishment.<br>
To interact with a remote user, the application implements REST API. Data<br>
xchange takes place in .JSON format.<br>
All account information is stored in a database managed by the Postgresql DBMS.<br>
In case of conflict situations in the application, they are processed using<br>
the exception mechanism. The result of exception handling allows you to issue<br> 
to the remote user exhaustive answers.<br>
All these situations, as well as successful completion of operations, are<br> 
logged to files, located in the project folder<br> 
internet-bank/src/main/resources/logs.<br><br> 
<hr>

## Structural elements

* REST API implemented in a class<br>
  ru.dreremin.internetbank.controllers.InternetBankController;
* The service layer is represented by the class<br>
  ru.dreremin.internetbank.services.BankAccountService;
* The model is represented by the BankAccount class.<br>
  Interaction with the database occurs through interface implementations<br>
  ru.dreaming.internetbank.repositories.BankAccountRepository,<br>
  inherited from CrudRepository<T, ID> and with the participation of the model;<br>
* To handle conflict situations, own exception classes (inheriting from<br>
  Exception) are implemented. They are in the package<br> 
  ru.dreremin.internetbank.exceptions. To manage exceptions was provided the<br>
  class ru.dreremin.internetbank.controllers.ExceptionController;
* To receive and transmit data in .JSON format, was implemented classes located<br> 
  in the package ru.dreremin.internetbank.dto.<br><br>
<hr>

## Database
The database is currently represented by a single entity 'bank_account' with<br> 
three attributes: 'id' (primary key), 'user_id', 'current_balance'.<br>

<table>
    <thead>
        <tr>
            <th colspan="4">Entity bank_account</th>
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
        <td>id (primary key)</td>
        <td>bigserial</td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>user_id</td>
        <td>bigint</td>
        <td>not null, user_id > 0, unique</td>
        <td></td>
    </tr>
    <tr>
        <td>current_balance</td>
        <td>numeric(20.2)</td>
        <td>not null, current_balance > 0</td>
        <td>0.00</td>
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
      <td>Incorrect user id format</td>
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
      <td>User with this id not found</td>
      <td align="center">0</td>
      <td>DataMissingException</td>
      <td align="center">404</td>
    </tr>
    <tr>
      <th colspan="5" align="left">BankAccountController.takeMoney()</th>
    </tr>
    <tr>
      <td align="center">1</td>
      <td>Incorrect user ID format</td>
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
      <td>User with this ID not found</td>
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
      <td>Incorrect user ID format</td>
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
      <td>User with this ID not found</td>
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
      <th colspan="5" align="left">BankAccountController.getOperationList()</th>
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
    
  </tbody>
</table>

