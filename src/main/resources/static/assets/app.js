(function() {

  'use strict';

  $(document).ready(function() {

    var htmlToAdd =
      '<div class="col-md-12 col-lg-4">' +
      '<div class="input">' +
      '<input type="text" class="form-control input__field" placeholder="variable">' +
      '</div>' +
      '</div>' +
      '<div class="col-md-12 col-lg-8">' +
      '<div class="input">' +
      '<input type="text" class="form-control input__field" placeholder="value">' +
      '</div>' +
      '</div>';

    $(document).on('click', '.js-add-new-env-var', function() {
      $('.js-section-add-env').append(htmlToAdd);
    });

  });

})();

(function() {

  'use strict';

  $(document).ready(function() {


  });

})();

(function() {

  'use strict';

  function createCharts() {
    var chart1 = document.getElementById("chart1").getContext("2d");
    var chart2 = document.getElementById("chart2").getContext("2d");
    var chart3 = document.getElementById("chart3").getContext("2d");

    // Fill with gradient

    var randomScalingFactor = function() {
      return Math.round(Math.random() * 50);
    };

    var xLabels =
      ["10Jun", "11Jun", "12Jun", "13Jun", "14Jun", "15Jun", "16Jun"];
    var bgColorGreen = "rgba(220, 242, 235, 0.7)";
    var bgColorBlue = "rgba(229, 242, 251, 0.7)";
    var lineColorGreen = "rgb(140, 233, 145)";
    var lineColorBlue = "rgb(147, 196, 250)";

    var config = {
      type: 'line',
      data: {
        labels: xLabels,
        datasets: [{
          backgroundColor: bgColorBlue,
          borderColor: lineColorBlue,
          pointBackgroundColor: lineColorBlue,
          borderWidth: 1.5,
          label: "CPU",
          data: [randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor()],
        }, {
          backgroundColor: bgColorGreen,
          borderColor: lineColorGreen,
          pointBackgroundColor: lineColorGreen,
          borderWidth: 1.5,
          label: "Media",
          data: [randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor()],
        }]
      },
      options: {
        legend: {
          display: false
        },
        responsive: true,
        title: {
          display: false,
          text: 'Chart.js Line Chart'
        },
        tooltips: {
          mode: 'label',
          callbacks: {
            // beforeTitle: function() {
            //     return '...beforeTitle';
            // },
            // afterTitle: function() {
            //     return '...afterTitle';
            // },
            // beforeBody: function() {
            //     return '...beforeBody';
            // },
            // afterBody: function() {
            //     return '...afterBody';
            // },
            // beforeFooter: function() {
            //     return '...beforeFooter';
            // },
            // footer: function() {
            //     return 'Footer';
            // },
            // afterFooter: function() {
            //     return '...afterFooter';
            // },
          }
        },
        hover: {
          mode: 'dataset'
        },
        scales: {
          xAxes: [{
            display: true,
            scaleLabel: {
              display: true
            },
            gridLines: {
              display: false,
            },
            ticks: {
              mirror: true
            }
          }],
          yAxes: [{
            display: false,
            scaleLabel: {
              display: false
            },
            ticks: {
              // mirror: true
              // suggestedMin: 0,
              // suggestedMax: 250,
            }
          }]
        }
      }
    };


    window.myLine = new Chart(chart1, config);
    window.myLine = new Chart(chart2, config);
    window.myLine = new Chart(chart3, config);
  }

  $(document).ready(function() {

    if (!!document.getElementById("chart1")) {
      createCharts();
    }

  });

})();

(function() {

  'use strict';

  $(document).ready(function() {

    var clipboard = new Clipboard('.js-clipboard-btn');
    var copyTooltip = $('.js-clipboard-btn');

    copyTooltip.tooltip({
      container: 'body',
      html: true,
      trigger: 'manual',
      title: 'Copied!'
    });

    //Show tooltip on copy success
    clipboard.on('success', function(e) {
      copyTooltip.tooltip('show');
    });

    // Hide the copy tooltip
    $(document).on('click', function(e) {
      if (e.target !== copyTooltip[0])
        copyTooltip.tooltip('hide');
    });
  });

})();

