var MIN_DISTANCE = 8; //捕获的最小距离

var zoom = 1;

var guides = []; // 没有可用的引导

var innerOffsetX, innerOffsetY;

var elmOffsetX, elmOffsetY, absOffsetX, absOffsetY;

var elm_pos = null;

var draggable = {

    _this: null,

    start: function (event) {

        var t = draggable._this = window.moving_block;

        guides = $.map($(".draggable").not([this._this]), computeGuidesForElement);

        elmOffsetX = event.clientX - util.getPos(t).offset().left;

        elmOffsetY = event.clientY - util.getPos(t).offset().top;

        //包含canvasOffset
        innerOffsetX = util.getPos(t).offset().left - util.getPos(t).position().left;
        //包含canvasOffset
        innerOffsetY = util.getPos(t).offset().top - util.getPos(t).position().top;

    },

    drag: function (event) {

        // 迭代所有的guids，记住最近的h和v guids

        var guideV, guideH, distV = MIN_DISTANCE + 1, distH = MIN_DISTANCE + 1, offsetV, offsetH;

        var chosenGuides = {top: {dist: MIN_DISTANCE + 1}, left: {dist: MIN_DISTANCE + 1}};

        var $t = $(this._this);

        // pageX、pageY：文档坐标x、y ;
        var pos = elm_pos = {
            top: event.pageY - innerOffsetY - elmOffsetY,
            left: event.pageX - innerOffsetX - elmOffsetX
        };


        //outerHeight、outerWidth：整个浏览器的高度、宽度

        var w = $t.outerWidth();

        var h = $t.outerHeight();

        var pos_abs = {top: event.pageY - elmOffsetY, left: event.pageX - elmOffsetX}

        var elemGuides = computeGuidesForElement(null, pos, w, h);

        $.each(guides, function (i, guide) {

            $.each(elemGuides, function (i, elemGuide) {

                if (guide.type == elemGuide.type) {

                    var prop = guide.type == "h" ? "top" : "left";

                    var d = Math.abs(elemGuide[prop] - guide[prop]);


                    if (d < chosenGuides[prop].dist) {

                        chosenGuides[prop].dist = d;

                        chosenGuides[prop].offset = elemGuide[prop] - pos[prop];

                        chosenGuides[prop].guide = guide;

                    }

                }

            });

        });

        if (chosenGuides.top.dist <= MIN_DISTANCE) {

            var ref_top = chosenGuides.top.guide.top - chosenGuides.top.offset;

            $(this._this).css({top: ref_top});

            elm_pos.top = ref_top;

            var guide_elm = chosenGuides.top.guide.owner;
            var cur_elm = this._this;
            var width = Math.abs(util.getPos(guide_elm).position().left - util.getPos(cur_elm).position().left) + w;
            var guide_left = util.getPos(guide_elm).position().left;
            var cur_left = util.getPos(cur_elm).position().left;
            var abs_left = (guide_left < cur_left ? guide_left : cur_left);
            $("#guide-h").css({"top": chosenGuides.top.guide.top, "width": width, "left": abs_left}).show();
        }

        else {

            $("#guide-h").hide();

            $(this._this).css({top: pos.top});

        }

        if (chosenGuides.left.dist <= MIN_DISTANCE) {

            var ref_left = chosenGuides.left.guide.left - chosenGuides.left.offset;
            $(this._this).css({left: ref_left});

            elm_pos.left = ref_left;

            var guide_elm = chosenGuides.left.guide.owner;
            var cur_elm = this._this;
            var height = Math.abs(util.getPos(guide_elm).position().top - util.getPos(cur_elm).position().top) + h;
            var guide_top = util.getPos(guide_elm).position().top;
            var cur_top = util.getPos(cur_elm).position().top;
            var abs_top = (guide_top < cur_top ? guide_top : cur_top);
            $("#guide-v").css({"left": chosenGuides.left.guide.left, height: height, "top": abs_top}).show();
        }

        else {

            $("#guide-v").hide();

            $(this._this).css({left: pos.left});


        }

    },

    stop: function (event) {

        $("#guide-v, #guide-h").hide();

        window.moving_block = null;

        elm_pos = null;

    }

};


//$(".draggable").draggable(draggable_param);


function computeGuidesForElement(elem, pos, w, h) {

    if (elem != null) {

        var $t = $(elem);

        //offset:返回当前元素 的偏移量

        pos = util.getPos($t).position();

        w = $t.outerWidth() - 1;

        h = $t.outerHeight() - 1;

    }
    // console.log((elem ? elem.id : '') + '--->' + pos.left)
    // console.log((elem ? 'jsabs-->' + util.getPos($t).offset().left : '--'))
    // console.log((elem ? 'jqabs-->' + $t.offset().left : '--'))
    // console.log((elem ? 'jsposition-->' + elem.offsetLeft : '--'))
    // console.log((elem ? 'jqposition-->' + $t.position().left : '--'))
    //
    //
    // console.log((elem ? elem.id : '') + 'w--->' + w)
    // console.log((elem ? $(elem).prop('outerHTML') : '') + 'w--->' + w)


    /*   pos.left *= zoom;
       pos.top *= zoom;
       w *= zoom;
       h *= zoom;*/


    return [

        {type: "h", left: pos.left, top: pos.top, owner: elem}, //垂直方向左下对齐线

        {type: "h", left: pos.left, top: pos.top + h, owner: elem},

        {type: "v", left: pos.left, top: pos.top, owner: elem},

        {type: "v", left: pos.left + w, top: pos.top, owner: elem},

        //您可以添加_any_其他指南在这里就好了（如指南10像素单元的左）

        {type: "h", left: pos.left, top: pos.top + h / 2, owner: elem},

        {type: "v", left: pos.left + w / 2, top: pos.top, owner: elem}

    ];

}