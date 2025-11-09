# Kafka Monitoring UI (kafka-ui)

A small React-based admin UI for monitoring Kafka resources. This repository contains the frontend app used to display Kafka cluster information, topics, and consumer group details. It's built with React, MUI (Material UI), React Router and a small local service layer.

## Status

- Stability: Work-in-progress
- Tested with Node.js + npm (see prerequisites)

## Table of contents

- [Features](#features)
- [Tech stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Install and run (PowerShell)](#install-and-run-powershell)
- [Available scripts](#available-scripts)
- [Project structure](#project-structure)
- [Key files and components](#key-files-and-components)
- [Development notes](#development-notes)
- [Testing](#testing)
- [Build for production](#build-for-production)
- [Contributing](#contributing)
- [License & contact](#license--contact)

## Features

- Browse Kafka clusters, topics, and basic metrics (UI components scaffolded)
- Simple service layer in `src/services/kafkaService.js` to centralize API interactions
- Responsive layout using MUI and Bootstrap styles

## Tech stack

- React 18
- Material UI (MUI)
- React Router DOM
- Bootstrap (utility styles)
- react-scripts (Create React App)

## Prerequisites

- Node.js (LTS recommended). Test with:

```powershell
node --version
npm --version
```

- If you plan to run against a live Kafka REST API or backend, have that backend available and set the required API base URL (see Development notes).

## Install and run (PowerShell)

1. Install dependencies

```powershell
npm install
```

2. Start the development server (opens at http://localhost:3000 by default)

```powershell
npm start
```

Notes for PowerShell on Windows: run the commands from the project root (where `package.json` is located). If you encounter permission issues running scripts, ensure execution policy allows running Node/npm commands (this is not changing PowerShell execution policy, only run Node/npm normally).

## Available scripts

The `package.json` defines the following npm scripts used during development:

- `start` — Starts the development server using `react-scripts start`.
- `build` — Builds the app for production using `react-scripts build`.
- `test` — Runs the test runner (Jest via Create React App).
- `eject` — Ejects the CRA configuration (irreversible).

You can run them via:

```powershell
npm run test
npm run build
```

## Project structure

Top-level files and folders you will work with:

- `public/` — static assets, `index.html`, manifest, etc.
- `src/` — app source
	- `App.js` — root React component
	- `index.js` — app entry
	- `components/` — UI components (`Header.js`, `Kafka.js`, ...)
	- `layout/` — layout components (`MainContent.js`)
	- `services/` — small API/service layer (`kafkaService.js`)
	- other files: `App.css`, `index.css`, `reportWebVitals.js`, `setupTests.js`

## Key files and components

- `src/components/Header.js` — top header/navigation used by the app.
- `src/components/Kafka.js` — main Kafka view (topic listing / details).
- `src/layout/MainContent.js` — layout wrapper for main pages.
- `src/services/kafkaService.js` — HTTP helper functions (fetching topics, clusters, etc.).

Open these files to adapt API endpoints and to wire real telemetry/metrics endpoints.

## Development notes

- The project uses Create React App (`react-scripts`). If you need to change Webpack or Babel config, the supported route is via `npm run eject` (be careful — this is irreversible).
- The UI currently expects a backend API to provide Kafka data. By default there's no environment variable baked into the repo; integrate by editing `src/services/kafkaService.js` to point to your API base URL or add a `.env` and use CRA environment variables (for example `REACT_APP_API_BASE_URL`). If you add `.env` keys, remember to restart the dev server.

Example (in `src/services/kafkaService.js`):

```js
// const API_BASE = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';
```

## Testing

Run tests with:

```powershell
npm test -- --watchAll=false
```

This will execute tests once and exit (handy for CI). The project contains basic test scaffolding (`App.test.js`) using React Testing Library.

## Build for production

Create a production bundle with:

```powershell
npm run build
```

The optimized bundle will be placed in the `build/` folder. Serve it using any static file server, or integrate into a backend.

## Contributing

- Fork the repo and create a feature branch for your changes.
- Add or update tests for new behavior.
- Open a pull request describing the change and motivation.

If you'd like help wiring the backend API or adding unit/integration tests, open an issue with details.

