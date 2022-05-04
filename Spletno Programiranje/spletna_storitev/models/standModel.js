var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var standSchema = new Schema({
	'name' : String,
	'parkSpots' : Number,
	'geometry': {
		'type': {
			type: String,
			enum: ['Point'],
			default: 'Point',
			required: true
		},
		'coordinates':{
			type:[Number],
			index: "2dsphere",
			required: true
		}
	}
}, {
	timestamps : true
});

module.exports = mongoose.model('stand', standSchema);
