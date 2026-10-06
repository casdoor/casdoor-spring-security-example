# Casdoor Spring Security Example

[![Build](https://github.com/casdoor/casdoor-spring-security-example/actions/workflows/build.yml/badge.svg)](https://github.com/casdoor/casdoor-spring-security-example/actions/workflows/build.yml)
[![License](https://img.shields.io/github/license/casdoor/casdoor-spring-security-example)](https://github.com/casdoor/casdoor-spring-security-example/blob/master/LICENSE)
[![Discord](https://img.shields.io/discord/1022748306096537660?logo=discord&label=discord&color=5865F2)](https://discord.gg/5rPsrAzK7S)

An example Spring Security app that signs users in with [Casdoor](https://casdoor.ai/) over OAuth 2.0 / OpenID Connect, using [casdoor-spring-boot-starter](https://github.com/casdoor/casdoor-spring-boot-starter).

![demo](https://github.com/casdoor/casdoor-spring-security-example/assets/3787410/ddc81f02-a608-4342-8d92-cf52dcc25814)

| Page    | Description                                                    |
|---------|----------------------------------------------------------------|
| `/`     | Public welcome page with a **Login** button                    |
| `/foos` | Protected page, shows some data and the signed-in user        |
| `/logout` (POST) | Signs out of the app and ends the Casdoor session     |

## How it works

1. casdoor-spring-boot-starter reads the `casdoor.*` properties and registers a Spring Security OAuth2 client named `casdoor` (endpoints from Casdoor's OIDC discovery, scopes `openid profile email`).
2. [SecurityConfig](src/main/java/org/casdoor/example/SecurityConfig.java) keeps `/` public, protects everything else and turns on `oauth2Login()`.
3. Visiting `/foos` without a session redirects to `/oauth2/authorization/casdoor` and then to the Casdoor sign-in page.
4. Casdoor redirects back to `http://localhost:8080/login/oauth2/code/casdoor`. Spring Security checks the state, exchanges the code for tokens and verifies the ID token.
5. Controllers get the user as an `OidcUser` (`@AuthenticationPrincipal`), its name is the Casdoor username (`preferred_username`).
6. `POST /logout` ends the Spring session and calls Casdoor's logout API with the access token (`AuthService.logoutCurrentSession()`), so the next sign-in asks for the password again.

## Prerequisites

- Java 17+
- Maven 3.9+ (or your IDE)
- A Casdoor server. The example is preconfigured for the public demo server https://door.casdoor.com, so it runs as is. To use your own, see [Casdoor installation](https://casdoor.ai/docs/basic/server-installation).

## Configuration

Skip this section to try the example with the public demo server.

In your Casdoor, create (or reuse) an organization and an application, and add `http://localhost:8080/login/oauth2/code/casdoor` to the application's **Redirect URLs**. Then fill in [application.yml](src/main/resources/application.yml):

```yaml
casdoor:
  endpoint: https://door.casdoor.com          # Casdoor server URL
  client-id: 294b09fbc17f95daf2fe             # client ID of the application
  client-secret: dd8982f7046ccba1bbd7851d5c1ece4e52bf039d  # client secret of the application
  organization-name: casbin                   # organization of the application
  application-name: app-vue-python-example    # name of the application
```

The standard `spring.security.oauth2.client.*` properties still work and override what the starter sets, for example `spring.security.oauth2.client.registration.casdoor.scope`.

## Run

```shell
git clone https://github.com/casdoor/casdoor-spring-security-example
cd casdoor-spring-security-example
mvn spring-boot:run
```

Open http://localhost:8080 and click **Login**. On the demo server, sign in with username `admin` and password `123`.

Run the tests:

```shell
mvn verify
```

## Resources

- [Casdoor documentation](https://casdoor.ai/docs/overview)
- [Casdoor Spring Security integration](https://casdoor.ai/docs/integration/java/spring-security/spring-security-oauth/)
- [casdoor-spring-boot-starter](https://github.com/casdoor/casdoor-spring-boot-starter)
- [Spring Security OAuth2 login](https://docs.spring.io/spring-security/reference/servlet/oauth2/login/index.html)

## License

[Apache-2.0](LICENSE)
