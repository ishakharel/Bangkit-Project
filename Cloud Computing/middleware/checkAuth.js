const jwt = require("jsonwebtoken");
const db = require("../config/db-config");
require("dotenv").config();

const checkAuth = (req, res, next) => {
  const authHeader = req.headers.authorization;

  db.query("SELECT * FROM users WHERE token = ? ", [espcapedToken], (err, result) => {
    console.log(token);
    console.log(result);
    // res.status(200).json({ data: result });
    if (authHeader) {
      const token = authHeader.split(" ")[1];
      jwt.verify(token, process.env.JWT_SECRET, (err, user) => {
        if (err) {
          res.status(403).json({
            status: 'error',
            message: 'User is Forbidden'
          });
          console.log(err);
          return;
        }
        req.user = user;
        next();
      });
    } else {
      res.sendStatus(401);
    }
  });
};

module.exports = checkAuth;
