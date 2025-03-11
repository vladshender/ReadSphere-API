# ReadSphere API 📚
Welcome to the ReadSphere API, a RESTful API designed for managing an online bookstore. It allows users to manage a catalog of books, shopping carts, and orders, and supports user registration and authentication via JWT. The API also includes role-based access control, enabling different levels of access for users and administrators.
## Technologies
- **Java 17**: Core programming language for backend development.
- **Spring Boot**: Framework for building microservices with minimal configuration.
- **Spring Web**: Module for building REST APIs and handling HTTP requests.
- **Spring Security**: Authentication and authorization framework.
- **MySql**: The basis of the database for the project.
- **MapStruct**: Simplifies object mapping between DTOs and entities.
- **Liquibase**: Tool for managing database schema migrations.
- **JUnit & MockMvc**: Frameworks for unit and integration testing.
- **Docker**: Containerization platform for consistent development and deployment environments.
## Features
### Users 👥
- User registration and login.
- JWT-based authentication.
- User roles: USER (regular user) and ADMIN (administrator).
### Books 📚 
- CRUD operations for books: create, read, update, and delete.
- Filter books by categories.
- Search books by title or author.
- Users can only perform GET operations
- Administrator can perform POST, PUT, DELETE operations
### Categories 📂
- Users can view all categories and see all books in a specific category.
- Administrators can add, update, and delete categories.
### Shopping Cart 🛒
- All actions are performed only by authenticated users.
- Add books to the cart.
- View cart contents.
- Update book quantities in the cart.
- Remove books from the cart.
- The user's shopping cart is created during registration
### Orders 📋
- Place orders from the cart (authenticated users only).
- View order history (authenticated users only).
- Users can create an order for their cart.
- Users can retrieve all their orders.
- Users can view all items in a specific order.
- Users can retrieve individual order items by order and product ID.
- Admins can update the status of an order by ID.
## Getting Started 🚀
### Setup
1. **Clone the repository:**
```
git clone https://github.com/vladshender/Online-Book-Store.git
```
2. **Build the project:**
```
mvn clean package
```
3. **Start the application using Docker Compose:**
```
docker-compose up
```
### Connecting to a Custom Database 🗄
To connect to a custom MySQL database, update the application.properties file with your database details:
```
spring.datasource.url=jdbc:mysql://<YOUR_DB_HOST>:<YOUR_DB_PORT>/<YOUR_DB_NAME>
spring.datasource.username=<YOUR_DB_USERNAME>
spring.datasource.password=<YOUR_DB_PASSWORD>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
server.servlet.context-path=/api

jwt.expiration=<TIME_SESSION_EXPERATION_IN_MS>
jwt.secret=<SECRET_WORD>
```
### You can explore the api through the Swagger UI:
```
http://localhost:8080/api/swagger-ui/index.html
```
## Endpoints
### Authentication

| Method          | Endpoint   |          Role         |     Description                         |
|-----------------|------------|-----------------------|-----------------------------------------|
|  `POST`          | `api/auth/login`       | Unauthorized       |  Login user                          | 
| `POST`         | `api/auth/registration`  | Unauthorized      |    Registration user with password and email  | 
### API Authentication Guide
<details>
  <summary><h4><strong>api/auth/login</strong></h4></summary>

  <strong>Request Body</strong>
  <pre>
  {
      "email": "example@example.com",
      "password": "password123"
  }
  </pre>  

  <strong>Response Body</strong>
  <pre>
{
  "token":"JWT-Token"
}    
  </pre>
</details>

<details>
  <summary><h4><strong>api/auth/registration</strong></h4></summary>

  <strong>Request Body</strong>
  <pre>
  {
      "email": "example@example.com",
      "password": "password123",
      "repeatPassword": "password123",
      "firstName": "Ivan",
      "lastName": "Ivanenko",
      "shippingAddress": "City of Kyiv, 19 Stepana Bandera Street"
  }
  </pre>

  <strong>Response Body</strong>
  <pre>
  {
      "id": 4,
      "email": "example@example.com",
      "firstName": "Ivan",
      "lastName": "Ivanenko",
      "shippingAddress": "City of Kyiv, 19 Stepana Bandera Street"
  }
  </pre>
</details>

