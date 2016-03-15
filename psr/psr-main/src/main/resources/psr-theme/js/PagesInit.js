/*
 _______   __      __  ______    ______  
 /       \ /  \    /  |/      \  /      \
 $$$$$$$  |$$  \  /$$//$$$$$$  |/$$$$$$  |
 $$ |__$$ | $$  \/$$/ $$ |  $$/ $$ |  $$ |
 $$    $$/   $$  $$/  $$ |      $$ |  $$ |
 $$$$$$$/     $$$$/   $$ |   __ $$ |  $$ |
 $$ |          $$ |   $$ \__/  |$$ \__$$ |
 $$ |          $$ |   $$    $$/ $$    $$/
 $$/           $$/     $$$$$$/   $$$$$$/

 * license Copyright 2015
 * 
 *
 * JS Main Initialization: Load & Library Initialization
 * Project: Pyco WebSite
 * Version: 0.1
 * Buld no: 1
 *
 * author Vincent Genevoix - vincent.genevoix@pycogroup.com
 *
 */

/***************************************
 * GENERIC MODULE - PYCO OBJECT WRAPPER *
 ***************************************/

var Pyco = (function ($) {
  var expose = {};//This will contain all "public" properties and methods of this module

  /* Utilities to be used in most (all) pages */
  expose.Utils = {};

  /* Global settings */
  expose.settings = {};

  /* Initialization method. Initialize variables, bind events,... Launched on document.ready*/
  expose.Initialize = function () {
    /* Menu Burger call to action */
    /* ************************** */
    $('body header, body section:not(#overlay), body footer').wrapAll('<div role="document">');
    var $win = $(window);
    var $body = $(document.body);
    var $html = $('html');
    var $documentWrapper = $('[role="document"]');

    $('#toggle').click(function () {
      $(this).toggleClass('active');
      $('#overlay').toggleClass('open');
      $('#overlay ul li').toggleClass('open');

      $body.toggleClass('is-overlay-open');
    });


    /* scroll to TOP */
    /* ************************** */


    $(function () {
      var $win = $(window);
      var $html = $('html');
      var $bodyAnimate = $('html, body');
      var $backToTop = $('#back-top');
      var timer;
      var TIMER_DELAY = 500;
      var SCROLL_DURATION = 500;

      $win.on('scroll.BackToTopButton', function () {
        if ($html.hasClass('ie') && $html.hasClass('mobile')) {
          clearTimeout(timer);
          $backToTop.removeClass('is-showing');

          timer = setTimeout(function () {
            backButtonPosition();
          }, TIMER_DELAY);

        } else {
          backButtonPosition();
        }

      });

      function backButtonPosition () {
        if ($win.scrollTop() > 300) {
          $backToTop.addClass('is-showing');
        } else {
          $backToTop.removeClass('is-showing');
        }
      }

      backButtonPosition();


      $backToTop.on('click.BackToTopButton', function (e) {
        e.preventDefault();

        $bodyAnimate.stop().animate({
          scrollTop: 0
        }, SCROLL_DURATION);

        return false;
      });

    });

    $('.printMe').click(function () {
      window.print();
    });

    // prevent special chars ~!@#$%^&*()+|{}:"<>?=`'\/
    var validateEmail = function (email) {
      var re = /^(([^<>()[\]\\.,;~!#$%^&*+|{}?:\s@"=`'\/]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    };

    var hideSuccessfulMessage = function () {
      $(this).removeClass('success');
    };

    var validateForms = function () {
      var forms = document.getElementsByTagName('form');
      var $error, $email_error, value, type;
      var error_email = false;
      var $email, $success;

      for (var i = 0; i < forms.length; i++) {
        forms[i].noValidate = true;

        forms[i].addEventListener('submit', function (event) {
          $error = $(this).find('.actions .required-error');
          $email_error = $(this).find('.actions .email-error');

          $success = $(this).find('.actions .success');
          if ($success.length) {
            $success.hide();
          }

          $(this).find('input[type="text"], input[type="email"], textarea').each(function () {
            value = $(this).val().trim();
            type = $(this).attr('data-type');
            $(this).val(value);

            if (value === '') {
              $(this).addClass('error');
              $(this).closest('.row').addClass('error');
            }
            else {
              $(this).removeClass('error');
              $(this).closest('.row').removeClass('error');
            }

            if (type === 'email') {
              $email = $(this);
              error_email = (!$email.hasClass('error') && !validateEmail(value));
            }
          });
          //Prevent submission if checkValidity on the form returns false.
          if (!event.target.checkValidity()) {
            event.preventDefault();
          }
          else {
            if (error_email) {
              event.preventDefault();
              $email.addClass('email-error');
            } else {
              $email.removeClass('email-error');
              if (!$(this).attr('action')) {
                event.preventDefault();
                $(this).closest('.form').addClass('success');
              }
            }
          }
        }, false);
      }
    };

    /* Menu Scroll UP OR DOWN action ADD #ID on HTML MARKER */
    /* ************************** */

    $(document).ready(function () {
      $(document).foundation(
        'interchange', {
          named_queries : {
            small_p : 'only screen and (max-width: 767px) and (orientation: portrait)',
            small_l : 'only screen and (max-width: 767px) and (orientation: landscape)',
            tablet_p : 'only screen and (min-width: 768px) and (max-width: 1019px) and (orientation: portrait)',
            tablet_l : 'only screen and (min-width: 768px) and (max-width: 1019px) and (orientation: landscape)'
          }
        }
      );

      $('.Slide').on('click', function (e) {
        e.preventDefault();
        var target = this.hash;
        var $target = $(target);
        $('html, body').stop().animate({
          'scrollTop': $target.offset().top
        }, 1400, 'swing', function () {
          //window.location.hash = target;
        });
      });

      $('#contact').on('click', '.form.success', hideSuccessfulMessage);

      //alert($(window).width() + ' ' + $(window).height());
      //alert($('html').attr('class'));
      //console.debug($('#carreerDetail .row').height());
      //console.debug($('.backgroundCover').height());

      validateForms();
    });


    /* scrollReveal init */
    /* ************************** */
    (function ($) {

      'use strict';

      window.sr = new scrollReveal({
        reset: false,
        delay: false,
        opacity: 0.35,
        move: '30px',
        mobile: false
      });

    })();


    /* CountTo Animation  Library */
    /* ************************** */
    var $countup = $('.countup');
    $countup.html("0");	
    var $firstItem = $($countup[0]);
    $win.on('scroll.counters', function () {

      //launch counters when they come to be visible)
      $countup.each(function (index, item) {
      	var $item = $(this);
      	var isVisibile = $firstItem.offset().top + $firstItem.height() <= ($win.scrollTop() + $win.height());
      	// && $item.offset().top >= $win.scrollTop();


      	if (isVisibile && !$item.hasClass('finished')) {
          $(this).countTo({
            from: 0,
            to: $(this).data('maxvalue'),
            speed: 2500,
            refreshInterval: 50,
            onComplete: function () {
            }
          }).addClass('finished');
        }
      });
    });


    /* Menu Scroll to Down Middle Animation velocity Library */
    /* ************************** */

    $("#buttonDown")
      .velocity({
        opacity: "0.5",
        translateY: -10
      }, {
        duration: 1000,
        delay: 500,
        loop: true
      });

    $("#buttonLeft")
      .velocity({
        opacity: "0.5",
        translateX: 10
      }, {
        duration: 1000,
        delay: 500,
        loop: true
      });


    /*
     Display weather widget ONLY on tablet and up
     or mobile landscape mode (iphone6, iphone6+, samsung s6,...)
     */
    var limited_height = 559;
    var weather_tablet = Foundation.utils.is_medium_up() && !$html.hasClass('iphone');
    var is_mobile = $html.hasClass('mobile');
    var weather_mobile = (is_mobile && $(window).height() >= limited_height);
    $(function () {
      var $window = $(window);

      $window.on('orientationchange', function () {
        $('#weatherWidget').hide();
        
        setTimeout(function() {
          weather_mobile = (is_mobile && $window.height() >= limited_height);
          if (weather_tablet || weather_mobile) {
            $('#weatherWidget').show();
          }
        }, 500);
      });
    });
    if (weather_tablet || weather_mobile) {
      $('#weatherWidget').show();
    }
    else {
      $('#weatherWidget').hide();
    }

    /* vh, vh hack vunit Bower library */
    /* ********************************************** */

    new vUnit({
      CSSMap: {
        // The selector (VUnit will create rules ranging from .selector1 to .selector100)
        '.vh_height': {
          // The CSS property (any CSS property that accepts px as units)
          property: 'height',
          // What to base the value on (vh, vw, vmin or vmax)
          reference: 'vh'
        },
        // Wanted to have a font-size based on the viewport width? You got it.
        '.vw_font-size': {
          property: 'font-size',
          reference: 'vmax'
        },
        // Wanted to have a font-size based on the viewport width? You got it.
        '.vw_line-height': {
          property: 'line-height',
          reference: 'vw'
        },
        // Wanted to have a font-size based on the viewport width? You got it.
        '.vmin_letterspacing': {
          property: 'letter-spacing',
          reference: 'vmin'
        },
        '.vmin_height': {
          property: 'height',
          reference: 'vmin'
        },
        // vmin and vmax can be used as well.
        '.vmin_margin-top': {
          property: 'margin-top',
          reference: 'vmin'
        }
      }
    }).init(); // call the public init() method


    /* Accordion Options slick-carousel Bower library */
    /* ********************************************** */

    $('.single-item').slick({
      dots: false,
      infinite: true,
      speed: 800,
      autoplay: true,
      slidesToShow: 5,
      adaptiveHeight: true,
      slidesToScroll: 1,
      responsive: [{
        breakpoint: 1440,
        settings: {
          slidesToShow: 5,
          slidesToScroll: 5,
          infinite: true,
          dots: false
        }
      }, {
        breakpoint: 1024,
        settings: {
          slidesToShow: 5,
          slidesToScroll: 5,
          infinite: true,
          arrows: false,
          dots: true
        }
      }, {
        breakpoint: 640,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 3,
          infinite: true,
          arrows: false,
          dots: true
        }
      }, {
        breakpoint: 490,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 2,
          infinite: true,
          arrows: false,
          dots: true
        }
      }]
    });


    /* Accordion Options slick-carousel Bower library */
    /* ********************************************** */

    $('.mac-item').slick({
      dots: false,
      infinite: true,
      speed: 300,
      autoplay: true,
      fade: true,
      slidesToShow: 1,
      slidesToScroll: 1,
      responsive: [{
        breakpoint: 1440,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
          infinite: true,
          dots: false
        }
      }, {
        breakpoint: 1024,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
          infinite: true,
          arrows: false,
          dots: true
        }
      }, {
        breakpoint: 640,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
          infinite: true,
          arrows: false,
          dots: true
        }
      }, {
        breakpoint: 480,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
          infinite: true,
          arrows: false,
          dots: true
        }
      }]
    });


    /* Video & picture main background */
    /* ********************************************** */

    var tpj = jQuery;
    var revapi46;
    tpj(document).ready(function () {
      if (tpj("#videoBack").revolution === undefined) {
      } else {
        revapi46 = tpj("#videoBack").show().revolution({
          sliderType: "standard",
          jsFileLocation: "",
          sliderLayout: "fullscreen",
          dottedOverlay: "none",
          delay: 100,
          navigation: {
            keyboardNavigation: "off",
            keyboard_direction: "horizontal",
            mouseScrollNavigation: "off",
            onHoverStop: "off",
            touch: {
              touchenabled: "on",
              swipe_threshold: 75,
              swipe_min_touches: 1,
              swipe_direction: "horizontal",
              drag_block_vertical: false
            }
          },
          responsiveLevels: [1920, 1440, 1024, 640],
          gridwidth: [1920, 1440, 1024, 640],
          gridheight: [868, 768, 960, 720],
          lazyType: "single",
          parallax: {
            type: "mouse",
            origo: "enterpoint",
            speed: 2000,
            levels: [2, 3, 4, 5, 6, 7, 12, 16, 10, 50],
            disable_onmobile: "on"
          },
          shadow: 0,
          spinner: "off",
          stopLoop: "on",
          stopAfterLoops: 0,
          stopAtSlide: 1,
          shuffle: "off",
          autoHeight: "off",
          fullScreenOffsetContainer: "",
          fullScreenOffset: "0px",
          disableProgressBar: "on",
          hideThumbsOnMobile: "off",
          hideSliderAtLimit: 0,
          hideCaptionAtLimit: 0,
          hideAllCaptionAtLilmit: 0,
          debugMode: false,
          fallbacks: {
            simplifyAll: "off",
            nextSlideOnWindowFocus: "off",
            disableFocusListener: false,
          }
        });
      }
    });


    /*  option searching &  ordering display - General */
    /* ************************** */
    $.extend(true, $.fn.dataTable.defaults, {
      "searching": true,
      "ordering": false
    });

    /*  Div Search field replacing */
    /* ************************** */
    function filterGlobal () {
      $('#tablecareer').DataTable().search(
        $('#global_filter').val()
      ).draw();
    }

    $('input.global_filter').on('keyup click', function () {
      filterGlobal();
    });

    /*  Coloring Rollover row  */
    /* ************************** */
    $(document).on({
      mouseenter: function () {
        var trIndex = $(this).index() + 1;
        $("table.dataTable").each(function () {
          $(this).find("tr:eq(" + trIndex + ")").addClass("hover");
        });
      },
      mouseleave: function () {
        var trIndex = $(this).index() + 1;
        $("table.dataTable").each(function () {
          $(this).find("tr:eq(" + trIndex + ")").removeClass("hover");
        });
      }
    }, ".dataTables_wrapper tr");


    /*  General INIT */
    /* ************************** */
    var $DataTable = $('#tablecareer').DataTable({
      /*   GENERAL options */
      "paging": true,
      "pagingType": "simple_numbers",
      "ordering": true,
      "info": true,
      "bAutoWidth": false,
      "bLengthChange": false,
      "oLanguage": {
        "sInfo": " Page <b>_PAGE_ / _PAGES_</b>",
        "sInfoEmpty": 'Page <b>0 / 0</b>',
        "sEmptyTable": 'There are no data to display',
        "sZeroRecords": 'There are no data to display',
        "sInfoFiltered": '',
        "oPaginate": {
          "sFirst": "First page", // This is the link to the first page
          "sPrevious": " ", // This is the link to the previous page
          "sNext": " ", // This is the link to the next page
          "sLast": "Last page", // This is the link to the last page
        }
      },
      /*  filter & pagination init*/
      "sDom": '<"row view-filter"<"small-8 large-8 columns"<"pull-left"l><"pull-right"f><"clearfix">>>t<"row view-pager"<"small-8 small-centered large-4 large-centered columns"<"text-center"ip>>>',
      "aoColumnDefs": [
        {"sWidth": "10%", "aTargets": [0]},
        {type: "alt-string", "aTargets": [0]},
        {"sWidth": "15%", "aTargets": [1]},
        {"sWidth": "32%", "aTargets": [2], className: "my_class"},
        {"sWidth": "12%", "aTargets": [3]},
        {"sWidth": "14%", "aTargets": [4]},
        {"sWidth": "10%", "aTargets": [5]},
        {"sWidth": "7%", "aTargets": [6], "bSortable": false},
      ],
      /*  Reorganize element on medium - small screen*/
      responsive: {
        breakpoints: [
          {name: 'desktop', width: Infinity},
          {name: 'tablet', width: 1000},
          {name: 'fablet', width: 768},
          {name: 'phone', width: 480}
        ],
        details: {
          type: 'column',
          target: 'tr',
          renderer: function (api, rowIdx, columns) {
            var datas0;
            var datas2;
            var datas3;
            var datas4;
            var datas5;
            var datas6;
            var temp;
            var data = $.map(columns, function (col, i) {
              switch (i) {
                case 0:
                  datas0 = '<div class="small-8 text-center column content' + i + 'Right">' + col.data + '</div>';
                  break;
                case 2:
                  datas2 = '<div class="small-8 column content' + i + 'Right">' + col.data + '</div>';
                  return col.hidden ? datas2 : '';
                case 3:
                  var datas3alt = '<div class="small-2 column text-left titlecarreer content' + i + 'Left">' + col.title + '</div></div></div>';
                  datas3 = '<div class="small-2 column text-left content' + i + 'Right">' + col.data + '</div></div></div>';
                  return col.hidden ? datas3alt : '';
                case 4:
                  var datas4alt = '<div class="small-3 column text-left titlecarreer content' + i + 'Left">' + col.title + '</div> ';
                  datas4 = '<div class="small-3 column text-left content' + i + 'Right">' + col.data + '</div>';
                  return col.hidden ? datas4alt : '';
                case 5:
                  var datas5alt = '<div class="row"><div class="columns small-8 medium-6 medium-centered"><div class="small-3 column text-left titlecarreer content' + i + 'Left">' + col.title + '</div> ';
                  datas5 = '<div class="row"><div class="columns small-8 medium-6 medium-centered"><div class="small-3 column text-left content' + i + 'Right">' + col.data + '</div>';
                  return col.hidden ? datas5alt : '';
                case 6:
                  datas6 = '<div class="small-8 text-center column content' + i + 'Right">' + col.data + '</div>';
                  return col.hidden ? datas5 + datas4 + datas3 + datas0 + datas6 : '';
              }
            });

            temp = data[1];
            data[1] = data[3];
            data[3] = temp;
            data = data.join('');

            return data ? $('<div class="row"/>').append(data) : false;
          }
        }
      },
      /*   inject select box searching in the proper div */
      initComplete: function () {
        this.api().columns().every(function () {
          var column = this;
          var select;
          switch (column.index()) {
            case 0:
              select = $('<select><option selected="selected" value="">All divisions</option></select>')
                .appendTo($('#division').empty())
                .on('change', function () {
                  var val = $.fn.dataTable.util.escapeRegex(
                    $(this).val()
                  );

                  column
                    .search(val)
                    .draw();
                });
              column.data().unique().sort().each(function (d) {

                var link = $(d);
                var text = link.text();
                //alert(JSON.stringify( text, null, 18));
                select.append('<option value=' + text + '>' + text + '</option>');
              });
              break;
            case 3:
              select = $('<select><option selected="selected" value="">All types</option></select>')
                .appendTo($('#jobtype').empty())
                .on('change', function () {
                  var val = $.fn.dataTable.util.escapeRegex(
                    $(this).val()
                  );

                  column
                    .search(val ? '^' + val + '$' : '', true, false)
                    .draw();
                });
              column.data().unique().sort().each(function (d) {
                select.append('<option value="' + d + '">' + d + '</option>');
              });
              break;
            case 4:
              select = $('<select><option selected="selected" value="">All locations</option></select>')
                .appendTo($('#location').empty())
                .on('change', function () {
                  var val = $.fn.dataTable.util.escapeRegex(
                    $(this).val()
                  );

                  column
                    .search(val ? '^' + val + '$' : '', true, false)
                    .draw();
                });
              column.data().unique().sort().each(function (d) {
                select.append('<option value="' + d + '">' + d + '</option>');
              });
              break;
          }
        });
      }
    });


    $DataTable.on('search', function (e, settings) {
      var $pager = $('.tableSearch .view-pager');
      if (settings.aiDisplay.length > 0) {
        $pager.show();
      } else {
        $pager.hide();
      }

    });
  };
  return expose;
})(jQuery);


