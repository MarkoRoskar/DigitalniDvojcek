var express = require('express');
var router = express.Router();
var bikePathController = require('../controllers/bikePathController.js');

/*
 * GET
 */
router.get('/', bikePathController.list);

/*
 * GET
 */
router.get('/:id', bikePathController.show);

/*
 * POST
 */
router.post('/', bikePathController.create);

/*
 * PUT
 */
router.put('/:id', bikePathController.update);

/*
 * DELETE
 */
router.delete('/:id', bikePathController.remove);

module.exports = router;
