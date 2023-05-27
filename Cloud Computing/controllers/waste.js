const { nanoid } = require("nanoid");
const db = require("../config/db-config");
const { bucket, processFileConfig } = require("../config/storage-config");
const { format } = require("util");
const sharp = require("sharp");
const path = require("path");
const userId = "4w3zSDRVZoNCCoFN";

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

const upload = async (req, res) => {
  try {
    await processFileConfig(req, res);

    if (!req.file) {
      return res.status(400).send({ message: "Please upload a file!" });
    }

    // Create a new blob in the bucket and upload the file data.
    const resizedImageBuffer = await sharp(req.file.buffer)
      .resize(800, 600) // Specify the desired width and height
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
      const values = [id, userId, 1, publicUrl, 100, new Date()];

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
        res.status(201).json({
          status: "success",
          message: "Successfully upload!",
          url: publicUrl,
        });
      });
    });

    blobStream.end(resizedImageBuffer);
  } catch (err) {
    res.status(500).send({
      message: `Could not upload the file: ${req.file.originalname}. ${err}`,
    });
  }
};

const historyWithId = (req, res) => {
  const { id } = req.params;
  console.log(id);

  db.query("SELECT * FROM waste_histories WHERE id = ? ", [id], (err, res) => {
    if (err) {
      return res.status(500).json({ message: id });
    }

    return res.status(200).json({ data: id });
  });
};

module.exports = { categories, histories, upload, historyWithId };
