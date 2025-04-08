const express = require('express');
const router = express.Router();
const controller = require('../controllers/notification.controller');

router.post('/', controller.createNotification);
router.get('/', controller.getNotifications);
router.get('/:id', controller.getNotificationById);
router.put('/:id', controller.updateNotification);
router.delete('/:id', controller.deleteNotification);

module.exports = router;
