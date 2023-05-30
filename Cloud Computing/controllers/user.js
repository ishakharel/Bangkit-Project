const db = require("../config/db-config");
const { nanoid } = require("nanoid");

const getUserById = (req, res) => {
  const userId = req.params.id;

  if (!userId) {
    res.status(400).json({
      status: "error",
      message: "Invalid request. Please provide user id",
    });
    return;
  }

  db.query("SELECT * FROM users WHERE id = ?", [userId], (error, results) => {
    if (error) {
      res.status(500).json({
        status: "error",
        message:
          "Internal server error. Cannot find user, please try again later",
      });
      console.log(error);
      return;
    }

    if (results.length === 0) {
      res.status(404).json({
        status: "error",
        message: "User Not Found",
      });
      return;
    }

    res.status(200).json({
      status: "success",
      data: results,
    });
  });
};

const changeUsername = (req, res) => {
  const userId = req.params.id;
  const { newUsername } = req.body;

  if (!userId || !newUsername) {
    res.status(400).json({
      status: "error",
      message: "Invalid request. Please provide user id and new username",
    });
    return;
  }

  db.query(
    "UPDATE users SET name = ? WHERE id = ?",
    [newUsername, userId],
    (error, results) => {
      if (error) {
        res.status(500).json({
          status: "error",
          message:
            "Internal server error. Cannot find user, please try again later",
        });
        console.log(error);
        return;
      }

      res.status(200).json({
        status: "success",
        message: "Username change successfully",
      });
    }
  );
};

const checkPoints = (req, res) => {
  const userId = req.params.id;
  if (!userId) {
    res.status(400).json({
      status: "error",
      message: "Invalid request. Please provide user id",
    });
    return;
  }
  db.query("SELECT * FROM users WHERE id = ?", [userId], (error, results) => {
    if (error) {
      res.status(500).json({
        status: "error",
        message:
          "Internal server error. Cannot find user, please try again later",
      });
      console.log(error);
      return;
    }

    const user = results[0];

    res.status(200).json({
      status: "success",
      points: user.total_points,
    });
  });
};

const exchangePoints = (req, res) => {
  const { merchId, merchPoints } = req.body;
  const userId = req.params.id;

  if (!userId || !merchId || !merchPoints) {
    res.status(400).json({
      status: "error",
      message:
        "Invalid request. Please provide user id, merch id, and merch points",
    });
    return;
  }
  db.query("SELECT * FROM users WHERE id = ?", [userId], (error, results) => {
    if (error) {
      res.status(500).json({
        status: "error",
        message:
          "Internal server error. Cannot find user, please try again later",
      });
      console.log(error);
      return;
    }

    const user = results[0];

    if (merchPoints > user.total_points) {
      res.status(400).json({
        message: "Points is not enough to exchange this merch",
      });
      return;
    }

    const total_points = user.total_points - merchPoints;

    db.query(
      "UPDATE users SET total_points = ? WHERE id = ?",
      [total_points, userId],
      (error, results2) => {
        if (error) {
          res.status(500).json({
            status: "error",
            message:
              "Internal server error. Cannot update user, please try again later",
          });
          console.log(error);
          return;
        }

        db.query(
          "UPDATE merch SET stok = stok - 1 WHERE id = ?",
          [merchId],
          (error, results3) => {
            if (error) {
              res.status(500).json({
                status: "error",
                message:
                  "Internal server error. Cannot update merch, please try again later",
              });
              console.log(error);
              return;
            }

            const id = nanoid(20);

            db.query(
              "INSERT INTO users_merch_redeem VALUES (?, ?, ?, NOW())",
              [id, userId, merchId],
              (error, results4) => {
                if (error) {
                  res.status(500).json({
                    status: "error",
                    message:
                      "Internal server error. Cannot insert merch redeem by user, please try again later",
                  });
                  console.log(error);
                  return;
                }

                res.status(200).json({
                  status: "success",
                  message: "Points is successfully exchange",
                });
              }
            );
          }
        );
      }
    );
  });
};

