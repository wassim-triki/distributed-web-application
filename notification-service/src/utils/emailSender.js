const nodemailer = require('nodemailer');

const transporter = nodemailer.createTransport({
  service: 'gmail', // ou autre
  auth: {
    user: process.env.EMAIL_USER,
    pass: process.env.EMAIL_PASS,
  },
});

exports.sendEmail = async (notification) => {
  const mailOptions = {
    from: process.env.EMAIL_USER,
    to: notification.to,
    subject: notification.subject || 'Notification',
    html: notification.message,
  };

  await transporter.sendMail(mailOptions);
};
