const mongoose = require('mongoose');

const notificationSchema = new mongoose.Schema({
  to: { type: String, required: true },
  subject: { type: String },
  message: { type: String, required: true },
  type: { type: String, enum: ['email', 'sms'], default: 'email' },
  status: {
    type: String,
    enum: ['pending', 'sent', 'failed'],
    default: 'pending',
  },
  createdAt: { type: Date, default: Date.now },
});

module.exports = mongoose.model('Notification', notificationSchema);
