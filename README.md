# User Management Web Service

User management web service with Spring Boot and MySQL, and interact with 
Wechat Login interface as third-party login method, aliyun Email and SMS 
service to complete reset password functionality.

### database structure:
![database schema](images/database%20schema.png "database schema")

### services list:
```java
public interface CartLineService {
   CartLineDTO createCartLine(CartLineDTO cartLineDTO);
   List<CartLineDTO> getCartLineByCustomerId(long customerId, Pageable pageRequest);
   CartLineDTO updateProductQuantityByCustomerIdAndProductId(long customerId, long productId, int quantity);
   void deleteCartLineByCustomerIdAndProductId(long customerId, long productId);
}
```
```java
public interface UserService {
    Optional<UserDTO> checkUsernameValid(String username);
    NormalUserDTO addUser(NormalUserDTO normalUserDTO, boolean oauth);
    OrganizationUserDTO addUser(OrganizationUserDTO organizationUserDTO, boolean oauth);
    EmployeeUserDTO addUser(EmployeeUserDTO employeeUserDTO, boolean oauth);
    UserDTO getByUsername(String username);
    NormalUserDTO updateUser(String username, NormalUserDTO normalUserDTO);
    OrganizationUserDTO updateUser(String username, OrganizationUserDTO organizationUserDTO);
    EmployeeUserDTO updateUser(String username, EmployeeUserDTO organizationUserDTO);
    void deleteUser(String username);
}
```
```java
public interface OAuthUserService {
    OAuthUserDTO addUser(OAuthUserDTO oAuthUserDTO, int depth);
    OAuthUserDTO getOAuthUserByOpenId(OAuthType type, String accessToken, String openId);
    OAuth2AccessToken getAccessToken(OAuthType type, UserStatus status, String username, String password);
    Optional<OAuthUserDTO> getOAuthUserByUsername(String username);
}
```
```java
public interface EmailService {
    void sendResetPasswordEmail(String token, String email);
    void sendGenericEmail(SimpleMailMessage simpleMailMessage);
}
```
```java
public interface PhoneService {
    void sendSMS(String token, String phone);
}
```
### Controller Details
* ##### UserController

