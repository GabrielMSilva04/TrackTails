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
  
**Time Examples:**
  - `-1h`: Last hour
  - `-1d`: Last day
  - `-1w`: Last week
  - `-1mo`: Last month
  - `2024-11-03T16:48:00Z`: Specific date and time
  - `now()`: Current date and time

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

---

This documentation provides an overview of the `AnimalController` API, listing available endpoints, descriptions, parameters, and success and error responses.