/*************
 * END MODULE *
 **************/
//Launch & Initialize

$(document).ready(function () {
  if ($('html').hasClass('android-jerky')) {
    var $window = $(window);
    var timerChange;
    // Viewport Unit Buggy Fill
    viewportUnitsBuggyfill.init({force: true, refreshDebounceWait: 86400000 });
    viewportUnitsBuggyfill.refresh();

    var cssText = viewportUnitsBuggyfill.getCss();
    console.log(cssText);

    $window.on('orientationchange', function(){
      clearTimeout(timerChange);
      timerChange = setTimeout(viewportUnitsBuggyfill.refresh, 500);
    });
    // End of Viewport Unit Buggy Fill

    var $fullScreenBlock = $('#fullScreenBlock');

    $window.on('scroll', function () {
      if($window.scrollTop() > $window.height() + 100) {
        $fullScreenBlock.addClass('scrolled');
      } else {
        $fullScreenBlock.removeClass('scrolled');
      }
    });
  }

  var iphone6plus = ($('html').hasClass('iphone') && $(window).width() === 736);
  var iphone6plus_landscape = (iphone6plus && $('html').hasClass('landscape'));
  Pyco.Initialize();
  if (is_android_native) {
    $('html').addClass('isAndroidNative');
  }

  // Landscape vs Portrait scroll difference
  var LP_SCROLL_DIFFERENCES = {
    'iphone': 16
  };

  var dimension = {
    height: 0,
    decoratorHeight: 0
  };

  //Start: Check for resize and open/close tab on homepage ****************
  if ($("html").hasClass("touch") && $(".menu404").length == 0) {
    implementResizeAndTabFunc();
  }

  function implementResizeAndTabFunc () {
    console.debug('PageInit.js [implementResizeAndTabFunc] Document visibility state:', document.visibilityState);

    var $window = $(window),
        $head = $("#head"),
        $textIntro = $("#textIntro"),
        $downBtn = $("#DownButton");

    var hidden, visibilityChange;
    var originalScrollTop;

    measureHeight();
    setHeight();
    //if (!iphone6plus_landscape) {
    //  setHeight();
    //}

    //$window.scroll(Foundation.utils.throttle(function () {
    //  if ($window.scrollTop() === 0) {
    //    setHeight();
    //  }
    //}, 300));


    $window.on('orientationchange', Foundation.utils.throttle(function () {
      // Do responsive stuff
      if ($('html').hasClass('ios')) {
        measureHeight();
        setHeight();
      }
    }, 500));

    if (typeof document.hidden !== "undefined") { // Opera 12.10 and Firefox 18 and later support
      hidden = "hidden";
      visibilityChange = "visibilitychange";
    } else if (typeof document.mozHidden !== "undefined") {
      hidden = "mozHidden";
      visibilityChange = "mozvisibilitychange";
    } else if (typeof document.msHidden !== "undefined") {
      hidden = "msHidden";
      visibilityChange = "msvisibilitychange";
    } else if (typeof document.webkitHidden !== "undefined") {
      hidden = "webkitHidden";
      visibilityChange = "webkitvisibilitychange";
    }

    document.addEventListener(visibilityChange, handleVisibilityChange, false);

    function handleVisibilityChange () {
      console.debug('PageInit.js [handleVisibilityChange]: Document visibility state:', document.visibilityState)

      if (document[hidden]) {
      }
      else {

        if($('html').hasClass('ios') && $('html').hasClass('landscape')) {
          console.log(bodyHeight);
          if(bodyHeight === 337) {
            $('html').addClass('iphone6have2tab');
          } else {
            $('html').removeClass('iphone6have2tab');
          }
        }
      }
    }

    function setHeight () {
      console.debug('PageInit.js [setHeight] Setting the height...');

      var $window = $(window);

      var bodyHeight = $window[0].innerHeight,
          headHeight = $head.outerHeight(),
          downBtnHeight = $downBtn.outerHeight(),
          fullScreenBlock = $('#fullScreenBlock');

      $textIntro.css("height", bodyHeight - headHeight - downBtnHeight + 1);

      if ($('html').hasClass('ios')) {
        fullScreenBlock.height(bodyHeight);

        if( $('html').hasClass('landscape')) {
          console.log(bodyHeight);
          if(bodyHeight === 337) {
            $('html').addClass('iphone6have2tab');
          } else {
            $('html').removeClass('iphone6have2tab');
          }
        }

      }

      //iphone 6 plus landscape 2 tabs

    }

    //End: Check for resize and open/close tab on homepage ****************
  }

  function getOrientation () {
    return (document.orientation === 90 || document.orientation === -90)?'landscape':'portrait';
  }

  function getScreenHeight () {
    if (getOrientation() === 'landscape') {
      return screen.width;
    }
    else {
      return screen.height;
    }
  }

  function measureHeight () {
    var $window = $(window);

    if (Modernizr.ios9) {
      dimension.decoratorHeight = 39;

      dimension.height = getScreenHeight() - dimension.decoratorHeight;
    }
    else {
      dimension.decoratorHeight = 0;
      dimension.height = $window.height();
    }
  }
});