**checkUsernameExist**
```java
@RequestMapping(method=GET, value="/check/{username}")
public org.springframework.http.ResponseEntity<com.wenqi.usermanagement.dto.UserDTO> 
    checkUsernameExist(@PathVariable(value="username") java.lang.String username)
```
check username exists or not, if exists, return HttpStatus CONFLICT and partially hidden user info. if not, return http ok.  
URL path: "/user/check/{username}"  
HTTP method: GET  
Parameters:  
username - The username  
Returns:  
a ResponseEntity with OK or CONFLICT  
**getNormalUser**
```java
@RequestMapping(method=GET, value="/{username}")
public org.springframework.http.ResponseEntity<com.wenqi.usermanagement.dto.UserDTO> 
    getNormalUser(@PathVariable(value="username") java.lang.String username,
                  org.springframework.security.core.Authentication authentication)
```
user query with username and return a ResponseEntity of UserDTO  
URL path: "/user/{username}"  
HTTP method: GET  
Parameters:  
username - The username  
authentication - The authentication  
Returns:  
a ResponseEntity of UserDTO  
**forgetPasswordRequest**
```java
@RequestMapping(method=POST, value="/forget-password")
public org.springframework.http.ResponseEntity 
    forgetPasswordRequest(@RequestParam(value="identifier") java.lang.String emailOrPhone)
```
make forget password request with email or phone number and return a ResponseEntity without response body  
URL path: "/user/forget-password"  
HTTP method: POST  
Parameters:  
emailOrPhone - The identifier with parameter name "identifier"  
Returns:  
a ResponseEntity without response body  
**resetPasswordRequest**
```java
@RequestMapping(method=POST, value="/reset-password")
public org.springframework.http.ResponseEntity 
    resetPasswordRequest(@RequestParam(value="token") java.lang.String token,
                         @RequestParam(value="password")java.lang.String password)
```
modify user password with reset token and new password and return a ResponseEntity without response body  
URL path: "/user/reset-password"  
HTTP method: POST  
Parameters:  
token - The reset password token  
password - The new password  
Returns:  
a ResponseEntity without response body  
**deleteNormalUser**
```java
@RequestMapping(method=DELETE, value="/{username}")
public org.springframework.http.ResponseEntity 
        deleteNormalUser(@PathVariable(value="username")java.lang.String username,
                         org.springframework.security.core.Authentication authentication)
```
user cancelled by username and return a ResponseEntity  
URL path: "/user/{username}"  
HTTP method: DELETE  
Parameters:  
username - The username  
authentication - The authentication  
Returns:  
a ResponseEntity without response body  
* #### NormalUserController
**signUp**
```java
@RequestMapping(method=POST, consumes="application/json")
public org.springframework.http.ResponseEntity<com.wenqi.usermanagement.dto.NormalUserDTO> 
        signUp(@RequestBody com.wenqi.usermanagement.dto.NormalUserDTO normalUserDTO)
```
normal user sign up with normal user DTO and return a ResponseEntity of NormalUserDTO  
URL path: "/normal"  
HTTP method: POST  
Parameters:  
normalUserDTO - The normal user DTO  
Returns:  
a ResponseEntity of NormalUserDTO  
**updateNormalUser**
```java
@RequestMapping(method=PUT, value="/{username}")
public org.springframework.http.ResponseEntity<com.wenqi.usermanagement.dto.NormalUserDTO> 
        updateNormalUser(@PathVariable(value="username") java.lang.String username,
                         @RequestBody com.wenqi.usermanagement.dto.NormalUserDTO normalUserDTO,
                         org.springframework.security.core.Authentication authentication)
```
normal user update with username and normal user DTO then return a ResponseEntity of NormalUserDTO  
URL path: "/normal/{username}"  
HTTP method: PUT  
Parameters:  
username - The username  
normalUserDTO - The normal user DTO  
authentication - The authentication  
Returns:  
a ResponseEntity of NormalUserDTO  
* #### OrganizationUserController
**signUp**
```java
@RequestMapping(method=POST, consumes="application/json")
public org.springframework.http.ResponseEntity<com.wenqi.usermanagement.dto.OrganizationUserDTO> 
    signUp(@RequestBody com.wenqi.usermanagement.dto.OrganizationUserDTO organizationUserDTO)
```
organization user sign up with organization user DTO and return a ResponseEntity of OrganizationUserDTO  
URL path: "/organization"  
HTTP method: POST  
Parameters:  
organizationUserDTO - The organization user DTO  
Returns:  
a ResponseEntity of OrganizationUserDTO  
**updateOrganizationUser**
```java
@RequestMapping(method=PUT, value="/{username}")
public org.springframework.http.ResponseEntity<com.wenqi.usermanagement.dto.OrganizationUserDTO>
    updateOrganizationUser(@PathVariable(value="username") java.lang.String username,
                           @RequestBody com.wenqi.usermanagement.dto.OrganizationUserDTO organizationUserDTO,
                           org.springframework.security.core.Authentication authentication)
```
organization user update with username and organization user DTO then return a ResponseEntity of OrganizationUserDTO  
URL path: "/organization/{username}"  
HTTP method: PUT  
Parameters:  
username - The username  
organizationUserDTO - The organization user DTO  
authentication - The authentication  
Returns:  
a ResponseEntity of OrganizationUserDTO  
* #### EmployeeUserController
**signUp**
```java
@RequestMapping(method=POST, consumes="application/json")
public org.springframework.http.ResponseEntity<com.wenqi.usermanagement.dto.EmployeeUserDTO> 
    signUp(@RequestBody com.wenqi.usermanagement.dto.EmployeeUserDTO employeeUserDTO)
```
employee user sign up with employee user DTO and return a ResponseEntity of EmployeeUserDTO  
URL path: "/employee"  
HTTP method: POST  
Parameters:  
employeeUserDTO - The employee user DTO  
Returns:  
a ResponseEntity of EmployeeUserDTO  
**updateEmployeeUser**
```java
@RequestMapping(method=PUT, value="/{username}")
public org.springframework.http.ResponseEntity<com.wenqi.usermanagement.dto.EmployeeUserDTO>
    updateEmployeeUser(@PathVariable(value="username") java.lang.String username,
                       @RequestBody com.wenqi.usermanagement.dto.EmployeeUserDTO employeeUserDTO,
                       org.springframework.security.core.Authentication authentication)
```
employee user update with username and employee user DTO then return a ResponseEntity of EmployeeUserDTO  
URL path: "/employee/{username}"  
HTTP method: PUT  
Parameters:  
username - The username  
employeeUserDTO - The employee user DTO  
authentication - The authentication  
Returns:  
a ResponseEntity of EmployeeUserDTO  
* #### OAuthAccountController
**showLogin**
```java
@RequestMapping(method=GET, value="/{type}/login")
public org.springframework.http.ResponseEntity<java.lang.String>
    showLogin(@PathVariable(name="type") com.wenqi.usermanagement.constants.OAuthType type)
```
Get oAuth login redirect URL  
URL path: "/oauth/{type}/login"  
HTTP method: GET  
Parameters:  
type - The oAuth type  
Returns:  
OAuth login redirect URL  
**getAccessToken**
```java
@RequestMapping(method=GET, value="/{type}/token")
public org.springframework.http.ResponseEntity<org.springframework.security.oauth2.common.OAuth2AccessToken>
    getAccessToken(@PathVariable(name="type") com.wenqi.usermanagement.constants.OAuthType type,
                   @RequestParam(name="code") java.lang.String code,
                   @RequestParam(name="state") java.lang.String state)
```
Get oAuth login unified token  
URL path: "/oauth/{type}/token"  
HTTP method: GET  
Parameters:  
type - The oAuth type  
code - The authorization code  
state - The custom state  
Returns:  
oAuth login unified token  
**getOAuthUser**
```java
@RequestMapping(method=GET, value="/{type}/info")
public org.springframework.http.ResponseEntity<com.wenqi.usermanagement.dto.OAuthUserDTO>
    getOAuthUser(@PathVariable(name="type") com.wenqi.usermanagement.constants.OAuthType type,
                 org.springframework.security.core.Authentication authentication)
```
Get oAuth user info  
URL path: "/oauth/{type}/info"  
HTTP method: GET  
Parameters:  
type - The oAuth type  
authentication - The authentication with access token  
Returns:  
oAuth user info  
