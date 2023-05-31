const express = require("express");
const checkAuth = require("../middleware/checkAuth");
const checkApiKey = require("../middleware/checkApiKey");
const router = express.Router();
const authController = require("../controllers/auth");
const wasteController = require("../controllers/waste");
const userController = require("../controllers/user");

//Authentication
router.post("/auth/register", checkApiKey, authController.register);
router.post("/auth/login", checkApiKey, authController.login);
router.post("/auth/forgot-pass", checkApiKey, authController.forgotPassword);
router.put("/auth/reset-pass", checkApiKey, authController.resetPassword);
router.put("/auth/change-pass", checkApiKey, checkAuth, authController.changePassword);

//User
router.post("/user/notif", checkApiKey, checkAuth, userController.addNotification);
router.get("/user/notif/", checkApiKey, checkAuth,userController.getNotificationByUserId);
router.get("/user", checkApiKey, checkAuth, userController.getUserById);
router.get("/user/merch/", checkApiKey, userController.getAllMerch);
router.get("/user/points/", checkApiKey, checkAuth, userController.checkPoints);
router.get("/user/merch-redeemed/", checkApiKey, checkAuth, userController.getMerchRedeemedByUserId);
router.put("/user/points/", checkApiKey, checkAuth, userController.exchangePoints);
router.put("/user/username/", checkApiKey, checkAuth,userController.changeUsername);
router.delete("/user/notif/", checkApiKey, checkAuth, userController.deleteNotificationById);

//Waste
router.get("/waste/categories", checkApiKey, wasteController.categories);
router.get("/waste/histories/", checkApiKey, checkAuth, wasteController.histories);
router.get("/waste/histories/:history_id", checkApiKey, checkAuth,wasteController.historyDetail);
router.get("/waste/categories/:category_id", checkApiKey, checkAuth,wasteController.categoryById);
router.post("/waste/upload", checkApiKey, checkAuth, wasteController.upload);

module.exports = router;
