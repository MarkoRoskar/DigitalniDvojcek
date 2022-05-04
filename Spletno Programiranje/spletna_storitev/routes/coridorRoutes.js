var express = require('express');
var router = express.Router();
var coridorController = require('../controllers/coridorController.js');

/*
 * GET
 */
router.get('/', coridorController.list);

/*
 * GET
 */
router.get('/:id', coridorController.show);

/*
 * POST
 */
router.post('/', coridorController.create);

/*
 * PUT
 */
router.put('/:id', coridorController.update);

/*
 * DELETE
 */
router.delete('/:id', coridorController.remove);

module.exports = router;
