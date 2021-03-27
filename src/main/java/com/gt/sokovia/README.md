# Summary

Sokovia is the database service <br>
It contains 3 characters:
   * Wanda
   * Vision
   * Pietro

# Wanda -- Logic for the database

Responsibilites include:

1. findAll()
	* Return all orders from the database

2. getAllOrdersForUser()
	* Return all orders from the database that were created by a specific user

3. getAllUsersInDatabase()
	* Return all users in the database

4. isCorrectLogin()
	* Return whether the login credentials exist in the database

5. isUserInDatabase()
	* Return whether the userID is a valid row in the database

6. save()
	* Create new order in the database

7. createNewOrder()
	* Create new order in the database

8. createNewUser()
	* Create new user in the database

9. getNumberOfOrdersInDatabase()
	* Return number of orders in the database

10. deleteOrder()
	* Remove order from database

11. getOrder()
	* Return order from database given orderID

12. updateOrder()
	* Given new order economics, update the matching order in the database to reflect these economics

# Pietro -- JPA (Auto-generated database functions)

Responsibilites include:

CRUD operations to database
	* Wanda uses Pietro's CRUD operations extensively

# Vision -- Controller which exposes endpoints to interact with the database
# NEED TO UPDATE

Responsibilites include:

@GetMapping("/all")
@GetMapping("/all/users")
@GetMapping("/users/validate/{userID}")
@PostMapping("/validate/login")
@PostMapping("/create/user")
@GetMapping("/get/user/{userID}")
@GetMapping("/count")
@PostMapping("/add")
@GetMapping("/get/{id}")
@DeleteMapping("/delete/{id}")
@RequestMapping(value = "/put/{id}", method = RequestMethod.PUT)
@PostMapping("/match")
