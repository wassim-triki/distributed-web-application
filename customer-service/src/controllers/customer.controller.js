const asyncHandler = require('express-async-handler');
const Customer = require('../models/Customer');
const jwt = require('jsonwebtoken');
const sendEmail = require('../utils/sendEmail');
const baseUrl = process.env.BASE_URL || 'http://localhost:5000';
// @desc    Create a new customer
// @route   POST /api/customers
exports.createCustomer = asyncHandler(async (req, res) => {
  const { firstName, lastName, email, address } = req.body;

  const existing = await Customer.findOne({ email });
  if (existing) {
    res.status(400);
    throw new Error('Customer with this email already exists');
  }

  const customer = await Customer.create({
    firstName,
    lastName,
    email,
    address,
  });
  res.status(201).json(customer);
});

// @desc    Get all customers
// @route   GET /api/customers
exports.getCustomers = asyncHandler(async (req, res) => {
  const customers = await Customer.find();
  res.json(customers);
});

// @desc    Get a customer by ID
// @route   GET /api/customers/:id
exports.getCustomerById = asyncHandler(async (req, res) => {
  const customer = await Customer.findById(req.params.id);
  if (!customer) {
    res.status(404);
    throw new Error('Customer not found');
  }
  res.json(customer);
});

// @desc    Update a customer
// @route   PUT /api/customers/:id
exports.updateCustomer = asyncHandler(async (req, res) => {
  const customer = await Customer.findById(req.params.id);
  if (!customer) {
    res.status(404);
    throw new Error('Customer not found');
  }

  const { firstname, lastname, email, address } = req.body;
  customer.firstname = firstname || customer.firstname;
  customer.lastname = lastname || customer.lastname;
  customer.email = email || customer.email;
  customer.address = address || customer.address;

  const updated = await customer.save();
  res.json(updated);
});

// @desc    Delete a customer
// @route   DELETE /api/customers/:id
exports.deleteCustomer = asyncHandler(async (req, res) => {
  const customer = await Customer.findById(req.params.id);
  if (!customer) {
    res.status(404);
    throw new Error('Customer not found');
  }

  await customer.deleteOne();
  res.json({ message: 'Customer deleted successfully' });
});

exports.sendVerificationEmail = asyncHandler(async (req, res) => {
  const customer = await Customer.findById(req.params.id);
  if (!customer) {
    res.status(404);
    throw new Error('Customer not found');
  }

  if (customer.isVerified) {
    res.status(400);
    throw new Error('Customer is already verified');
  }

  // Create JWT token
  const token = jwt.sign({ id: customer._id }, process.env.JWT_SECRET, {
    expiresIn: '1h',
  });

  const verifyLink = `${baseUrl}/customers/verify/${token}`;
  const html = `<p>Hello ${customer.firstName},</p>
    <p>Please click the link below to verify your email:</p>
    <a href="${verifyLink}">${verifyLink}</a>
    <p>This link expires in 1 hour.</p>`;

  await sendEmail(customer.email, 'Verify your email', html);

  res.json({ message: 'Verification email sent' });
});

exports.verifyCustomer = asyncHandler(async (req, res) => {
  const token = req.params.token;

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    const customer = await Customer.findById(decoded.id);

    if (!customer) {
      res.status(404);
      throw new Error('Customer not found');
    }

    if (customer.isVerified) {
      return res.status(400).json({ message: 'Customer is already verified' });
    }

    customer.isVerified = true;
    await customer.save();

    res.json({ message: 'Email verified successfully' });
  } catch (err) {
    res.status(400).json({ message: 'Invalid or expired token' });
  }
});
