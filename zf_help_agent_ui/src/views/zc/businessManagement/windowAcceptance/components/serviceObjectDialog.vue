<template>
  <el-dialog
    title="服务对象选择"
    :visible.sync="visible"
    custom-class="service-object-dialog"
    width="550px"
    append-to-body
  >
    <div class="service-object-content">
      <div
        class="service-object-item"
        :class="[activePerson ? 'service-object--active' : '']"
      >
        <img
          :src="
            activePerson
              ? require('@/assets/image/person-active.png')
              : require('@/assets/image/person.png')
          "
          width="70px"
          height="82px"
          alt=""
        />
        <span>个人</span>
        <div class="service-object-item--btn" @click="handlePerson">
          <i class="el-icon-circle-check"></i>
        </div>
      </div>
      <div
        class="service-object-item"
        :class="[activeLegal ? 'service-object--active' : '']"
      >
        <img
          :src="
            activeLegal
              ? require('@/assets/image/legal-active.png')
              : require('@/assets/image/legal.png')
          "
          width="70px"
          height="82px"
          alt=""
        />
        <span>法人</span>
        <div class="service-object-item--btn" @click="handleLegal">
          <i class="el-icon-circle-check"></i>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'serviceObjectDialog',
  props: {
    visibleDialog: Boolean,
    serviceObject: [String, Number]
  },
  computed: {
    visible: {
      get () {
        return this.visibleDialog;
      },
      set () {
        this.$emit('update:visibleDialog', false)
      }
    },

    activePerson ({ serviceObject }) {
      return serviceObject == 0;
    },

    activeLegal ({ serviceObject }) {
      return serviceObject == 1;
    }
  },
  methods: {
    handlePerson () {
      this.$emit('setServiceObject', '0');
      this.$emit('update:visibleDialog', false);
    },

    handleLegal () {
      this.$emit('setServiceObject', '1');
      this.$emit('update:visibleDialog', false);
    },
  }
}
</script>

<style scoped lang="scss">
.service-object-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 45px 150px;

  .service-object-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    &.service-object--active {
      span {
        color: #2e7dff;
      }

      .service-object-item--btn {
        background: #2e7dff;

        i {
          color: #fff;
        }
      }
    }

    span {
      font-size: 18px;
      font-weight: 400;
      color: #afb2b8;
      margin: 20px 0;
    }

    .service-object-item--btn {
      width: 75px;
      height: 40px;
      background: rgba(237, 240, 244, 0.55);
      border: 1px solid #c8cdd3;
      border-radius: 20px;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;

      i {
        font-size: 16px;
        color: #cdd3da;
      }
    }
  }
}
</style>
