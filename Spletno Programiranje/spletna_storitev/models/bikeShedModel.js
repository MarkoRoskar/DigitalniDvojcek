var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var bikeShedSchema = new Schema({
	'providerName' : String,
	'providerLink' : String,
	'address' : String,
	'quantity' : Number,
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

module.exports = mongoose.model('bikeShed', bikeShedSchema);
