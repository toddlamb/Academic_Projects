# RepCounter

A simple configurable repetition counter that beeps to guide timed sets and repetitions.

## Setup

1. Install [Node.js](https://nodejs.org/).
2. Install dependencies (none required).
3. Start the server:

```bash
npm start
```
4. Open `http://localhost:3000` in your browser.

## Docker / unRAID

1. Build the image:

   ```bash
   docker build -t rep-counter .
   ```
2. Run the container:

   ```bash
   docker run -d -p 3000:3000 rep-counter
   ```

   On unRAID, you can use the Docker or Docker Compose plugin to build and run the included `docker-compose.yml`.

## Usage

Adjust the timing and repetition settings in the form and click **Start Workout**. The app will beep to indicate set and repetition boundaries:

- Two beeps at the start of each set
- One beep at the start and end of each repetition
- Three beeps when all sets are complete
