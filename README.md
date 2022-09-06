# casdoor-spring-security-example


Casdoor can use OIDC protocol as IDP to connect various applications. Here we will use Spring Security as an example to show you how to use OIDC to connect to your applications.

## Step1. Deploy Casdoor

Firstly, the Casdoor should be deployed.

You can refer to the Casdoor official documentation for the [Server Installation](https://casdoor.org/docs/basic/server-installation).

After a successful deployment, you need to ensure:

- The Casdoor server is successfully running on **http://localhost:8000**.
- Open your favorite browser and visit **http://localhost:8000**, you will see the login page of Casdoor.
- Input `admin` and `123` to test login functionality is working fine.

Then you can quickly implement a casdoor based login page in your own app with the following steps.


## Step2. Configure Casdoor application
1. Create or use an existing Casdoor application.
2. Add Your redirect url (You can see more detials about how to get redirect url in the next section)
   ![Casdoor Application Setting](https://casdoor.org/assets/images/appsetting_spring_security-f2662d02db2032cea21f3b39b03b6c60.png)
3. Add provider you want and supplement other settings.

Not surprisingly, you can get two values ​​on the application settings page: `Client ID` and `Client secret` like the picture above, we will use them in next step.

Open your favorite browser and visit: **http://`CASDOOR_HOSTNAME`/.well-known/openid-configuration**, you will see the OIDC configure of Casdoor.

## Step3. Configure Spring Security
Spring Security natively support OIDC.

You can customize the settings of Spring Security OAuth2 Client:

You should replace the configuration with your own Casdoor instance especially the `<Client ID>` and others in `application.yaml`.

For default situation of Spring Security, the `<Redirect URL>` should be like `http://<You Spirng Boot Application Endpoint>/<Servlet Prefix if it is configured>/login/oauth2/code/custom`.
For example, to the following demo, the redirect URL should be `http://localhost:8080/login/oauth2/code/custom`.

You should also configure this in `casdoor` application.

## Step4. Get Started with A Demo

1. We can create a Spring Boot application in `SSOClientApplication`.

2. We can add a configuration which protects all endpoints except `/` and `/login**` for users to login `UiSecurityConfiguration`.

3. We can add a naive page for user to login in `resources/templates/index.html`. When user clicks the `login` button, he will be redirected to `casdoor`.

4. Next, we can define our protected resource. We can export an endpoint called `/foos` and a web page for display.

## Step5. Try the demo!

Firstly, you can try to open your favorite browser and directly visit `/foos`. It will automatically redirect to casdoor's login page. You can log in here or from the root page.

If you visit your root page,
![Casdoor Application Setting](https://casdoor.org/assets/images/spring_security_welcome-5a0d467a89d0134ed035b7718f3c834d.png)

Click the `login` button and the page will redirect to casdoor's login page.
![Casdoor Application Setting](https://casdoor.org/assets/images/spring_security_casdoor-c0e156eb1514956214cad0b0d8d3ac98.png)

After you log in, the page will redirect to `/foos`.
![Casdoor Application Setting](https://casdoor.org/assets/images/spring_security_resource-820aab1821d09e451632a46bb73df602.png)