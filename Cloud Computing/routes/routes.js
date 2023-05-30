const express = require("express");
const checkAuthentication = require("./../middleware/checkAuth");
const router = express.Router();
const authController = require("../controllers/auth");
const wasteController = require("../controllers/waste");
const userController = require("../controllers/user");

//Authentication
router.post("/auth/register", authController.register);
router.post("/auth/login", authController.login);
router.post("/auth/forgot-pass", authController.forgotPassword);
router.put("/auth/reset-pass", authController.resetPassword);
router.put("/auth/change-pass", authController.changePassword);

//User
router.post("/user/notif", checkAuthentication, userController.addNotification);
router.get(
  "/user/notif/",
  checkAuthentication,
  userController.getNotificationByUserId
);
router.get("/user", checkAuthentication, userController.getUserById);
router.get("/user/merch/", userController.getAllMerch);
router.get("/user/points/", checkAuthentication, userController.checkPoints);
router.get(
  "/user/merch-redeemed/",
  checkAuthentication,
  userController.getMerchRedeemedByUserId
);
router.put("/user/points/", checkAuthentication, userController.exchangePoints);
router.put(
  "/user/username/",
  checkAuthentication,
  userController.changeUsername
);
router.delete(
  "/user/notif/",
  checkAuthentication,
  userController.deleteNotificationById
);

//Waste
router.get("/waste/categories", wasteController.categories);
router.get("/waste/histories/", checkAuthentication, wasteController.histories);
router.get(
  "/waste/histories/:history_id",
  checkAuthentication,
  wasteController.historyDetail
);
router.get(
  "/waste/categories/:category_id",
  checkAuthentication,
  wasteController.categoryById
);
router.post("/waste/upload", checkAuthentication, wasteController.upload);

module.exports = router;
