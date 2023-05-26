const express = require("express");
const router = express.Router();
const authController = require("../controllers/auth");
const wasteController = require("../controllers/waste");
const user = require("../controllers/user");

router.post("/auth/register", authController.register);
router.post("/auth/login", authController.login);
router.post("/auth/forgot-pass", authController.forgotPassword);
router.get("/auth/verify-otp", authController.verifyOTP);
router.put("/auth/reset-pass", authController.resetPassword);
router.put("/auth/change-pass", authController.changePassword);

router.get("/user/points", user.checkPoints);
router.put("/user/points", user.exchangePoints);

router.get("/waste/categories", wasteController.categories);
router.get("/waste/histories", wasteController.histories);

module.exports = router;
