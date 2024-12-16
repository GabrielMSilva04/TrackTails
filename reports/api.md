# API Documentation

## Endpoints

### 1. Animal Data Controller

Base URL: `/api/v1/animaldata`

---

#### **Create Animal Data**

- **Endpoint:** `POST /api/v1/animaldata`
- **Description:** Creates a new data record for an animal.
- **Request Body:**
  - `AnimalDataDTO` (JSON) - Data to be recorded for the animal.
- **Responses:**
  - **201 Created**: Data created successfully.
    - **Schema:** `AnimalDataDTO`
  - **400 Bad Request**: Invalid request.
- **Example Request:**
    ```json
    POST /api/v1/animaldata
    {
      "animalId": "A12345",
      "weight": 20.3,
      "latitude": -34.6118,
      "longitude": -58.4173,
      "speed": 15.5,
      "batteryPercentage": 85.0,
      "blinking": true,
      "timestamp": "2024-12-01T12:30:00Z"
    }
    ```

---

#### **Get Latest Data for a Specific Animal and Field**

- **Endpoint:** `GET /api/v1/animaldata/latest/{animalId}/{field}`
- **Description:** Returns the latest data for a specific field of an animal.
- **Path Parameters:**
  - `animalId` (String) - Animal ID.
  - `field` (String) - Field of the data to be retrieved.
- **Responses:**
  - **200 OK**: Data retrieved successfully.
    - **Schema:** Value of the specific field (`Double`, `Boolean`, or other depending on the field).
  - **404 Not Found**: Data not found.
    - **Schema:** `ErrorResponse`
- **Example Request:**
    ```http
    GET /api/v1/animaldata/latest/A12345/weight
    ```

---

#### **Get Latest Data for a Specific Animal**

- **Endpoint:** `GET /api/v1/animaldata/latest/{animalId}`
- **Description:** Returns the latest data for all fields of a specific animal.
- **Path Parameters:**
  - `animalId` (String) - Animal ID.
- **Responses:**
  - **200 OK**: Data retrieved successfully.
    - **Schema:** `AnimalDataDTO`
  - **404 Not Found**: Data not found.
    - **Schema:** `ErrorResponse`
- **Example Request:**
    ```http
    GET /api/v1/animaldata/latest/A12345
    ```

---

#### **Get Historic Data for a Specific Animal and Field**

- **Endpoint:** `GET /api/v1/animaldata/historic/{animalId}/{field}`
- **Description:** Returns historic data for a specific field of an animal within a given time range.
- **Path Parameters:**
  - `animalId` (String) - Animal ID.
  - `field` (String) - Field of the data to be retrieved.
- **Query Parameters:**
  - `start` (String) - Start of the time range (e.g., `-1d` for the last day). Default: `-1d`.
  - `end` (String) - End of the time range (e.g., `now()` for the current time). Default: `now()`.
  - `interval` (String) - Data sampling interval (e.g., `15m` for 15 minutes). Default: `15m`.
  - `aggregate` (String) - Aggregation function to use. Default: `last`.
    - Allowed values: `"mean"`, `"median"`, `"max"`, `"min"`, `"sum"`, `"last"`, `"first"`, `"stddev"`.
- **Responses:**
  - **200 OK**: Data retrieved successfully.
    - **Schema:** `List<AnimalDataDTO>`
  - **400 Bad Request**: Invalid aggregation function.
    - **Schema:** `ErrorResponse`
  - **404 Not Found**: Data not found.
    - **Schema:** `ErrorResponse`
- **Example Request:**
    ```http
    GET /api/v1/animaldata/historic/A12345/weight?start=-2d&end=now()&interval=30m&aggregate=mean
    ```

---

### AnimalDataDTO Schema

The `AnimalDataDTO` data transfer object (DTO) represents the structure for tracking various data points about an animal. Fields in this schema are optional and will only be included in JSON responses if they contain data, as the class uses `JsonInclude.Include.NON_EMPTY`.

#### Schema: `AnimalDataDTO`