(function ($) {

  //995 - Jerky Android
  var $window = $(window);
  var ORIENTATIONCHANGE_DELAY = 600;
  var ORIENTATIONCHANGE_TIMER = 0;
  var $headRowVHheight35 = $('#head .vh_height35'),
      $headRowVHheight10 = $('#head .vh_height10'),
      $logoIntroRowVHheight = $('#logoIntro .vh_height37'),
      $CaseDownButtonRowVHheight = $('#CaseDownButton .vh_height32'),
      $carreerDetailRowVHheight = $('#carreerDetail .vh_height50'),
      $backgroundCover = $('.backgroundCover'),
      $detailCover = $('.detail-cover'),
      $textIntro = $('#textIntro'),
      $textRowVHheight55 = $('#textIntro .vh_height55'),
      $downButtonRowVHheight12 = $('#DownButton .vh_height12'),
      $fullScreenBlock = $('#fullScreenBlock'),
      $textMiddleRowVHheight30 = $('#textMiddle .vh_height30');


  function jerkyAndroidPolyfill() {
    //return false;
    if(!$('html').hasClass('android')) return false;

    console.log("jerkyAndroidPolyfill");

    var heightTextIntro = 0;

    //reset style
    $headRowVHheight35.attr('style', '');
    $headRowVHheight10.attr('style', '');
    $CaseDownButtonRowVHheight.attr('style', '');

    $carreerDetailRowVHheight.attr('style', '');
    $logoIntroRowVHheight.attr('style', '');
    $textIntro.attr('style', '');
    $textRowVHheight55.attr('style', '');
    $downButtonRowVHheight12.attr('style', '');

    $fullScreenBlock.css('height', 'auto');
    $backgroundCover.css('height', 'auto');
    $detailCover.css('height', 'auto');
    $textMiddleRowVHheight30.attr('style', '');

    //re-calulate
    $CaseDownButtonRowVHheight.css('height', $CaseDownButtonRowVHheight.outerHeight());
    $carreerDetailRowVHheight.css('height', $carreerDetailRowVHheight.outerHeight());
    $headRowVHheight35.css('height', $headRowVHheight35.outerHeight());
    $headRowVHheight10.css('height', $headRowVHheight10.outerHeight());
    $backgroundCover.css('height', $backgroundCover.height() + 20);
    $logoIntroRowVHheight.css('height', $logoIntroRowVHheight.outerHeight());
    $textRowVHheight55.css('height', $textRowVHheight55.outerHeight());
    $downButtonRowVHheight12.css('height', $downButtonRowVHheight12.outerHeight());

    heightTextIntro = $window.height() - $headRowVHheight35.outerHeight() - $downButtonRowVHheight12.outerHeight() + 1;
    $textIntro.css('height', heightTextIntro);

    $textMiddleRowVHheight30.css('height', $textMiddleRowVHheight30.outerHeight());

    if($detailCover.length) {
      $detailCover.css('height', 'auto');
      $detailCover.css('height', $('#head').outerHeight() + $('#carreerDetail').outerHeight());
    }


    $fullScreenBlock.css({
      'height': $window.height(),
      'bottom': 'initial'
    });

  }

  $(document).on('ready', function () {
    jerkyAndroidPolyfill();
  });


  $window.on(' orientationchange', function () {
    clearTimeout(ORIENTATIONCHANGE_TIMER);
    ORIENTATIONCHANGE_TIMER = setTimeout(function() {
      jerkyAndroidPolyfill();

    }, 500);

  });

  $window.on('scroll', function () {
    if($window.scrollTop() > $window.height() + 100) {
      $fullScreenBlock.addClass('isScrolling');
    } else {
      $fullScreenBlock.removeClass('isScrolling');
    }
  });
})(jQuery);


