# Springboot-Consuming-and-Modifying-Webservices-Secure-Rest-APIs-By-Role

1. USER role can perform following
  a. Get a list of all customers. GET /users
  b. Get a single customer. GET /users/{userId}
2. MANAGER role can perform following 
  a. GET endpoints and
  b. Add a new customer. POST /users
  c. Update an existing customer. PUT /users
3. ADMIN role can perform following
  a. GET, POST and 
  b. Delete a customer. DELETE /users/{userId}
