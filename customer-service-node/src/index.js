const express = require('express');
const dotenv = require('dotenv');
const cors = require('cors');
const connectDB = require('./config/database.js');
const customerRoutes = require('./routes/customer.routes.js');
const eureka = require('./config/eureka.js');
const registerWithEureka = require('./config/eureka.js');

dotenv.config();

// Connect to MongoDB
connectDB();

const app = express();

// Middleware
app.use(cors()); // Enable CORS
app.use(express.json()); // Body parser

// Routes

app.use('/customers', customerRoutes);

app.use((err, req, res, next) => {
  const status = res.statusCode === 200 ? 500 : res.statusCode;
  res.status(status).json({ message: err.message });
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
  console.log(`Customer Service API is running on port ${PORT}`);
  registerWithEureka(PORT); // Register only after server is up
});
