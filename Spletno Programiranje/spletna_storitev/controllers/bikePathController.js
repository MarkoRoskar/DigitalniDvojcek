var BikepathModel = require('../models/bikePathModel.js');

/**
 * bikePathController.js
 *
 * @description :: Server-side logic for managing bikePaths.
 */
module.exports = {

    /**
     * bikePathController.list()
     */
    list: function (req, res) {
        BikepathModel.find(function (err, bikePaths) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting bikePath.',
                    error: err
                });
            }

            return res.json(bikePaths);
        });
    },

    /**
     * bikePathController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        BikepathModel.findOne({_id: id}, function (err, bikePath) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting bikePath.',
                    error: err
                });
            }

            if (!bikePath) {
                return res.status(404).json({
                    message: 'No such bikePath'
                });
            }

            return res.json(bikePath);
        });
    },

    /**
     * bikePathController.create()
     */
    create: function (req, res) {
        var bikePath = new BikepathModel({

        });

        bikePath.save(function (err, bikePath) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating bikePath',
                    error: err
                });
            }

            return res.status(201).json(bikePath);
        });
    },

    /**
     * bikePathController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        BikepathModel.findOne({_id: id}, function (err, bikePath) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting bikePath',
                    error: err
                });
            }

            if (!bikePath) {
                return res.status(404).json({
                    message: 'No such bikePath'
                });
            }

            
            bikePath.save(function (err, bikePath) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating bikePath.',
                        error: err
                    });
                }

                return res.json(bikePath);
            });
        });
    },

    /**
     * bikePathController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        BikepathModel.findByIdAndRemove(id, function (err, bikePath) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the bikePath.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
