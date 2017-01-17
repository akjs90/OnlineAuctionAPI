(function() {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajaxSetup({
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			}
		});
	});

	var toogle = function(id) {
		$.ajax({
			url : "./changestatus/" + id,
			method : "GET",
			success : function(data, status) {
				//alert("yay");
			}
		});
	};
	$(document).ready(function(e) {
		$('.search-panel .dropdown-menu').find('a').click(function(e) {
			e.preventDefault();
			var param = $(this).attr("href").replace("#", "");
			var concept = $(this).text();
			$('.search-panel span#search_concept').text(concept);
			$('.input-group #search_param').val(param);
		});
		$('#query').autocomplete({
			autoFocus : false,
			minLength : 2,
			delay : 500,
			source : function(request, response) {
				var c = $('.input-group #search_param').val();
				$.ajax({
					url : "search",
					dataType : "json",
					data : {
						"t" : request.term,
						"c" : c
					},
					success : function(data) {
						//alert("data");
						response(data);
					}
				});
			}
		});

		$('#rows').change(function(e) {
			e.preventDefault();
			var limit = $('#rows').val()
			window.top.location = "/admin/people?l=" + limit;
		});
		
		$('td a').click(function(e) {
			e.preventDefault();
			var param=$(this).attr("href").replace("#", "");
			$.ajax({
				url:'info',
				dataType:'json',
				data:{t:param},
				success:function(data,status){
				     $('#p-name').text(data[0][0]);
				     $('#p-address').text(data[0][1]);
				     $('#p-email').text(data[0][2]);				     
					$('.a-profile-card').modal('show');
				},
				error:function(data,status){
					console.log("Data Not Found");
				}
			})
		})
	});