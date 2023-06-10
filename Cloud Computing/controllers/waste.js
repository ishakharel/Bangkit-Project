const { nanoid } = require("nanoid");
const db = require("../config/db-config");
const { bucket, processFileConfig } = require("../config/storage-config");
const { format, promisify } = require("util");
const sharp = require("sharp");
const axios = require("axios");
const fs = require("fs");
const FormData = require("form-data");
const { error } = require("console");
require("dotenv").config();

const categories = (req, res) => {
  db.query("SELECT * FROM waste_category", (error, result) => {
    if (error) {
      console.log(error);
      return res.status(500).send("Server Error!");
    }

    return res.status(200).json({
      status : "success",
      data : result
    });
  });
};

const categoryById = (req, res) => {
  const { id } = req.params;

  db.query(
    "SELECT * FROM waste_category WHERE id = ? ",
    [id],
    (error, result) => {
      if (error) {
        console.log(error);
        return res.status(500).send("Server Error!");
      }

      return res.status(200).json({
        status : "success",
        data : result[0]
      });
    }
  );
};

const histories = (req, res) => {
  const userId = req.user.id;

  db.query(
    "SELECT a.id, b.name as name, b.category as category, a.date as date, a.point as points, a.image FROM waste_history a JOIN waste_category b ON a.category_id = b.id WHERE a.user_id = ? ORDER BY a.date DESC",
    [userId],
    (error, result) => {
      if (error) {
        console.log(error);
        return res.status(500).send("Server Error!");
      }

      return res.status(200).json({
        status : "success",
        id: userId,
        data: result 
      });
    }
  );
};

const historyDetail = (req, res) => {
  const userId = req.user.id;
  const { id } = req.params;

  db.query(
    "SELECT b.name as name, b.category as category, b.description_recycle as description_recycle, a.date as date, a.point as points, a.image as image FROM waste_history a JOIN waste_category b ON a.category_id = b.id WHERE a.user_id = ? AND a.id = ?",
    [userId, id],
    (error, result) => {
      if (error) {
        console.log(error);
        return res.status(500).send("Server Error!");
      }

      return res.status(200).json({
        status: 'success',
        data: result[0]
      });
    }
  );
};

const upload = async (req, res) => {
  try {
    const userId = req.user.id;
    const id = nanoid(16);

    if (!userId) {
      res.status(400).json({
        error: true,
        message: "Invalid request. Please provide user id",
      });
      return;
    }

    await processFileConfig(req, res);

    if (!req.file) {
      return res.status(400).send({ message: "Please upload a file!" });
    }

    await promisify(fs.writeFile)(req.file.originalname, req.file.buffer);

    const formData = {
      image: fs.createReadStream(req.file.originalname),
    };

    const server = "https://cloud-run-app-ki2c4ur6wa-et.a.run.app/upload";

    const options = {
      url: server,
      formData,
      headers: {
        "Content-Type": "multipart/form-data",
        apikey: process.env.API_KEY,
      },
    };

    const response = await axios.post(options.url, formData, {
      headers: options.headers,
    });

    const categoryId = response.data.predicted_class;

    // Create a new blob in the bucket and upload the file data.
    const resizedImageBuffer = await sharp(req.file.buffer)
      .resize(200, 200) // Specify the desired width and height
      .toBuffer();

    const blob = bucket.file("waste_history/" + id);
    const blobStream = blob.createWriteStream({
      resumable: false,
      metadata: {
        contentType: req.file.mimetype,
      },
    });

    blobStream.on(true, (err) => {
      res.status(500).send({ message: err.message });
    });

    blobStream.on("finish", async (data) => {
      // Create URL for directly file access via HTTP.
      const publicUrl = format(
        `https://storage.googleapis.com/${bucket.name}/${blob.name}`
      );

      try {
        // Make the file public
        await bucket.file(req.file.originalname).makePublic();
      } catch {
        db.query(
          "SELECT * FROM waste_category WHERE id = ? ",
          [categoryId],
          (error, results) => {
            const points = results[0].points;
            const insertWaste =
              "INSERT INTO waste_history VALUES (?, ?, ?, ?, ?, ?)";
            const valuesWaste = [
              id,
              userId,
              categoryId,
              publicUrl,
              points,
              new Date(),
            ];
            const updatePoints =
              "UPDATE users SET total_points = total_points + ? WHERE id = ?";
            db.query(insertWaste, valuesWaste, (err, result1) => {
              db.query(updatePoints, [points, userId], (err, result) => {
                if (err) {
                  res.status(400).json({
                    error: true,
                    message: "Error connection!",
                  });
                }

                db.query(
                  "SELECT a.id, b.name as name, b.category as category, b.description_recycle as description_recycle, a.date as date, a.point as points, a.image FROM waste_history a JOIN waste_category b ON a.category_id = b.id WHERE a.user_id = ? AND a.id = ? ",
                  [userId, id],
                  (error, results) => {
                    if (error) {
                      console.log(error);
                      return res.status(500).send("Server Error!");
                    }
                    res.status(200).json({
                      status: "success",
                      message: "Successfully upload!",
                      data: results[0],
                    });
                  }
                );
              });
            });
          }
        );
      }
    });
    fs.unlinkSync(req.file.originalname);
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
