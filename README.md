# Back-End-minhas-financas-app

Desenvolvimento back-end usando Spring Boot de uma API para registrar as finanças.

Codigo front-end disponivel em: https://github.com/Edufgs/Front-End-minhas-financas-app

Aplicação pronta está disponivel: https://minhasfinancas-edufgs-app.herokuapp.com/

# Ferramentas usadas:
* <b>Spring Boot V2.2.2:</b> O Spring Boot é um framework Open Source que nasceu a partir do Spring framework e veio para facilitar as configurações iniciais de um projeto.
  <br>Site da Documentação: https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#data.sql
  
* <b>PostgreSQL:</b> O PostgreSQL é um poderoso sistema de banco de dados relacional de objeto de código.
  <br>Site Oficial: https://www.postgresql.org/

* <b>H2:</b> O H2 é um sistema de gerenciamento de banco de dados relacional escrito em Java. Ele pode ser incorporado em aplicativos Java ou executado no modo cliente-servidor. (Utilizado em testes).
  <br>Site Oficial: https://www.h2database.com/html/main.html

* <b>Lombok:</b> O Lombok é um framework para Java que permite escrever código eliminando a verbosidade, o que permite ganhar tempo de desenvolvimento para o que realmente é importante. Seu uso permite gerar em tempo de compilação os métodos getters e setters, métodos construtores, padrão builder e muito mais.
 <br>Site Oficial: https://projectlombok.org/

* <b>Jsonwebtoken:</b> é um pacote que implementa o protocolo JSON Web Token.
<br>Site: https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt

# Editor de código-fonte:
* <b>Eclipse:</b> é uma IDE para desenvolvimento Java, porém suporta várias outras linguagens a partir de plugins como C/C++, PHP, ColdFusion, Python, Scala e Kotlin. Ele foi feito em Java e segue o modelo open source de desenvolvimento de software.

# Testes feitos com:
* <b>Junit5:</b> é um framework open-source, que se assemelha ao raio de testes software java.

* <b>Insomnia:</b> enviar solicitações REST, SOAP, GraphQL e GRPC. Assim com insomnia é possivel fazer testes de requisições HTTP.

* <b>H2:</b> O H2 é um sistema de gerenciamento de banco de dados relacional escrito em Java. Ele pode ser incorporado em aplicativos Java ou executado no modo cliente-servidor. Foi usado para testes do código.

# Instalações: 

<b>Spring Boot:</b> No site https://start.spring.io/ crie o projeto preenchendo os dados e colocando todas as dependências como <b>PostgreSQL</b>, <b>H2</b>, <b>Lombok</b>, entre outros. Algumas dependências é preciso adicionar manualmente no arquivo pom.xml como o <b>Jsonwebtoken</b>:

```
  <dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt</artifactId>
	<version>0.9.1</version>
  </dependency>
```

Ou indo no site https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt onde tem várias versões para ser selecionadas.
