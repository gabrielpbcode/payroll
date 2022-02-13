# Payroll Management

### Definition

**Payroll Management** it is a system that enables the user to register new enterprises, new employees and run payrolls in
conformation with pre-defined requirements

### Guides

#### Running the application
1. Download this repo
2. Inside the app folder run ```mvn clean install```
3. Run ```docker compose up``` (for recent versions of docker), or ```docker-compose up``` inside the terminal
4. Execute queries using an app like [Insominia](https://insomnia.rest/download) or [Postman](https://www.postman.com/downloads/)

There are two APIs: enterprises and employees
When the application is running, you can access the data with the **GET** verb from

```localhost:8082/api/enterprises``` or ```localhost:8082/api/employees```

Running those end points, the system returns a list of enterprises/employees pre-defined inside the database.

#### Accessing enterprise end points

* To register an enterprise

1. Call ```localhost:8082/api/enterprises/register```
2. Change http verb to **POST**
3. Create an object whith at least:
``` json
{
    "name": "Joao das Neves",
    "fantasyName": "Watch Patrol",
    "email": "watch@email.com",
    "cnpj": "32.100.000/0001-71",
    "payrollUserId": 2
}
```

**OBS**: The fields **email** and **cnpj** must be unique. In case of repetition, the system throws an error message

* To process the payroll of an interprise do:

1. Change http verb to **PUT**
2. Access ```localhost:8082:/api/enterprises/processPayroll/{id}```

* To GET the balance:

1. Change http verb to **GET**
2. Access ```localhost:8082:/api/enterprises/getBalance/{id}```

* To GET enterprise info:

1. Change http verb to **GET**
2. Access ```localhost:8082:/api/enterprises/getEnterprise/{id}```

#### Accessing Employees end points

* To register an employee

1. Call ```localhost:8082/api/employees/register```
2. Change http verb to **POST**
3. Create an object whith at least:
``` json
{
    "name": "Romario",
    "cpf": "213.564.879-03",
    "birthday": "1966-01-29",
    "email": "romario@email.com",
    "referenceAccount": "456123-1",
    "referenceAgency": "000001",
    "wage": 1000.0,
    "enterpriseId" : 2
}
```

**OBS**: The fields **email** and **cpf** must be unique. In case of repetition, the system throws an error message

* To GET employee information:

1. Change http verb to **GET**
2. Access ```localhost:8082/api/employees/getBalance/{id}```

#### Accessing Payroll Users end points

* To GET employee information:

1. Change http verb to **GET**
2. Access ```localhost:8082/admin/api/users```