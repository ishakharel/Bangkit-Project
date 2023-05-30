const jwt = require("jsonwebtoken");
const db = require("../config/db-config");

const checkAuthentication = (req, res, next) => {
  const authHeader = req.headers.authorization;
  const token = authHeader.split(" ")[1];
  // console.log(token);

  db.query("SELECT * FROM users WHERE token = ? ", [token], (err, result) => {
    // res.status(200).json({ data: result });
    if (authHeader) {
      jwt.verify(token, process.env.JWT_SECRET, (err, user) => {
        if (err) {
          return res.sendStatus(403);
        }
        req.user = user;
        next();
      });
    } else {
      res.sendStatus(401);
    }
  });
};

module.exports = checkAuthentication;
