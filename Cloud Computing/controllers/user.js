const db = require("../config/db-config");
const { nanoid } = require("nanoid");
const { bucket } = require("../config/storage-config");

const getUserById = (req, res) => {
  const userId = req.user.id;

  if (!userId) {
    res.status(400).json({
      error: true,
      message: "Invalid request. Please provide user id",
    });
    return;
  }

  db.query("SELECT * FROM users WHERE id = ?", [userId], (error, results) => {
    if (error) {
      res.status(500).json({
        error: true,
        message:
          "Internal server error. Cannot find user, please try again later",
      });
      console.log(error);
      return;
    }

    if (results.length === 0) {
      res.status(404).json({
        error: true,
        message: "User Not Found",
      });
      return;
    }

    res.status(200).json({
      status: "success",
      data: results[0],
    });
  });
};

const editProfile = (req, res) => {
  const userId = req.user.id;
  const { email, newUsername, address, gender, age, job, dob } = req.body;

  if (!userId || !newUsername) {
    res.status(400).json({
      error: true,
      message: "Invalid request. Please provide user id and new username",
    });
    return;
  }

  db.query(
    "UPDATE users SET email = ?, name = ?, address = ?, gender = ?, age = ?, job = ?, dob = ? WHERE id = ?",
    [email, newUsername, address, gender, age, job, dob, userId],
    (error, results) => {
      if (error) {
        res.status(500).json({
          error: true,
          message:
            "Internal server error. Cannot find user, please try again later",
        });
        console.log(error);
        return;
      }

      res.status(200).json({
        status: "success",
        data: {
          email: email,
          username: newUsername,
          address: address,
          gender: gender,
          age: age,
          job: job,
          dob: dob,
        },
      });
    }
  );
};

const changeImage = (req, res) => {
  const userId = req.user.id;
  if (!req.file)
    return res
      .status(400)
      .send({ status: "error", message: "Please provide an image" });

  const id = nanoid(16);
  const fileName = `user_profile/${id}`;
  const blob = bucket.file(fileName);

  db.query("SELECT * FROM users WHERE id = ?", [userId], (error, results) => {
    if (error) {
      res.status(500).json({
        error: true,
        message:
          "Internal server error. Cannot find user, please try again later",
      });
      console.log(error);
      return;
    }

    if (results.length !== 0 && results[0].image) {
      const oldFileName = results[0].image.replace(
        "https://storage.googleapis.com/ecoloops_bucket/",
        ""
      );
      const blobDelete = bucket.file(oldFileName);

      blobDelete.delete().catch((err) => {
        console.log(err);
      });
    }

    const blobStream = blob.createWriteStream({
      metadata: {
        contentType: req.file.mimetype,
      },
    });

    blobStream.on(true, (err) => {
      console.log(err);
      res.status(500).json({ status: "error", message: err });
    });

    blobStream.on("finish", () => {
      const publicUrl = `https://storage.googleapis.com/ecoloops_bucket/${blob.name}`;
      console.log(publicUrl);
      db.query(
        "UPDATE users SET image = ? WHERE id = ?",
        [publicUrl, userId],
        (error, results) => {
          if (error) {
            res.status(500).json({
              error: true,
              message:
                "Internal server error. Cannot update user image, please try again later",
            });
            console.log(error);
            return;
          }

          res.status(200).json({
            status: "success",
            message: "Image is successfully updated",
            imageUrl: publicUrl,
          });
        }
      );
    });

    blobStream.end(req.file.buffer);
  });
};

const getUserDashboardData = (req, res) => {
  const userId = req.user.id;
  if (!userId) {
    res.status(400).json({
      error: true,
      message: "Invalid request. Please provide user id",
    });
    return;
  }
  db.query("SELECT * FROM users WHERE id = ?", [userId], (error, results) => {
    if (error) {
      res.status(500).json({
        error: true,
        message:
          "Internal server error. Cannot find user, please try again later",
      });
      console.log(error);
      return;
    }

    const user = results[0];

    db.query("SELECT COUNT(id) as scan FROM waste_history WHERE user_id = ?", [userId], (error, results2) => {
      if (error) {
        res.status(500).json({
          error: true,
          message:
            "Internal server error. Cannot find user, please try again later",
        });
        console.log(error);
        return;
      }

      db.query("SELECT COUNT(id) as rewards FROM users_merch_redeem WHERE user_id = ?", [userId], (error, results3) => {
        if (error) {
          res.status(500).json({
            error: true,
            message:
              "Internal server error. Cannot find user, please try again later",
          });
          console.log(error);
          return;
        }

        res.status(200).json({
          status: "success",
          data: {
            rewards: results3[0].rewards,
            scan: results2[0].scan,
            points: user.total_points,
          }
        });
      });
    });
  });
};