(function ($) {
  //Implement same height function for digital page
  var $window = $(window);

  if ($(".digital-table").length > 0) {
    window.addEventListener("load", function () {
      specificSameHeight();
      window.setTimeout(function () {
        specificSameHeight();
      }, 2000);
      window.setTimeout(function () {
        specificSameHeight();
      }, 10000);

      $window.resize(function () {
        specificSameHeight();
      });
    });
  }

  function specificSameHeight () {

    if ($window[0].innerWidth >= 1020) {
      setSameHeight("same-height-3");
      setSameHeight("same-height-6");
    }
    else {

      setSameHeight("same-height-1");
      setSameHeight("same-height-2");
      setSameHeight("same-height-4");
      setSameHeight("same-height-5");
    }
  }

  function setSameHeight (className) {
    var $cl = $("." + className);

    var maxHeight = 0;

    $cl.css({"height": "initial"});

    $cl.each(function (i) {
      var $el = $cl.eq(i),
        elHeight = $el.outerHeight();
      maxHeight = maxHeight < elHeight ? maxHeight = elHeight : maxHeight;
    });

    $cl.css("height", maxHeight);

  }
})(jQuery);

var nua = navigator.userAgent;
var is_android_native = (nua.indexOf('Mozilla/5.0') > -1 &&
nua.indexOf('Android ') > -1 &&
nua.indexOf('Chrome') == -1 &&
nua.indexOf('AppleWebKit') > -1);