var EventModel = require('../models/eventModel.js');

/**
 * eventController.js
 *
 * @description :: Server-side logic for managing events.
 */
module.exports = {

    /**
     * eventController.list()
     */
    list: function (req, res) {
        EventModel.find(function (err, events) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting event.',
                    error: err
                });
            }

            return res.json(events);
        });
    },

    /**
     * eventController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        EventModel.findOne({_id: id}, function (err, event) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting event.',
                    error: err
                });
            }

            if (!event) {
                return res.status(404).json({
                    message: 'No such event'
                });
            }

            return res.json(event);
        });
    },

    /**
     * eventController.create()
     */
    insert: function (req, res) {
        EventModel.findOne({description: req.body.description}, function (err, data) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    error: err
                });
            }
            if (data) {
                return res.status(500).json({
                    inserted: false,
                    message: "data already inserted",
                    data: data
                });
            }
            else {
                // event location
                var objGeometry = {
                    type: 'Point',
                    coordinates: [parseFloat(req.body.longitude), parseFloat(req.body.latitude)]
                }
                // create new event
                var event = new EventModel({
                    id : require("crypto").randomBytes(64).toString('hex'),
                    category : req.body.category,
                    description : req.body.description,
                    geometry : objGeometry,
                });
                // save event
                event.save(function (err, data) {
                    if (err) {
                        return res.status(500).json({
                            success: false,
                            message: "Error saving event",
                            error: err
                        });
                    }
                    return res.json({
                        success: true,
                        inserted: true,
                        event: data
                    })
                });
            }
        })

        /*var event = new EventModel({
			category : req.body.category,
			description : req.body.description
        });

        event.save(function (err, event) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating event',
                    error: err
                });
            }

            return res.status(201).json(event);
        });*/
    },

    /**
     * eventController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        EventModel.findOne({_id: id}, function (err, event) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting event',
                    error: err
                });
            }

            if (!event) {
                return res.status(404).json({
                    message: 'No such event'
                });
            }

            event.category = req.body.category ? req.body.category : event.category;
			event.description = req.body.description ? req.body.description : event.description;
			
            event.save(function (err, event) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating event.',
                        error: err
                    });
                }

                return res.json(event);
            });
        });
    },

    /**
     * eventController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        EventModel.findByIdAndRemove(id, function (err, event) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the event.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
