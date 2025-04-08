const Notification = require('../models/Notification');
const { sendEmail } = require('../utils/emailSender');
const { sendSMS } = require('../utils/smsSender');

exports.createNotification = async (req, res, next) => {
  try {
    const notification = new Notification(req.body);
    await notification.save();

    try {
      if (notification.type === 'email') {
        await sendEmail(notification);
      } else if (notification.type === 'sms') {
        await sendSMS(notification);
      }

      notification.status = 'sent';
    } catch (err) {
      notification.status = 'failed';
    }

    await notification.save();
    res.status(201).json(notification);
  } catch (err) {
    next(err);
  }
};

exports.getNotifications = async (req, res) => {
  const notifications = await Notification.find().sort({ createdAt: -1 });
  res.json(notifications);
};

exports.getNotificationById = async (req, res) => {
  const notif = await Notification.findById(req.params.id);
  if (!notif)
    return res.status(404).json({ message: 'Notification not found' });
  res.json(notif);
};

exports.updateNotification = async (req, res) => {
  const notif = await Notification.findByIdAndUpdate(req.params.id, req.body, {
    new: true,
  });
  if (!notif)
    return res.status(404).json({ message: 'Notification not found' });
  res.json(notif);
};

exports.deleteNotification = async (req, res) => {
  const notif = await Notification.findByIdAndDelete(req.params.id);
  if (!notif)
    return res.status(404).json({ message: 'Notification not found' });
  res.json({ message: 'Notification deleted' });
};
