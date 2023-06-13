const db = require("../config/db-config");

const checkUpload = (req, res, next) => {
  const userId = req.user.id;

  db.query(
    "SELECT COUNT(id) as total_scan FROM waste_history WHERE user_id = ? AND date = CURDATE()",
    [userId],
    (error, results) => {
      const total_scan = results[0].total_scan;
      if (total_scan > 5) {
        res.status(400).json({
          error: true,
          message: "User alredy upload 5 waste today!",
          total_scan_today: total_scan,
        });
      } else {
        next();
      }
    }
  );
};

module.exports = checkUpload;
