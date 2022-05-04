var express = require('express');
var router = express.Router();
var MBajkAvailabilitiesController = require('../controllers/MBajkAvailabilitiesController.js');

/*
 * GET
 */
router.get('/', MBajkAvailabilitiesController.list);

/*
 * GET
 */
router.get('/:id', MBajkAvailabilitiesController.show);

/*
 * POST
 */
router.post('/', MBajkAvailabilitiesController.create);

/*
 * PUT
 */
router.put('/:id', MBajkAvailabilitiesController.update);

/*
 * DELETE
 */
router.delete('/:id', MBajkAvailabilitiesController.remove);

module.exports = router;