const exchangePoints = (req, res) => {
  const { merchId } = req.body;
  const userId = req.user.id;

  if (!userId || !merchId) {
    res.status(400).json({
      error: true,
      message:
        "Invalid request. Please provide user id, merch id, and merch points",
    });
    return;
  }
  db.query("SELECT * FROM merch WHERE id = ?", [merchId], (error, results) => {
    if (error) {
      res.status(500).json({
        error: true,
        message:
          "Internal server error. Cannot find user, please try again later",
      });
      console.log(error);
      return;
    }

    const merch = results[0];

    db.query("SELECT * FROM users WHERE id = ?", [userId], (error, results) => {
      if (error) {
        res.status(500).json({
          error: true,
          message:
            "Internal server error. Cannot find user, please try again later",
        });
        console.log(error);
        return;
      }

      const user = results[0];

      if (merch.points > user.total_points) {
        res.status(400).json({
          error: true,
          message: "Points is not enough to exchange this merch",
        });
        return;
      }

      const total_points = user.total_points - merch.points;

      db.query(
        "UPDATE users SET total_points = ? WHERE id = ?",
        [total_points, userId],
        (error, results2) => {
          if (error) {
            res.status(500).json({
              error: true,
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
                  error: true,
                  message:
                    "Internal server error. Cannot update merch, please try again later",
                });
                console.log(error);
                return;
              }

              const id = nanoid(20);

              db.query(
                "INSERT INTO users_merch_redeem VALUES (?, ?, ?, ?, NOW())",
                [id, userId, merchId, "On Process"],
                (error, results4) => {
                  if (error) {
                    res.status(500).json({
                      error: true,
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
  });
};

const addNotification = (req, res) => {
  const { title, message } = req.body;
  const userId = req.user.id;
  if (!userId || !title || !message) {
    res.status(400).json({
      error: true,
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
          error: true,
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
  const userId = req.user.id;
  if (!userId) {
    res.status(400).json({
      error: true,
      message: "Invalid request. Please provide user id",
    });
    return;
  }

  db.query(
    "SELECT * FROM users_notifications WHERE user_id = ? ORDER BY date DESC",
    [userId],
    (error, results) => {
      if (error) {
        res.status(500).json({
          error: true,
          message:
            "Internal server error. Cannot find user notification, please try again later",
        });
        console.log(error);
        return;
      }

      if (results.length === 0) {
        res.status(404).json({
          error: true,
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
  const notifId = req.body.id;
  const userId = req.user.id;
  if (!notifId) {
    res.status(400).json({
      error: true,
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
          error: true,
          message:
            "Internal server error. Cannot delete user notification, please try again later",
        });
        console.log(error);
        return;
      }

      if (results.length === 0) {
        res.status(404).json({
          error: true,
          message: "Notification not found",
        });
        return;
      }
      db.query(
        "DELETE FROM users_notifications WHERE id = ? AND user_id = ?",
        [notifId, userId],
        (error, results) => {
          if (error) {
            res.status(500).json({
              error: true,
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
        error: true,
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
  const userId = req.user.id;
  if (!userId) {
    res.status(400).json({
      error: true,
      message: "Invalid request. Please provide user id",
    });
    return;
  }

  db.query(
    "SELECT b.name as name, b.image as image, a.date as date FROM users_merch_redeem a JOIN merch b ON a.merch_id = b.id JOIN users c ON a.user_id = c.id WHERE a.user_id = ? ORDER BY a.date DESC",
    [userId],
    (error, results) => {
      if (error) {
        res.status(500).json({
          error: true,
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
  editProfile,
  getUserDashboardData,
  exchangePoints,
  addNotification,
  getNotificationByUserId,
  deleteNotificationById,
  getAllMerch,
  getMerchRedeemedByUserId,
  changeImage,
};
