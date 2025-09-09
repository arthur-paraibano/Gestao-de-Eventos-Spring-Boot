# Sistema de Gestão de Eventos

Um sistema completo para gestão de eventos, venda de ingressos e controle de acesso desenvolvido em Spring Boot.

## 📋 Sobre o Projeto

Este sistema permite a criação e gestão completa de eventos, desde o cadastro até a validação de ingressos no local do evento. Inclui funcionalidades de venda online, processamento de pagamentos via Mercado Pago, geração de QR Codes para ingressos e sistema completo de notificações.

## 🚀 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security** - Autenticação e autorização com JWT
- **Spring Data JPA** - Persistência de dados
- **MySQL** - Banco de dados principal
- **Mercado Pago API** - Processamento de pagamentos
- **iText** - Geração de relatórios em PDF
- **AWS S3** - Armazenamento de imagens (opcional)
- **JavaMail** - Envio de e-mails
- **QR Code Generator** - Geração de códigos QR para ingressos
- **Maven** - Gerenciamento de dependências
- **Swagger/OpenAPI** - Documentação da API

## 🏗️ Arquitetura

O sistema segue o padrão MVC (Model-View-Controller) com as seguintes camadas:

- **Controllers** - Endpoints REST da API
- **Services** - Lógica de negócio
- **Repositories** - Acesso aos dados
- **Models** - Entidades do banco de dados
- **DTOs** - Objetos de transferência de dados
- **Security** - Configurações de segurança e JWT

## 📦 Instalação

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- MySQL 8.0+
- Conta no Mercado Pago (para pagamentos)
- Conta AWS (opcional, para upload de imagens)

### Passos para instalação

1. **Clone o repositório**
```bash
git clone <[url-do-repositorio](https://github.com/arthur-paraibano/Gestao-de-Eventos-Spring-Boot)>
cd gestaoevento
```

2. **Configure o banco de dados**
```sql
CREATE DATABASE gestao_eventos;
```

3. **Configure as variáveis de ambiente**

Crie um arquivo `application.properties` em `src/main/resources/` com as seguintes configurações:

```properties
# Configurações do Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/gestao_eventos
spring.datasource.username=seu_usuario
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Configurações JWT
jwt.secret=sua_chave_secreta_jwt
jwt.expiration=86400000

# Configurações do Mercado Pago
mercadopago.access.token=seu_access_token
mercadopago.public.key=sua_public_key

# Configurações de Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu_email@gmail.com
spring.mail.password=sua_senha_app
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Configurações AWS S3 (opcional)
aws.access.key=sua_access_key
aws.secret.key=sua_secret_key
aws.s3.bucket=nome_do_bucket
aws.s3.region=us-east-1
aws.s3.enabled=false
```

4. **Execute o projeto**
```bash
mvn spring-boot:run
```

O sistema estará disponível em `http://localhost:8080`

## 🔧 Configuração

### Mercado Pago

1. Acesse o [Mercado Pago Developers](https://www.mercadopago.com.br/developers)
2. Crie uma aplicação
3. Obtenha o Access Token e Public Key
4. Configure no `application.properties`

### AWS S3 (Opcional)

1. Crie um bucket no S3
2. Configure as credenciais IAM
3. Defina `aws.s3.enabled=true` no `application.properties`

## 📚 Documentação da API

A documentação completa da API está disponível via Swagger em:
`http://localhost:8080/swagger-ui.html`

### Principais Endpoints

#### Autenticação
- `POST /api/auth/register` - Registrar usuário
- `POST /api/auth/login` - Fazer login
- `POST /api/auth/refresh` - Renovar token

#### Eventos
- `GET /event/all` - Listar eventos
- `POST /event/id` - Buscar evento por ID

#### Ingressos
- `GET /ticket/all` - Listar ingressos
- `POST /api/qrcode/validate` - Validar QR Code
- `POST /api/qrcode/use-ticket` - Usar ingresso

#### Pagamentos
- `POST /payment/create-preference` - Criar preferência de pagamento
- `POST /payment/webhook/mercadopago` - Webhook do Mercado Pago
- `GET /payment/order/{orderId}` - Buscar pagamentos por pedido

#### Relatórios
- `GET /api/reports/sales/event/{eventId}` - Relatório de vendas por evento
- `GET /api/reports/sales/general` - Relatório geral de vendas
- `GET /api/reports/financial` - Relatório financeiro

#### Notificações
- `POST /api/notifications/purchase-confirmation` - Confirmação de compra
- `POST /api/notifications/payment-approved` - Pagamento aprovado
- `POST /api/notifications/event-reminder` - Lembrete de evento

#### Upload de Imagens
- `POST /api/images/upload/event-banner` - Upload de banner
- `POST /api/images/upload/user-avatar` - Upload de avatar
- `DELETE /api/images/delete` - Deletar imagem

## 👥 Tipos de Usuário

### USER (Usuário)
- Comprar ingressos
- Visualizar eventos
- Gerenciar perfil

### ORGANIZER (Organizador)
- Criar e gerenciar eventos
- Visualizar relatórios de seus eventos
- Validar ingressos
- Enviar notificações

### ADMIN (Administrador)
- Acesso completo ao sistema
- Gerenciar usuários
- Relatórios gerais
- Configurações do sistema

## 🔒 Segurança

- **Autenticação JWT** - Tokens seguros para autenticação
- **Autorização baseada em roles** - Controle de acesso por tipo de usuário
- **Validação de entrada** - Validação de dados em todos os endpoints
- **CORS configurado** - Controle de origem das requisições
- **Criptografia de senhas** - BCrypt para hash de senhas

## 📊 Funcionalidades Principais

### Gestão de Eventos
- Criação e edição de eventos
- Categorização de eventos
- Upload de imagens (banner, galeria)
- Definição de localização

### Sistema de Ingressos
- Criação de tipos e lotes de ingressos
- Controle de quantidade e preços
- Geração automática de QR Codes
- Validação em tempo real

### Processamento de Pagamentos
- Integração com Mercado Pago
- Múltiplas formas de pagamento
- Webhooks para atualizações automáticas
- Controle de status de pagamento

### Sistema de Notificações
- E-mails de confirmação
- Lembretes de eventos
- Notificações de pagamento
- Notificações em lote

### Relatórios
- Relatórios de vendas
- Relatórios financeiros
- Relatórios de usuários
- Exportação em PDF

## 🧪 Testes

Para executar os testes:

```bash
mvn test
```

## 📝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 📞 Suporte

Para suporte e dúvidas:
- Abra uma issue no GitHub
- Entre em contato via email

## 🚀 Deploy

### Docker (Recomendado)

```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/gestaoevento-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Heroku

1. Crie um `Procfile`:
```
web: java -Dserver.port=$PORT -jar target/gestaoevento-*.jar
```

2. Configure as variáveis de ambiente no Heroku
3. Faça o deploy

---