const client = require('../config/twilio');

exports.sendSMS = async (notification) => {
  await client.messages.create({
    body: notification.message,
    to: notification.to,
    from: process.env.TWILIO_PHONE_NUMBER,
  });
};
