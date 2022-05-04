var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var tourPathSchema = new Schema({
    'geometry': {
		'type': {
			type: String,
			enum: ['LineString'],
			default: 'LineString',
			required: true
		},
		'coordinates':{
			type:[[Number]],
			required: true
		}
	}
}, {
    timestamps : true
});

module.exports = mongoose.model('tourPath', tourPathSchema);
