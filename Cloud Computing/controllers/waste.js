const db = require("../database/db-config");

require("dotenv").config();

const wasteCategory = (req, res) => {
  db.query("SELECT * FROM waste_category", (error, result) => {
    if (error) {
      console.log(error);
      return res.status(500).send("Server Error!");
    }

    return res.status(200).json(result);
  });
};

const wasteHistory = (req, res) => {
  const userId = req.session.userId;

  return res
    .status(200)
    .json({ userId: userId, message: "Profile data retrieved" });
};

module.exports = { wasteCategory, wasteHistory };
