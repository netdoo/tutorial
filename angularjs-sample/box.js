"use strict";

var Box = function(height, width) {
    this.height = height;
    this.width = width;
}

Box.prototype = {
    height: Number(),
    width: Number(),
    setHeight: function(height) {
        this.height = height;
    },
    getHeight: function() {
        return this.height;
    },
    setWidth: function(width) {
        this.width = width;
    },
    getWidth: function() {
        return this.width;
    },
    toJson: function() {
        return {
            name: 'box',
            height: this.height,
            width: this.width
        };
    }
};

