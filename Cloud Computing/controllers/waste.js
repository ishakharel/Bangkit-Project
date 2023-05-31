const { nanoid } = require("nanoid");
const db = require("../config/db-config");
const { bucket, processFileConfig } = require("../config/storage-config");
const { format } = require("util");
const sharp = require("sharp");
const { tf } = require("@tensorflow/tfjs");

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

const categoryById = (req, res) => {
  const categoryId = req.params.category_id;
  console.log(categoryId);

  db.query(
    "SELECT * FROM waste_category WHERE id = ? ",
    [categoryId],
    (error, result) => {
      if (error) {
        console.log(error);
        return res.status(500).send("Server Error!");
      }

      return res.status(200).json(result);
    }
  );
};

const histories = (req, res) => {
  const userId = req.user.id;

  db.query(
    "SELECT a.*, b.name as waste_name, b.category FROM waste_history a JOIN waste_category b ON a.category_id = b.id WHERE user_id = ?",
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

const historyDetail = (req, res) => {
  const userId = req.user.id;
  const id = req.params.history_id;

  db.query(
    "SELECT * FROM waste_history WHERE user_id = ? AND id = ? ",
    [userId, id],
    (error, result) => {
      if (error) {
        console.log(error);
        return res.status(500).send("Server Error!");
      }

      return res.status(200).json({ id: userId, data: result });
    }
  );
};

const upload = async (req, res) => {
  try {
    const userId = req.user.id;
    const categoryId = req.body.categoryId;

    if (!userId) {
      res.status(400).json({
        status: "error",
        message: "Invalid request. Please provide user id",
      });
      return;
    }

    await processFileConfig(req, res);

    if (!req.file) {
      return res.status(400).send({ message: "Please upload a file!" });
    }

    // Create a new blob in the bucket and upload the file data.
    const resizedImageBuffer = await sharp(req.file.buffer)
      .resize(200, 200) // Specify the desired width and height
      .toBuffer();

    const blob = bucket.file(req.file.originalname);
    const blobStream = blob.createWriteStream({
      resumable: false,
    });

    blobStream.on("error", (err) => {
      res.status(500).send({ message: err.message });
    });

    blobStream.on("finish", async (data) => {
      // Create URL for directly file access via HTTP.
      const publicUrl = format(
        `https://storage.googleapis.com/${bucket.name}/${blob.name}`
      );

      const id = nanoid(16);
      const sql = "INSERT INTO waste_history VALUES (?, ?, ?, ?, ?, ?)";
      const values = [id, userId, 8, publicUrl, 100, new Date()];

      try {
        // Make the file public
        await bucket.file(req.file.originalname).makePublic();
      } catch {
        return res.status(500).send({
          message: `Uploaded the file successfully: ${req.file.originalname}, but public access is denied!`,
          url: publicUrl,
        });
      }

      db.query(sql, values, (err, result) => {
        db.query(
          "UPDATE users SET total_points = (SELECT SUM(point) FROM waste_history WHERE waste_history.user_id = ?)",
          [userId],
          (err, result) => {
            res.status(201).json({
              status: "success",
              message: "Successfully upload!",
              url: publicUrl,
            });
          }
        );
      });
    });

    blobStream.end(resizedImageBuffer);
  } catch (err) {
    res.status(500).send({
      message: `Could not upload the file: ${req.file.originalname}. ${err}`,
    });
  }
};

module.exports = {
  categories,
  histories,
  upload,
  categoryById,
  historyDetail,
};
