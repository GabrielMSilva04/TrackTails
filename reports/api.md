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

This document provides an overview of the `AnimalDataController` API, listing available endpoints, descriptions, parameters, and success and error responses.