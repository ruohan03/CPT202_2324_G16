$(function() {
    'use strict';
    //Add active class to nav-link based on url dynamically
    //Active class can be hard coded directly in html file also as required
    var current = location.pathname.split("/").slice(-1)[0].replace(/^\/|\/$/g, '');
    $('.nav li a', '#sidebar').each(function () {
        var $this = $(this);
        if (current === "") {
            //for root url
            if ($this.attr('href').indexOf("index.html") !== -1) {
                $(this).parents('.nav-item').last().addClass('active');
                if ($(this).parents('.sub-menu').length) {
                    $(this).closest('.collapse').addClass('show');
                    $(this).addClass('active');
                }
            }
        } else {
            //for other url
            if ($this.attr('href').indexOf(current) !== -1) {
                $(this).parents('.nav-item').last().addClass('active');
                if ($(this).parents('.sub-menu').length) {
                    $(this).closest('.collapse').addClass('show');
                    $(this).addClass('active');
                }
            }
        }
    })
});