- **`animalId`** (String, required): Unique identifier for the animal.
- **`weight`** (Optional\<Double\>): The weight of the animal in kilograms.
- **`height`** (Optional\<Double\>): The height of the animal in meters.
- **`latitude`** (Optional\<Double\>): The geographical latitude of the animal's location.
- **`longitude`** (Optional\<Double\>): The geographical longitude of the animal's location.
- **`speed`** (Optional\<Double\>): The speed of the animal in meters per second.
- **`heartRate`** (Optional\<Double\>): The heart rate of the animal in beats per minute.
- **`breathRate`** (Optional\<Double\>): The respiratory rate of the animal in breaths per minute.
- **`batteryPercentage`** (Optional\<Double\>): The battery level of the animal's tracking device (in percentage).
- **`blinking`** (Optional\<Boolean\>): Indicates if the animal's device is currently blinking.
- **`additionalTags`** (Map\<String, String\>): Additional custom key-value pairs for extra data.
- **`timestamp`** (Optional\<Instant\>): The timestamp indicating when this data was recorded.

#### Example JSON Representation

```json
{
  "animalId": "A12345",
  "weight": 25.0,
  "height": 0.8,
  "latitude": -34.6118,
  "longitude": -58.4173,
  "speed": 20.0,
  "heartRate": 75,
  "breathRate": 18,
  "batteryPercentage": 90.0,
  "blinking": true,
  "additionalTags": {
    "tag1": "value1",
    "tag2": "value2"
  },
  "timestamp": "2024-12-01T12:30:00Z"
}
```

### Additional Details

- **Optional Fields**: Fields such as `weight`, `height`, `latitude`, `longitude`, `speed`, `heartRate`, `breathRate`, and `timestamp` are wrapped in `Optional`. If not provided, these fields will be omitted from the JSON response.
- **Additional Tags**: This map allows for flexible, dynamic fields that might be relevant for different use cases.

---
---

### 2. Animal Controller

Base URL: `/api/v1/animals`

---

#### **Create Animal**

- **Endpoint:** `POST /api/v1/animals`
- **Description:** Creates a new animal entry associated with the authenticated user.
- **Request Headers:**
  - `Authorization` (String, required) - Bearer token for user authentication.
- **Request Body:**
  - `Animal` (JSON) - Data for the animal to be created, including:
    - `name` (String, required) - Name of the animal.
    - `species` (String, required) - Species of the animal.
    - `birthDate` (Date, optional) - Birth date of the animal.
- **Responses:**
  - **201 Created**: Animal created successfully.
    - **Schema:** `Animal`
  - **401 Unauthorized**: Missing or invalid authorization token.
- **Example Request:**
    ```json
    POST /api/v1/animals
    Authorization: Bearer <JWT_TOKEN>
    {
      "name": "Leo",
      "species": "Lion",
      "birthDate": "2018-05-20"
    }
    ```

---

#### **Delete Animal**

- **Endpoint:** `DELETE /api/v1/animals/{id}`
- **Description:** Deletes an animal entry by its ID.
- **Path Parameters:**
  - `id` (long) - ID of the animal to be deleted.
- **Responses:**
  - **200 OK**: Animal deleted successfully.
    - **Body:** `"Animal deleted successfully"`
  - **404 Not Found**: Animal not found.
- **Example Request:**
    ```http
    DELETE /api/v1/animals/1
    ```

---

#### **Update Animal**

- **Endpoint:** `PUT /api/v1/animals/{id}`
- **Description:** Updates the details of an existing animal entry.
- **Path Parameters:**
  - `id` (long) - ID of the animal to be updated.
- **Request Body:**
  - `Animal` (JSON) - Updated data for the animal, including:
    - `name` (String, required) - Name of the animal.
    - `species` (String, required) - Species of the animal.
    - `birthDate` (Date, optional) - Birth date of the animal.
- **Responses:**
  - **200 OK**: Animal updated successfully.
    - **Schema:** `Animal`
  - **404 Not Found**: Animal not found.
- **Example Request:**
    ```json
    PUT /api/v1/animals/1
    {
      "name": "Leo",
      "species": "Tiger",
      "birthDate": "2018-05-20"
    }
    ```

---

#### **Get Animal by ID**

- **Endpoint:** `GET /api/v1/animals/{id}`
- **Description:** Retrieves details of an animal by its ID.
- **Path Parameters:**
  - `id` (long) - ID of the animal.
- **Responses:**
  - **200 OK**: Animal retrieved successfully.
    - **Schema:** `Animal`
  - **404 Not Found**: Animal not found.
- **Example Request:**
    ```http
    GET /api/v1/animals/1
    ```

---

#### **Get All Animals**

