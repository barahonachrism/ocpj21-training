# Java SE 21 Exam Simulator - Frontend

This is the frontend component of the Oracle Java SE 21 Developer Exam Simulator. It is built using Angular and provides an interactive user interface for taking practice exams.

## Technologies Used

- **Angular 17+**: A robust framework for building web applications.
- **TypeScript**: For type-safe development.
- **RxJS**: For reactive programming and handling asynchronous data streams.
- **Angular Router**: For navigation between home, exam, and results pages.

## Project Structure

- `src/app/components`: Contains the UI components (Home, Exam, Results).
- `src/app/services`: Contains services for interacting with the backend API.
- `src/app/models`: Defines the data models used in the application.
- `src/app/app.routes.ts`: Defines the application routes.

## Development Server

To start a local development server, run:

```bash
npm start
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Integration with Backend

The frontend communicates with the backend API located at `http://localhost:8080`. Ensure the backend is running before starting the exam.

## Building

To build the project run:

```bash
npm run build
```

The build artifacts will be stored in the `dist/` directory.

## Running Unit Tests

To execute unit tests, use the following command:

```bash
npm test
```

