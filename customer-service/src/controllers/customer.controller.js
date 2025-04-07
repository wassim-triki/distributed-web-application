const asyncHandler = require('express-async-handler');
const Customer = require('../models/Customer');

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