- **Endpoint:** `GET /api/v1/animals`
- **Description:** Retrieves a list of all animals, or a specific animal by name if the `name` parameter is provided.
- **Query Parameters:**
  - `name` (String, optional) - Name of the animal to filter the results.
- **Responses:**
  - **200 OK**: List of animals retrieved successfully.
    - **Schema:** `List<Animal>` (returns a list of all animals if `name` is not provided)
    - **Schema:** `Animal` (returns a single animal if `name` is provided)
  - **404 Not Found**: Animal not found.
- **Example Request:**
    ```http
    GET /api/v1/animals?name=Leo
    ```

---

## Animal Entity Schema

The `Animal` entity represents the animal data structure.

- **Schema:** `Animal`
  - `id` (long): Unique identifier of the animal.
  - `name` (String): Name of the animal.
  - `species` (String): Species of the animal.
  - `birthDate` (Date): Birth date of the animal.

---
---

### 3. Notification Controller

Base URL: `/api/v1/notifications`

---

#### **Create Notification**

- **Endpoint:** `POST /api/v1/notifications`
- **Description:** Creates a new notification for a specified user and animal.
- **Request Body:**
  - `Notification` (JSON) - Data for the notification to be created, including:
    - `userId` (long, required) - ID of the user associated with the notification.
    - `animalId` (long, required) - ID of the animal associated with the notification.
    - `title` (String, required) - Title of the notification.
    - `content` (String, required) - Content or message of the notification.
- **Responses:**
  - **201 Created**: Notification created successfully.
    - **Schema:** `Notification`
  - **400 Bad Request**: Notification creation failed.
- **Example Request:**
    ```json
    POST /api/v1/notifications
    {
      "userId": 1,
      "animalId": 101,
      "title": "Vaccination Reminder",
      "content": "Your pet is due for a vaccination."
    }
    ```

---

#### **Delete Notification**

- **Endpoint:** `DELETE /api/v1/notifications/{id}`
- **Description:** Deletes a notification by its ID.
- **Path Parameters:**
  - `id` (long) - ID of the notification to be deleted.
- **Responses:**
  - **200 OK**: Notification deleted successfully.
    - **Schema:** `Notification`
  - **404 Not Found**: Notification not found.
- **Example Request:**
    ```http
    DELETE /api/v1/notifications/1
    ```

---

#### **Update Notification**

- **Endpoint:** `PUT /api/v1/notifications/{id}`
- **Description:** Updates an existing notification's details.
- **Path Parameters:**
  - `id` (long) - ID of the notification to be updated.
- **Request Body:**
  - `Notification` (JSON) - Updated data for the notification, including:
    - `title` (String, required) - Updated title of the notification.
    - `content` (String, required) - Updated content or message.
- **Responses:**
  - **200 OK**: Notification updated successfully.
    - **Schema:** `Notification`
  - **404 Not Found**: Notification not found.
- **Example Request:**
    ```json
    PUT /api/v1/notifications/1
    {
      "title": "Updated Reminder",
      "content": "Vaccination reminder updated."
    }
    ```

---

#### **Get All Notifications**

- **Endpoint:** `GET /api/v1/notifications`
- **Description:** Retrieves all notifications or filters by `userId` and/or `animalId`.
- **Query Parameters:**
  - `userId` (long, optional) - ID of the user to filter notifications.
  - `animalId` (long, optional) - ID of the animal to filter notifications.
- **Responses:**
  - **200 OK**: Notifications retrieved successfully.
    - **Schema:** List of `Notification`
- **Example Request:**
    ```http
    GET /api/v1/notifications?userId=1&animalId=101
    ```

---

#### **Get Notification by ID**

- **Endpoint:** `GET /api/v1/notifications/{id}`
- **Description:** Retrieves a single notification by its ID.
- **Path Parameters:**
  - `id` (long) - ID of the notification to retrieve.
- **Responses:**
  - **200 OK**: Notification retrieved successfully.
    - **Schema:** `Notification`
  - **404 Not Found**: Notification not found.
- **Example Request:**
    ```http
    GET /api/v1/notifications/1
    ```

---

## Notification Entity Schema

The `Notification` entity represents the structure for notifications.

- **Schema:** `Notification`
  - `id` (long): Unique identifier of the notification.
  - `userId` (long): ID of the user associated with the notification.
  - `animalId` (long): ID of the animal associated with the notification.
  - `title` (String): Title of the notification.
  - `content` (String): Content or message of the notification.
  - `createdAt` (LocalDateTime): Date and time when the notification was created.
  - `read` (boolean): Indicates if the notification has been read.

