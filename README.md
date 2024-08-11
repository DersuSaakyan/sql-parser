# SQLParser

SQLParser is a Java-based project designed to parse SQL `SELECT` queries and return a structured `QueryResponse` object. This project leverages the Chain of Responsibility and Null Object patterns to provide a robust and flexible parsing solution.

## Features

- **Chain of Responsibility Pattern**: The parsing process is handled by a series of chained handlers, each responsible for parsing specific parts of the SQL `SELECT` clause.
- **Null Object Pattern**: Ensures that the parsing process handles missing or unrecognized components gracefully without the need for extensive null checks.

## Test
The `SQLParserTest` is designed to facilitate easy testing of the application.

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle for dependency management

### Installation

Clone the repository and include it in your project.

```bash
git clone https://github.com/DersuSaakyan/sql-parser.git