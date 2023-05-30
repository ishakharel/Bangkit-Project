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
router.get("/user/user/:id", userController.getUserById);
router.get("/user/merch", userController.getAllMerch);
router.get("/user/points/:id", userController.checkPoints);
router.get("/user/merch-redeemed/:id", userController.getMerchRedeemedByUserId);
router.put("/user/points/:id", userController.exchangePoints);
router.put("/user/username/:id", userController.changeUsername);
router.delete("/user/notif/:id", userController.deleteNotificationById);

//Waste
router.get("/waste/categories", wasteController.categories);
router.get("/waste/histories/:user_id", wasteController.histories);
router.get(
  "/waste/histories/:user_id/:history_id",
  wasteController.historyDetail
);
router.get("/waste/categories/:category_id", wasteController.categoryById);
router.post("/waste/upload", wasteController.upload);

module.exports = router;
