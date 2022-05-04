var express = require('express');
var router = express.Router();
var tourPathController = require('../controllers/tourPathController.js');

/*
 * GET
 */
router.get('/', tourPathController.list);

/*
 * GET
 */
router.get('/:id', tourPathController.show);

/*
 * POST
 */
router.post('/', tourPathController.create);

/*
 * PUT
 */
router.put('/:id', tourPathController.update);

/*
 * DELETE
 */
router.delete('/:id', tourPathController.remove);

module.exports = router;
