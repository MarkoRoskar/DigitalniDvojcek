var TourpathModel = require('../models/tourPathModel.js');

/**
 * tourPathController.js
 *
 * @description :: Server-side logic for managing tourPaths.
 */
module.exports = {

    /**
     * tourPathController.list()
     */
    list: function (req, res) {
        TourpathModel.find(function (err, tourPaths) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting tourPath.',
                    error: err
                });
            }

            return res.json(tourPaths);
        });
    },

    /**
     * tourPathController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        TourpathModel.findOne({_id: id}, function (err, tourPath) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting tourPath.',
                    error: err
                });
            }

            if (!tourPath) {
                return res.status(404).json({
                    message: 'No such tourPath'
                });
            }

            return res.json(tourPath);
        });
    },

    /**
     * tourPathController.create()
     */
    create: function (req, res) {
        var tourPath = new TourpathModel({

        });

        tourPath.save(function (err, tourPath) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating tourPath',
                    error: err
                });
            }

            return res.status(201).json(tourPath);
        });
    },

    /**
     * tourPathController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        TourpathModel.findOne({_id: id}, function (err, tourPath) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting tourPath',
                    error: err
                });
            }

            if (!tourPath) {
                return res.status(404).json({
                    message: 'No such tourPath'
                });
            }

            
            tourPath.save(function (err, tourPath) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating tourPath.',
                        error: err
                    });
                }

                return res.json(tourPath);
            });
        });
    },

    /**
     * tourPathController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        TourpathModel.findByIdAndRemove(id, function (err, tourPath) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the tourPath.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