const addNotification = (req, res) => {
  const { userId, title, message } = req.body;
  if (!userId || !title || !message) {
    res.status(400).json({
      status: "error",
      message: "Invalid request. Please provide user id, title, and message",
    });
    return;
  }

  const id = nanoid(20);

  db.query(
    "INSERT INTO users_notifications VALUES (?, ?, ?, ?, NOW())",
    [id, userId, title, message],
    (error, results) => {
      if (error) {
        res.status(500).json({
          status: "error",
          message:
            "Internal server error. Cannot insert user notification, please try again later",
        });
        console.log(error);
        return;
      }

      res.status(201).json({
        status: "success",
        message: "Notification is created successfully",
      });
    }
  );
};

const getNotificationByUserId = (req, res) => {
  const userId = req.params.id;
  if (!userId) {
    res.status(400).json({
      status: "error",
      message: "Invalid request. Please provide user id",
    });
    return;
  }

  db.query(
    "SELECT * FROM users_notifications WHERE user_id = ?",
    [userId],
    (error, results) => {
      if (error) {
        res.status(500).json({
          status: "error",
          message:
            "Internal server error. Cannot find user notification, please try again later",
        });
        console.log(error);
        return;
      }

      if (results.length === 0) {
        res.status(404).json({
          status: "error",
          message: "Notification not found",
        });
        return;
      }

      res.status(200).json({
        status: "success",
        data: results,
      });
    }
  );
};

const deleteNotificationById = (req, res) => {
  const notifId = req.params.id;
  if (!notifId) {
    res.status(400).json({
      status: "error",
      message: "Invalid request. Please provide notification id",
    });
    return;
  }
  db.query(
    "SELECT * FROM users_notifications WHERE id = ?",
    [notifId],
    (error, results) => {
      if (error) {
        res.status(500).json({
          status: "error",
          message:
            "Internal server error. Cannot delete user notification, please try again later",
        });
        console.log(error);
        return;
      }

      if (results.length === 0) {
        res.status(404).json({
          status: "error",
          message: "Notification not found",
        });
        return;
      }
      db.query(
        "DELETE FROM users_notifications WHERE id = ?",
        [notifId],
        (error, results) => {
          if (error) {
            res.status(500).json({
              status: "error",
              message:
                "Internal server error. Cannot delete user notification, please try again later",
            });
            console.log(error);
            return;
          }

          res.status(200).json({
            status: "success",
            message: "notifications is successfully delete",
          });
        }
      );
    }
  );
};

const getAllMerch = (req, res) => {
  db.query("SELECT * FROM merch", (error, results) => {
    if (error) {
      res.status(500).json({
        status: "error",
        message:
          "Internal server error. Cannot get data from merch, please try again later",
      });
      console.log(error);
      return;
    }

    res.status(200).json({
      status: "success",
      data: results,
    });
  });
};

const getMerchRedeemedByUserId = (req, res) => {
  const userId = req.params.id;
  if (!userId) {
    res.status(400).json({
      status: "error",
      message: "Invalid request. Please provide user id",
    });
    return;
  }

  db.query(
    "SELECT b.name as name, b.image as image, a.date as date FROM users_merch_redeem a JOIN merch b ON a.merch_id = b.id JOIN users c ON a.user_id = c.id WHERE a.user_id = ?",
    [userId],
    (error, results) => {
      if (error) {
        res.status(500).json({
          status: "error",
          message:
            "Internal server error. Cannot get data, please try again later",
        });
        console.log(error);
        return;
      }

      res.status(200).json({
        status: "success",
        data: results,
      });
    }
  );
};

module.exports = {
  getUserById,
  changeUsername,
  checkPoints,
  exchangePoints,
  addNotification,
  getNotificationByUserId,
  deleteNotificationById,
  getAllMerch,
  getMerchRedeemedByUserId,
};
