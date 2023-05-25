const db = require("../database/db-config");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");
const { nanoid } = require("nanoid");

require("dotenv").config();

const login = (req, res) => {
  const { email, password } = req.body;
  if (!email || !password) {
    res
      .status(400)
      .json({ error: "Invalid request. Please provide email and password" });
    return;
  }

  db.query("SELECT * FROM users WHERE email = ?", [email], (err, results) => {
    if (err) {
      res.status(500).send({
        status: "error",
        message: "Internal server error, please try again later",
      });
      return;
    }

    if (results.length === 0) {
      res.status(401).send({
        status: "error",
        message: "Email not registered",
      });
      return;
    }

    const user = results[0];

    if (!user.pass) {
      res.status(500).send({
        error: "Failed to retrieve user password",
      });
      return;
    }

    const isPassValid = bcrypt.compareSync(password, user.pass);

    if (!isPassValid) {
      res.status(401).send({
        status: "error",
        message: "Invalid password",
      });
      return;
    }

    if (!user.password) {
      res.status(500).send({
        error: "Failed to retrieve user password",
      });
      return;
    }

    const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET, {
      expiresIn: process.env.JWT_EXPIRES_IN,
    });

    res.status(200).send({
      status: "success",
      data: {
        id: user.id,
        name: user.name,
        email: user.email,
        token: token,
      },
    });
  });
};

const register = (req, res) => {
  const { email, password, name } = req.body;
  db.query("SELECT * FROM users WHERE email = ?", [email], (err, results) => {
    if (err) {
      res.status(500).send({
        status: "error",
        message: "Internal server error, please try again later",
      });
      return;
    }

    if (results.length > 0) {
      res.status(409).send({
        status: "error",
        message: "Email alredy exist",
      });
      return;
    }

    const id = nanoid(16);
    const hashedPassword = bcrypt.hashSync(password, 8);

    db.query(
      "INSERT INTO users VALUES (?, ?, ?, ?)",
      [id, email, hashedPassword, name],
      (err, results) => {
        if (err) {
          res.status(500).send({
            status: "error",
            message: "Internal server error, please try again later",
          });
          return;
        }

        if (results.length > 0) {
          res.status(409).send({
            status: "error",
            message: "Email alredy exist",
          });
          return;
        }

        const id = nanoid(16);
        const hashedPassword = bcrypt.hashSync(password, 8);

        db.query(
          "INSERT INTO users (id, name, email, password) VALUES (?, ?, ?, ?)",
          [id, name, email, hashedPassword],
          (err, results) => {
            if (err) {
              res.status(500).send({
                status: "error",
                message: "Internal server error, please try again later",
              });
              return;
            }
            res.status(201).send({
              status: "success",
              message: "User registered succesfully",
            });
          }
        );
      }
    );
  });
};

module.exports = { register, login };
