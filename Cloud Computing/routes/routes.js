const express = require("express");
const router = express.Router();
const authController = require("../controllers/auth");
const wasteController = require("../controllers/waste");

router.post("/auth/register", authController.register);
router.post("/auth/login", authController.login);

router.get("/waste/categories", wasteController.categories);
router.get("/waste/histories", wasteController.histories);
router.post("/waste/upload", wasteController.upload);
router.get("/waste/histories/:id", wasteController.historyWithId);

module.exports = router;
