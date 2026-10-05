# atv-selenium

Testes end-to-end com Selenium para o site [automationexercise.com](https://automationexercise.com/), feitos para a disciplina de Qualidade e Teste.
Cobrem o **Test Case 3** (login com e-mail e senha incorretos) e o **Test Case 1** (registrar usuário e excluir a conta).

## Como rodar

Precisa do JDK 17+ e do Google Chrome instalados. O chromedriver é baixado automaticamente pelo WebDriverManager.

```
mvnw.cmd test                                  # Windows
./mvnw test                                    # Linux / Mac
mvnw.cmd test -Dheadless=true                  # sem abrir a janela do navegador
mvnw.cmd test -Dtest=LoginEmailIncorretoTest   # só um dos testes
```

Se aparecer `JAVA_HOME not found`, aponte a variável para a pasta do JDK (ex.: `C:\Program Files\Java\jdk-26`).

## Casos de teste

As entradas foram escolhidas usando classes de equivalência e análise de valor limite.

### Login com e-mail incorreto (`LoginEmailIncorretoTest`)

| # | E-mail | Senha | O que cobre | Esperado |
|---|---|---|---|---|
| 1 | `naoexiste<timestamp>@teste.com` | `Senha@123` | e-mail válido não cadastrado | mensagem "Your email or password is incorrect!" |
| 2 | `a@b` | `Senha@123` | menor e-mail que o navegador aceita (3 caracteres) | mensagem de erro |
| 3 | e-mail com 254 caracteres | `Senha@123` | maior e-mail permitido pela RFC | mensagem de erro |
| 4 | e-mail não cadastrado | `1` | senha com 1 caractere | mensagem de erro |
| 5 | e-mail não cadastrado | 100 caracteres | senha longa | mensagem de erro |
| 6 | vazio | `Senha@123` | e-mail vazio | navegador barra o envio |
| 7 | `emailsemarroba.com` | `Senha@123` | e-mail sem `@` | navegador barra o envio |
| 8 | e-mail não cadastrado | vazia | senha vazia | navegador barra o envio |

### Registrar usuário (`RegistrarUsuarioTest`)

| # | Entrada | O que cobre | Esperado |
|---|---|---|---|
| 1 | Mr, "Joao QA", todos os campos, newsletter e ofertas marcados, nascimento 1 / January / 1900 | caso base, menores valores das listas de data | conta criada, "Logged in as Joao QA", conta excluída |
| 2 | Mrs, "Maria QA", Company e Address 2 vazios, sem checkboxes, nascimento 31 / December / 2020 | opcionais vazios, maiores valores das listas de data | conta criada e excluída |
| 3 | nome `A`, senha `1` | nome e senha com 1 caractere | conta criada e excluída |
| 4 | e-mail que acabou de ser cadastrado | e-mail duplicado | mensagem "Email Address already exist!" |
| 5 | nome vazio | campo obrigatório vazio | navegador barra o envio |
| 6 | `joao.teste.com` | e-mail sem `@` | navegador barra o envio |

Os campos de e-mail do site são `type="email"` e `required`, então quando o campo está vazio ou sem `@` o próprio navegador impede o envio e a mensagem do servidor não aparece. Nesses casos o teste confere que o campo ficou inválido (`checkValidity()`), que a página continua em `/login` e que nenhuma mensagem de erro apareceu.

Todo cadastro usa um e-mail único e termina excluindo a conta criada, então os testes não deixam nada no site.

## Estrutura

```
src/test/java/br/com/aulaSelenium/
├── BaseTest.java                 abre e fecha o Chrome, salva um print se o teste falhar
├── Usuario.java                  dados usados no cadastro
├── LoginEmailIncorretoTest.java  Test Case 3
├── RegistrarUsuarioTest.java     Test Case 1
└── pages/                        Page Objects (HomePage, LoginPage, CadastroPage)
```
