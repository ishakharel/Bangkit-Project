const db = require("../database/db-config");

require("dotenv").config();

const categories = (req, res) => {
  db.query("SELECT * FROM waste_category", (error, result) => {
    if (error) {
      console.log(error);
      return res.status(500).send("Server Error!");
    }

    return res.status(200).json(result);
  });
};

const histories = (req, res) => {
  const userId = req.session.userId;
  db.query(
    "SELECT * FROM waste_history WHERE user_id = ?",
    [userId],
    (error, result) => {
      if (error) {
        console.log(error);
        return res.status(500).send("Server Error!");
      }

      return res.status(200).json({ id: userId, data: result });
    }
  );
};

module.exports = { categories, histories };