### Book
| Method          | Endpoint   |          Role         |     Description                         | 
|-----------------|------------|-----------------------|-----------------------------------------|
|  `GET`          | `api/books`       | USER               |   Get all book                          |
| `GET`         | `api/books/{id}`     | USER             |    Get book by id                         |
| `POST`        | `api/books`         | ADMIN             |    Сreate a new book                     |
| `DELETE`      | `api/books/{id}`     | ADMIN            |     Delete a book by id                  |
| `PUT`         |  `api/books/{id}`    | ADMIN            |     Update book by id                    |
| `GET`         | `api/books/search`   | USER              |   Search for a book by title or author  |
### API Book Guide
<details>
  <summary><h4><strong>GET api/books</strong></h4></summary>
  <strong>Request Body</strong>
  <pre>
    missing
  </pre>
  <strong>Response Body</strong>
  <pre>
    {
        "id": 1,
        "title": "Example Book 1",
        "author": "John Doe",
        "isbn": "1924292334409",
        "price": 250.00,
        "description": "Example Book 1 description",
        "coverImage": "skdl33333",
        "categoryIds": [
            1
        ]
    },
    {
        "id": 2,
        "title": "Example Book 2",
        "author": "Bob Doe",
        "isbn": "1924392334409",
        "price": 350.00,
        "description": "Example Book 2 description",
        "coverImage": "sk3333333",
        "categoryIds": [
            1
        ]
    },
    {
        "id": 3,
        "title": "Example Book 3",
        "author": "Alice Doe",
        "isbn": "1924396334409",
        "price": 350.00,
        "description": "Example Book 3 description",
        "coverImage": "sk332223333",
        "categoryIds": [
            2
        ]
    }
  </pre>
</details>

<details>
  <summary><h4><strong>GET api/books/{id}</strong></h4></summary>
    <strong>Request Body</strong>
  <pre>
    missing
  </pre>
  <strong>Response Body</strong>
  <pre>
{
        "id": 2,
        "title": "Example Book 2",
        "author": "Bob Doe",
        "isbn": "1924392334409",
        "price": 350.00,
        "description": "Example Book 2 description",
        "coverImage": "sk3333333",
        "categoryIds": [
            1
        ]
}
  </pre>
</details>
<details>
  <summary><h4><strong>POST api/books</strong></h4></summary>
  <strong>Request Body</strong>
  <pre>
{
  "title": "Example Book Title",
  "author": "John Doe",
  "isbn": "1234567890123",
  "price": 49.99,
  "description": "This is a brief description of the book.",
  "coverImage": "https://example.com/image.jpg",
  "categories": [1]
}
  </pre>  

  <strong>Response Body</strong>
  <pre>
      "id": 4,
    "title": "Example Book Title",
    "author": "John Doe",
    "isbn": "1234567890123",
    "price": 49.99,
    "description": "This is a brief description of the book.",
    "coverImage": "https://example.com/image.jpg",
    "categoryIds": [
        1
    ]
  </pre>
</details>

<details>
  <summary><h4><strong>DELETE api/books/{id}</strong></h4></summary>
  <strong>Request Body</strong>
  <pre>
    missing
  </pre>
  <strong>Response Body</strong>
  <pre>
    missing
  </pre>
</details>

<details>
  <summary><h4><strong>PUT api/books/{id}</strong></h4></summary>

  <strong>Request Body</strong>
  <pre>
  "title": "Example Book Title",
  "author": "John Doe",
  "isbn": "1234567890123",
  "price": 99.99, <strong>modified field</strong>
  "description": "This is a brief description of the book.",
  "coverImage": "https://example.com/image.jpg",
  "categories": [1]
  </pre>  

  <strong>Response Body</strong>
  <pre>
      "id": 1,
    "title": "Example Book Title",
    "author": "John Doe",
    "isbn": "1924292334409",
    "price": 99.99,
    "description": "This is a brief description of the book.",
    "coverImage": "https://example.com/image.jpg",
    "categoryIds": [
        1
    ]
  </pre>
</details>

<details>
  <summary><h4><strong>GET api/books/search</strong></h4></summary>
  <strong>Endpoint</strong>
  <pre>
  api/books/search?title=&author=John Doe
  </pre>
  <strong>Response Body</strong>
  <pre>
    {
        "id": 1,
        "title": "Example Book 1",
        "author": "John Doe",
        "isbn": "1924292334409",
        "price": 250.00,
        "description": "Example Book 1 description",
        "coverImage": "skdl33333",
        "categoryIds": [
            1
        ]
    },
    {
        "id": 4,
        "title": "Example Book Title",
        "author": "John Doe",
        "isbn": "1234567890123",
        "price": 49.99,
        "description": "This is a brief description of the book.",
        "coverImage": "https://example.com/image.jpg",
        "categoryIds": [
            1
        ]
    }
  </pre>
</details>