(function() {

  $(document).ready(function() {
    'use strict';

    // Progress bar simulation
    var progressBar = $('.js-progress-bar');
    var maxValue = $('.js-progress-bar').attr('aria-valuemax');
    var currentValue = $('.js-progress-bar').attr('aria-valuenow');
    var step = 100 / maxValue;
    var width = currentValue * step;

    var notifications = ['small', 'warning', 'danger'];

    progressBar.css('width', width + '%');
    $('.js-progress-label').html(currentValue);

    // Set some custom notification for progress bar
    if (width <= 49) {
      $('.js-progress-notification').html(notifications[0]);
    } else if (width > 49 && width < 75) {
      $('.js-progress-notification').html(notifications[1]);
    } else if (width >= 75) {
      $('.js-progress-notification').html(notifications[2]);
    }

  });

})();

(function() {

  $(document).ready(function() {
    'use strict';

    // Toggle form inputs via special checkbox
    // Trebuie sa fac uncheck la checkboxuirle care au fost deja
    // selectate dar care sunt disabled in momentul de fata?

    var inputsToToggle;

    $(document).on('change', '.js-toggle-inputs', function() {
      $(this)
        .closest('.js-toggle-input-container')
        .find('.js-input-to-toggle')
        .prop('disabled', function(index, state) {
          inputsToToggle = $('.js-input-to-toggle')
            .closest('.js-control');

          if (!state) {
            inputsToToggle.addClass('is-disabled');
          } else {
            inputsToToggle.removeClass('is-disabled');
          }

          return !state;
        });
    });
  });

})();

(function() {

  'use strict';

  $(document).ready(function() {

    function activateOverlay() {
      $('.js-menu-overlay').toggleClass('is-active');
    }

    // Toggle left menu
    $(document).on('click', '.js-toggle-left-menu', function() {
      $('body').toggleClass('left-menu-is-opened');
      activateOverlay();
    });

    // Toggle right menu
    $(document).on('click', '.js-toggle-top-menu', function() {
      $('body').toggleClass('top-menu-is-opened');
      activateOverlay();
    });

    $(document).on('click', '.js-menu-overlay', function() {
      $(this).removeClass('is-active');
      $('body').removeClass('top-menu-is-opened left-menu-is-opened');
    });

  });

})();

