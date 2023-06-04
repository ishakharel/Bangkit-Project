const jwt = require("jsonwebtoken");
const db = require("../config/db-config");
require("dotenv").config();

const checkAuth = (req, res, next) => {
  const authHeader = req.headers.authorization;
  const token = authHeader.split(" ")[1];

  db.query("SELECT * FROM users WHERE token = ? ", [token], (err, result) => {
    if (authHeader) {
      jwt.verify(token, process.env.JWT_SECRET, (err, user) => {
        if (err) {
          res.status(403).json({
            status: "error",
            message: "User is Forbidden",
          });
          console.log(err);
          return;
        }
        req.user = user;
        next();
      });
    } else {
      res.status(401).json({
        status: "error",
        message: "Unauthorized User",
      });
    }
  });
};

module.exports = checkAuth;
