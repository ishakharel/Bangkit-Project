const { Storage } = require("@google-cloud/storage");
const util = require("util");
const multer = require("multer");

// Configure Multer for file uploads
const multerConfig = multer({
  storage: multer.memoryStorage(),
  limits: {
    fileSize: 10 * 1024 * 1024, // Limit file size to 10MB
  },
}).single("file");
let processFileConfig = util.promisify(multerConfig);

// Configure Google Cloud Storage
const storage = new Storage({
  keyFilename: "key.json",
  projectId: "my-project-exam-382102",
});
const bucket = storage.bucket("scan-waste-users");

const resize = async (image) =>
  await sharp(image)
    .resize(200, 200)
    .jpeg({ quality: 90 })
    .toFile(path.resolve(req.file.destination, "resized", image));

module.exports = { multerConfig, storage, bucket, processFileConfig };
