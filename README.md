# biblioteca


### Build

Para buildar a aplicação, execute:
```bash
./mvnw clean install
```
Para buildar a aplicação sem rodar os testes, execute:
```bash
./mvnw clean install -Dmaven.test.skip=true
```
<br/>

### Run

Para rodar execute o comando abaixo:
```bash
./mvnw spring-boot:run
```
<br/>

### Unit Tests

Executados durante o build. Iniciam o contexto Spring e utilizam banco de dados em memória. Para rodar manualmente:
```bash
./mvnw clean test surefire-report:report-only
```
<br/>


### Endpoints Authorization

Todos os endpoints da classe LoanEndpoint precisam de autenticação, para isso no Postman antes de enviar a 
requisição, selecione a aba "Authorization", e depois selecione o type "Basic Auth".
```bash
Username: admin
Password: admin
```

### Documentação Via Swagger

Para visualizar os Endpoints da aplicação via Swagger, é necessário subir a aplicação, conforme o comando abaixo:

```bash
./mvnw spring-boot:run
```

Após subir a aplicação, é necessário abrir seu navegador e inserir a seguinte URL:
```bash
http://localhost:8080/swagger-ui/index.html
```