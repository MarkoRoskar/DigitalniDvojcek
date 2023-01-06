var express = require('express');
var router = express.Router();
var eventController = require('../controllers/eventController.js');

/*
 * GET
 */
router.get('/', eventController.list);
router.get('/:id', eventController.show);

/*
 * POST
 */
router.post('/', eventController.insert);

/*
 * PUT
 */
router.put('/:id', eventController.update);

/*
 * DELETE
 */
router.delete('/:id', eventController.remove);

module.exports = router;