---
---

### 4. Report Controller

Base URL: `/api/v1/reports`

---

#### **Hello Endpoint**

- **Endpoint:** `GET /api/v1/reports/hello`
- **Description:** A simple endpoint to test the Report Service.
- **Responses:**
  - **200 OK**: Returns a greeting message from the Report Service.
- **Example Response:**
    ```json
    {
      "message": "Hello from Report Service"
    }
    ```

---

#### **Create Report**

- **Endpoint:** `POST /api/v1/reports`
- **Description:** Creates a new report for a specified animal.
- **Request Body:**
  - `Report` (JSON) - Contains the following fields:
    - `animalId` (Long, required) - The ID of the animal associated with the report.
    - `fileName` (String, required) - The file name or identifier of the report document.
- **Responses:**
  - **201 Created**: Report created successfully.
    - **Schema:** `Report`
  - **400 Bad Request**: Report creation failed due to invalid data.
- **Example Request:**
    ```json
    POST /api/v1/reports
    {
      "animalId": 123,
      "fileName": "report_123.pdf"
    }
    ```

---

#### **Get Report by ID**

- **Endpoint:** `GET /api/v1/reports/{id}`
- **Description:** Retrieves a report by its unique ID.
- **Path Parameters:**
  - `id` (Long) - The ID of the report to retrieve.
- **Responses:**
  - **200 OK**: Report retrieved successfully.
    - **Schema:** `Report`
  - **404 Not Found**: Report not found.
- **Example Request:**
    ```http
    GET /api/v1/reports/1
    ```

---

## Report Entity Schema

The `Report` entity represents the structure of each report.

- **Schema:** `Report`
  - `id` (Long): Unique identifier of the report.
  - `animalId` (Long): ID of the animal associated with the report.
  - `timestamp` (LocalDateTime): Date and time when the report was created.
  - `fileName` (String): Name or identifier of the report file.

### Example Report Object

```json
{
  "id": 1,
  "animalId": 123,
  "timestamp": "2023-12-15T12:34:56",
  "fileName": "report_123.pdf"
}
```

Here's the API documentation for the `UserController` in English:

---

### 5. User Controller

Base URL: `/api/v1/users`

---

#### **Register User**

- **Endpoint:** `POST /api/v1/users`
- **Description:** Registers a new user with the provided information.
- **Request Body:**
  - `User` (JSON) - Contains the following fields:
    - `displayName` (String, required) - The display name of the user (max 15 characters).
    - `email` (String, required) - The unique email address of the user.
    - `password` (String, required) - The password for the user (hashed and salted upon registration).
- **Responses:**
  - **201 Created**: User registered successfully.
    - **Schema:** `User`
  - **400 Bad Request**: User registration failed due to invalid data.
- **Example Request:**
    ```json
    POST /api/v1/users
    {
      "displayName": "JohnDoe",
      "email": "johndoe@example.com",
      "password": "securepassword123"
    }
    ```

---

#### **Update User**

- **Endpoint:** `PUT /api/v1/users/{userId}`
- **Description:** Updates the information for an existing user.
- **Path Parameters:**
  - `userId` (Long) - The unique ID of the user to update.
- **Request Body:**
  - `User` (JSON) - Contains the updated fields:
    - `displayName` (String) - New display name of the user (optional).
    - `email` (String) - New email address of the user (optional).
    - `password` (String) - New password for the user (optional).
- **Responses:**
  - **200 OK**: User updated successfully.
    - **Schema:** `User`
  - **404 Not Found**: User not found.
- **Example Request:**
    ```json
    PUT /api/v1/users/1
    {
      "displayName": "JaneDoe",
      "email": "janedoe@example.com"
    }
    ```

---

#### **Get User by ID**

- **Endpoint:** `GET /api/v1/users/{userId}`
- **Description:** Retrieves a user by their unique ID.
- **Path Parameters:**
  - `userId` (Long) - The ID of the user to retrieve.
- **Responses:**
  - **200 OK**: User retrieved successfully.
    - **Schema:** `User`
  - **404 Not Found**: User not found.
- **Example Request:**
    ```http
    GET /api/v1/users/1
    ```

---

#### **Delete User**

- **Endpoint:** `DELETE /api/v1/users/{userId}`
- **Description:** Deletes a user by their unique ID.
- **Path Parameters:**
  - `userId` (Long) - The ID of the user to delete.
