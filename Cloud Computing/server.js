const express = require("express");
const cors = require("cors");

const app = express();

const port = 8080;

app.use(cors("*"));

app.get('/test', (req, res) => {
  res.send('Hello World!');
});

app.listen(port, () => {
    console.log(`Server berjalan di http://localhost:${port}`);
});