//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbImFkZE5ld0VudlZhci5qcyIsImFwcC5qcyIsImNoYXJ0cy5qcyIsImNvcHlDbGlwYm9hcmQuanMiLCJwcm9ncmVzc0Jhci5qcyIsInRvZ2dsZUlucHV0cy5qcyIsInRvZ2dsZU1lbnUuanMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQ3pCQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FDVkE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUMzSEE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FDN0JBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQzdCQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FDL0JBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBIiwiZmlsZSI6ImFwcC5qcyIsInNvdXJjZXNDb250ZW50IjpbIihmdW5jdGlvbigpIHtcblxuICAndXNlIHN0cmljdCc7XG5cbiAgJChkb2N1bWVudCkucmVhZHkoZnVuY3Rpb24oKSB7XG5cbiAgICB2YXIgaHRtbFRvQWRkID1cbiAgICAgICc8ZGl2IGNsYXNzPVwiY29sLW1kLTEyIGNvbC1sZy00XCI+JyArXG4gICAgICAnPGRpdiBjbGFzcz1cImlucHV0XCI+JyArXG4gICAgICAnPGlucHV0IHR5cGU9XCJ0ZXh0XCIgY2xhc3M9XCJmb3JtLWNvbnRyb2wgaW5wdXRfX2ZpZWxkXCIgcGxhY2Vob2xkZXI9XCJ2YXJpYWJsZVwiPicgK1xuICAgICAgJzwvZGl2PicgK1xuICAgICAgJzwvZGl2PicgK1xuICAgICAgJzxkaXYgY2xhc3M9XCJjb2wtbWQtMTIgY29sLWxnLThcIj4nICtcbiAgICAgICc8ZGl2IGNsYXNzPVwiaW5wdXRcIj4nICtcbiAgICAgICc8aW5wdXQgdHlwZT1cInRleHRcIiBjbGFzcz1cImZvcm0tY29udHJvbCBpbnB1dF9fZmllbGRcIiBwbGFjZWhvbGRlcj1cInZhbHVlXCI+JyArXG4gICAgICAnPC9kaXY+JyArXG4gICAgICAnPC9kaXY+JztcblxuICAgICQoZG9jdW1lbnQpLm9uKCdjbGljaycsICcuanMtYWRkLW5ldy1lbnYtdmFyJywgZnVuY3Rpb24oKSB7XG4gICAgICAkKCcuanMtc2VjdGlvbi1hZGQtZW52JykuYXBwZW5kKGh0bWxUb0FkZCk7XG4gICAgfSk7XG5cbiAgfSk7XG5cbn0pKCk7XG4iLCIoZnVuY3Rpb24oKSB7XG5cbiAgJ3VzZSBzdHJpY3QnO1xuXG4gICQoZG9jdW1lbnQpLnJlYWR5KGZ1bmN0aW9uKCkge1xuXG5cbiAgfSk7XG5cbn0pKCk7XG4iLCIoZnVuY3Rpb24oKSB7XG5cbiAgJ3VzZSBzdHJpY3QnO1xuXG4gIGZ1bmN0aW9uIGNyZWF0ZUNoYXJ0cygpIHtcbiAgICB2YXIgY2hhcnQxID0gZG9jdW1lbnQuZ2V0RWxlbWVudEJ5SWQoXCJjaGFydDFcIikuZ2V0Q29udGV4dChcIjJkXCIpO1xuICAgIHZhciBjaGFydDIgPSBkb2N1bWVudC5nZXRFbGVtZW50QnlJZChcImNoYXJ0MlwiKS5nZXRDb250ZXh0KFwiMmRcIik7XG4gICAgdmFyIGNoYXJ0MyA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwiY2hhcnQzXCIpLmdldENvbnRleHQoXCIyZFwiKTtcblxuICAgIC8vIEZpbGwgd2l0aCBncmFkaWVudFxuXG4gICAgdmFyIHJhbmRvbVNjYWxpbmdGYWN0b3IgPSBmdW5jdGlvbigpIHtcbiAgICAgIHJldHVybiBNYXRoLnJvdW5kKE1hdGgucmFuZG9tKCkgKiA1MCk7XG4gICAgfTtcblxuICAgIHZhciB4TGFiZWxzID1cbiAgICAgIFtcIjEwSnVuXCIsIFwiMTFKdW5cIiwgXCIxMkp1blwiLCBcIjEzSnVuXCIsIFwiMTRKdW5cIiwgXCIxNUp1blwiLCBcIjE2SnVuXCJdO1xuICAgIHZhciBiZ0NvbG9yR3JlZW4gPSBcInJnYmEoMjIwLCAyNDIsIDIzNSwgMC43KVwiO1xuICAgIHZhciBiZ0NvbG9yQmx1ZSA9IFwicmdiYSgyMjksIDI0MiwgMjUxLCAwLjcpXCI7XG4gICAgdmFyIGxpbmVDb2xvckdyZWVuID0gXCJyZ2IoMTQwLCAyMzMsIDE0NSlcIjtcbiAgICB2YXIgbGluZUNvbG9yQmx1ZSA9IFwicmdiKDE0NywgMTk2LCAyNTApXCI7XG5cbiAgICB2YXIgY29uZmlnID0ge1xuICAgICAgdHlwZTogJ2xpbmUnLFxuICAgICAgZGF0YToge1xuICAgICAgICBsYWJlbHM6IHhMYWJlbHMsXG4gICAgICAgIGRhdGFzZXRzOiBbe1xuICAgICAgICAgIGJhY2tncm91bmRDb2xvcjogYmdDb2xvckJsdWUsXG4gICAgICAgICAgYm9yZGVyQ29sb3I6IGxpbmVDb2xvckJsdWUsXG4gICAgICAgICAgcG9pbnRCYWNrZ3JvdW5kQ29sb3I6IGxpbmVDb2xvckJsdWUsXG4gICAgICAgICAgYm9yZGVyV2lkdGg6IDEuNSxcbiAgICAgICAgICBsYWJlbDogXCJDUFVcIixcbiAgICAgICAgICBkYXRhOiBbcmFuZG9tU2NhbGluZ0ZhY3RvcigpLCByYW5kb21TY2FsaW5nRmFjdG9yKCksIHJhbmRvbVNjYWxpbmdGYWN0b3IoKSwgcmFuZG9tU2NhbGluZ0ZhY3RvcigpLCByYW5kb21TY2FsaW5nRmFjdG9yKCksIHJhbmRvbVNjYWxpbmdGYWN0b3IoKSwgcmFuZG9tU2NhbGluZ0ZhY3RvcigpXSxcbiAgICAgICAgfSwge1xuICAgICAgICAgIGJhY2tncm91bmRDb2xvcjogYmdDb2xvckdyZWVuLFxuICAgICAgICAgIGJvcmRlckNvbG9yOiBsaW5lQ29sb3JHcmVlbixcbiAgICAgICAgICBwb2ludEJhY2tncm91bmRDb2xvcjogbGluZUNvbG9yR3JlZW4sXG4gICAgICAgICAgYm9yZGVyV2lkdGg6IDEuNSxcbiAgICAgICAgICBsYWJlbDogXCJNZWRpYVwiLFxuICAgICAgICAgIGRhdGE6IFtyYW5kb21TY2FsaW5nRmFjdG9yKCksIHJhbmRvbVNjYWxpbmdGYWN0b3IoKSwgcmFuZG9tU2NhbGluZ0ZhY3RvcigpLCByYW5kb21TY2FsaW5nRmFjdG9yKCksIHJhbmRvbVNjYWxpbmdGYWN0b3IoKSwgcmFuZG9tU2NhbGluZ0ZhY3RvcigpLCByYW5kb21TY2FsaW5nRmFjdG9yKCldLFxuICAgICAgICB9XVxuICAgICAgfSxcbiAgICAgIG9wdGlvbnM6IHtcbiAgICAgICAgbGVnZW5kOiB7XG4gICAgICAgICAgZGlzcGxheTogZmFsc2VcbiAgICAgICAgfSxcbiAgICAgICAgcmVzcG9uc2l2ZTogdHJ1ZSxcbiAgICAgICAgdGl0bGU6IHtcbiAgICAgICAgICBkaXNwbGF5OiBmYWxzZSxcbiAgICAgICAgICB0ZXh0OiAnQ2hhcnQuanMgTGluZSBDaGFydCdcbiAgICAgICAgfSxcbiAgICAgICAgdG9vbHRpcHM6IHtcbiAgICAgICAgICBtb2RlOiAnbGFiZWwnLFxuICAgICAgICAgIGNhbGxiYWNrczoge1xuICAgICAgICAgICAgLy8gYmVmb3JlVGl0bGU6IGZ1bmN0aW9uKCkge1xuICAgICAgICAgICAgLy8gICAgIHJldHVybiAnLi4uYmVmb3JlVGl0bGUnO1xuICAgICAgICAgICAgLy8gfSxcbiAgICAgICAgICAgIC8vIGFmdGVyVGl0bGU6IGZ1bmN0aW9uKCkge1xuICAgICAgICAgICAgLy8gICAgIHJldHVybiAnLi4uYWZ0ZXJUaXRsZSc7XG4gICAgICAgICAgICAvLyB9LFxuICAgICAgICAgICAgLy8gYmVmb3JlQm9keTogZnVuY3Rpb24oKSB7XG4gICAgICAgICAgICAvLyAgICAgcmV0dXJuICcuLi5iZWZvcmVCb2R5JztcbiAgICAgICAgICAgIC8vIH0sXG4gICAgICAgICAgICAvLyBhZnRlckJvZHk6IGZ1bmN0aW9uKCkge1xuICAgICAgICAgICAgLy8gICAgIHJldHVybiAnLi4uYWZ0ZXJCb2R5JztcbiAgICAgICAgICAgIC8vIH0sXG4gICAgICAgICAgICAvLyBiZWZvcmVGb290ZXI6IGZ1bmN0aW9uKCkge1xuICAgICAgICAgICAgLy8gICAgIHJldHVybiAnLi4uYmVmb3JlRm9vdGVyJztcbiAgICAgICAgICAgIC8vIH0sXG4gICAgICAgICAgICAvLyBmb290ZXI6IGZ1bmN0aW9uKCkge1xuICAgICAgICAgICAgLy8gICAgIHJldHVybiAnRm9vdGVyJztcbiAgICAgICAgICAgIC8vIH0sXG4gICAgICAgICAgICAvLyBhZnRlckZvb3RlcjogZnVuY3Rpb24oKSB7XG4gICAgICAgICAgICAvLyAgICAgcmV0dXJuICcuLi5hZnRlckZvb3Rlcic7XG4gICAgICAgICAgICAvLyB9LFxuICAgICAgICAgIH1cbiAgICAgICAgfSxcbiAgICAgICAgaG92ZXI6IHtcbiAgICAgICAgICBtb2RlOiAnZGF0YXNldCdcbiAgICAgICAgfSxcbiAgICAgICAgc2NhbGVzOiB7XG4gICAgICAgICAgeEF4ZXM6IFt7XG4gICAgICAgICAgICBkaXNwbGF5OiB0cnVlLFxuICAgICAgICAgICAgc2NhbGVMYWJlbDoge1xuICAgICAgICAgICAgICBkaXNwbGF5OiB0cnVlXG4gICAgICAgICAgICB9LFxuICAgICAgICAgICAgZ3JpZExpbmVzOiB7XG4gICAgICAgICAgICAgIGRpc3BsYXk6IGZhbHNlLFxuICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIHRpY2tzOiB7XG4gICAgICAgICAgICAgIG1pcnJvcjogdHJ1ZVxuICAgICAgICAgICAgfVxuICAgICAgICAgIH1dLFxuICAgICAgICAgIHlBeGVzOiBbe1xuICAgICAgICAgICAgZGlzcGxheTogZmFsc2UsXG4gICAgICAgICAgICBzY2FsZUxhYmVsOiB7XG4gICAgICAgICAgICAgIGRpc3BsYXk6IGZhbHNlXG4gICAgICAgICAgICB9LFxuICAgICAgICAgICAgdGlja3M6IHtcbiAgICAgICAgICAgICAgLy8gbWlycm9yOiB0cnVlXG4gICAgICAgICAgICAgIC8vIHN1Z2dlc3RlZE1pbjogMCxcbiAgICAgICAgICAgICAgLy8gc3VnZ2VzdGVkTWF4OiAyNTAsXG4gICAgICAgICAgICB9XG4gICAgICAgICAgfV1cbiAgICAgICAgfVxuICAgICAgfVxuICAgIH07XG5cblxuICAgIHdpbmRvdy5teUxpbmUgPSBuZXcgQ2hhcnQoY2hhcnQxLCBjb25maWcpO1xuICAgIHdpbmRvdy5teUxpbmUgPSBuZXcgQ2hhcnQoY2hhcnQyLCBjb25maWcpO1xuICAgIHdpbmRvdy5teUxpbmUgPSBuZXcgQ2hhcnQoY2hhcnQzLCBjb25maWcpO1xuICB9XG5cbiAgJChkb2N1bWVudCkucmVhZHkoZnVuY3Rpb24oKSB7XG5cbiAgICBpZiAoISFkb2N1bWVudC5nZXRFbGVtZW50QnlJZChcImNoYXJ0MVwiKSkge1xuICAgICAgY3JlYXRlQ2hhcnRzKCk7XG4gICAgfVxuXG4gIH0pO1xuXG59KSgpO1xuIiwiKGZ1bmN0aW9uKCkge1xuXG4gICd1c2Ugc3RyaWN0JztcblxuICAkKGRvY3VtZW50KS5yZWFkeShmdW5jdGlvbigpIHtcblxuICAgIHZhciBjbGlwYm9hcmQgPSBuZXcgQ2xpcGJvYXJkKCcuanMtY2xpcGJvYXJkLWJ0bicpO1xuICAgIHZhciBjb3B5VG9vbHRpcCA9ICQoJy5qcy1jbGlwYm9hcmQtYnRuJyk7XG5cbiAgICBjb3B5VG9vbHRpcC50b29sdGlwKHtcbiAgICAgIGNvbnRhaW5lcjogJ2JvZHknLFxuICAgICAgaHRtbDogdHJ1ZSxcbiAgICAgIHRyaWdnZXI6ICdtYW51YWwnLFxuICAgICAgdGl0bGU6ICdDb3BpZWQhJ1xuICAgIH0pO1xuXG4gICAgLy9TaG93IHRvb2x0aXAgb24gY29weSBzdWNjZXNzXG4gICAgY2xpcGJvYXJkLm9uKCdzdWNjZXNzJywgZnVuY3Rpb24oZSkge1xuICAgICAgY29weVRvb2x0aXAudG9vbHRpcCgnc2hvdycpO1xuICAgIH0pO1xuXG4gICAgLy8gSGlkZSB0aGUgY29weSB0b29sdGlwXG4gICAgJChkb2N1bWVudCkub24oJ2NsaWNrJywgZnVuY3Rpb24oZSkge1xuICAgICAgaWYgKGUudGFyZ2V0ICE9PSBjb3B5VG9vbHRpcFswXSlcbiAgICAgICAgY29weVRvb2x0aXAudG9vbHRpcCgnaGlkZScpO1xuICAgIH0pO1xuICB9KTtcblxufSkoKTtcbiIsIihmdW5jdGlvbigpIHtcblxuICAkKGRvY3VtZW50KS5yZWFkeShmdW5jdGlvbigpIHtcbiAgICAndXNlIHN0cmljdCc7XG5cbiAgICAvLyBQcm9ncmVzcyBiYXIgc2ltdWxhdGlvblxuICAgIHZhciBwcm9ncmVzc0JhciA9ICQoJy5qcy1wcm9ncmVzcy1iYXInKTtcbiAgICB2YXIgbWF4VmFsdWUgPSAkKCcuanMtcHJvZ3Jlc3MtYmFyJykuYXR0cignYXJpYS12YWx1ZW1heCcpO1xuICAgIHZhciBjdXJyZW50VmFsdWUgPSAkKCcuanMtcHJvZ3Jlc3MtYmFyJykuYXR0cignYXJpYS12YWx1ZW5vdycpO1xuICAgIHZhciBzdGVwID0gMTAwIC8gbWF4VmFsdWU7XG4gICAgdmFyIHdpZHRoID0gY3VycmVudFZhbHVlICogc3RlcDtcblxuICAgIHZhciBub3RpZmljYXRpb25zID0gWydzbWFsbCcsICd3YXJuaW5nJywgJ2RhbmdlciddO1xuXG4gICAgcHJvZ3Jlc3NCYXIuY3NzKCd3aWR0aCcsIHdpZHRoICsgJyUnKTtcbiAgICAkKCcuanMtcHJvZ3Jlc3MtbGFiZWwnKS5odG1sKGN1cnJlbnRWYWx1ZSk7XG5cbiAgICAvLyBTZXQgc29tZSBjdXN0b20gbm90aWZpY2F0aW9uIGZvciBwcm9ncmVzcyBiYXJcbiAgICBpZiAod2lkdGggPD0gNDkpIHtcbiAgICAgICQoJy5qcy1wcm9ncmVzcy1ub3RpZmljYXRpb24nKS5odG1sKG5vdGlmaWNhdGlvbnNbMF0pO1xuICAgIH0gZWxzZSBpZiAod2lkdGggPiA0OSAmJiB3aWR0aCA8IDc1KSB7XG4gICAgICAkKCcuanMtcHJvZ3Jlc3Mtbm90aWZpY2F0aW9uJykuaHRtbChub3RpZmljYXRpb25zWzFdKTtcbiAgICB9IGVsc2UgaWYgKHdpZHRoID49IDc1KSB7XG4gICAgICAkKCcuanMtcHJvZ3Jlc3Mtbm90aWZpY2F0aW9uJykuaHRtbChub3RpZmljYXRpb25zWzJdKTtcbiAgICB9XG5cbiAgfSk7XG5cbn0pKCk7XG4iLCIoZnVuY3Rpb24oKSB7XG5cbiAgJChkb2N1bWVudCkucmVhZHkoZnVuY3Rpb24oKSB7XG4gICAgJ3VzZSBzdHJpY3QnO1xuXG4gICAgLy8gVG9nZ2xlIGZvcm0gaW5wdXRzIHZpYSBzcGVjaWFsIGNoZWNrYm94XG4gICAgLy8gVHJlYnVpZSBzYSBmYWMgdW5jaGVjayBsYSBjaGVja2JveHVpcmxlIGNhcmUgYXUgZm9zdCBkZWphXG4gICAgLy8gc2VsZWN0YXRlIGRhciBjYXJlIHN1bnQgZGlzYWJsZWQgaW4gbW9tZW50dWwgZGUgZmF0YT9cblxuICAgIHZhciBpbnB1dHNUb1RvZ2dsZTtcblxuICAgICQoZG9jdW1lbnQpLm9uKCdjaGFuZ2UnLCAnLmpzLXRvZ2dsZS1pbnB1dHMnLCBmdW5jdGlvbigpIHtcbiAgICAgICQodGhpcylcbiAgICAgICAgLmNsb3Nlc3QoJy5qcy10b2dnbGUtaW5wdXQtY29udGFpbmVyJylcbiAgICAgICAgLmZpbmQoJy5qcy1pbnB1dC10by10b2dnbGUnKVxuICAgICAgICAucHJvcCgnZGlzYWJsZWQnLCBmdW5jdGlvbihpbmRleCwgc3RhdGUpIHtcbiAgICAgICAgICBpbnB1dHNUb1RvZ2dsZSA9ICQoJy5qcy1pbnB1dC10by10b2dnbGUnKVxuICAgICAgICAgICAgLmNsb3Nlc3QoJy5qcy1jb250cm9sJyk7XG5cbiAgICAgICAgICBpZiAoIXN0YXRlKSB7XG4gICAgICAgICAgICBpbnB1dHNUb1RvZ2dsZS5hZGRDbGFzcygnaXMtZGlzYWJsZWQnKTtcbiAgICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgICAgaW5wdXRzVG9Ub2dnbGUucmVtb3ZlQ2xhc3MoJ2lzLWRpc2FibGVkJyk7XG4gICAgICAgICAgfVxuXG4gICAgICAgICAgcmV0dXJuICFzdGF0ZTtcbiAgICAgICAgfSk7XG4gICAgfSk7XG4gIH0pO1xuXG59KSgpO1xuIiwiKGZ1bmN0aW9uKCkge1xuXG4gICd1c2Ugc3RyaWN0JztcblxuICAkKGRvY3VtZW50KS5yZWFkeShmdW5jdGlvbigpIHtcblxuICAgIGZ1bmN0aW9uIGFjdGl2YXRlT3ZlcmxheSgpIHtcbiAgICAgICQoJy5qcy1tZW51LW92ZXJsYXknKS50b2dnbGVDbGFzcygnaXMtYWN0aXZlJyk7XG4gICAgfVxuXG4gICAgLy8gVG9nZ2xlIGxlZnQgbWVudVxuICAgICQoZG9jdW1lbnQpLm9uKCdjbGljaycsICcuanMtdG9nZ2xlLWxlZnQtbWVudScsIGZ1bmN0aW9uKCkge1xuICAgICAgJCgnYm9keScpLnRvZ2dsZUNsYXNzKCdsZWZ0LW1lbnUtaXMtb3BlbmVkJyk7XG4gICAgICBhY3RpdmF0ZU92ZXJsYXkoKTtcbiAgICB9KTtcblxuICAgIC8vIFRvZ2dsZSByaWdodCBtZW51XG4gICAgJChkb2N1bWVudCkub24oJ2NsaWNrJywgJy5qcy10b2dnbGUtdG9wLW1lbnUnLCBmdW5jdGlvbigpIHtcbiAgICAgICQoJ2JvZHknKS50b2dnbGVDbGFzcygndG9wLW1lbnUtaXMtb3BlbmVkJyk7XG4gICAgICBhY3RpdmF0ZU92ZXJsYXkoKTtcbiAgICB9KTtcblxuICAgICQoZG9jdW1lbnQpLm9uKCdjbGljaycsICcuanMtbWVudS1vdmVybGF5JywgZnVuY3Rpb24oKSB7XG4gICAgICAkKHRoaXMpLnJlbW92ZUNsYXNzKCdpcy1hY3RpdmUnKTtcbiAgICAgICQoJ2JvZHknKS5yZW1vdmVDbGFzcygndG9wLW1lbnUtaXMtb3BlbmVkIGxlZnQtbWVudS1pcy1vcGVuZWQnKTtcbiAgICB9KTtcblxuICB9KTtcblxufSkoKTtcbiJdLCJzb3VyY2VSb290IjoiL3NvdXJjZS8ifQ==
