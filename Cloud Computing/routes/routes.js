const express = require("express");
const checkAuth = require("../middleware/checkAuth");
const checkApiKey = require("../middleware/checkApiKey");
const router = express.Router();
const authController = require("../controllers/auth");
const wasteController = require("../controllers/waste.js");
const userController = require("../controllers/user");
const { multerConfig } = require("../config/storage-config");

//Authentication
router.post("/auth/register", authController.register);
router.post("/auth/login", authController.login);
router.post("/auth/logout", checkAuth, authController.logout);
router.post("/auth/forgot-pass", authController.forgotPassword);
router.put("/auth/reset-pass", authController.resetPassword);
router.put("/auth/change-pass", checkAuth, authController.changePassword);

//User
router.post("/user/notif", checkAuth, userController.addNotification);
router.get("/user/notif/", checkAuth, userController.getNotificationByUserId);
router.get("/user", checkAuth, userController.getUserById);
router.get("/user/merch/", userController.getAllMerch);
router.get("/user/points/", checkAuth, userController.checkPoints);
router.get(
  "/user/merch-redeemed/",
  checkAuth,
  userController.getMerchRedeemedByUserId
);
router.put("/user/points/", checkAuth, userController.exchangePoints);
router.put("/user/username/", checkAuth, userController.editProfile);
router.put(
  "/user/profile/",
  checkAuth,
  multerConfig,
  userController.changeImage
);
router.delete(
  "/user/notif/:id",
  checkAuth,
  userController.deleteNotificationById
);

//Waste
router.get("/waste/categories/", wasteController.categories);
router.get("/waste/histories/", checkAuth, wasteController.histories);
router.get("/waste/histories/:id", checkAuth, wasteController.historyDetail);
router.get("/waste/categories/:category_id", wasteController.categoryById);
router.post("/waste/upload/", checkAuth, wasteController.upload);

module.exports = router;
