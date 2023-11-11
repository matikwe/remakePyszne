### Purpose and scoper of work
The purpose of the work is to develop a mobile application for restaurant delivery, implement it in the Java programming language, and create a connection between a Microsoft SQL Server database and the Android platform. The application is designed to organize delivery for restaurants that do not hire employees to deliver orders. The restaurant owner focuses solely on organizing the food. It is the application that enables the restaurant to find a potential supplier. A supplier is assigned to an order and declares his ability to deliver the meal. He is then required to pick up the order from the premises and deliver the order to the specified address. The application has been developed in such a way that the user can navigate it intuitively, without prior training and tutorials.
In planning the application, the following user roles were envisioned: customer, restaurateur and supplier. The customer can access the restaurant, products, enter an address and view order history. The restaurateur role allows users to modify data and take orders. For the supplier, only the ability to change delivery statuses and declaration of meal delivery is planned.  

![Main](https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExN25tdThpdzU3ZHVmb3F2YTFqdzhmZzA2ZXFuYTRndnUwMDdqamg0bSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/M4oVd0tl1VEFIjhKVM/giphy.gif)

#### The application should meet the most important requirements:

- the possibility of registration and user login taking into account the user's role during account creation, <br>
- the ability to download the current location using the phone's GPS (Global Positioning System) receiver and search for the address location based on the downloaded geographic coordinates,<br>
- ability to suggest restaurants available to the user, taking into account his location and the opening hours of the restaurant,<br>
- the ability to sort restaurants by category,<br>
- the ability to add multiple products to a shopping cart and then place an order,<br>
- ability to modify the contents of the shopping cart,<br>
- the ability to view order history for the role of a classic user,<br>
- ability to add restaurants, products, delivery neighborhoods for the role of restaurateur,<br>
- ability to edit information about the restaurant and products for the role of restaurateur,<br>
- ability to track current orders taking into account sorting against current order status for the role of restaurateur,<br>
- The ability to track current deliveries for the supplier role taking into account sorting against delivery status,<br>
- Validation of data entered by the user,<br>
- Ability to add your own restaurant logo or product image,<br>
- ability to log out of current user account.<br>
### Application operation
The main task of the application is to relieve the restaurateur in taking take-out orders and scheduling deliveries. The application has been programmed in such a way that a customer placing an order, gives his address data, and the program, based on the information about the delivery area, proposes possible restaurants that have declared delivery range in the area. It is the restaurant that decides in which regions the meals can be delivered, so that their delivery time does not affect the quality of the dishes offered. In the next step, the restaurant changes the status of the order. Then the supplier who declares delivery can undertake the delivery of the order in question. While the restaurant is preparing the meal, the supplier will receive information about the possibility of collecting the meal from the premises. The order will then be delivered to the user's specified address. The customer can track his order in the Order History tab. In addition, an administrator panel has been created, which offers the ability to add and modify restaurant or product data. The ability to hide dishes if the restaurant has run out of ingredients on any day has been added. If a restaurateur plans to resume serving a meal, he or she can modify the visibility of the product on the menu using the admin panel. The application also offers the ability to store meals in a shopping cart so that the customer does not have to create multiple orders. He can add selected products and then place one large order. Such a solution will greatly improve the efficiency of the meals served by the restaurant.
