const db = require("../config/db-config");

const checkUpload = (req, res, next) => {
    const userId = req.user.id;

    db.query("SELECT COUNT(id) as total_scan FROM waste_history WHERE user_id = ? AND date = CURDATE()", [userId], (error, results) => {
        const total_scan = results[0].total_scan;
        if(total_scan > 4){
            res.status(400).json({
                error: true,
                message: "User alredy upload 4 waste today!"
            });
        } else {
            next();
        }
    });
}

module.exports = checkUpload;