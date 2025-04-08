const express = require('express');
const dotenv = require('dotenv');
const cors = require('cors');
const connectDB = require('./config/database');
const notificationRoutes = require('./routes/notification.routes');
const registerWithEureka = require('./config/eureka');

dotenv.config();
connectDB();

const app = express();
app.use(cors());
app.use(express.json());

app.use('/notifications', notificationRoutes);

app.get('/info', (req, res) => {
  res.json({ status: 'UP', service: 'notification-service' });
});

const PORT = process.env.PORT || 4000;
app.listen(PORT, () => {
  console.log(`📡 Notification Service running on port ${PORT}`);
  registerWithEureka(PORT);
});
