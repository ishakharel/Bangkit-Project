const db = require("../config/db-config");

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

const upload = (req, res) => {
  // const userId = req.session.userId;
  const id = nanoid(16);

  // if (!userId) {
  //   res.send("You are not logged in");
  //   return;
  // }

  const { categoryId, userId } = req.body;
  const imageFile = req.file;

  // Upload the image file to Google Cloud Storage
  const file = storage.bucket(bucketName).file(imageFile.originalname);
  const stream = file.createWriteStream({
    metadata: {
      contentType: imageFile.mimetype,
    },
  });

  stream.on("error", (err) => {
    console.error("Error uploading image to Google Cloud Storage:", err);
    res.status(500).send("Server Error");
  });

  stream.on("finish", async () => {
    // Insert the post data into the MySQL database
    const imageUrl = `https://storage.googleapis.com/${bucketName}/${file.name}`;
    const sql =
      "INSERT INTO posts (id, user_id, category_id, image, point, date) VALUES (?, ?, ?, ?, ?)";
    const values = [id, userId, categoryId, imageUrl, 100, new Date()];

    try {
      await db.query(sql, values);
      res.status(200).send("Post created successfully");
    } catch (error) {
      console.error("Error inserting post:", error);
      res.status(500).send("Server Error");
    }
  });

  stream.end(imageFile.buffer);
};

module.exports = { categories, histories, upload };