- **Responses:**
  - **200 OK**: User deleted successfully.
  - **404 Not Found**: User not found.
- **Example Request:**
    ```http
    DELETE /api/v1/users/1
    ```

---

#### **List All Users**

- **Endpoint:** `GET /api/v1/users`
- **Description:** Retrieves a list of all registered users.
- **Responses:**
  - **200 OK**: List of users retrieved successfully.
    - **Schema:** Array of `User` objects.
- **Example Request:**
    ```http
    GET /api/v1/users
    ```

---

## User Entity Schema

The `User` entity represents the structure of each user.

- **Schema:** `User`
  - `userId` (Long): Unique identifier for the user.
  - `displayName` (String): Display name of the user.
  - `email` (String): Email address of the user.
  - `hashPassword` (String): Hashed password of the user (hidden in JSON responses).
  - `salt` (String): Salt used for hashing the password (hidden in JSON responses).

### Example User Object

```json
{
  "userId": 1,
  "displayName": "JohnDoe",
  "email": "johndoe@example.com"
}
```

### 6. Fence Controller

Base URL: `/api/v1/fences`

---

#### **Create or Update a Fence**

- **Endpoint:** `POST /api/v1/fences`
- **Description:** Creates or updates a geofence associated with a specific animal.
- **Request Body:**
  - `FenceDTO` (JSON) - Details of the fence to be created or updated.
- **Responses:**
  - **201 Created**: Fence created or updated successfully.
    - **Schema:** 
      ```json
      {
        "message": "Fence created/updated successfully"
      }
      ```
  - **400 Bad Request**: Invalid input data.
    - **Schema:** 
      ```json
      {
        "error": "Invalid input data: <details>"
      }
      ```
  - **500 Internal Server Error**: Failed to process the request.
    - **Schema:** 
      ```json
      {
        "error": "Failed to process request: <details>"
      }
      ```
- **Example Request:**
    ```json
    POST /api/v1/fences
    {
      "animalId": 12345,
      "vertices": [
        {"latitude": 40.7128, "longitude": -74.0060},
        {"latitude": 40.73061, "longitude": -73.935242},
        {"latitude": 40.7519, "longitude": -73.9878}
      ],
      "type": "polygon"
    }
    ```

---

#### **Get Fence by Animal ID**

- **Endpoint:** `GET /api/v1/fences/{animalId}`
- **Description:** Fetches the geofence details for a specific animal.
- **Path Parameters:**
  - `animalId` (Long) - The unique ID of the animal.
- **Responses:**
  - **200 OK**: Fence data retrieved successfully.
    - **Schema:** `FenceDTO`
  - **404 Not Found**: Fence not found for the specified animal.
    - **Schema:** 
      ```json
      {
        "error": "Fence not found for animalId: <animalId>"
      }
      ```
  - **500 Internal Server Error**: Failed to fetch the fence.
    - **Schema:** 
      ```json
      {
        "error": "Failed to fetch fence: <details>"
      }
      ```
- **Example Request:**
    ```http
    GET /api/v1/fences/12345
    ```

---

#### **Delete Fence by Animal ID**

- **Endpoint:** `DELETE /api/v1/fences/{animalId}`
- **Description:** Deletes the geofence details associated with a specific animal.
- **Path Parameters:**
  - `animalId` (Long) - The unique ID of the animal.
- **Responses:**
  - **200 OK**: Fence deleted successfully.
    - **Schema:** 
      ```json
      {
        "message": "Fence deleted successfully"
      }
      ```
  - **404 Not Found**: Fence not found.
    - **Schema:** 
      ```json
      {
        "error": "Fence not found for animalId: <animalId>"
      }
      ```
  - **400 Bad Request**: Invalid input data.
  - **500 Internal Server Error**: Failed to delete the fence.
    - **Schema:** 
      ```json
      {
        "error": "Failed to delete fence: <details>"
      }
      ```
- **Example Request:**
    ```http
    DELETE /api/v1/fences/12345
    ```

---

#### **Check if a Point is Inside the Fence**

- **Endpoint:** `GET /api/v1/fences/{animalId}/isInside`
- **Description:** Checks whether a given latitude and longitude point is inside the geofence for a specific animal.
- **Path Parameters:**
  - `animalId` (Long) - The unique ID of the animal.
