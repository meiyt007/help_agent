<template>
  <div class="video-gallery" :style="{padding: padding + 'px 0px'}">
    <div v-for="(row, index) in rows" :key="index" class="video-row" v-bind:style="{height: itemSize.height + 'px', 'margin-top': (index > 0 ? gap : 0) + 'px'}">
      <Video class="video-col" v-for="(col, i) in row" :key="col.getStreamId()" :ref="col.getStreamId()"  v-bind:style="{width: itemSize.width + 'px', height: itemSize.height + 'px', 'margin-left': (i > 0 ? gap : 0) + 'px'}" :stream="col"/>
    </div>
  </div>
</template>

<script>

import Video from './Video'

function createGroup (array, groupSize) {
  var groups = [ ];
  array.forEach((item, index) => {
    index = Math.floor(index / groupSize);
    if (!groups[index]) {
      groups[index] = [ ];
    }
    groups[index].push(item);
  })
  return groups;
};

export default {
  name: 'VideoGallery',

  props: {
    streamList: {
      type: Array,
      default: function () {
        return []
      }
    },

    gap: {
      type: Number,
      default: 1
    },

    ratio: {
      type: Number,
      default: 16 / 9
    },

    maxCols: {
      type: Number,
      default: 5
    },
    maxRows: {
      type: Number,
      default: 5
    },

    width: {
      type: Number,
      require: true
    },

    height: {
      type: Number,
      require: true
    }
  },

  data () {

    return {
      
    }
  },

  filters: {
  },

  computed: {
    rows: function () {
      let me = this;
      let maxRows = this.maxRows;
      let maxCols = this.maxCols;
      let userVisible = this.streamList;
      userVisible = userVisible.slice();

      // 尽量排满，先确定每行排几个
      let colCount;
      let rowCount = 1;
      let length = userVisible.length;

      if (!length) {
        return [];
      }

      function sort(maxRows, maxCols) {
        while (true) {
          colCount = Math.ceil(length / rowCount);
          if (colCount <= maxCols) {
            if (maxRows === maxCols) {
              if (colCount - rowCount < 2) {
                break;
              }
            }
            else {
              break;
            }
          }
          rowCount++;
        }
      }

      if (maxCols > 3
        && maxRows > 3
        && length <= 9
      ) {
        sort(3, 3);
      }
      else if (maxCols > 4 
        && maxRows > 4
        && length <= 16
      ) {
        sort(4, 4);
      }
      else {
        sort(maxRows, maxCols);
      }
      return createGroup(userVisible, colCount);
    },

    itemSize: function () {
      let ratio = this.ratio;
      let itemWidth;
      let itemHeight;

      let containerWidth = this.width;
      let containerHeight = this.height;
      let gap = this.gap;

      let rowCount = this.rows.length;
      if (rowCount > 1) {
        containerHeight -= gap * (rowCount - 1);
      }

      let colCount = this.rows[0] && this.rows[0].length || 0;
      if (colCount > 1) {
        containerWidth -= gap * (colCount - 1);
      }

      itemHeight = containerHeight / rowCount;

      itemWidth = ratio * itemHeight;

      if (itemWidth * colCount > containerWidth) {
        itemWidth = containerWidth / colCount;
      }
      // 纠正一下 height
      itemHeight = itemWidth / ratio;

      return {
        width: Math.floor(itemWidth),
        height: Math.floor(itemHeight)
      };
    },

    padding: function () {
      let rowCount = this.rows.length;
      let containerHeight = this.height;
      let itemHeight = this.itemSize.height;
      return Math.floor((containerHeight - itemHeight * rowCount - (rowCount - 1) * this.gap) / 2)
    }
  },

  methods: {
    getInstance: function (id) {
      let video = this.$refs[id]
      if (Array.isArray(video)) {
        return video[0]
      }
      else {
        return video
      }
    }
  },

  mounted: function () {

  },

  beforeDestroy: function () {
    
  },

  components: {
    Video
  }
}
</script>

<style lang="less">
  .video-gallery {
    height: 100%;
  }
</style>
