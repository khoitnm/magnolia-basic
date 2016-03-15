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
 * General stylesheet weather & randow video
 * Project: Pyco WebSite 
 * Version: 1
 *
 * author Vincent Genevoix - vincent.genevoix@pycogroup.com
 *
 */
var baseUrl = baseUrl || '';
var Pyco = Pyco || {};
Pyco.CurrentPage = Pyco.Homepage = (function ($) {

  var expose = {};//This will contain all "public" properties and methods of this module
  var Utils = {};

  Utils.GetDaylightSavingsPeriod = function (iType, iYear) {
    var returnValue = {};

    switch (iType) {
      case 1://Europe summertime ? Last Sunday of March => Last Sunday of October
        //Find last Sunday of March
        var start = new Date(iYear, 2, 1);
        var firstMarchSunday = start.getDay() === 0 ? 1 : (6 - start.getDay());
        var lastMarchSunday = (firstMarchSunday + 28) > 31 ? firstMarchSunday + 21 : firstMarchSunday + 28;
        var lastMarchSundayDate = new Date(iYear, 2, lastMarchSunday);
        returnValue.start = lastMarchSundayDate;
        //Find last Sunday of October
        start = new Date(iYear, 9, 1);
        var firstOctoberSunday = start.getDay() === 0 ? 1 : (6 - start.getDay());
        var lastOctoberSunday = (firstOctoberSunday + 28) > 31 ? firstOctoberSunday + 21 : firstOctoberSunday + 28;
        var lastOctoberSundayDate = new Date(iYear, 9, lastOctoberSunday);
        returnValue.end = lastOctoberSundayDate;
        break;
      case 2://USA summertime ? 2nd Sunday of March => 1st Sunday of November
        //Find second Sunday of March
        start = new Date(iYear, 2, 7);//Start to search from last date being possibly the first sunday
        var secondMarchSunday = 7 + (7 - start.getDay());
        var secondMarchSundayDate = new Date(iYear, 2, secondMarchSunday);
        returnValue.start = secondMarchSundayDate;
        //Find first Sunday of November
        start = new Date(iYear, 10, 1);
        var firstNovemberSunday = start.getDay() === 0 ? 1 : (6 - start.getDay());
        var firstNovemberSundayDate = new Date(iYear, 10, firstNovemberSunday);
        returnValue.end = firstNovemberSundayDate;
        break;
    }
    return returnValue;
  };

  var LoadHeader = function () {
    var selectedCity = Cities.Init(); //select random city
    var $weather = $('#weatherWidget');
    var $weatherHead = $weather.find('h3');
    var today = new Date();
    var curCity, $locationItem;

    /* Carousel init */
    /* Update HTML: link on widget */
    $weather.attr("href", "#" + selectedCity.identifier);
    /* Update HTML: city name */
    $weatherHead.attr('data-city', selectedCity.name).html("PYCO " + selectedCity.name);
    var todayUtc = new Date(today.getUTCFullYear(), today.getUTCMonth(), today.getUTCDate(), today.getUTCHours(), today.getUTCMinutes(), today.getUTCSeconds());
    var selectedCityTime = new Date(todayUtc.getTime() + (selectedCity.utcOffset * 60 * 60 * 1000));
    var hours = selectedCityTime.getHours();
    var minutes = selectedCityTime.getMinutes();
    var ampm = "<small>" + (hours >= 12 ? 'PM' : 'AM') + "</small>";
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    minutes = minutes < 10 ? '0' + minutes : minutes;
    var formattedTime = hours + ':' + minutes + ampm;

    $weatherHead.on('click', function () {
      curCity = $(this).attr('data-city');
      $locationItem = $('.our-location').find('li[data-city="' + curCity + '"]');
      $('html, body').animate({
        scrollTop: $locationItem.offset().top
      }, 2000)
    });

    $("#timerBlock").html(formattedTime);
    /* Update HTML: weather */
    $.simpleWeather({
      location: selectedCity.name,
      woeid: '',
      unit: 'f',
      success: function (weather) {
        var $weatherControl = $("#weather");
        $weatherControl.html('<small>' + (weather.currently.toLowerCase() !== "unknown" ? weather.currently : weather.text) + '</small><br><i class="icon-' + (weather.code === "3200" ? weather.todayCode : weather.code) + '"></i>');
        $weatherControl.fadeIn();
      },
      error: function (error) {
        $("#weatherWidget").html('<p>' + error + '</p>');
      }
    });
    /* Update HTML: video */
    //alert(selectedCity.backgroundImg);
    /*$('#videoBack .rs-background-video-layer').attr("data-videomp4",""+selectedCity.videoUrl+"");
     $(".rev-slidebg").attr("href",selectedCity.backgroundImg);
     $('#videoBack .rs-background-video-layer').attr("data-videoposter",""+selectedCity.backgroundImg+"");

     //jQuery('#sliderid').revredraw();  */
    $("#fullScreenBlock").css("background-image", "url(" + selectedCity.backgroundImg + ")");
    if ($(window).width() > 765 && !Modernizr.touch) {
      $("#fullScreenBlock").html('<video loop muted autoplay poster="' + selectedCity.backgroundImg + '" class="fullscreen-bg__video"><source src="' + selectedCity.videoUrl + '" type="video/mp4"></video>').addClass(selectedCity.identifier);
    }


    //$("#videoBack .rs-background-video-layer").css("background-image", "url("+selectedCity.backgroundImg+")");

    //$("#fullScreenBlock").html('<video loop muted autoplay poster="img/video_bg/'+selectedCity.identifier+'.jpg" class="fullscreen-bg__video"><source src="'+selectedCity.videoUrl+'" type="video/mp4"></video>').addClass(selectedCity.identifier);
  };

  expose.Initialize = function () {
    LoadHeader();
  };

  /********************
   * Cities management *
   ********************/

  var Cities = {};
  Cities.allItems = [];

  //Create the city object for easier use later
  Cities.PushCity = function (sCityName, sPictureUrl, sVideoUrl, sIdentifier, sGMTSpan, iDaylightSavingType) {
    Cities.allItems.push({
      name: sCityName,
      backgroundImg: sPictureUrl,
      videoUrl: sVideoUrl,
      identifier: sIdentifier,
      utcOffset: sGMTSpan,
      daylightSavingType: iDaylightSavingType //0 -> No Daylight saving time , 1 -> European Daylight saving time, 2-> USA Daylight saving time
    });
  };

  //This builds the cities list, select one randomly and return the object after adding the utc time offset, taking into account Daylight savings time
  Cities.Init = function () {
    if ($(window).width() < 1024) {
      /* Create array of objects; This is where you edit cities settings to be used later */
      Cities.PushCity("Paris", baseUrl + "asset/img/imgVideo/mobile/Static_0005_Paris.jpg", "http://az837324.vo.msecnd.net/cdn/video/LowDef/Paris.mp4", "paris", 1, 1);
      Cities.PushCity("Brussels", baseUrl +  "asset/img/imgVideo/mobile/Static_0002_Brussels.jpg", "http://az837324.vo.msecnd.net/cdn/video/LowDef/Brussels.mp4", "brussels", 1, 1);
      Cities.PushCity("Ho Chi Minh City", baseUrl +  "asset/img/imgVideo/mobile/Static_0000_HoChiMinCity.jpg", "http://az837324.vo.msecnd.net/cdn/video/LowDef/Ho_Chi_min_City.mp4", "hochimincity", 7, 0);
      Cities.PushCity("Geneva", baseUrl +  "asset/img//imgVideo/mobile/Static_0001_Geneve.jpg", "http://az837324.vo.msecnd.net/cdn/video/LowDef/Geneve.mp4", "geneva", 1, 1);
      Cities.PushCity("Johannesburg", baseUrl +  "asset/img/imgVideo/mobile/Static_0007_Johannesburg.jpg", "http://az837324.vo.msecnd.net/cdn/video/LowDef/Johannesburg.mp4", "johannesburg", 2, 0);
      Cities.PushCity("Hong Kong", baseUrl +  "asset/img/imgVideo/mobile/Static_0008_HongKong.jpg", "http://az837324.vo.msecnd.net/cdn/video/LowDef/Hong_Kong.mp4", "hongkong", 8, 0);
      Cities.PushCity("New York", baseUrl +  "asset/img/imgVideo/mobile/Static_0006_NewYork.jpg", "http://az837324.vo.msecnd.net/cdn/video/LowDef/New_York.mp4", "newyork", -5, 2);
      Cities.PushCity("Seattle", baseUrl +  "asset/img/imgVideo/mobile/Static_0004_Seattle.jpg", "http://az837324.vo.msecnd.net/cdn/video/LowDef/Seattle.mp4", "seattle", -8, 2);
      Cities.PushCity("Bangkok", baseUrl +  "asset/img/imgVideo/mobile/Static_0003_Bangkok.jpg", "http://az837324.vo.msecnd.net/cdn/video/LowDef/Bangkok.mp4", "bangkok", 7, 0);
    }
    else {
      /* Create array of objects; This is where you edit cities settings to be used later */
      Cities.PushCity("Paris", "http://az837324.vo.msecnd.net/cdn/imgVideo/Static_0005_Paris.jpg", "http://az837324.vo.msecnd.net/cdn/video/HighDef/Paris.mp4", "paris", 1, 1);
      Cities.PushCity("Brussels", "http://az837324.vo.msecnd.net/cdn/imgVideo/Static_0002_Brussels.jpg", "http://az837324.vo.msecnd.net/cdn/video/HighDef/Brussels.mp4", "brussels", 1, 1);
      Cities.PushCity("Ho Chi Minh City", "http://az837324.vo.msecnd.net/cdn/imgVideo/Static_0000_HoChiMinCity.jpg", "http://az837324.vo.msecnd.net/cdn/video/HighDef/Ho_Chi_min_City.mp4", "hochimincity", 7, 0);
      Cities.PushCity("Geneva", "http://az837324.vo.msecnd.net/cdn/imgVideo/Static_0001_Geneve.jpg", "http://az837324.vo.msecnd.net/cdn/video/HighDef/Geneve.mp4", "geneva", 1, 1);
      Cities.PushCity("Johannesburg", "http://az837324.vo.msecnd.net/cdn/imgVideo/Static_0007_Johannesburg.jpg", "http://az837324.vo.msecnd.net/cdn/video/HighDef/Johannesburg.mp4", "johannesburg", 2, 0);
      Cities.PushCity("Hong Kong", "http://az837324.vo.msecnd.net/cdn/imgVideo/Static_0008_HongKong.jpg", "http://az837324.vo.msecnd.net/cdn/video/HighDef/Hong_Kong.mp4", "hongkong", 8, 0);
      Cities.PushCity("New York", "http://az837324.vo.msecnd.net/cdn/imgVideo/Static_0006_NewYork.jpg", "http://az837324.vo.msecnd.net/cdn/video/HighDef/New_York.mp4", "newyork", -5, 2);
      Cities.PushCity("Seattle", "http://az837324.vo.msecnd.net/cdn/imgVideo/Static_0004_Seattle.jpg", "http://az837324.vo.msecnd.net/cdn/video/HighDef/Seattle.mp4", "seattle", -8, 2);
      Cities.PushCity("Bangkok", "http://az837324.vo.msecnd.net/cdn/imgVideo/Static_0003_Bangkok.jpg", "http://az837324.vo.msecnd.net/cdn/video/HighDef/Bangkok.mp4", "bangkok", 7, 0);
    }

    /* Select a random city */
    var randomIndex = new Date().getSeconds() % Cities.allItems.length;
    var selectedCity = Cities.allItems[randomIndex];
    /* Calculate proper UTC offset of selected city keeping daylight savings time. supposing local date is correct. */
    var today = new Date();

    var cityUtcOffset = selectedCity.utcOffset;
    if (selectedCity.daylightSavingType > 0) {
      var dstPeriod = Utils.GetDaylightSavingsPeriod(selectedCity.daylightSavingType, today.getFullYear());
      if (today >= dstPeriod.start && today <= dstPeriod.end) {
        cityUtcOffset += 1;
      }
    }
    selectedCity.utcOffset = cityUtcOffset;

    return selectedCity;
  };

  return expose;

})(jQuery);

/*************
 * END MODULE *
 **************/
//This launches the magic :-)
$(document).ready(function () {
  Pyco.CurrentPage.Initialize();
});