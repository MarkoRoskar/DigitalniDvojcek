var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

/**
 * definition of attributes for our custom events
 */
var eventSchema = new Schema({
	'id' : {
		type : String,
		required : true
	},
	'category' : {
		type : String,
		required : true
	},
	'description' : {
		type : String,
		required : true
	},
	'geometry' : {
		'type' : {
			type : String,
			enum : ['Point'],
			default : 'Point',
			required : true
		},
		'coordinates' : {
			type : [Number],
			index : "2dsphere",
			required : true
		}
	}
}, {
	timestamps : true
});

module.exports = mongoose.model('event', eventSchema);

/*
{
	"category" : "temperature",
	"description" : "negative temperatures at night",
	"longitude" : "15.646833",
	"latitude" : "46.5577328"
}

{
	"category" : "accident",
	"description" : "car crash on Gosposvetska Street",
	"longitude" : "15.346833",
	"latitude" : "46.4577328"
}
 */