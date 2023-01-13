var express = require('express');
var router = express.Router();
var SensorTempController = require('../controllers/SensorTempController.js');

// authenticateToken function
const jwtAuth = require('../authenticateJWT.js');


/**
 * GET
 */
router.get('/', SensorTempController.list);
router.get('/:id', SensorTempController.show);

/**
 * POST
 */
router.post('/', SensorTempController.insert);


module.exports = router;