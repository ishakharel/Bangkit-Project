const express = require("express");
const db = require("./config/db-config");
const authRouter = require("./routes/routes");
const cors = require("cors");
const session = require("express-session");
const bodyParser = require("body-parser");

const PORT = process.env.PORT || 8000;
const app = express();
app.use(
  session({
    secret: "your-secret-key",
    resave: false,
    saveUninitialized: true,
  })
);
app.use(cors("*"));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

db.connect((err) => {
  if (err) throw err;
  console.log("database connected");
});

app.get("/test", (req, res) => {
  res.status(200).send({
    status: "success",
    message: "API is running",
  });
});

app.use("/", authRouter);

app.listen(PORT, () => {
  console.log(`Server running on port: http://localhost:${PORT}/`);
});
