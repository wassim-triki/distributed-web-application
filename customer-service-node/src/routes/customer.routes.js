const {
  createCustomer,
  getCustomers,
  getCustomerById,
  updateCustomer,
  deleteCustomer,
  sendVerificationEmail,
  verifyCustomer,
} = require('../controllers/customer.controller');
const express = require('express');
const router = express.Router();
router.get('/test', (req, res) => {
  res.send('Customer Service is working');
});
router.route('/').post(createCustomer).get(getCustomers);

router
  .route('/:id')
  .get(getCustomerById)
  .put(updateCustomer)
  .delete(deleteCustomer);

router.post('/send-verification/:id', sendVerificationEmail);
router.get('/verify/:token', verifyCustomer);

module.exports = router;
