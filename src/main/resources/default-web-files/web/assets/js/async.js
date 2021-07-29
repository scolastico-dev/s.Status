let currentInterval = 0;

Date.prototype.ddmmyyyy = function() {
  const mm = this.getMonth() + 1;
  const dd = this.getDate();
  return [(dd>9 ? '' : '0') + dd,
    (mm>9 ? '' : '0') + mm,
    this.getFullYear()
  ].join('.');
};

function getDateStringMinusDays(days) {
  return new Date(Date.now()-((86400*days)*1000)).ddmmyyyy();
}

function timestampToDateAndTime(timestamp) {
  return new Date(timestamp*1000).toLocaleString();
}

function loadCardAsync(id) {
  $.ajax({
    url: "/api/v1/details/" + id,
    success: function (result) {
      $("#loading-"+id).remove();
      const div = $("#server-details-loaded-"+id);
      div.html(result);
      //const scripts = div.find('script');
      //for (let n = 0; n < scripts.length; n++) eval(scripts[n].innerHTML);
      replaceAllUnixTimeStamps();
    },
    error: function(jqXHR, textStatus, errorThrown) {
      if (jqXHR.status === 429) {
        toastr["error"](toManyRequests, toManyRequestsTitle);
      } else {
        toastr["error"](unknownError, unknownErrorTitle);
      }
    }
  })
}

function updateAsync() {
  $(".collapse.show").each(function () {
    loadCardAsync($(this).attr('id').substr(15));
  });
}

setInterval(function() {
  currentInterval++;
  const remaining = nextUpdateIn-currentInterval;
  $("#updateCounter").html(nextUpdateInLanguage.replace("%seconds%", remaining.toString()))
  if (currentInterval >= nextUpdateIn) {
    currentInterval = 0;
    updateAsync();
  }
}, 1000);

function replaceAllUnixTimeStamps() {
  $(".raw-unix-timestamp").each(function () {
    $(this).html(timestampToDateAndTime($(this).html()));
    $(this).removeClass("raw-unix-timestamp");
  });
}

function setLanguage(id) {
  const date = new Date();
  date.setFullYear(date.getFullYear()+1);
  const expires = "; expires= " + date.toUTCString();
  document.cookie = "language=" + id + expires + "; path=/";
  location.reload();
}

$(".collapse").on('shown.bs.collapse', function () {
  const id = $(this).attr('id').substr(15);
  if ($("#loading-"+id).length >= 0) {
    loadCardAsync(id);
  }
})

toastr.options = {
  "closeButton": false,
  "debug": false,
  "newestOnTop": false,
  "progressBar": true,
  "positionClass": "toast-top-right",
  "preventDuplicates": true,
  "onclick": null,
  "showDuration": "300",
  "hideDuration": "1000",
  "timeOut": "5000",
  "extendedTimeOut": "1000",
  "showEasing": "swing",
  "hideEasing": "linear",
  "showMethod": "fadeIn",
  "hideMethod": "fadeOut"
}
