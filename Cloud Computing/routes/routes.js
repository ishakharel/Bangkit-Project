const express = require("express");
const router = express.Router();
const authController = require("../controllers/auth");
const wasteController = require("../controllers/waste");
const userController = require("../controllers/user");

//Authentication
router.post("/auth/register", authController.register);
router.post("/auth/login", authController.login);
router.post("/auth/forgot-pass", authController.forgotPassword);
router.post("/auth/check-auth", authController.checkAuth);
router.put("/auth/reset-pass", authController.resetPassword);
router.put("/auth/change-pass", authController.changePassword);

//User
router.post("/user/notif", userController.addNotification);
router.get("/user/notif/:id", userController.getNotificationByUserId);
router.get("/user/:id", userController.getUserById);
router.get("/user/merch", userController.getAllMerch);
router.get("/user/points/:id", userController.checkPoints);
router.get("/user/merchredeem/:id", userController.getMerchRedeemedByUserId)
router.put("/user/points/:id", userController.exchangePoints);
router.put("/user/:id", userController.changeUsername);
router.delete("/user/notif/:id", userController.deleteNotificationById);

//Waste
router.get("/waste/categories", wasteController.categories);
router.get("/waste/histories", wasteController.histories);

module.exports = router;