- **Query Parameters:**
  - `latitude` (Double) - Latitude of the point.
  - `longitude` (Double) - Longitude of the point.
- **Responses:**
  - **200 OK**: Point check completed successfully.
    - **Schema:** 
      ```json
      {
        "animalId": 12345,
        "latitude": 40.7128,
        "longitude": -74.0060,
        "isInside": true
      }
      ```
  - **404 Not Found**: Fence not found for the specified animal.
    - **Schema:** 
      ```json
      {
        "error": "Fence not found for animalId: <animalId>"
      }
      ```
  - **400 Bad Request**: Invalid input data.
    - **Schema:** 
      ```json
      {
        "error": "Invalid input data: <details>"
      }
      ```
  - **500 Internal Server Error**: Failed to check if the point is inside the fence.
    - **Schema:** 
      ```json
      {
        "error": "Failed to check point: <details>"
      }
      ```
- **Example Request:**
    ```http
    GET /api/v1/fences/12345/isInside?latitude=40.7128&longitude=-74.0060
    ```

---

## FenceDTO Schema

The `FenceDTO` represents the structure for managing geofence data for animals.

### Schema: `FenceDTO`

- **`animalId`** (Long, required): The unique ID of the animal.
- **`vertices`** (List\<Object\>, required): List of geographical points (latitude, longitude) defining the geofence boundary.
  - **`latitude`** (Double): Latitude of the vertex.
  - **`longitude`** (Double): Longitude of the vertex.
- **`type`** (String, optional): The type of geofence. Example: `"polygon"` or `"circle"`.
- **`radius`** (Double, optional): Radius of the fence (if the type is `"circle"`).

### Example JSON Representation

```json
{
  "animalId": 12345,
  "vertices": [
    {"latitude": 40.7128, "longitude": -74.0060},
    {"latitude": 40.73061, "longitude": -73.935242},
    {"latitude": 40.7519, "longitude": -73.9878}
  ],
  "type": "polygon"
}
```

---
---

Aqui está a documentação detalhada para os controladores `FindersController` do `animal` e `user`. Adaptei o estilo da referência que você forneceu:

---

### 7. Finders Animal Controller

Base URL: `/api/v1/finders/animal`

---

#### **Get Animal By DeviceId**

- **Endpoint:** `POST /api/v1/finders/animal`
- **Description:** Retrieve an animal by the ID of the device that is attached to it.
- **Request Body:**
  - `FindersRequest` (JSON) - Contains the `deviceId` of the device attached to the animal.
- **Responses:**
  - **200 OK**: Animal found successfully.
    - **Schema:** `Animal`
  - **404 Not Found**: Animal not found.
    - **Schema:** `ErrorResponse`
- **Example Request:**
    ```json
    POST /api/v1/finders/animal
    {
      "deviceId": "device12345"
    }
    ```

---

#### **Get Public Animal Image**

- **Endpoint:** `GET /api/v1/finders/animal/{id}/image`
- **Description:** Retrieve the uploaded image for an animal (public access).
- **Path Parameters:**
  - `id` (Long) - ID of the animal whose image is being requested.
- **Responses:**
  - **200 OK**: Image retrieved successfully.
    - **Content Type:** `image/jpeg`
  - **404 Not Found**: Animal or image not found.
    - **Schema:** `ErrorResponse`
  - **500 Internal Server Error**: Error while retrieving the image.
    - **Schema:** `ErrorResponse`
- **Example Request:**
    ```http
    GET /api/v1/finders/animal/123/image
    ```

---
---

### 8. Finders User Controller

Base URL: `/api/v1/finders/user`

---

#### **Get User By Id**

- **Endpoint:** `POST /api/v1/finders/user`
- **Description:** Retrieve user contact information by providing the ID of the animal's attached device.
- **Request Body:**
  - `FindersRequest` (JSON) - Contains the `deviceId` of the device attached to the animal.
- **Responses:**
  - **200 OK**: User contact information retrieved successfully.
    - **Schema:** `User`
  - **404 Not Found**: Animal or user not found.
    - **Schema:** `ErrorResponse`
- **Example Request:**
    ```json
    POST /api/v1/finders/user
    {
      "deviceId": "device12345"
    }
    ```

---

## DTOs and Models

### Schema: `FindersRequest`

- **`deviceId`** (String, required): The unique identifier of the device attached to the animal.

**Example JSON:**

```json
{
  "deviceId": "device12345"
}
```

---

### Schema: `Animal`

