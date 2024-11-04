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
      "animalId": "12345",
      "weight": 10.5,
      "latitude": 40.73061,
      "longitude": -73.935242,
      "timestamp": "2024-11-03T16:48:00Z"
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
    - **Schema:** `Double` - Value of the specific field.
  - **404 Not Found**: Data not found.
    - **Schema:** `ErrorResponse`
- **Example Request:**
    ```http
    GET /api/v1/animaldata/latest/12345/weight
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
    GET /api/v1/animaldata/latest/12345
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
    GET /api/v1/animaldata/historic/12345/weight?start=-2d&end=now()&interval=15m&aggregate=mean
    ```
Here's the API documentation for the `AnimalController`:

---
---

### 2. Animal Controller

Base URL: `/api/v1/animals`

---

#### **Create Animal**

- **Endpoint:** `POST /api/v1/animals`
- **Description:** Creates a new animal entry. Requires authorization.
- **Headers:**
  - `Authorization` (String) - Bearer token for authentication.
- **Request Body:**
  - `Animal` (JSON) - Animal data to be recorded.
- **Responses:**
  - **201 Created**: Animal created successfully.
    - **Schema:** `Animal`
  - **401 Unauthorized**: Missing or invalid authorization header.
- **Example Request:**
    ```json
    POST /api/v1/animals
    Authorization: Bearer <your_token>
    {
      "name": "Buddy",
      "species": "Dog",
      "age": 4
    }
    ```

---

#### **Delete Animal**

- **Endpoint:** `DELETE /api/v1/animals/{id}`
- **Description:** Deletes an animal entry by its ID.
- **Path Parameters:**
  - `id` (long) - Unique identifier of the animal to delete.
- **Responses:**
  - **200 OK**: Animal deleted successfully.
  - **404 Not Found**: Animal with the specified ID not found.
- **Example Request:**
    ```http
    DELETE /api/v1/animals/12345
    ```

---

#### **Update Animal**

- **Endpoint:** `PUT /api/v1/animals/{id}`
- **Description:** Updates an existing animal's data.
- **Path Parameters:**
  - `id` (long) - Unique identifier of the animal to update.
- **Request Body:**
  - `Animal` (JSON) - Updated animal data.
- **Responses:**
  - **200 OK**: Animal updated successfully.
    - **Schema:** `Animal`
  - **404 Not Found**: Animal with the specified ID not found.
- **Example Request:**
    ```json
    PUT /api/v1/animals/12345
    {
      "name": "Buddy",
      "species": "Dog",
      "age": 5
    }
    ```

---

#### **Get Animal by ID**

- **Endpoint:** `GET /api/v1/animals/{id}`
- **Description:** Retrieves a specific animal by its ID.
- **Path Parameters:**
  - `id` (long) - Unique identifier of the animal.
- **Responses:**
  - **200 OK**: Animal retrieved successfully.
    - **Schema:** `Animal`
  - **404 Not Found**: Animal with the specified ID not found.
- **Example Request:**
    ```http
    GET /api/v1/animals/12345
    ```

---

#### **Get All Animals**

- **Endpoint:** `GET /api/v1/animals`
- **Description:** Retrieves all animals, or searches by name if a name query parameter is provided.
- **Query Parameters:**
  - `name` (String) - (Optional) Name of the animal to search for.
- **Responses:**
  - **200 OK**: Animal(s) retrieved successfully.
    - **Schema:** List of `Animal` objects.
  - **404 Not Found**: No animal found with the specified name.
- **Example Request:**
    ```http
    GET /api/v1/animals?name=Buddy
    ```

---

## Error Response Schema

In case of errors, the API may return an `ErrorResponse` object, which includes:

- **Schema:** `ErrorResponse`
  - `message` (String): Error message.
  - `status` (HttpStatus): HTTP status code as a string.
  - `code` (int): Numeric HTTP status code.

### Example Error Response

```json
{
  "message": "Authorization header missing or invalid",
  "status": "UNAUTHORIZED",
  "code": 401
}
```


---

## Error Response Schema

In case of errors, the API returns an `ErrorResponse` object in the response body, formatted as follows:

- **Schema:** `ErrorResponse`
  - `message` (String): Error message describing the issue.
  - `status` (HttpStatus): HTTP status of the error.
  - `code` (int): Numeric code of the HTTP status.

### Example Error Response

```json
{
  "message": "Invalid aggregate function",
  "status": "BAD_REQUEST",
  "code": 400
}
```

--- 
