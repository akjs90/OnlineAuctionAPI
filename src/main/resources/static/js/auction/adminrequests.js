/*<![CDATA[*/
var _element;
$('document').ready(function() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajaxSetup({
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
            console.log('set');
        }
    });
});
var requestLoad=function(){
	$.ajax({
		url:'requested'
	});
}
$('a[href="#request"]').on('shown.bs.tab',function(e){
	
});
$('a[href="#ongoing"]').on('hide.bs.tab', function(e) {
    for (i = 0; i < intervals.length; i++) {
        clearInterval(intervals[i]);
    }
    intervals = [];
});
$('a[href="#ongoing"]').on('shown.bs.tab', function(e) {
    console.log(e.target);
    for (i = 0; i < intervals.length; i++) {
        clearInterval(intervals[i]);
    }
    intervals = [];
    $.ajax({
        url: '/auction/ongoing',
        success: function(data) {
            //$('#ongoing').html("<h1>HHHHH</h1>");
            var string = "";
            data.forEach(function(e) {
                console.log(e['itemName']);
                var id = e['auction_id'];
                string += '<div class="list-group-item"><div class="row"><div class="col-md-8"><p style="font-size: 16px;">' + e['itemName'] +
                    '</p></div><div class="col-md-4">';
                string += '<div><span style="font-size: 16px; font-weight: 700;margin-right: 30px;">Time Left</span> <span style="font-size: 16px; font-weight: 700" id="time' + id + '">' +
                    e['timeRemaining'] + '</span></div>';
                string += '<div><span style="font-size: 16px;margin-right: 12px;">Current Price</span><span style="font-size: 16px;" id="price' + id + '">Rs ' +
                    e['current_bid'] + '</span></div>';
                string += '<div><span style="font-size: 16px;margin-right: 36px;">Total Bids</span><span style="font-size: 16px;" id="bids' + id + '">' +
                    e['totalBids'] + '</span></div>';
                string += '<div><span style="font-size: 16px;margin-right: 14px;">Total Bidders</span><span style="font-size: 16px;" id="bidders' + id + '">' +
                    e['totalBidders'] + '</span></div>';
                string += '</div></div></div>';
                timer(e['timeRemaining'], id);
               
            });
            $('#error_on').empty();
            $('#curr_auction').html(string);
            $('a[href="#ongoing"]').html('Ongoing <span class="badge">'+data.length+'</span>');
            
        },
        error: function(data) {
            $('#curr_auction').empty();
            $('#error_on').html("<h1>ERROR</h1>");
        }
    });
});
$('a[href="#complete"]').on('shown.bs.tab', function(e) {
    console.log(e.target);
    $.ajax({
        url: '/auction/completed',
        success: function(data) {
        	var string = "";
        	data=JSON.parse(data);
        	data.forEach(function(e){
        		var id=e['auction_id'];
        		var start=new Date(e['start_date']);
        		var end=new Date(e['end_date']);
        		console.log(e['item_name']);
        		string += '<div class="list-group-item"><div class="row"><div class="col-md-6"><p style="font-size: 16px;">' + e['item_name'] +
                '</p></div><div class="col-md-6">';
        		string += '<div><span style="font-size: 16px;margin-right: 30px;">Start Date & Time</span> <span style="font-size: 16px; font-weight: 700">' +
                start.toDateString()+' '+start.toLocaleTimeString() + '</span></div>';
        		string += '<div><span style="font-size: 16px;margin-right: 30px;">End Date & Time</span> <span style="font-size: 16px; font-weight: 700">' +
        		end.toDateString()+' '+end.toLocaleTimeString()+ '</span></div>';
        		string += '<div><span style="font-size: 16px;margin-right: 12px;">Winning Bid</span><span style="font-size: 16px;" id="price' + id + '">Rs ' +
                e['wining_bid'] + '</span></div>';
        		string += '<div><span style="font-size: 16px;margin-right: 36px;">Total Bids</span><span style="font-size: 16px;" id="bids' + id + '">' +
                e['total_bids'] + '</span></div>';
        		string += '<div><span style="font-size: 16px;margin-right: 14px;">Total Bidders</span><span style="font-size: 16px;" id="bidders' + id + '">' +
                e['total_bidders'] + '</span></div>';
        		string += '</div></div></div>';
            
        	});
        	$('#completed_auc').html(string);
        	$('a[href="#complete"]').html('Complete <span class="badge">'+data.length+'</span>');
        },
        error: function(e) {
        	
        }
    });
});
$('.reject').on('click', function(e) {
    e.preventDefault();
    var val = $(this).parent().find('input[name="aid"]').val();
    var cur = $(this).parent();
    $.ajax({
        url: 'verify',
        method: 'put',
        data: {
            id: val,
        },
        success: function(data, status) {
            cur.hide(500, function() {
                $(this).remove();
            });
        },
        error: function(data, status) {
            console.log(status);
        }
    });
});
$('.verify').on('click', function(e) {
    var val = $(this).parent().find('input[name="aid"]').val();
    $('input[name=verify_auc_id]').val(val);
    _element = $(this).parent();
    $('#myModal').modal('show');
});
$('#verify-form').on('submit', function(e) {
    e.preventDefault();
    var form_data = $(this).serialize();
    $.ajax({
        url: 'verify',
        method: 'post',
        data: form_data,
        success: function(data, status) {
            $('#myModal').modal('hide');
            _element.hide(500, function() {
                $(this).remove();
            });
        },
        error: function(data, status) {
            console.error("Failure");
        }
    });

});
$('.view-img').on('click', function(e) {
    e.preventDefault();
    var val = $(this).parent().find('input[name="aid"]').val();
    $.ajax({
        url: '/getimages/' + val,
        method: 'get',
        success: function(data, status) {
            var thumbs = "";
            var carsoule = "";
            data.forEach(function(e, index) {
                console.log(e + " -- " +
                    index);
                thumbs += '<li class="col-sm-3"><a class="thumbnail" id="carousel-selector-' + index + '"><img src="/image/' + e + '"/></a></li>';
                carsoule += '<div class="item" data-slide-number="' + (index + 1) + '"><img src="/image/' + e + '"/></div>';

            });
            $('.carousel-img-thumb').html(
                carsoule);
            $('.img-thumbs').html(thumbs);
            var t = $('.carousel-img-thumb>.item')[0];
            $(t).addClass('active');
            carouselUpdate();
            $('#imageViewer').modal('show');
            console.error("Success");
        },
        error: function(data, status) {
            console.error("Failure");
        }
    });

});
var intervals = [];
var timer = function(miliseconds, id) {
    var distance = miliseconds;
    // Update the count down every 1 second
    var x = setInterval(function() {

        // Get todays date and time
        var now = new Date().getTime();

        // Find the distance between now an the count down date
        distance -= 1000;
        // Time calculations for days, hours, minutes and seconds
        var days = Math.floor(distance / (1000 * 60 * 60 * 24));
        var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) /
            (1000 * 60 * 60));
        var minutes = Math.floor((distance % (1000 * 60 * 60)) /
            (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);

        // Display the result in the element with id="demo"
        document.getElementById("time" + id).innerHTML = days + "d " +
            hours + "h " + minutes + "m " + seconds + "s ";

        // If the count down is finished, write some text 

        if (distance < 0) {
            clearInterval(x);
            document.getElementById("time" + id).innerHTML = "EXPIRED";
        }

    }, 1000);
    intervals.push(x);
};

var carouselUpdate = function() {
    console.log("carousel updated");
    $('#myCarousel').carousel({
        interval: 3000
    });

    //Handles the carousel thumbnails
    $('[id^=carousel-selector-]').on('click', function() {
        var id_selector = $(this).attr("id");

        try {
            var id = /-(\d+)$/.exec(id_selector)[1];

            $('#myCarousel').carousel(parseInt(id));
        } catch (e) {
            console.log('Regex failed!', e);
        }
    });
    // When the carousel slides, auto update the text
    $('#myCarousel').on('slid.bs.carousel', function(e) {
        var id = $('.item.active').data('slide-number');
        $('#carousel-text').html($('#slide-content-' + id).html());
    });
};
/*]]>*/