### Category
| Method          | Endpoint   |          Role         |     Description                         |
|-----------------|------------|-----------------------|-----------------------------------------|
|  `GET`          | `api/categores`       | USER               |   Get all categories             |
| `GET`         | `api/categores/{id}`     | USER             |    Get category by id              | 
| `POST`        | `api/categores`         | ADMIN             |    Сreate a new category             |
| `DELETE`      | `api/categores/{id}`     | ADMIN            |     Delete a category by id           |
| `PUT`         |  `api/categores/{id}`    | ADMIN            |     Update category by id            |
| `GET`         | `api/categores/{id}/books`   | USER              |   Get all books by category id  |  
### API Category Guide
<details>
  <summary><h4><strong>GET api/categores</strong></h4></summary>
  <strong>Request Body</strong>
  <pre>
    missing
  </pre>
  <strong>Response Body</strong>
  <pre>
    {
        "id": 1,
        "name": "Novel",
        "description": "Novel books"
    },
    {
        "id": 2,
        "name": "Romance",
        "description": "Romance  books"
    },
    {
        "id": 3,
        "name": "Science Fiction",
        "description": "Science Fiction books"
    }
  </pre>
</details>

<details>
  <summary><h4><strong>GET api/categores/{id}</strong></h4></summary>
    <strong>Request Body</strong>
  <pre>
    missing
  </pre>
  <strong>Response Body</strong>
  <pre>
{
    "id": 1,
    "name": "Novel",
    "description": "Novel books"
}
  </pre>
</details>
<details>
  <summary><h4><strong>POST api/categores</strong></h4></summary>
  <strong>Request Body</strong>
  <pre>
{
    "name":"Thriller",
    "description":"Thriller books"
}
  </pre>  
  <strong>Response Body</strong>
  <pre>
{
    "id": 4,
    "name": "Thriller",
    "description": "Thriller books"
}
  </pre>
</details>

<details>
  <summary><h4><strong>DELETE api/categores/{id}</strong></h4></summary>
  <strong>Request Body</strong>
  <pre>
    missing
  </pre>
  <strong>Response Body</strong>
  <pre>
    missing
  </pre>
</details>

<details>
  <summary><h4><strong>PUT api/categores/{id}</strong></h4></summary>
  <pre>
{
    "name":"Thriller",
    "description":"Thriller books"
}
  </pre>
  <strong>Response Body</strong>
  <pre>
{   
    "id": 2,
    "name": "Thriller",
    "description": "Thriller books"
}
  </pre>
</details>

<details>
  <summary><h4><strong>GET api/categores/{id}/books</strong></h4></summary>
  <strong>Endpoint</strong>
  <pre>
  missing
  </pre>
  <strong>Response Body</strong>
  <pre>
    {
        "id": 1,
        "title": "Example Book Title",
        "author": "John Doe",
        "isbn": "1924292334409",
        "price": 99.99,
        "description": "This is a brief description of the book.",
        "coverImage": "https://example.com/image.jpg"
    },
    {
        "id": 2,
        "title": "Love",
        "author": "Tom Love",
        "isbn": "1924392334409",
        "price": 350.00,
        "description": "Book for LOve",
        "coverImage": "sk3333333"
    }
  </pre>
</details>

### Order
| Method          | Endpoint   |          Role         |     Description                         |
|-----------------|------------|-----------------------|-----------------------------------------|
|  `GET`          | `api/order`       | USER               |   Get all orders of the logged in user             |
| `GET`         | `api/order/{id}/items`     | USER             |    Get all items for order by order id              | 
| `GET`        | `api/order/{orderId}/items/{itemId}`| USER      |    Get item for the order by id and product id      |
| `POST`      | `api/order`     | USER            |     Сreating an order for a logged-in user           |
| `PATCH`         |  `api/order/{id}`    | ADMIN            |     Update order status by id            |

### API Order Guide
<details>
  <summary><h4><strong>GET api/order</strong></h4></summary>
  <strong>Request Body</strong>
  <pre>
    missing
  </pre>
  <strong>Response Body</strong>
  <pre>
    {
        "id": 1,
        "userId": 3,
        "orderItemsList": [
            {
                "id": 1,
                "bookId": 3,
                "quantity": 1
            },
            {
                "id": 2,
                "bookId": 1,
                "quantity": 2
            },
            {
                "id": 3,
                "bookId": 2,
                "quantity": 1
            }
        ],
        "orderDate": "2024-09-02 15:35:49",
        "total": 1200.00,
        "status": "PENDING"
    }
  </pre>
</details>

<details>
  <summary><h4><strong>GET api/order/{id}/items</strong></h4></summary>
    <strong>Request Body</strong>
  <pre>
    missing
  </pre>
  <strong>Response Body</strong>
  <pre>
    {
        "id": 1,
        "bookId": 3,
        "quantity": 1
    },
    {
        "id": 3,
        "bookId": 2,
        "quantity": 1
    },
    {
        "id": 2,
        "bookId": 1,
        "quantity": 2
    }
  </pre>
