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
  projectId: "ecoloopse-bangkit-project",
});
const bucket = storage.bucket("ecoloops_bucket");

module.exports = { multerConfig, storage, bucket, processFileConfig };
