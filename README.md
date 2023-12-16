# casdoor-spring-security-example

This example shows how a Spring Security application can connect to Casdoor via OAuth 2.0.

## Deploy Casdoor

We assume you have deployed Casdoor in your own URL (refer to: [Server Installation](https://casdoor.org/docs/basic/server-installation)), in this example, we use the Casdoor demo site: https//door.casdoor.com

## Configuration

In the application edit page of your Casdoor (like: https://door.casdoor.com/applications/casbin/app-vue-python-example), you can get the `Client ID` and `Client secret` of the application. Remember to add your application's URL: `http://localhost:8080/` to Casdoor's "Redirect URLs":

![Casdoor Application Setting](https://casdoor.org/assets/images/appsetting_spring_security-f2662d02db2032cea21f3b39b03b6c60.png)

Open in a browser: https://door.casdoor.com/.well-known/openid-configuration, you will get the OIDC endpoints of Casdoor.

```json
{
  "issuer": "https://door.casdoor.com",
  "authorization_endpoint": "https://door.casdoor.com/login/oauth/authorize",
  "token_endpoint": "https://door.casdoor.com/api/login/oauth/access_token",
  "userinfo_endpoint": "https://door.casdoor.com/api/userinfo",
  "jwks_uri": "https://door.casdoor.com/.well-known/jwks",
  "introspection_endpoint": "https://door.casdoor.com/api/login/oauth/introspect",
  "response_types_supported": [
    "code",
    "token",
    "id_token",
    "code token",
    "code id_token",
    "token id_token",
    "code token id_token",
    "none"
  ],
  "response_modes_supported": [
    "query",
    "fragment",
    "login",
    "code",
    "link"
  ],
  "grant_types_supported": [
    "password",
    "authorization_code"
  ],
  "subject_types_supported": [
    "public"
  ],
  "id_token_signing_alg_values_supported": [
    "RS256"
  ],
  "scopes_supported": [
    "openid",
    "email",
    "profile",
    "address",
    "phone",
    "offline_access"
  ],
  "claims_supported": [
    "iss",
    ...
  ],
  "request_parameter_supported": true,
  "request_object_signing_alg_values_supported": [
    "HS256",
    "HS384",
    "HS512"
  ],
  "end_session_endpoint": "https://door.casdoor.com/api/logout"
}
```

Replace the settings in this application's YAML file: [application.yml](https://github.com/casdoor/casdoor-spring-security-example/blob/master/src/main/resources/application.yml) with your own Casdoor's settings.

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          casdoor:
            client-id: 294b09fbc17f95daf2fe
            client-secret: dd8982f7046ccba1bbd7851d5c1ece4e52bf039d
            scope: read,write
            authorization-grant-type: authorization_code
            redirect-uri: http://localhost:8080/login/oauth2/code/custom
        provider:
          casdoor:
            authorization-uri: https://door.casdoor.com/login/oauth/authorize
            token-uri: https://door.casdoor.com/api/login/oauth/access_token
            user-info-uri: https://door.casdoor.com/api/get-account
            user-name-attribute: name
```

## Run the demo!

In the browser, visit: http://localhost:8080/foos. It will automatically redirect to Casdoor's login page. Login with username: `admin` and password: `123`.

If you visit your root page: http://localhost:8080, you will see:

![Casdoor Application Setting](https://casdoor.org/assets/images/spring_security_welcome-5a0d467a89d0134ed035b7718f3c834d.png)

Click the `Login` button to redirect to Casdoor's login page.

![Casdoor Application Setting](https://casdoor.org/assets/images/spring_security_casdoor-c0e156eb1514956214cad0b0d8d3ac98.png)

After you logged in, the page will redirect to `/foos`.

![Casdoor Application Setting](https://casdoor.org/assets/images/spring_security_resource-820aab1821d09e451632a46bb73df602.png)

## How is this demo developed? (No need for actions)

1. Create a Spring Boot application in `SSOClientApplication`.

2. Add a configuration which protects all endpoints except `/` and `/login**` for users to login `UiSecurityConfiguration`.

3. Add a naive page for user to login in `resources/templates/index.html`. When user clicks the `login` button, he will be redirected to `casdoor`.

4. Define our protected resource. We can export an endpoint called `/foos` and a web page for display.