</details>
<details>
  <summary><h4><strong>GET api/order/{orderId}/items/{itemId}</strong></h4></summary>
  <strong>Request Body</strong>
  <pre>
{
    "name":"Thriller",
    "description":"Thriller books"
}
  </pre>  
  <strong>Response Body</strong>
  <pre>
{
    "id": 2,
    "bookId": 1,
    "quantity": 2
}
  </pre>
</details>

<details>
  <summary><h4><strong>POST api/order</strong></h4></summary>
  <strong>Request Body</strong>
It is created on the basis of Shopping Cart
  <pre>
{
    "shippingAddress":"vyl. Velika Vasilkivska 13"
}
  </pre>
  <strong>Response Body</strong>
  <pre>
{
    "id": 6,
    "userId": 4,
    "orderItemsList": [
        {
            "id": 16,
            "bookId": 4,
            "quantity": 2
        }
    ],
    "orderDate": "2024-10-03 21:48:35",
    "total": 99.98,
    "status": "PENDING"
}
  </pre>
</details>

<details>
  <summary><h4><strong>PATCH api/order/{id}</strong></h4></summary>
  <pre>
{
    "status":"DELIVERED"
}
  </pre>
  <strong>Response Body</strong>
  <pre>
{
    "id": 4,
    "userId": 2,
    "orderItemsList": [
        {
            "id": 11,
            "bookId": 2,
            "quantity": 1
        },
        {
            "id": 10,
            "bookId": 3,
            "quantity": 1
        },
        {
            "id": 12,
            "bookId": 3,
            "quantity": 1
        }
    ],
    "orderDate": "2024-09-05 09:53:25",
    "total": 1050.00,
    "status": "DELIVERED"
}    
  </pre>
</details>

### ShoppingCart
| Method          | Endpoint   |          Role         |     Description                         |
|-----------------|------------|-----------------------|-----------------------------------------|
| `GET`         | `api/cart`     | USER             |    Get shopping cart for logging user              | 
| `POST`        | `api/cart`         | ADMIN             |    Create cart items in shopping cart for logging user             |
| `DELETE`      | `api/cart/items/{id}`     | ADMIN            |     Delete cart item by id           |
| `PUT`         |  `api/cart/items/{id}`    | ADMIN            |     Update quantity for cart items by id in shopping cart            |

### API Shopping Cart Guide
<details>
  <summary><h4><strong>GET api/cart</strong></h4></summary>
  <strong>Request Body</strong>
  <pre>
    missing
  </pre>
  <strong>Response Body</strong>
  <pre>
{
    "id": 2,
    "userId": 3,
    "cartItemsList": [
        {
            "id": 4,
            "bookId": 1,
            "bookTitle": "Example Book 1 Example",
            "quantity": 2
        },
        {
            "id": 26,
            "bookId": 3,
            "bookTitle": Example Book 3 Example",
            "quantity": 1
        },
        {
            "id": 5,
            "bookId": 2,
            "bookTitle": "Example Book 2 Example",
            "quantity": 1
        }
    ]
}
  </pre>
</details>

<details>
  <summary><h4><strong>POST api/cart</strong></h4></summary>
    <strong>Request Body</strong>
  <pre>
{
    "bookId":4,
    "quantity":2
}
  </pre>
  <strong>Response Body</strong>
  <pre>
{
    "id": 3,
    "userId": 4,
    "cartItemsList": [
        {
            "id": 27,
            "bookId": 4,
            "bookTitle": "Example Book Title",
            "quantity": 2
        }
    ]
}
  </pre>
</details>
<details>
  <summary><h4><strong>DELETE api/cart/items/{id}</strong></h4></summary>
  <strong>Request Body</strong>
  <pre>
missing
  </pre>  
  <strong>Response Body</strong>
  <pre>
missing
  </pre>
</details>

<details>
  <summary><h4><strong>PUT api/cart/items/{id}</strong></h4></summary>
  <pre>
{
    "quantity":"3"
}
  </pre>
  <strong>Response Body</strong>
  <pre>
{
    "id": 2,
    "userId": 3,
    "cartItemsList": [
        {
            "id": 4,
            "bookId": 1,
            "bookTitle": "Example Book 1 Example",
            "quantity": 3 <strong>modified field</strong>
        },
        {
            "id": 26,
            "bookId": 3,
            "bookTitle": Example Book 3 Example",
            "quantity": 1
        },
        {
            "id": 5,
            "bookId": 2,
            "bookTitle": "Example Book 2 Example",
            "quantity": 1
        }
    ]
}
  </pre>
</details>


## Scheme of main interactions and functions in the project 
![my image](bookstorediagram.png)
##
The ReadSphere API provides a robust foundation for managing users, books, categories, and orders in an online bookstore. The technology stack provides scalability, security and ease of maintenance. Feel free to contribute or provide feedback to help us improve.
##