- **`id`** (Long, required): Unique identifier for the animal.
- **`name`** (String): Name of the animal.
- **`species`** (String): Species of the animal.
- **`age`** (Integer): Age of the animal in years.
- **`imagePath`** (String): Path to the animal's image.
- **`userId`** (Long): ID of the user associated with the animal.

**Example JSON:**

```json
{
  "id": 123,
  "name": "Buddy",
  "species": "Dog",
  "age": 5,
  "imagePath": "buddy.jpg",
  "userId": 456
}
```

---

### Schema: `User`

- **`id`** (Long, required): Unique identifier for the user.
- **`name`** (String): Name of the user.
- **`email`** (String): Email address of the user.
- **`phone`** (String): Contact phone number of the user.

**Example JSON:**

```json
{
  "id": 456,
  "name": "John Doe",
  "email": "johndoe@example.com",
  "phone": "+1234567890"
}
```

---
---

Aqui está a documentação para o **Actions Controller**, seguindo o formato solicitado:

---

### 9. Actions Controller

Base URL: `/api/v1/actions`

---

#### **Trigger an Action**

- **Endpoint:** `POST /api/v1/actions`
- **Description:** Triggers an action for a specified animal.
- **Headers:**
  - `X-User-Id` (String, required): The ID of the user initiating the action.
- **Request Body:**
  - `ActionDTO` (JSON) - Details of the action to be triggered.
- **Responses:**
  - **200 OK**: Action triggered successfully.
  - **400 Bad Request**: The action type is invalid.
    - **Schema:** `ErrorResponse`
  - **401 Unauthorized**: Missing `X-User-Id` header.
    - **Schema:** `ErrorResponse`
  - **403 Forbidden**: The `X-User-Id` header does not match the owner of the animal.
    - **Schema:** `ErrorResponse`
  - **404 Not Found**: The specified animal was not found.
    - **Schema:** `ErrorResponse`
- **Example Request:**
    ```json
    POST /api/v1/actions
    {
      "actionType": "FEED",
      "animalId": 12345
    }
    ```

- **Example Response (400 Bad Request):**
    ```json
    {
      "message": "Invalid action type",
      "status": "BAD_REQUEST",
      "code": 400
    }
    ```

- **Example Response (404 Not Found):**
    ```json
    {
      "message": "Animal not found",
      "status": "NOT_FOUND",
      "code": 404
    }
    ```

---

## ActionDTO Schema

The `ActionDTO` data transfer object represents the structure for defining actions to be performed on an animal.

### Schema: `ActionDTO`

- **`actionType`** (String, required): The type of action to be triggered (e.g., `"FEED"`, `"WALK"`).
- **`animalId`** (Long, required): The unique identifier of the animal for which the action is triggered.

### Example JSON Representation

```json
{
  "actionType": "FEED",
  "animalId": 12345
}
```

---

## ErrorResponse Schema

The `ErrorResponse` data transfer object is used to provide error details for failed requests.

### Schema: `ErrorResponse`

- **`message`** (String, required): A human-readable message describing the error.
- **`status`** (String, required): The HTTP status associated with the error (e.g., `"BAD_REQUEST"`, `"NOT_FOUND"`).
- **`code`** (Integer, required): The numeric HTTP status code.

### Example JSON Representation

```json
{
  "message": "Invalid action type",
  "status": "BAD_REQUEST",
  "code": 400
}
```

---

### Additional Details

- **Validation:**
  - The `actionType` and `animalId` fields in the `ActionDTO` are mandatory. Requests missing these fields will result in a validation error.
  - The `X-User-Id` header must match the owner of the specified animal; otherwise, a `403 Forbidden` response is returned.

- **Action Service Validation:**
  - The `actionService.isActionTypeValid` method ensures that the provided action type is supported.

- **Animal Existence Check:**
  - The `animalService.getAnimal` method verifies the existence of the animal with the specified `animalId`.

---
---

Se precisar de ajustes ou mais detalhes em alguma parte, é só avisar!

## Error Response Schema

In case of errors, the API returns an `ErrorResponse` object formatted as follows:

- **Schema:** `ErrorResponse`
  - `message` (String): Error message.
  - `status` (HttpStatus): HTTP status.
  - `code` (int): Numeric HTTP status code.

### Example Error Response

```json
{
  "message": "Animal not found",
  "status": "NOT_FOUND",
  "code": 404
}
```
